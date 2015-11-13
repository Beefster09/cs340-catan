package shared.model;

import java.util.*;

import client.data.GameInfo;
import shared.communication.GameHeader;
import shared.communication.PlayerHeader;
import shared.definitions.ResourceType;
import shared.definitions.TurnStatus;
import shared.exceptions.InsufficientResourcesException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.NotYourTurnException;
import shared.exceptions.TradeException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
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

		log.add(player.getName(), "upgraded a settlement into a city.");
		
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
		
		log.add(player.getName(), "placed a starting road.");
		
		++version;
	}

	void buildRoad(PlayerReference player, EdgeLocation loc)
			throws InvalidActionException, InsufficientResourcesException {
		if (!map.canBuildRoadAt(player, loc)) {
			throw new InvalidActionException("Invalid Road Placement");
		}
		// Check resource counts
		ResourceList hand = player.getHand();
		if (player.getPlayer().canBuildRoad()) {
			throw new InsufficientResourcesException("Insufficient " +
					"resources for a road.");
		}
		ResourceList bank = getBank().getResources();
		hand.transferTo(bank, ResourceType.WOOD, 1);
		hand.transferTo(bank, ResourceType.BRICK, 1);
		getMap().buildRoad(player, loc);
		
		log.add(player.getName(), "built a road.");
		
		++version;
	}

	void buildStartingSettlement(PlayerReference player, VertexLocation loc) throws InvalidActionException {
		if (!map.canPlaceStartingSettlement(loc)) {
			throw new InvalidActionException("Invalid Starting Road Placement");
		}
		// This movement is free.
		map.buildStartingSettlement(player, loc);
		
		// Give starting resources
		if (turnTracker.getStatus() == TurnStatus.SecondRound) {
			ResourceList bank = this.bank.getResources();
			ResourceList hand = player.getPlayer().getResources();
			for (HexLocation hexLoc : loc.getHexes()) {
				try {
					Hex hex = map.getHexAt(hexLoc);
					ResourceType resource = hex.getResource();
					if (resource != null) {
						bank.transferTo(hand, resource, 1);
					}
				} catch (IndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		
		log.add(player.getName(), "placed a starting settlement.");
		
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
		ResourceList hand = player.getHand();
		ResourceList bank = getBank().getResources();
		hand.transferTo(bank, ResourceType.WOOD, 1);
		hand.transferTo(bank, ResourceType.BRICK, 1);
		hand.transferTo(bank, ResourceType.SHEEP, 1);
		hand.transferTo(bank, ResourceType.WHEAT, 1);
		map.buildSettlement(player, loc);
		
		log.add(player.getName(), "built a road.");
		
		++version;
	}

	/**
	 * @param roll
	 * @pre The current phase is the rolling phase and the roll is valid
	 * @post Appropriate resources will be given, the robber will trigger if a 7 was rolled,
	 * and players will be required to discard if necessary.
	 */
	void roll(int roll) {
		assert turnTracker.getStatus() == TurnStatus.Rolling;
		assert roll >= 2 && roll <= 12;
		
		// Give resources to the appropriate players
		ResourceList resBank = bank.getResources();
		for (Hex hex : map.getHexesByNumber(roll)) {
			for (Municipality town : map.getMunicipalitiesAround(hex.getLocation())) {
				try {
					resBank.transferTo(town.getOwner().getPlayer().getResources(),
							hex.getResource(), town.getIncome());
				} catch (InsufficientResourcesException e) {
					// Sucks to be you. You don't get your resources.
					// TODO? Give resources one at a time in turn order?
				}
			}
		}
		
		// Change the status of the game
		turnTracker.roll(roll);
		
		turnTracker.getCurrentPlayer().getPlayer().setHasRolled(true);
		
		log.add(turnTracker.getCurrentPlayer().getName(), "rolled a " + roll);
		
		++version;
	}

	void finishTurn() throws InvalidActionException {
		assert tradeOffer == null;
		
		PlayerReference curPlayer = turnTracker.getCurrentPlayer();
		
		turnTracker.passTurn();
		
		log.add(curPlayer.getName(), "finished their turn.");
		
		++version;
	}

	void maritimeTrade(PlayerReference player,
			ResourceType fromResource, ResourceType toResource) throws InsufficientResourcesException {
		assert canMaritimeTrade(player, fromResource, toResource);
		
		ResourceList bankRes = bank.getResources();
		ResourceList hand = player.getPlayer().getResources();
		
		hand.transferTo(bankRes, fromResource, getMaritimeRatios(player).get(fromResource));
		bankRes.transferTo(hand, toResource, 1);
		
		log.add(player.getName(), "traded " + fromResource + " for " +
					toResource + " with the bank.");
		
		++version;
	}

	public Map<ResourceType, Integer> getMaritimeRatios(PlayerReference player) {
		Map<ResourceType, Integer> ratios = new HashMap<>();
		int defaultRatio = 4;
		
		Board map = getMap();
		
		for (Port port : map.getPorts()) {
			if (player.equals(map.getOwnerOfPortAt(port.getLocation()))) {
				if (port.getResource() == null) {
					defaultRatio = 3;
				}
				else {
					ratios.put(port.getResource(), 2);
				}
			}
		}
		
		for (ResourceType resource : ResourceType.values()) {
			if (!ratios.containsKey(resource)) {
				ratios.put(resource, defaultRatio);
			}
		}
		
		return ratios;
	}

	public boolean canMaritimeTrade(PlayerReference player, ResourceType fromResource, ResourceType toResource) {
		// It must be your turn to trade
		if (!isTurn(player)) {
			return false;
		}
		
		ResourceList bank = getBank().getResources();
		
		if (bank.count(toResource) < 1) {
			return false;
		}
		
		Map<ResourceType, Integer> ratios = getMaritimeRatios(player);
		
		return player.getPlayer().getResources().count(fromResource) >= ratios.get(fromResource);
	}

	void acceptTrade() throws TradeException {
		tradeOffer.makeTrade();
		
		log.add(tradeOffer.getReceiver().getName(), "accepted " +
				tradeOffer.getSender().getName() + "'s trade offer.");
		
		tradeOffer = null;
		
		++version;
	}

	void declineTrade() {
		log.add(tradeOffer.getReceiver().getName(), "declined " +
				tradeOffer.getSender().getName() + "'s trade offer.");
		
		tradeOffer = null;
		
		++version;
	}

	void offerTrade(TradeOffer offer) throws InvalidActionException {
		if (tradeOffer != null) {
			throw new InvalidActionException("You cannot offer a trade while " +
					"there is already a trade waiting to be accepted");
		}
		
		log.add(offer.getSender().getName(), "offered to trade with " + 
				offer.getReceiver().getName());
		
		tradeOffer = offer;
		
		++version;
	}
	
	
}
