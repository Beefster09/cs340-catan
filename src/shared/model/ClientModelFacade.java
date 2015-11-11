package shared.model;

import java.util.ArrayList;
import java.util.logging.*;
import java.util.List;

import org.json.simple.JSONObject;

import shared.communication.Session;
import shared.exceptions.GameInitializationException;
import shared.exceptions.SchemaMismatchException;
import client.communication.ServerPoller;
import client.data.GameInfo;
import client.misc.ClientManager;


public class ClientModelFacade extends ModelFacade {
		private static final Logger log = Logger.getLogger( ClientModelFacade.class.getName() );
		
		private List<IModelListener> listeners;
		private ServerPoller poller;
		
		public ClientModelFacade() {
			//when does any of this get initialized?
			this(new CatanModel());
		}
		
		public ClientModelFacade(CatanModel startingModel) {
			super(startingModel);
			
			listeners = new ArrayList<>();
		}
		
		public synchronized void registerListener(IModelListener listener) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
		
		public synchronized void unregisterListener(IModelListener listener) {
			listeners.remove(listener);
		}
		
		/** Updates the model from json
		 * THIS is a CLIENT-ONLY method!
		 * @param json
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public synchronized CatanModel updateFromJSON(JSONObject json) {
			int newVersion = (int) (long) json.get("version");
			//We are still waiting for players.
			List<JSONObject> listOfPlayers = (List<JSONObject>)json.get("players");
			int currentPlayerCount = 4;
			for(JSONObject play : listOfPlayers){
				if(play == null){
					--currentPlayerCount;
				}
			}
			if (newVersion == 0 &&
				currentPlayerCount < 4) {
				updatePlayersFromJSON(json);
				return model;
			}
			if (getVersion() == newVersion) {
				return null;
			}
			model.setVersion(newVersion);
			
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
			
			//CHAT
			updateChatFromJSON(json);
			
			//LOG
			updateLogFromJSON(json);
			
			//WINNER
			updateWinnerFromJSON(json);
			
			return model;
			
		}
		
		private void updateBankFromJSON(JSONObject json) {
			//JSONObject object = (JSONObject) json.get("bank");
			try {
				Bank otherBank = new Bank(json);
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
			int i = 0;
			for (Object obj : (List) json.get("players")) {
				JSONObject player = (JSONObject) obj;
				if (player != null) {
					try {
						Player newPlayer = new Player(player);
						if (ClientManager.getSession() != null && newPlayer.getPlayerID() == ClientManager.getSession().getPlayerID()) {
							ClientManager.setLocalPlayer(new PlayerReference(model, i));
						}
						players.add(newPlayer);
					} catch (SchemaMismatchException e) {
						e.printStackTrace();
					}
				}
				i++;
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
							}
							catch (Exception e) {
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
						}
						catch (Exception e) {
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
						}
						catch (Exception e) {
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
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (SchemaMismatchException e) {
					e.printStackTrace();
				}
			}
			else{// if (model.getTradeOffer() != null) {
				model.setTradeOffer(null);
				for (IModelListener listener : listeners) {
					try {
						listener.tradeOfferChanged(null);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
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
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (SchemaMismatchException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void updateLogFromJSON(JSONObject json) {
			if (json.containsKey("log")) {
				JSONObject object = (JSONObject) json.get("log");
				try {
					MessageList otherLog = new MessageList(object);
					if (!otherLog.equals(model.getLog())) {
						model.setLog(otherLog);
						for (IModelListener listener : listeners) {
							try {
								listener.logChanged(otherLog);
							} catch (Exception e) {
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
			if (!json.containsKey("winner")) {
				return;
			}
			PlayerReference winner = new PlayerReference((String) json.get("winner"));
			//PlayerReference otherPlayer = new PlayerReference(model, winner);
			if (model.getWinner() != null || !model.getWinner().equals(winner)) {
				model.setWinner(winner);
				for (IModelListener listener : listeners) {
					try {
						listener.winnerChanged(winner);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		public void setGameInfo(GameInfo header) {
			model.setHeader(header);
		}
		
		public GameInfo getGameInfo() {
			return model.getGameInfo();
		}
		
		// It's implemented a little differently client-side
		@Override
		public synchronized void rollDice(PlayerReference player) {
			Player currentPlayer = getCurrentPlayer().getPlayer();
			
			currentPlayer.setHasRolled(true);
		}
		
		public void notifyGameFinished() {
			if (poller.isRunning()) {
				poller.stop();
			}
			this.model = new CatanModel();
			for (IModelListener listener : listeners) {
				try {
					listener.gameFinished();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void setPoller(ServerPoller poller) {
			this.poller = poller;
			
		}
}
