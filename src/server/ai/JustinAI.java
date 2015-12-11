package server.ai;

import java.util.*;
import java.util.logging.Logger;

import server.communication.Server;
import shared.Utils;
import shared.communication.IServer;
import shared.definitions.*;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.*;
import shared.model.*;

public class JustinAI extends AIPlayer {
	
	private static final int MAX_DISTANCE = 15;

	private static Logger logger = Logger.getLogger("JustinAI");
	
	// How much each resource is collectively worth.
	private static Map<ResourceType, Integer> resBaseValue = new HashMap<>();
	private static Set<VertexLocation> allVertexes = new HashSet<>();
	
	static {
		resBaseValue.put(ResourceType.BRICK, 1400);
		resBaseValue.put(ResourceType.WOOD,  1400);
		resBaseValue.put(ResourceType.SHEEP,  650);
		resBaseValue.put(ResourceType.WHEAT, 1050);
		resBaseValue.put(ResourceType.ORE,    850);
		
		for (HexLocation hex : HexLocation.locationsWithinRadius(2)) {
			for (VertexLocation vertex : hex.getVertices()) {
				allVertexes.add(vertex);
			}
		}
	}
	
	private interface Move {
		Map<ResourceType, Integer> requirements();
		boolean isPossible();
		void play();
	}
	
	private class BuildRoad implements Move {
		
		EdgeLocation location;
		
		public BuildRoad(EdgeLocation location) {
			this.location = location;
		}

		@Override
		public Map<ResourceType, Integer> requirements() {
			return Utils.resourceMap(1, 1, 0, 0, 0);
		}

		@Override
		public boolean isPossible() {
			return getGame().canBuildRoad(getPlayerReference(), location);
		}

		@Override
		public void play() {
			try {
				server.buildRoad(getPlayerID(), getGameID(), location);
			} catch (ServerException | UserException e) {
				
			}
		}
		
		public EdgeLocation getLocation() {
			return location;
		}
		@Override
		public String toString() {
			return "BuildRoad [location=" + location + "]";
		}
	}

	private class BuildSettlement implements Move {
		
		VertexLocation location;
		
		public BuildSettlement(VertexLocation location) {
			this.location = location;
		}

		@Override
		public Map<ResourceType, Integer> requirements() {
			return Utils.resourceMap(1, 1, 1, 1, 0);
		}

		@Override
		public boolean isPossible() {
			return getGame().canBuildSettlement(getPlayerReference(), location);
		}

		@Override
		public void play() {
			try {
				server.buildSettlement(getPlayerID(), getGameID(), location);
			} catch (ServerException | UserException e) {
				
			}
		}
		
		@Override
		public String toString() {
			return "BuildSettlement [location=" + location + "]";
		}
	}
	
	//private Random rand = new Random();
	private IServer server = Server.getSingleton();
	
	private Map<ResourceType, Integer> resourceValue = new HashMap<>();
	private Map<VertexLocation, Integer> vertexValues = new HashMap<>();
	private Map<VertexLocation, Integer> vertexPips = new HashMap<>();

	private Map<ResourceType, Integer> productionPips = new HashMap<>();
	private Map<VertexLocation, Integer> roadsNeeded = new HashMap<>();
	private Set<VertexLocation> availableVertexes;
	// spots owned by other players. Relevant because you can't build through them.
	private Set<VertexLocation> deadVertexes; 
	
	private Deque<Move> plan;
	private VertexLocation nextUpgrade = null;

	public JustinAI(ModelFacade game, Player player) {
		super(game, player);
		logger.info("Initializing...");
		
		evaluateResources();
		evaluateVertexes();
	}

	@Override
	public void firstRound() {
		logger.info("Deciding where to place starting pieces...");
		filterAvailableVertexes();
		// Select the outright best location available
		VertexLocation settlement = null;
		int bestValue = 0, bestPips = 0;
		for (VertexLocation vertex : availableVertexes) {
			if (vertexPips.get(vertex) > bestPips) {
				settlement = vertex;
				bestPips = vertexPips.get(vertex);
				bestValue = vertexValues.get(vertex);
			}
			else if (vertexPips.get(vertex) == bestPips &&
					vertexValues.get(vertex) > bestValue) {
				settlement = vertex;
				bestPips = vertexPips.get(vertex);
				bestValue = vertexValues.get(vertex);
			}
		}
		
		EdgeLocation road = chooseStartingRoadLocation(settlement);
		
		try {
			server.buildStartingPieces(getPlayerID(), getGameID(), settlement, road);
		} catch (ServerException | UserException e) {
			e.printStackTrace();
			// RIP
		}
	}

