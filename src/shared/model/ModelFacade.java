package shared.model;

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
			return true;
		}
		
		/**
		 * @param hexLoc The location on the map where the robber is to be placed
		 * @return true if the hex is not a desert hex.
		 * @return false otherwise
		 */
		public synchronized boolean canRob(HexLocation hexLoc) {
			return true;
		}
		
		public synchronized void doRob() {
		}
		
		/**
		 * 
		 * @return true if the player has already rolled the die
		 * @return false otherwise
		 */
		public synchronized boolean canFinishTurn() {
			return true;
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
			return true;
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
		public synchronized boolean canBuildRoad(EdgeLocation edgeLoc) {
			return true;
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
			return true;
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
			return true;
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
			return true;
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
			return true;
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
			return true;
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
			return true;
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
			return true;
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
			return true;
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
		public synchronized boolean canDiscardCards() {
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
