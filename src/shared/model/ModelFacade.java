package shared.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import shared.definitions.*;
import shared.exceptions.GameInitializationException;
import shared.exceptions.SchemaMismatchException;
import shared.locations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class ModelFacade {
	
		private CatanModel model;
		
		public ModelFacade() {
			model = new CatanModel();
		}
		
		public ModelFacade(CatanModel startingModel) {
			model = startingModel;
		}
		
		public CatanModel getCatanModel(){
			return model;
		}
		
		public static void main(String args[]) throws Exception {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			JSONObject model = ((JSONObject) parseResult);
			
			CatanModel temp = new CatanModel();
			ModelFacade test = new ModelFacade(temp);
			test.updateFromJSON(model);
			
			//System.out.println(parseResult);
			//System.out.println(port);
		}
		
		public synchronized CatanModel updateFromJSON(JSONObject json) {
			//int newVersion = (int) (long) json.get("version");
			//if (getVersion() == newVersion)
			//	return;
			try {
				//BANK
				JSONObject object = (JSONObject) json.get("bank");
				model.setBank(new Bank(object));
				
				//BOARD
				object = (JSONObject) json.get("map");
				model.setMap(new Board(object));
				
				//PLAYERS
				List<Player> players = new ArrayList<Player>();
				for (Object obj : (List) json.get("players")) {
					JSONObject player = (JSONObject) obj;
					if (player != null)
						players.add(new Player(model, player));
				}
				model.setPlayers(players);
				
				//TURNTRACKER
				if (json.containsKey("turnTracker")) {
					object = (JSONObject) json.get("turnTracker");
					model.setTurnTracker(new TurnTracker(players,object));
					
					//LARGEST ARMY
					if (object.containsKey("largestArmy")) {
						int longestRoadPlayer = (int) (long) object.get("largestArmy");
						model.setLongestRoad(new PlayerReference(model, longestRoadPlayer));
					}
					
					//LONGEST ROAD
					if (object.containsKey("longestRoad")) {
						int longestRoadPlayer = (int) (long) object.get("longestRoad");
						model.setLongestRoad(new PlayerReference(model, longestRoadPlayer));
					}
				}
				
				//TRADEOFFER
				if (json.containsKey("tradeOffer")) {
					JSONObject tradeOffer = (JSONObject) json.get("tradeOffer");
					model.setTradeOffer(new TradeOffer(players,tradeOffer));
				}
				
				//CHAT NOT DONE
				if (json.containsKey("chat")) {
					object = (JSONObject) json.get("chat");
					model.setChat(new MessageList(object));
				}
				
				//LOG NOT DONE
				if (json.containsKey("log")) {
					object = (JSONObject) json.get("log");
					model.setLog(new MessageList(object));
				}
				
				//WINNER
				int winner = (int) (long) json.get("winner");
				model.setWinner(new PlayerReference(model, winner));
				
				return model;
				
			} catch (SchemaMismatchException | GameInitializationException e) {
				System.out.println("Can't update");
				e.printStackTrace();
			}
			return model;
			
			
		}
		
		public synchronized void updateBankFromJSON(JSONObject json) {
			try {
				model.setBank(new Bank(json));
			} catch (SchemaMismatchException e) {
				e.printStackTrace();
			}

		}
		
		public PlayerReference getCurrentPlayer() {
			return model.getTurnTracker().getCurrentPlayer();
		}
	
		/**
		 * @return true if it is the players turn, and 
		 * they haven't rolled already
		 * @return false otherwise
		 */
		public synchronized boolean canRoll(PlayerReference player) {
			
			Player currentPlayer = getCurrentPlayer().getPlayer();
			if(currentPlayer.equals(player.getPlayer()) && !currentPlayer.hasRolled()) {
				return true;
			}
			else {
				return false;
			}
		}
		
		public synchronized void doRoll(PlayerReference player) {
			
			
			Player currentPlayer = getCurrentPlayer().getPlayer();
			
			currentPlayer.setHasRolled(true);
		}
		
		/**
		 * @param hexLoc The location on the map where the robber is to be placed
		 * @return true if the hex is not a desert hex.
		 * @return false otherwise
		 */
		public synchronized boolean canRob(HexLocation hexLoc) {
			
			Board map = model.getMap();
			Hex tile = map.getHexAt(hexLoc);
			
			if(map.canMoveRobberTo(hexLoc))
				return true;
			else
				return false;
		}
		
		public synchronized void doRob() {
				
		}
		
		/**
		 * 
		 * @return true if the player has already rolled the die
		 * @return false otherwise
		 */
		public synchronized boolean canFinishTurn() {
			
			Player currentPlayer = getCurrentPlayer().getPlayer();
			
			if(!currentPlayer.hasRolled())
				return true;
			else
				return false;
		}
		
		public synchronized boolean doFinishTurn() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player has at least one wool, one stone, and one wheat
		 * in their current hand.
		 * @return false if otherwise
		 */
		public synchronized boolean canBuyDevelopmentCard() {
			
			Player currentPlayer = getCurrentPlayer().getPlayer();
			ResourceList hand = currentPlayer.getResources();
			
			if(hand.count(ResourceType.SHEEP) > 0 &&
					hand.count(ResourceType.ORE) > 0 &&
					hand.count(ResourceType.WHEAT) > 0)
				return true;
			else
				return false;
		}
		
		
		public synchronized boolean doBuyDevelopmentCard() {
			return true;
		}
		
		/**
		 * 
		 * @param edgeLoc The location (one of the 6 sides of one hex on the board)
		 *  where the road is to be placed.
		 * @return true if the given edge location is adjacent to at least one of
		 * the player's roads or municipalities (city or settlement), and that there is
		 * no currently placed road at that location.
		 * @return false otherwise
		 */
		//What do I do if there is an enemy city in the way?
		public synchronized boolean canBuildRoad(EdgeLocation edgeLoc) {			
					
			Board map = model.getMap();
			PlayerReference currentPlayer = getCurrentPlayer();
			return map.canBuildRoadAt(currentPlayer, edgeLoc);
			
		}
		
		public synchronized boolean doBuildRoad() {
			return true;
		}
		
		/**
		 * 
		 * @param vertexLoc The location (one of the 6 vertices of one hex on the board)
		 * where the settlement is to be placed.
		 * @return true if the given vertex location is adjacent to at least one road
		 * that the player owns, the given location is empty, and the given location is 
		 * at least 2 vertices away from every other municipality (city/settlement)
		 * @return false otherwise
		 */
		public synchronized boolean canBuildSettlement(VertexLocation vertexLoc) {
			
			Board map = model.getMap();
			
			PlayerReference currentPlayer = getCurrentPlayer();
			
			return map.canBuildSettlement(currentPlayer, vertexLoc);
			
			

		}
		
		public synchronized boolean doBuildSettlement(VertexLocation vertexLoc) {
			return true;
		}
		
		/**
		 * 
		 * @param vertexLoc The location (one of the 6 vertices of one hex on the board)
		 * where the city is to be placed.
		 * @return true if the given vertex location is adjacent to at least one road
		 * that the player owns, the given location is empty, and the player owns a settlement
		 * at the given location.
		 * @return false otherwise
		 */
		public synchronized boolean canBuildCity(VertexLocation vertexLoc) {
			
			Board map = model.getMap();
			
			PlayerReference currentPlayer = getCurrentPlayer();
			
			return map.canBuildCity(currentPlayer, vertexLoc);
		}
		/**
		 * 
		 * @param vertexLoc
		 * @return
		 */
		public synchronized boolean doBuildCity(VertexLocation vertexLoc) {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player owns at least one year of plenty card
		 * @return false otherwise
		 */
		public synchronized boolean canYearOfPlenty() {
			Player currentPlayer = getCurrentPlayer().getPlayer();
			DevCardList list = currentPlayer.getOldDevCards();
			
			if(list.count(DevCardType.YEAR_OF_PLENTY) > 0)
				return true;
			
			return false;
		}
		
		public synchronized boolean doYearOfPlenty() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player owns at least one road building card,
		 * and has at least one unplaced road.
		 * @return false otherwise
		 */
		public synchronized boolean canRoadBuildingCard() {
			Player currentPlayer = getCurrentPlayer().getPlayer();
			DevCardList list = currentPlayer.getOldDevCards();
			
			if(list.count(DevCardType.ROAD_BUILD) > 0)
				return true;
			
			return false;
		}
		
		public synchronized boolean doRoadBuildCard() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the players owns at least one soldier card
		 * @return false otherwise
		 */
		public synchronized boolean canSoldier() {
			Player currentPlayer = getCurrentPlayer().getPlayer();
			DevCardList list = currentPlayer.getOldDevCards();
			
			if(list.count(DevCardType.SOLDIER) > 0)
				return true;
			
			return false;
		}
		
		public synchronized boolean doSoldier() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player owns at least one monopoly card
		 * @return false if player owns zero monopoly cards
		 */
		public synchronized boolean canMonopoly() {
			Player currentPlayer = getCurrentPlayer().getPlayer();
			DevCardList list = currentPlayer.getOldDevCards();
			
			if(list.count(DevCardType.MONOPOLY) > 0)
				return true;
			
			return false;
		}
		
		/**
		 * 
		 * @return
		 */
		public synchronized boolean doMonopoly() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player owns at least one monument card
		 * @return false otherwise
		 */
		public synchronized boolean canMonument() {
			Player currentPlayer = getCurrentPlayer().getPlayer();
			DevCardList list = currentPlayer.getOldDevCards();
			
			if(list.count(DevCardType.MONUMENT) > 0)
				return true;
			
			return false;
		}
		
		/**
		 * 
		 * @return
		 */
		public synchronized boolean doMonument() {
			return true;
		}
		
		/**
		 * 
		 * @return true if it is your turn and you have sufficient cards
		 * @return false otherwise
		 */
		public synchronized boolean canOfferTrade() {
			
			TradeOffer tradeOffer = model.getTradeOffer();
			ResourceTradeList tradeList = tradeOffer.getOffer();
			Map<ResourceType, Integer> offered = tradeList.getOffered();
			
			Player offeringPlayer = tradeOffer.getSender().getPlayer();
			ResourceList list = offeringPlayer.getResources();
			
			//iterate through all resources in the offer
			for(Map.Entry<ResourceType, Integer> entry : offered.entrySet()) {
				
				//check to see if there are as many resources in the hand of the offering player as there are in the offer
				if(!(list.count(entry.getKey()) >= entry.getValue()))
					return false;
				
			}
			return true;
			
		}
		
		/**
		 * 
		 * @return
		 */
		public synchronized boolean doOfferTrade() {
			return true;
		}
		
		/**
		 * 
		 * @return true if player has enough cards
		 * @return false otherwise
		 */
		public synchronized boolean canAcceptTrade() {
			
			TradeOffer tradeOffer = model.getTradeOffer();
			ResourceTradeList tradeList = tradeOffer.getOffer();
			Map<ResourceType, Integer> wanted = tradeList.getWanted();
			
			Player receivingPlayer = tradeOffer.getReceiver().getPlayer();
			ResourceList list = receivingPlayer.getResources();
			
			//iterate through all resources in the offer
			for(Map.Entry<ResourceType, Integer> entry : wanted.entrySet()) {
				
				//check to see if there are as many resources in the hand of the receiving player as there are in the offer
				if(!(list.count(entry.getKey()) >= entry.getValue()))
					return false;
				
			}
			
			return true;
		}
		
		/**
		 * 
		 * @return
		 */
		public synchronized boolean doAcceptTrade() {
			return true;
		}
		
		/**
		 * 
		 * @return true if player has a settlement or city on a port.
		 * @return false otherwise
		 */
		public synchronized boolean canMaritimeTrade() {
			
			Board map = model.getMap();
			Map<EdgeLocation, Port> ports = map.getPortMap();
			Map<VertexLocation, Municipality> municipalities = map.getMunicipalityMap();
			Player currentPlayer = getCurrentPlayer().getPlayer();
			
			
			//iterate through all ports
			for(Map.Entry<EdgeLocation, Port> entry : ports.entrySet()) {
				EdgeLocation edge = entry.getKey();
				
				//get vertices off of port edge
				Collection<VertexLocation> vertices = edge.getVertices();
				
				//iterate through all municipalities
				for(Map.Entry<VertexLocation, Municipality> Mentry : municipalities.entrySet()) {
					Municipality municipality = Mentry.getValue();
					
					//if municipality is on the port and it is owned by the player, you're good
					for(VertexLocation vertexLoc : vertices) {
						if(municipality.getLocation().equals(vertexLoc) && municipality.getOwner().getPlayer().equals(currentPlayer))
							return true;
					}
				}
			}
			
			return false;
		}
		
		/**
		 * 
		 * @return
		 */
		public synchronized boolean doMaritimeTrade() {
			return true;
		}
		
		public synchronized int getVersion() {
			return model.getVersion();
		}

		public synchronized boolean canBuildStartingSettlement(VertexLocation loc) {
			return model.getMap().canPlaceStartingSettlement(loc);
		}
		
		public synchronized boolean canBuildStartingPieces(VertexLocation settlement, EdgeLocation road) {
			return model.getMap().canPlaceStartingPieces(settlement, road);
		}
		
		public synchronized boolean canBuild2Roads(EdgeLocation first, EdgeLocation second) {
			Board map = model.getMap();
			PlayerReference currentPlayer = getCurrentPlayer();
			return map.canBuild2Roads(currentPlayer, first, second);
		}
}
