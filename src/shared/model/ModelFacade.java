package shared.model;

import java.util.Collection;
import java.util.Map;

import shared.IDice;
import shared.NormalDice;
import shared.communication.GameHeader;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.definitions.TurnStatus;
import shared.exceptions.InsufficientResourcesException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.NotYourTurnException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

public class ModelFacade {

	protected CatanModel model;
	private IDice dice;

	public ModelFacade() {
		this(new CatanModel(), new NormalDice());
	}

	public ModelFacade(CatanModel startingModel) {
		this(startingModel, new NormalDice());
	}

	public ModelFacade(CatanModel startingModel, IDice dice) {
		model = startingModel;
		
		this.dice = dice;
	}

	public synchronized CatanModel getCatanModel() {
		return model;
	}

	public synchronized PlayerReference getCurrentPlayer() {
		return model.getTurnTracker().getCurrentPlayer();
	}

	public synchronized GameHeader getGameHeader() {
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

	public synchronized void rollDice(PlayerReference player) throws NotYourTurnException {
		if (!isTurn(player)) {
			throw new NotYourTurnException();
		}
		
		model.roll(dice.roll());
		
		player.getPlayer().setHasRolled(true);
	}

	/**
	 * @param hexLoc The location on the map where the robber is to be placed
	 * @return true if the hex is not a desert hex.
	 * @return false otherwise
	 */
	public synchronized boolean canRob(HexLocation hexLoc) {		
		return model.getMap().canMoveRobberTo(hexLoc);
	}

	public synchronized void doRob() {
			
	}

	/**
	 * 
	 * @return true if the player has already rolled the die
	 * @return false otherwise
	 */
	public synchronized boolean canFinishTurn() {
		return !getCurrentPlayer().getPlayer().hasRolled();
	}

	/**
	 * 
	 * @return true if the player has already rolled the die
	 * @return false otherwise
	 */
	public synchronized boolean canFinishTurn(PlayerReference player) {
		return isTurn(player) && !player.getPlayer().hasRolled();
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
		// Make sure there are development cards in the bank.
		if (getCatanModel().getBank().getDevCards().count() <= 0) {
			return false;
		}
		
		return getCurrentPlayer().getPlayer().canBuyDevCard();
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
				
		try {
			Board map = model.getMap();
			PlayerReference currentPlayer = getCurrentPlayer();
			return map.canBuildRoadAt(currentPlayer, edgeLoc);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		
	}

	public synchronized void buildRoad(PlayerReference player, EdgeLocation loc)
			throws InvalidActionException {
				if (!isTurn(player)) {
					throw new NotYourTurnException();
				}
				TurnStatus phase = currentPhase();
				if (phase == TurnStatus.FirstRound || phase == TurnStatus.SecondRound) {
					model.buildStartingRoad(player, loc);
				}
				else {
					model.buildRoad(player, loc);
				}
			}

	public synchronized boolean canBuildStartingRoad(EdgeLocation loc) {
		try {
			Board map = model.getMap();
			PlayerReference currentPlayer = getCurrentPlayer();
			return map.canBuildStartingRoadAt(currentPlayer, loc);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
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

	public synchronized void buildSettlement(PlayerReference player, VertexLocation loc)
			throws InvalidActionException {
				if (!isTurn(player)) {
					throw new NotYourTurnException();
				}
				TurnStatus phase = currentPhase();
				if (phase == TurnStatus.FirstRound || phase == TurnStatus.SecondRound) {
					// Give starting resources- Do this before so that it will be in sync
					// with the model's version.
					if (phase == TurnStatus.SecondRound) {
						giveStartingResources(player, loc);
					}
					
					model.buildStartingSettlement(player, loc);
				}
				else {
					model.buildSettlement(player, loc);
				}
			}

	private void giveStartingResources(PlayerReference player, VertexLocation loc)
			throws InsufficientResourcesException {
				ResourceList bank = model.getBank().getResources();
				ResourceList hand = player.getPlayer().getResources();
				for (HexLocation hexLoc : loc.getHexes()) {
					try {
						Hex hex = model.getMap().getHexAt(hexLoc);
						ResourceType resource = hex.getResource();
						if (resource != null) {
							bank.transferTo(hand, resource, 1);
						}
					} catch (IndexOutOfBoundsException e) {
						continue;
					}
				}
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
		
		return model.canBuildCity(currentPlayer, vertexLoc);
	}

	/**
	 * 
	 * @param vertexLoc
	 * @return
	 * @throws InvalidActionException 
	 */
	public synchronized void buildCity(PlayerReference player, VertexLocation loc)
			throws InvalidActionException {
				model.buildCity(player, loc);
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

	public synchronized Collection<Municipality> getMunicipalitiesAround(HexLocation hex) {
		return model.getMap().getMunicipalitiesAround(hex);
	}

	protected TurnStatus currentPhase() {
		return model.getTurnTracker().getStatus();
	}

	protected boolean isTurn(PlayerReference player) {
		return model.isTurn(player);
	}

}