	@Override
	public void secondRound() {
		Board map = getGame().getCatanModel().getMap();
		
		evaluateProduction();
		
		filterAvailableVertexes();

		// Select a location that balances out your resource production
		VertexLocation settlement = null;
		int bestSitValue = 0;
		for (VertexLocation vertex : availableVertexes) {
			int sitValue = 0;
			for (HexLocation hexLoc : vertex.getHexes()) {
				if (hexLoc.getDistanceFromCenter() > 2) {
					continue; // Outside board
				}
				Hex hex = map.getHexAt(hexLoc);
				if (hex.getResource() == null) {
					continue;
				}
				// Prefer locations with more of the (better) missing resources
				sitValue += Utils.numPips(hex.getNumber())
						* resourceValue.get(hex.getResource())
						/ (productionPips.get(hex.getResource()) + 1);
			}
			
			if (sitValue > bestSitValue) {
				settlement = vertex;
				bestSitValue = sitValue;
			}
		}
		
		EdgeLocation road = chooseStartingRoadLocation(settlement);
		
		try {
			server.buildStartingPieces(getPlayerID(), getGameID(), settlement, road);
		} catch (ServerException | UserException e) {
			e.printStackTrace();
			// RIP
		}
	}

	@Override
	public void discard() {
		ResourceList hand = getPlayer().getResources();
		ResourceList dummyHand = new ResourceList(hand);
		ResourceList discardChoices = new ResourceList(0);
		int toDiscard = hand.count() / 2;
		
		for (int i=0; i<toDiscard; ++i) {
			ResourceType bestRes = null;
			int lowestCost = Integer.MAX_VALUE;
			for (ResourceType res : ResourceType.values()) {
				if (dummyHand.count(res) > 0) { // Don't try to discard what you do not have...
					int cost = situationalCost(res, 1);
					if (cost < lowestCost) {
						bestRes = res;
						lowestCost = cost;
					}
				}
			}
			dummyHand.transferAtMost(discardChoices, bestRes, 1);
		}
		
		try {
			server.discardCards(getPlayerID(), getGameID(), discardChoices);
		} catch (ServerException | UserException e) {
			logger.severe("RIP");
		}
	}

	@Override
	public void robber() {
		Board map = getGame().getCatanModel().getMap();
		HexLocation bestSpot = null;
		// Place the robber where it hurts others the most and hurts you the least.
		int bestDamage = Integer.MIN_VALUE;
		for (HexLocation hexLoc : HexLocation.locationsWithinRadius(2)) {
			if (map.canMoveRobberTo(hexLoc)) {
				Collection<Municipality> towns = map.getMunicipalitiesAround(hexLoc);
				Hex hex = map.getHexAt(hexLoc);
				ResourceType res = hex.getResource();
				int pips = Utils.numPips(hex.getNumber());
				int damage = 0;
				for (Municipality town : towns) {
					if (town.getOwner().equals(getPlayerReference())) {
						damage -= resourceValue.get(res) * pips * town.getIncome();
					}
					else {
						damage += resourceValue.get(res) * pips * town.getIncome();
					}
				}
				if (damage > bestDamage) {
					bestSpot = hexLoc;
					bestDamage = damage;
				}
			}
		}
		
		UUID bestVictim = null;
		int bestEnemyScore = 0;
		for (Municipality town : map.getMunicipalitiesAround(bestSpot)) {
			PlayerReference owner = town.getOwner();
			if (!owner.equals(getPlayerReference())) {
				if (owner.getPlayer().getVictoryPoints() > bestEnemyScore) {
					bestVictim = owner.getPlayerUUID();
					bestEnemyScore = owner.getPlayer().getVictoryPoints();
				}
			}
		}
		
		try {
			server.robPlayer(getPlayerID(), getGameID(), bestSpot, bestVictim);
		} catch (ServerException | UserException e) {
			logger.severe("RIP");
		}
	}


