package shared.model;

import java.util.ArrayList;
import java.util.logging.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import shared.communication.GameHeader;
import shared.communication.Session;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.exceptions.GameInitializationException;
import shared.exceptions.SchemaMismatchException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import client.data.GameInfo;
import client.misc.ClientManager;


public class ModelFacade {
	
	
		//Get the Singleton for this class
		//private static ModelFacade instance = new ModelFacade();
		@Deprecated
		public static ModelFacade getInstance(){
		    return ClientManager.getModel();
		}
	
		private CatanModel model;
		private Session localPlayer;
		private static final Logger log = Logger.getLogger( ModelFacade.class.getName() );
		
		private List<IModelListener> listeners;
		
		public ModelFacade() {
			
			//when does any of this get initialized?
			this(new CatanModel());
		}
		
		public ModelFacade(CatanModel startingModel) {
			
			model = startingModel;
			
			listeners = new ArrayList<>();
		}
		
		public CatanModel getCatanModel(){
			return model;
		}
		
		public synchronized void registerListener(IModelListener listener) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
		
		public synchronized void unregisterListener(IModelListener listener) {
			listeners.remove(listener);
		}
		
		public synchronized CatanModel updateFromJSON(JSONObject json) {
			int newVersion = (int) (long) json.get("version");
			if (newVersion == 0) {
				updatePlayersFromJSON(json);
				return model;
			}
			if (getVersion() == newVersion) {
				return null;
			}
			model.setVersion(newVersion);
			
			try {
				//BANK
				updateBankFromJSON(json);
				
				//PLAYERS
				List<Player> players = updatePlayersFromJSON(json);
				
				//BOARD
				updateMapFromJSON(json, players);
				
				//TURNTRACKER
				updateTurnTrackerFromJSON(json,players);
				
				//TRADEOFFER
				updateTradeOfferFromJSON(json,players);
				
				//CHAT NOT DONE
				updateChatFromJSON(json);
				
				//LOG NOT DONE
				if (json.containsKey("log")) {
					JSONObject object = (JSONObject) json.get("log");
					model.setLog(new MessageList(object));
				}
				
				//WINNER
				updateWinnerFromJSON(json);
				
				return model;
				
			} catch (SchemaMismatchException e) {
				System.out.println("Can't update");
				e.printStackTrace();
			}
			return model;
			
			
		}
		
		private void updateBankFromJSON(JSONObject json) {
			JSONObject object = (JSONObject) json.get("bank");
			try {
				Bank otherBank = new Bank(object);
				if (model.getBank() == null || !model.getBank().equals(otherBank)) {
					model.setBank(otherBank);
					for (IModelListener listener : listeners) {
						listener.bankChanged(otherBank);
					}
				}
			} catch (SchemaMismatchException e) {
				e.printStackTrace();
			}
		}
		
