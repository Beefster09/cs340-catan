package shared.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import shared.definitions.*;
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
		
		public ModelFacade(CatanModel startingModel) {
			model = startingModel;
		}
		
		public static void main(String args[]) throws Exception {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_sample.json"));
			Object parseResult = parser.parse(r);
			JSONObject model = ((JSONObject) parseResult);
			
			CatanModel temp = new CatanModel();
			ModelFacade test = new ModelFacade(temp);
			test.updateFromJSON(model);
			
			System.out.println(parseResult);
			//System.out.println(port);
		}
		
		public synchronized void updateFromJSON(JSONObject json) {
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
				while (json.containsKey("playerID")) {
					players.add(new Player(model,json));
				}
				model.setPlayers(players);
				
				//TURNTRACKER
				model.setTurnTracker(new TurnTracker(players,json));
				
				//TRADEOFFER
				if (json.containsKey("tradeOffer")) {
					JSONObject tradeOffer = (JSONObject) json.get("tradeOffer");
					model.setTradeOffer(new TradeOffer(players,tradeOffer));
				}
				
				//CHAT NOT DONE
				List<MessageLine> chats = new ArrayList<MessageLine>();
				//model.setChat(chat);
				
				//LOG NOT DONE
				
				//WINNER
				int winner = (int) (long) json.get("winner");
				model.setWinner(new PlayerReference(model, winner));
				
			} catch (SchemaMismatchException e) {
				System.out.println("Can't update");
				e.printStackTrace();
			}
		}
		public synchronized void updateBankFromJSON(JSONObject json) {
			try {
				model.setBank(new Bank(json));
			} catch (SchemaMismatchException e) {
				e.printStackTrace();
			}

		}
	
		/**
		 * @return true if it is the players turn, and 
		 * they haven't rolled already
		 * @return false otherwise
		 */
		public synchronized boolean canRoll(PlayerReference player) {
			
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
			if(currentPlayer.equals(player) && !currentPlayer.hasRolled()) {
				return true;
			}
			else {
				return false;
			}
		}
		
		/**
		 * @param hexLoc The location on the map where the robber is to be placed
		 * @return true if the hex is not a desert hex.
		 * @return false otherwise
		 */
		public synchronized boolean canRob(HexLocation hexLoc) {
			
			Board map = model.getMap();
			Hex tile = map.getHexAt(hexLoc);
			
			if(tile.getResource().equals(ResourceType.BRICK) ||	
					tile.getResource().equals(ResourceType.WOOD) ||	
					tile.getResource().equals(ResourceType.WHEAT) || 
					tile.getResource().equals(ResourceType.SHEEP) || 
					tile.getResource().equals(ResourceType.ORE)) 
			{
				return true;
			}
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
			
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
			
			if(currentPlayer.hasRolled())
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
			
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
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
			Map<EdgeLocation, Road> roads = map.getRoads();
			Map<VertexLocation, Municipality> municipalities = map.getMunicipalities();
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
			
			if(roads.get(edgeLoc) != null)
				return false;
				
			//iterate through map of roads
			for(Map.Entry<EdgeLocation, Road> entry : roads.entrySet()) {
				
				//if there is an adjacent road and that road is owned by the current player, build
				if(edgeLoc.isAdjacent(entry.getKey()) && currentPlayer.equals(entry.getValue().getOwner()))
					return true;
				}
			
			//iterate through map of municipalities
			for(Map.Entry<VertexLocation, Municipality> entry : municipalities.entrySet()) {
				
				VertexLocation vertexLoc = entry.getKey();
				Municipality municipality = entry.getValue();			
				Collection<EdgeLocation> adjacentEdges = vertexLoc.getEdges();
				
				//goes through all municipalities and sees if the edge is adjacent to a municipality owned by the owner.  If so, build.
				for(EdgeLocation e : adjacentEdges) {
					if(edgeLoc.equals(e) && currentPlayer.equals(municipality.getOwner()))
						return true;
				}
			}
			return false;
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
			Map<VertexLocation, Municipality> municipalities = map.getMunicipalities();
			Map<EdgeLocation, Road> roads = map.getRoads();
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
			
			//iterate through map of municipalities
			for(Map.Entry<VertexLocation, Municipality> entry : municipalities.entrySet()) {
				//check if where you want to build is adjacent to any existing municipality
				if(vertexLoc.isAdjacent(entry.getKey()))
					return false;
			}
			
			Collection<EdgeLocation> adjacentEdges = vertexLoc.getEdges();
						
			//iterate through map of roads
			for(Map.Entry<EdgeLocation, Road> entry : roads.entrySet()) {
				//iterates through edges stemming off of vertex
				for(EdgeLocation edge : adjacentEdges) {
					//if there is existing road there that is owned by the current player, then he can build.
					if(edge.equals(entry.getKey()) && currentPlayer.equals(entry.getValue().getOwner()))
						return true;
					}	
			}
			return false;
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
			Map<VertexLocation, Municipality> municipalities = map.getMunicipalities();
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
			
			//iterates through all municipalities
			for(Map.Entry<VertexLocation, Municipality> entry : municipalities.entrySet()) {
				Municipality municipality = entry.getValue();
				//if settlement exists at given location that is owned by the current player, go ahead and build
				if(vertexLoc.equals(entry.getKey()) && currentPlayer.equals(municipality.getOwner()) && municipality.getType().equals(MunicipalityType.SETTLEMENT))
					return true;
			}
			return false;
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
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
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
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
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
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
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
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
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
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
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
			Map<EdgeLocation, Port> ports = map.getPorts();
			Map<VertexLocation, Municipality> municipalities = map.getMunicipalities();
			Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
			
			
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
						if(municipality.getLocation().equals(vertexLoc) && municipality.getOwner().equals(currentPlayer))
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
		
		/**
		 * 
		 * @return true if seven is rolled and amount of cards in hand is over seven
		 * @return false otherwise
		 */
		//what the.... what am I supposed to do?
		public synchronized boolean canDiscardCards() {
			
			//List<Player> players = model.getPlayers();
			
			return true;
		}
		
		/**
		 * 
		 * @return
		 */
		public synchronized boolean doDiscardCards() {
			return true;
		}
		
		public synchronized int getVersion() {
			return model.getVersion();
		}
}
