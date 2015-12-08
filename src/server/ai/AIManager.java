package server.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	
	private Map<UUID, AIPlayer> aiPlayers;
	private ModelFacade game;
	private IServer server;
	private IDice dice = new NormalDice();
	
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
					// no break
				case Discarding:
					if (turnTracker.getStatus() == TurnStatus.Discarding) {
						for (AIPlayer ai : aiPlayers.values()) {
							ai.discard();
						}
						turnTracker = game.getCatanModel().getTurnTracker();
						if (turnTracker.getStatus() == TurnStatus.Discarding) {
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
	
					server.finishTurn(current, getGameID());
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
