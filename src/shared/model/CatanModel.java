package shared.model;

import java.util.List;
import java.util.UUID;

import client.data.GameInfo;
import shared.communication.GameHeader;

/**
 * Contains all information about the current game: references the map, players, chat, and bank
 * for the current game.  All information relevant to a particular game can be accessed through
 * this class.
 * @author Jordan
 *
 */
public class CatanModel {
	private GameHeader header;
	
	private UUID id;
	private String title;
	
	private MessageList chat;
	private MessageList log;
	private Board map;
	private TradeOffer tradeOffer;
	private TurnTracker turnTracker;
	private Bank bank;
	private List<Player> players;
	private PlayerReference longestRoad;
	private PlayerReference largestArmy;
	
	private PlayerReference winner = null;
	
	private int version;

	/** Makes a brand spanking new Model
	 * 
	 */
	public CatanModel() {
		version = -1;
		winner = null;
		
		id = UUID.randomUUID();
	}
	
	public UUID getID() {
		return id;
	}
	
	public int getShortID() {
		return id.hashCode();
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

	void setLog(MessageList log) {
		this.log = log;
	}

	void setMap(Board map) {
		this.map = map;
	}

	void setTurnTracker(TurnTracker turnTracker) {
		this.turnTracker = turnTracker;
	}

	void setBank(Bank bank) {
		this.bank = bank;
	}

	void setPlayers(List<Player> players) {
		this.players = players;
	}

	void setWinner(PlayerReference winner) {
		this.winner = winner;
	}

	void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	public GameInfo getGameInfo() {
		if (header != null)
			return new GameInfo(header);
		return null;
	}

	public GameHeader getHeader() {
		return header;
	}

	public void setHeader(GameInfo info) {
		this.header = new GameHeader(info);
	}

	public void setHeader(GameHeader gameHeader) {
		this.header = gameHeader;
	}

	public String getTitle() {
		return title;
	}

	void setTitle(String title) {
		this.title = title;
	}
	
	
}
