package server.ai;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import server.communication.Server;
import shared.IDice;
import shared.NormalDice;
import shared.communication.IServer;
import shared.definitions.TurnStatus;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.AbstractModelListener;
import shared.model.ModelFacade;
import shared.model.TradeOffer;
import shared.model.TurnTracker;

public class AIManager extends AbstractModelListener {
	
	private IServer server = Server.getSingleton();
	
	private Map<UUID, AIPlayer> aiPlayers = new HashMap<>();
	private IDice dice = new NormalDice();
	
	private ModelFacade game;
	
	public AIManager (ModelFacade game) {
		this.game = game;
	}
	
	/**
	 * @return the gameid
	 */
	UUID getGameID() {
		return game.getUUID();
	}
	
	public void addAIPlayer(AIPlayer ai) {
		aiPlayers.put(ai.getPlayerID(), ai);
	}
	
	public Collection<AIPlayer> getPlayers() {
		return aiPlayers.values();
	}

	@Override
	public void turnTrackerChanged(TurnTracker turnTracker) {
		if (turnTracker.getStatus() == TurnStatus.Discarding) {
			for (AIPlayer ai : aiPlayers.values()) {
				if (!ai.getPlayer().hasDiscarded()) {
					ai.discard();
					return; // Only process one AI per call
				}
			}
		}
		
		UUID current = turnTracker.getCurrentPlayer().getPlayerUUID();
		if (aiPlayers.containsKey(current)) {
			try {
				switch(turnTracker.getStatus()) {
				case FirstRound:
					aiPlayers.get(current).firstRound();
					break;
				case SecondRound:
					aiPlayers.get(current).secondRound();
					break;
				case Rolling:
					server.rollDice(current, getGameID(), dice.roll());
					
					turnTracker = game.getCatanModel().getTurnTracker();
					break;
				case Robbing:
					aiPlayers.get(current).robber();
					break;
				case Playing:
					aiPlayers.get(current).takeTurn();
	
					server.finishTurn(current, getGameID());
					break;
				default:
					break;
				}
			} catch (ServerException | UserException e) {
				e.printStackTrace();
				// probably a bad thing
			}
		}
	}

	@Override
	public void tradeOfferChanged(TradeOffer offer) {
		if (offer == null) {
			return;
		}
		UUID receiver = offer.getReceiver().getPlayerUUID();
		if (aiPlayers.containsKey(receiver)) {
			boolean shouldAccept = aiPlayers.get(receiver).tradeOffered(offer);
			try {
				server.respondToTrade(receiver, getGameID(), shouldAccept);
			} catch (ServerException | UserException e) {
				e.printStackTrace();
				// This is probably bad.
			}
		}
	}
	
}
