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

	private boolean ttRecursionGuard = false;
	@Override
	public void turnTrackerChanged(TurnTracker turnTracker) {
		// Keep this from being recursed (and thus, running certain logic twice)
		if (ttRecursionGuard) {
			return;
		}
		ttRecursionGuard = true;
		UUID current = turnTracker.getCurrentPlayer().getPlayerUUID();
		if (aiPlayers.containsKey(current)) {
			try {
				switch(turnTracker.getStatus()) {
				case FirstRound:
					ttRecursionGuard = false;
					aiPlayers.get(current).firstRound();
					break;
				case SecondRound:
					ttRecursionGuard = false;
					aiPlayers.get(current).secondRound();
					break;
				case Rolling:
					server.rollDice(current, getGameID(), dice.roll());
					
					turnTracker = game.getCatanModel().getTurnTracker();
					// no break
				case Discarding:
					if (turnTracker.getStatus() == TurnStatus.Discarding) {
						for (AIPlayer ai : aiPlayers.values()) {
							ai.discard();
						}
						turnTracker = game.getCatanModel().getTurnTracker();
						if (turnTracker.getStatus() == TurnStatus.Discarding) {
							ttRecursionGuard = false;
							return; // Some human players still need to discard
						}
					}
					// no break
				case Robbing:
					if (turnTracker.getStatus() == TurnStatus.Robbing) {
						aiPlayers.get(current).robber();
					}
					// no break
				case Playing:
					aiPlayers.get(current).takeTurn();
					
					ttRecursionGuard = false;
	
					server.finishTurn(current, getGameID());
					break;
				}
			} catch (ServerException | UserException e) {
				e.printStackTrace();
				// probably a bad thing
			}
			ttRecursionGuard = false;
		}
	}

	@Override
	public void tradeOfferChanged(TradeOffer offer) {
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
