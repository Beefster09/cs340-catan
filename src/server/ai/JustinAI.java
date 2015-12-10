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
	}
	
	private class UpgradeSettlement implements Move {
		
		VertexLocation location;
		
		public UpgradeSettlement(VertexLocation location) {
			this.location = location;
		}

		@Override
		public Map<ResourceType, Integer> requirements() {
			return Utils.resourceMap(0, 0, 0, 2, 3);
		}

		@Override
		public boolean isPossible() {
			return getGame().canBuildCity(getPlayerReference(), location);
		}

		@Override
		public void play() {
			try {
				server.buildCity(getPlayerID(), getGameID(), location);
			} catch (ServerException | UserException e) {
				
			}
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
	
	private List<Move> plan;

	private Set<VertexLocation> settledVertexes;

	private Set<VertexLocation> deadVertexes;

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
		
		evaluateProduction();
		logger.info("...Done");
	}

	@Override
	public void secondRound() {
		// TODO Select the location that will balance out resource production
		
		firstRound(); // TEMP
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
		// TODO
		Player human = getGame().getCatanModel().getPlayers().get(0);
		ResourceTradeList trade = new ResourceTradeList(
				getPlayer().getResources().getResources(),
				human.getResources().getResources());
		try {
			server.offerTrade(getPlayerID(), getGameID(), trade, human.getUUID());
			if (getGame().getTradeResponse()) {
				server.sendChat(getPlayerID(), getGameID(), "Teehee. We traded hands.");
			}
			else {
				server.sendChat(getPlayerID(), getGameID(),
						human.getName() + " is a jerk. He won't trade hands with me. ;'(");
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
	
	private EdgeLocation chooseStartingRoadLocation(VertexLocation settlement) {
		for (EdgeLocation edge : settlement.getEdges()) {
			return edge;
		}
		return null;
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
	
	private void calculateRoadsNeeded() {
		Board map = getGame().getCatanModel().getMap();
		
		settledVertexes = new HashSet<>();
		for (Road road : map.getRoadsOwnedBy(getPlayerReference())) {
			settledVertexes.addAll(road.getLocation().getVertices());
		}
		
		deadVertexes = new HashSet<>();
		for (Municipality town : map.getMunicipalitiesNotOwnedBy(getPlayerReference())) {
			deadVertexes.add(town.getLocation());
		}
		
		roadsNeeded = new HashMap<>();
		
		for (VertexLocation node : settledVertexes) {
			distanceStep(node, 0);
		}
	}
	
	private void distanceStep(VertexLocation node, int distance) {
		roadsNeeded.put(node, distance);
		for (VertexLocation neighbor : node.getNeighbors()) {
			if (neighbor.getDistanceFromCenter() > 2
					|| deadVertexes.contains(neighbor)) {
				continue;
			}
			if (roadsNeeded.containsKey(neighbor)) {
				int otherdist = roadsNeeded.get(neighbor);
				if (otherdist > distance + 1) {
					distanceStep(neighbor, distance + 1);
				}
			}
			else {
				distanceStep(neighbor, distance + 1);
			}
		}
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
