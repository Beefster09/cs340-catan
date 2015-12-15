package server.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.communication.Server;
import shared.communication.IServer;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.DevCardList;
import shared.model.ModelFacade;
import shared.model.Player;
import shared.model.PlayerReference;
import shared.model.Port;
import shared.model.ResourceList;

/**
 * ActionManager keeps track of all the available actions that
 * an AI can do, and uses algorithms to determine which one will
 * be the best, usually to be returned by the getBestAction method.
 * 
 * @author jchip
 *
 */
public class JordanActionManager {

	private ModelFacade game;
	private Player player;
	
	/*
	 * Actions contains mappings from an action ("BuildRoad"), to
	 * a priority rating for that mapping (0-10).
	 * Currently, the ratings are more or less as follows:
	 *  0 means that an action should not be performed
	 * (Not enough resources, a really bad idea to play)
	 *  5 means that an action is a good idea, should do if there is nothing else to do
	 *  10 means that an action is without doubt the best play
	 *  (Can win the game with these plays)
	 *  
	 *  I also need to setup so that actions of the same priority will all be executed,
	 *  if possible.
	 *  So, if a city is an important priority, getting the resources for it should also
	 *  be the same priority.
	 */
	private Map<String, Integer> actions = new HashMap<String, Integer>();
	
	private IServer server = Server.getSingleton();
	
	public JordanActionManager(ModelFacade game, Player player) {
		this.game = game;
		this.player = player;
	}

	/**
	 * Find the best action out of possible actions
	 * "Best" = Highest priority
	 * Returns "End_Turn" if no actions exist.
	 * @return The best action available to the ai at the current stage
	 */
	public List<String> getBestActions() {
		
		//First generate all the actions with their priorities
		generateActionPriorities();
		
		int max = 0;
		List<String> bestActions = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry: actions.entrySet()) {
			
			String action = entry.getKey();
			int priority = entry.getValue();
			
			if (priority > max) {
				bestActions = new ArrayList<String>();
				bestActions.add(action);
			}
			else if (priority == max) {
				bestActions.add(action);
			}
		}
		return bestActions;
	}
	
	/**
	 * 
	 * @return
	 */
	private Map<String, Integer> generateActionPriorities() {
		
		boolean trade = false;
		
		ResourceList resources = player.getResources();
		DevCardList devCards = player.getOldDevCards();
		
		int points = player.getVictoryPoints();
		int monuments = devCards.count(DevCardType.MONUMENT);
		if(points + monuments >= 10){
			actions.put("Monument", 10);
		}
		
		int priority = 0;
		if(player.canBuyDevCard()){
			priority = getDevCardPriority();
			actions.put("BuyDevCard", priority);
		}
		if(devCards.count(DevCardType.YEAR_OF_PLENTY) > 0){
			priority = getYearOfPlentyPriority();
			actions.put("YearOfPlenty", priority);
		}
		if(devCards.count(DevCardType.SOLDIER) > 0){
			priority = getSoldierPriority();
			actions.put("Soldier", priority);
		}
		if(devCards.count(DevCardType.MONOPOLY) > 0){
			priority = getMonopolyPriority();
			actions.put("Monopoly", priority);
		}
		if(devCards.count(DevCardType.ROAD_BUILD) > 0){
			priority = getRoadBuildingPriority();
			actions.put("RoadBuilding", priority);
		}
		if(player.canBuildRoad()){
			priority = getRoadPriority();
			actions.put("BuildRoad", priority);
		}
		if(player.canBuildSettlement()){
			priority = getSettlementPriority();
			actions.put("BuildSettlement", priority);
		}
		if(player.canBuildCity()){
			priority = getCityPriority();
			actions.put("BuildCity", priority);
		}
		if(resources.count() > 0 && trade){
			priority = getTradePriority();
			actions.put("OfferTrade", priority);
		}
		for(ResourceType type : ResourceType.values()){
			int count = resources.count(type);
			int ratio = getRatio(type);
			
			if(count >= ratio){
				switch (type){
				case BRICK:
					priority = getMaritimeBrickPriority();
					actions.put("MaritimeTradeBrick", priority);
					break;
				case WOOD:
					priority = getMaritimeWoodPriority();
					actions.put("MaritimeTradeWood", priority);
					break;
				case SHEEP:
					priority = getMaritimeSheepPriority();
					actions.put("MaritimeTradeSheep", priority);
					break;
				case ORE:
					priority = getMaritimeOrePriority();
					actions.put("MaritimeTradeOre", priority);
					break;
				case WHEAT:
					priority = getMaritimeWheatPriority();
					actions.put("MaritimeTradeWheat", priority);
					break;
				}
			}
		}
		
		return actions;
	}
	
	
	
	
	private int getDevCardPriority() {
		// TODO Auto-generated method stub
		return 5;
	}

	private int getYearOfPlentyPriority() {
		// TODO Auto-generated method stub
		return 9;
	}

	private int getSoldierPriority() {
		// TODO Auto-generated method stub
		return 9;
	}

	private int getMonopolyPriority() {
		// TODO Auto-generated method stub
		return 9;
	}

	private int getRoadBuildingPriority() {
		// TODO Auto-generated method stub
		return 9;
	}

	private int getRoadPriority() {
		// TODO Auto-generated method stub
		return 7;
	}

	private int getSettlementPriority() {
		// TODO Auto-generated method stub
		return 8;
	}

	private int getCityPriority() {
		// TODO Auto-generated method stub
		return 8;
	}

	private int getTradePriority() {
		// TODO Auto-generated method stub
		return 4;
	}

	private int getMaritimeBrickPriority() {
		// TODO Auto-generated method stub
		return 3;
	}

	private int getMaritimeWoodPriority() {
		// TODO Auto-generated method stub
		return 3;
	}

	private int getMaritimeSheepPriority() {
		// TODO Auto-generated method stub
		return 3;
	}

	private int getMaritimeOrePriority() {
		// TODO Auto-generated method stub
		return 3;
	}

	private int getMaritimeWheatPriority() {
		// TODO Auto-generated method stub
		return 3;
	}

	private int getRatio(ResourceType type){
		int ratio = 4;
		Collection<Port> ports = game.getCatanModel().getMap().getPorts();
		for(Port p : ports){
			PlayerReference owner = game.getCatanModel().getMap().getPortOwner(p);
			if(owner == null){
				continue;
			}
			if(owner.getPlayerUUID().equals(player.getUUID())){
				ResourceType resourceType = p.getResource();
				if(p.getRatio() == 3){
					ratio = 3;
					continue;
				}
				if(resourceType.equals(type)){
					ratio = 2;
					break;
				}
			}
		}
		return ratio;
	}
	
	
}