		private void updateMapFromJSON(JSONObject json, List<Player> players) {
			JSONObject object = (JSONObject) json.get("map");
			try {
				Board otherBoard = new Board(players, object);
				if (model.getMap() == null) 
				{ 
					model.setMap(otherBoard);
					for (IModelListener listener : listeners) {
						try {
							listener.mapInitialized();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else if (!model.getMap().equals(otherBoard)) {
					model.setMap(otherBoard);
					for (IModelListener listener : listeners) {
						try {
							listener.mapChanged(otherBoard);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (SchemaMismatchException e) {
				e.printStackTrace();
			} catch (GameInitializationException e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("rawtypes")
		private List<Player> updatePlayersFromJSON(JSONObject json) {
			List<Player> players = new ArrayList<Player>();
			for (Object obj : (List) json.get("players")) {
				JSONObject player = (JSONObject) obj;
				if (player != null)
					try {
						players.add(new Player(model, player));
					} catch (SchemaMismatchException e) {
						e.printStackTrace();
					}
			}
			if (model.getPlayers() == null || !model.getPlayers().equals(players)) {
				model.setPlayers(players);
				for (IModelListener listener : listeners) {
					log.fine("Players Changed");
					try {
						listener.playersChanged(players);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return players;
			}
			else
				return model.getPlayers();
		}
		
		private void updateTurnTrackerFromJSON(JSONObject json, List<Player> players) {
			if (json.containsKey("turnTracker")) {
				JSONObject object = (JSONObject) json.get("turnTracker");
				try {
					TurnTracker otherTurnTracker = new TurnTracker(players,object);
					if (model.getTurnTracker() == null || 
						!model.getTurnTracker().equals(otherTurnTracker)) {
						model.setTurnTracker(otherTurnTracker);
						for (IModelListener listener : listeners) {
							try {
								listener.turnTrackerChanged(otherTurnTracker);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					//LARGEST ARMY
					updateLargestArmyFromJSON(object);
					
					//LONGEST ROAD
					updateLongestRoadFromJSON(object);
					
				} catch (SchemaMismatchException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void updateLargestArmyFromJSON(JSONObject json) {
			if (json.containsKey("largestArmy")) {
				int largestArmyPlayer = (int) (long) json.get("largestArmy");
				PlayerReference otherPlayer = new PlayerReference(model, largestArmyPlayer);
				if (model.getLargestArmy() == null || 
					!model.getLargestArmy().equals(otherPlayer)) {
					model.setLongestRoad(otherPlayer);
					for (IModelListener listener : listeners) {
						try {
							listener.largestArmyChanged(otherPlayer);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		private void updateLongestRoadFromJSON(JSONObject json) {
			if (json.containsKey("longestRoad")) {
				int longestRoadPlayer = (int) (long) json.get("longestRoad");
				PlayerReference otherPlayer = new PlayerReference(model, longestRoadPlayer);
				if (model.getLongestRoad() == null ||
					!model.getLongestRoad().equals(otherPlayer)) {
					model.setLongestRoad(otherPlayer);
					for (IModelListener listener : listeners) {
						try {
							listener.longestRoadChanged(otherPlayer);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
			
		private void updateTradeOfferFromJSON(JSONObject json, List<Player> players) {
			if (json.containsKey("tradeOffer")) {
				JSONObject tradeOffer = (JSONObject) json.get("tradeOffer");
				TradeOffer otherOffer;
				try {
					otherOffer = new TradeOffer(players,tradeOffer);
					if (model.getTradeOffer() == null || !model.getTradeOffer().equals(otherOffer)) {
						model.setTradeOffer(otherOffer);
						for (IModelListener listener : listeners) {
							try {
								listener.tradeOfferChanged(otherOffer);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} catch (SchemaMismatchException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void updateChatFromJSON(JSONObject json) {
			if (json.containsKey("chat")) {
				JSONObject object = (JSONObject) json.get("chat");
				MessageList otherChat;
				try {
					otherChat = new MessageList(object);
					if (model.getChat() == null || !model.getChat().equals(otherChat)) {
						model.setChat(otherChat);
						for (IModelListener listener : listeners) {
							try {
								listener.chatChanged(otherChat);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} catch (SchemaMismatchException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void updateWinnerFromJSON(JSONObject json) {
			int winner = (int) (long) json.get("winner");
			PlayerReference otherPlayer = new PlayerReference(model, winner);
			if (model.getWinner() == null || !model.getWinner().equals(otherPlayer)) {
				model.setWinner(otherPlayer);
				for (IModelListener listener : listeners) {
					try {
						listener.winnerChanged(otherPlayer);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		public PlayerReference getCurrentPlayer() {
			return model.getTurnTracker().getCurrentPlayer();
		}
		
		//Possibly want to move this up, just make the facade have a reference to the
		//current player.
		public void setLocalPlayer(Session player) {
			localPlayer = player;
		}
		
		public Session getLocalPlayer() {
			return localPlayer;
		}
		
		public void setGameInfo(GameInfo header) {
			model.setHeader(header);
		}
		
		public GameInfo getGameInfo() {
			return model.getGameInfo();
		}
		
		public GameHeader getGameHeader() {
			return model.getHeader();
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
			//Hex tile = map.getHexAt(hexLoc);
			
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
