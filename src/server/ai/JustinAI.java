package server.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
	Random rand = new Random();
	
	static {
		resBaseValue.put(ResourceType.BRICK, 1400);
		resBaseValue.put(ResourceType.WOOD,  1400);
		resBaseValue.put(ResourceType.SHEEP,  650);
		resBaseValue.put(ResourceType.WHEAT, 1050);
		resBaseValue.put(ResourceType.ORE,    850);
	}
	
	IServer server = Server.getSingleton();
	private Map<ResourceType, Integer> resRelativeValue = new HashMap<>();
	private Map<ResourceType, Integer> productionPips = new HashMap<>();
	private Map<VertexLocation, Integer> vertexValue = new HashMap<>();

	public JustinAI(ModelFacade game, Player player) {
		super(game, player);
		logger.info("Initializing...");
		
		evaluateResources();
		evaluateVertexes();
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
			resRelativeValue.put(rt, resBaseValue.get(rt) / pipCount.get(rt));
		}
		
		logger.info("Evaluated resource valuability: " + resRelativeValue.toString());
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
		
		vertexValue.clear();

		for (HexLocation hexLocOuter : HexLocation.locationsWithinRadius(2)) {
			for (VertexLocation vertex : hexLocOuter.getVertices()) {
				if (vertexValue.containsKey(vertex)) {
					continue; // Duplicate (~2/3 of these will trigger)
				}
				int totalValue = 0;
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
							* Utils.numPips(hex.getNumber())
							* resRelativeValue.get(hex.getResource());
				}
				vertexValue.put(vertex, totalValue);
			}
		}
		
		logger.info("Evaluated vertex valuability: " + vertexValue.toString());
	}
	
	private void filterAvailableVertexes() {
		Board map = getGame().getCatanModel().getMap();
		
		for (VertexLocation vertex : new ArrayList<>(vertexValue.keySet())) {
			if (!map.canPlaceStartingSettlement(vertex)) { // Quick and dirty distance rule
				vertexValue.remove(vertex);
			}
		}
	}

	@Override
	public void firstRound() {
		logger.info("Deciding where to place starting pieces...");
		filterAvailableVertexes();
		// Select the outright best location available
		VertexLocation settlement = null;
		int bestValue = 0;
		for (Map.Entry<VertexLocation, Integer> pair : vertexValue.entrySet()) {
			if (pair.getValue() > bestValue) {
				settlement = pair.getKey();
				bestValue = pair.getValue();
			}
		}
		
		EdgeLocation road = null;
		for (EdgeLocation edge : settlement.getEdges()) {
			road = edge;
			break;
		}
		
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
		// Select the location that will balance out resource production
		
		firstRound(); // See what this looks like first
	}

	@Override
	public void discard() {
		// TODO Auto-generated method stub

	}

	@Override
	public void robber() {
		// TODO Auto-generated method stub

	}


	@Override
	public void takeTurn() {
		
	}

	@Override
	public boolean tradeOffered(TradeOffer trade) {
		if (!trade.isPossible()) {
			return false;
		}
		return false;
	}
	
	

}
