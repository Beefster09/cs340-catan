package shared.model;

import java.util.List;

import shared.communication.Session;

/**
 * Contains all information about the current game: references the map, players, chat, and bank
 * for the current game.  All information relevant to a particular game can be accessed through
 * this class.
 * @author Jordan
 *
 */
public class CatanModel {
	private MessageList chat;
	private MessageList log;
	private Board map;
	private TradeOffer tradeOffer;
	private TurnTracker turnTracker;
	private Bank bank;
	private List<Player> players;
	private PlayerReference longestRoad;
	private PlayerReference largestArmy;
	private PlayerReference winner;
	private Session localPlayer;
	
	private int version;

	public CatanModel() {
		
	}

	/**
	 * @param chat
	 * @param log
	 * @param map
	 * @param tradeOffer
	 * @param turnTracker
	 * @param bank
	 * @param players
	 * @param longestRoad
	 * @param largestArmy
	 * @param winner
	 * @param version
	 */
	
	public CatanModel(MessageList chat, MessageList log, Board map,
			TradeOffer tradeOffer, TurnTracker turnTracker, Bank bank,
			List<Player> players, PlayerReference longestRoad,
			PlayerReference largestArmy, PlayerReference winner, int version) {
		super();
		this.chat = chat;
		this.log = log;
		this.map = map;
		this.tradeOffer = tradeOffer;
		this.turnTracker = turnTracker;
		this.bank = bank;
		this.players = players;
		this.longestRoad = longestRoad;
		this.largestArmy = largestArmy;
		this.winner = winner;
		this.version = version;
	}

	/**
	 * @return the chat
	 */
	public MessageList getChat() {
		return chat;
	}

	/**
	 * @return the log
	 */
	public MessageList getLog() {
		return log;
	}

	/**
	 * @return the tradeOffer
	 */
	public TradeOffer getTradeOffer() {
		return tradeOffer;
	}

	/**
	 * @param tradeOffer the tradeOffer to set
	 */
	public void setTradeOffer(TradeOffer tradeOffer) {
		this.tradeOffer = tradeOffer;
	}

	/**
	 * @return the longestRoad
	 */
	public PlayerReference getLongestRoad() {
		return longestRoad;
	}

	/**
	 * @param longestRoad the longestRoad to set
	 */
	public void setLongestRoad(PlayerReference longestRoad) {
		this.longestRoad = longestRoad;
	}

	/**
	 * @return the largestArmy
	 */
	public PlayerReference getLargestArmy() {
		return largestArmy;
	}

	/**
	 * @param largestArmy the largestArmy to set
	 */
	public void setLargestArmy(PlayerReference largestArmy) {
		this.largestArmy = largestArmy;
	}

	/**
	 * @return the map
	 */
	public Board getMap() {
		return map;
	}

	/**
	 * @return the turnTracker
	 */
	public TurnTracker getTurnTracker() {
		return turnTracker;
	}

	/**
	 * @return the bank
	 */
	public Bank getBank() {
		return bank;
	}

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @return the winner
	 */
	public PlayerReference getWinner() {
		return winner;
	}

	public void setChat(MessageList chat) {
		this.chat = chat;
	}

	public void setLog(MessageList log) {
		this.log = log;
	}

	public void setMap(Board map) {
		this.map = map;
	}

	public void setTurnTracker(TurnTracker turnTracker) {
		this.turnTracker = turnTracker;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public void setWinner(PlayerReference winner) {
		this.winner = winner;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	public Session getLocalPlayer() {
		return localPlayer;
	}

	public void setLocalPlayer(Session localPlayer) {
		this.localPlayer = localPlayer;
	}
	
	
}
