package shared.model;

import java.util.*;

import client.data.GameInfo;
import shared.communication.GameHeader;
import shared.communication.PlayerHeader;
import shared.definitions.ResourceType;
import shared.exceptions.InsufficientResourcesException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.NotYourTurnException;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

/**
 * Contains all information about the current game: references the map, players, chat, and bank
 * for the current game.  All information relevant to a particular game can be accessed through
 * this class.
 * @author Jordan
 *
 */
public class CatanModel {	
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
		return new GameInfo(getHeader());
	}

	public GameHeader getHeader() {
		List<PlayerHeader> players = new ArrayList<>();
		for (Player player : getPlayers()) {
			players.add(player.getHeader());
		}
		return new GameHeader(title, id, players);
	}

	public void setHeader(GameInfo info) {
		title  = info.getTitle();
		id = info.getUUID();
		
	}

	public void setHeader(GameHeader gameHeader) {
		title  = gameHeader.getTitle();
		id = gameHeader.getUUID();
	}

	public String getTitle() {
		return title;
	}

	void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @param player TODO
	 * @param loc TODO
	 * @return
	 * @throws InvalidActionException 
	 */
	void buildCity(PlayerReference player, VertexLocation loc)
			throws InvalidActionException {
		if (!isTurn(player)) {
			throw new NotYourTurnException();
		}
		if (!player.getPlayer().canBuildCity()) {
			throw new InsufficientResourcesException("You do not have the sufficient " +
					"resources for a city.");
		}
		if (!canBuildCity(player, loc)) {
			throw new InvalidActionException("You must build a city over one " +
					"of your existing settlements.");
		}
		
		ResourceList hand = player.getPlayer().getResources();
		ResourceList bank = getBank().getResources();
		hand.transferTo(bank, ResourceType.ORE, 3);
		hand.transferTo(bank, ResourceType.WHEAT, 2);
		
		getMap().upgradeSettlementAt(player, loc);
		
		++version;
	}

	public boolean canBuildCity(PlayerReference player,	VertexLocation loc) {
		return isTurn(player) && player.getPlayer().canBuildCity()
				&& map.canBuildCity(player, loc);
	}

	public boolean isTurn(PlayerReference player) {
		return player.equals(turnTracker.getCurrentPlayer());
	}

	void buildStartingRoad(PlayerReference player, EdgeLocation loc)
			throws InvalidActionException {
		if (!map.canBuildStartingRoadAt(player, loc)) {
			throw new InvalidActionException("Invalid Starting Road Placement");
		}
		// This movement is free.
		getMap().buildStartingRoad(player, loc);
		
		++version;
	}

	void buildRoad(PlayerReference player, EdgeLocation loc)
			throws InvalidActionException, InsufficientResourcesException {
		if (!map.canBuildRoadAt(player, loc)) {
			throw new InvalidActionException("Invalid Road Placement");
		}
		// Check resource counts
		ResourceList hand = player.getPlayer().getResources();
		if (player.getPlayer().canBuildRoad()) {
			throw new InsufficientResourcesException("Insufficient " +
					"resources for a road.");
		}
		ResourceList bank = getBank().getResources();
		hand.transferTo(bank, ResourceType.WOOD, 1);
		hand.transferTo(bank, ResourceType.BRICK, 1);
		getMap().buildRoad(player, loc);
		
		++version;
	}

	void buildStartingSettlement(PlayerReference player, VertexLocation loc) throws InvalidActionException {
		if (!map.canPlaceStartingSettlement(loc)) {
			throw new InvalidActionException("Invalid Starting Road Placement");
		}
		// This movement is free.
		map.buildStartingSettlement(player, loc);
		
		++version;
	}

	void buildSettlement(PlayerReference player, VertexLocation loc)
			throws InvalidActionException, InsufficientResourcesException {
		if (!map.canBuildSettlement(player, loc)) {
			throw new InvalidActionException("Invalid Road Placement");
		}
		if (player.getPlayer().canBuildSettlement()) {
			throw new InsufficientResourcesException("Insufficient resources " +
					"for a settlement.");
		}
		// Check resource counts
		ResourceList hand = player.getPlayer().getResources();
		ResourceList bank = getBank().getResources();
		hand.transferTo(bank, ResourceType.WOOD, 1);
		hand.transferTo(bank, ResourceType.BRICK, 1);
		hand.transferTo(bank, ResourceType.SHEEP, 1);
		hand.transferTo(bank, ResourceType.WHEAT, 1);
		map.buildSettlement(player, loc);
		
		++version;
	}
	
	
}