	@Override
	public void takeTurn() {
		devisePlan();
		
		try {
			// TODO: when to trade...
			
			while (!plan.isEmpty() && plan.peekFirst().isPossible()) {
				plan.pollFirst().play();
			}
			
			if (nextUpgrade != null && getGame().canBuildCity(nextUpgrade)) {
				server.buildCity(getPlayerID(), getGameID(), nextUpgrade);
			}
			
		} catch (ServerException | UserException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean tradeOffered(TradeOffer trade) {
		if (!trade.isPossible()) {
			return false;
		}
		ResourceTradeList offer = trade.getOffer();
		int cost = 0, benefit = 0;
		for (ResourceType res : ResourceType.values()) {
			cost += situationalCost(res, offer.getWanted().get(res));
			benefit += situationalBenefit(res, offer.getOffered().get(res));
		}
		return benefit > cost;
	}
	
	private void devisePlan() {
		if (getPlayer().getSettlements() > 0) {
			Map<VertexLocation, Integer> roadsNeeded = calculateRoadsNeeded();
			VertexLocation goal = chooseGoal(roadsNeeded);
			
			plan = new ArrayDeque<>();
			plan.addFirst(new BuildSettlement(goal));
			
			// Retrace back to a "settled" node.
			VertexLocation node = goal;
			while (true) {
				int distance = roadsNeeded.get(node);
				if (distance == 0) {
					break;
				}
				for (VertexLocation neighbor : node.getNeighbors()) {
					if (roadsNeeded.get(neighbor) != null
						&& roadsNeeded.get(neighbor) < distance) {
						plan.addFirst(new BuildRoad(node.getEdgeBetween(neighbor)));
						node = neighbor;
						break;
					}
				}
			}
			
			logger.info("Plan: " + plan.toString());
		}
		if (getPlayer().getSettlements() < 2) {
			nextUpgrade = null;
			int bestValue = 0;
			for (Municipality town : getGame().getCatanModel()
					.getMap().getMunicipalitiesOwnedBy(getPlayerReference())) {
				if (town.getType() == MunicipalityType.SETTLEMENT) {
					VertexLocation location = town.getLocation();
					if (vertexValues.get(location) > bestValue) {
						nextUpgrade = location;
						bestValue = vertexValues.get(location);
					}
				}
			}
		}
	}

	private EdgeLocation chooseStartingRoadLocation(VertexLocation settlement) {
		Map<VertexLocation, Integer> distances = calculateRoadsNeeded(settlement);
		
		// get the best target
		VertexLocation bestTarget = chooseGoal(distances, 2);
		
		// point the road in the direction of the best target
		Map<VertexLocation, Integer> reverseDistances = calculateRoadsNeeded(bestTarget);
		int distance = reverseDistances.get(settlement);
		for (VertexLocation neighbor : settlement.getNeighbors()) {
			if (reverseDistances.get(neighbor) < distance) {
				return settlement.getEdgeBetween(neighbor);
			}
		}
		return null;
	}

	private VertexLocation chooseGoal(Map<VertexLocation, Integer> distances) {
		return chooseGoal(distances, 0);
	}

	private VertexLocation chooseGoal(Map<VertexLocation, Integer> distances,
			int minDistance) {
		Board map = getGame().getCatanModel().getMap();
		
		VertexLocation bestTarget = null;
		int bestPriority = 0;
		
		for (Map.Entry<VertexLocation, Integer> entry : distances.entrySet()) {
			VertexLocation vertex = entry.getKey();
			int distance = entry.getValue();
			if (distance < minDistance
				|| !map.canPlaceStartingSettlement(vertex)) {
				continue;
			}
			int priority = vertexValues.get(vertex) / (distance + 1);
			if (priority > bestPriority) {
				bestTarget = vertex;
				bestPriority = priority;
			}
		}
		return bestTarget;
	}

	private int situationalValue(ResourceType resource, int current, int added) {
		double multiplier = added * (1.2 / Math.max(current, 1));
		int bonus = 0;
		if (current == 0 && added > 0) {
			// TODO: determine if the resource is actually needed
			bonus += 350;
		}
		return (int) (resourceValue.get(resource) * multiplier + bonus);
	}
	
	private int situationalBenefit(ResourceType resource, int added) {
		return situationalValue(resource, getPlayer().getResources().count(resource), added);
	}
	
	private int situationalCost(ResourceType resource, int lost) {
		return situationalValue(resource, getPlayer().getResources().count(resource) - lost, lost);
	}

	private void evaluateResources() {
		CatanModel game = getGame().getCatanModel();
		
		Map<ResourceType, Integer> pipCount = new HashMap<>();
		for (ResourceType rt : ResourceType.values()) {
			pipCount.put(rt, 0);
		}
		for (HexLocation hexLoc : HexLocation.locationsWithinRadius(2)) {
			Hex hex = game.getMap().getHexAt(hexLoc);
			ResourceType res = hex.getResource();
			if (res == null) {
				continue;
			}
			pipCount.put(res, pipCount.get(res) + Utils.numPips(hex.getNumber()));
		}
	
		for (ResourceType rt : ResourceType.values()) {
			resourceValue.put(rt, resBaseValue.get(rt) / pipCount.get(rt));
		}
		
		logger.info("Evaluated resource valuability: " + resourceValue.toString());
	}

	private void evaluateProduction() {
		Board map = getGame().getCatanModel().getMap();
	
		for (ResourceType rt : ResourceType.values()) {
			productionPips.put(rt, 0);
		}
		for (Municipality town : map.getMunicipalitiesOwnedBy(getPlayerReference())) {
			for (HexLocation hexLoc : town.getLocation().getHexes()) {
				if (hexLoc.getDistanceFromCenter() <= 2) {
					Hex hex = map.getHexAt(hexLoc);
					ResourceType res = hex.getResource();
					if (res == null) {
						continue;
					}
					productionPips.put(res, productionPips.get(res) + town.getIncome());
				}
			}
		}
		
		logger.info("Evaluated production: " + productionPips.toString());
	}

	private void evaluateVertexes() {
		Board map = getGame().getCatanModel().getMap();
		
		vertexValues.clear();
	
		for (VertexLocation vertex : allVertexes) {
			int totalValue = 0, totalPips = 0;
			for (HexLocation hexLoc : vertex.getHexes()) {
				if (hexLoc.getDistanceFromCenter() > 2) {
					continue; // Outside board
				}
				Hex hex = map.getHexAt(hexLoc);
				if (hex.getResource() == null) {
					continue;
				}
				// TODO: port value
				totalValue += Utils.numPips(hex.getNumber())
						* resourceValue.get(hex.getResource());
				totalPips += Utils.numPips(hex.getNumber());
			}
			vertexValues.put(vertex, totalValue);
			vertexPips.put(vertex, totalPips);
		}
		
		logger.info("Evaluated vertex valuability: " + vertexValues.toString());
	}
	
	private Map<VertexLocation, Integer> calculateRoadsNeeded() {
		return calculateRoadsNeeded(getSettledVertexes());
	}

	private Map<VertexLocation, Integer> calculateRoadsNeeded(VertexLocation start) {
		return calculateRoadsNeeded(new HashSet<>(Arrays.asList(start)));
	}
	
	private Map<VertexLocation, Integer> calculateRoadsNeeded(Set<VertexLocation> startingPoints) {
		Board map = getGame().getCatanModel().getMap();
		
		deadVertexes = new HashSet<>();
		for (Municipality town : map.getMunicipalitiesNotOwnedBy(getPlayerReference())) {
			deadVertexes.add(town.getLocation());
		}
		
		startingPoints.removeAll(deadVertexes);
		
		// Doubles as the settled list
		Map<VertexLocation, Integer> distanceMap = new HashMap<>();
		Set<VertexLocation> currentLayer = startingPoints;
		Set<VertexLocation> nextLayer;
		
		// Use a BFS search to get distances
		for (int distance = 0; distance <= MAX_DISTANCE; ++distance) {
			
			nextLayer = new HashSet<>();
			
			// Settle the entire layer
			for (VertexLocation node : currentLayer) {
				distanceMap.put(node, distance);
			}

			for (VertexLocation node : currentLayer) {
				// Populate the next layer
				for (VertexLocation neighbor : node.getNeighbors()) {
					if (neighbor.getDistanceFromCenter() > 2 ||
							distanceMap.containsKey(neighbor)) {
						continue;
					}
					else {
						nextLayer.add(neighbor);
					}
				}
			}
			
			if (nextLayer.isEmpty()) {
				break;
			}
			
			currentLayer = nextLayer;
		}
		
		return distanceMap;
	}

	private Set<VertexLocation> getSettledVertexes() {
		Set<VertexLocation> settledVertexes = new HashSet<>();
		for (Road road : getGame().getCatanModel().getMap()
				.getRoadsOwnedBy(getPlayerReference())) {
			settledVertexes.addAll(road.getLocation().getVertices());
		}
		return settledVertexes;
	}

	private void filterAvailableVertexes() {
		Board map = getGame().getCatanModel().getMap();
		
		availableVertexes = new HashSet<>();
		
		for (VertexLocation vertex : new ArrayList<>(vertexValues.keySet())) {
			if (map.canPlaceStartingSettlement(vertex)) { // Quick and dirty distance rule
				availableVertexes.add(vertex);
			}
		}
	}

}
