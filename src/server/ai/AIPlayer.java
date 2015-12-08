package server.ai;

import java.util.UUID;

import server.communication.Server;
import shared.communication.IServer;
import shared.model.Player;
import shared.model.TradeOffer;

public abstract class AIPlayer {
	
	private final IServer server = Server.getSingleton();
	private final UUID gameid;
	private final Player player;
	
	/**
	 * @param server
	 * @param player
	 */
	public AIPlayer(UUID gameid, Player player) {
		super();
		this.gameid = gameid;
		this.player = player;
	}
	
	public UUID getPlayerID() {
		return player.getUUID();
	}

	public UUID getGameID() {
		return gameid;
	}

	/**
	 * @return the server
	 */
	protected IServer getServer() {
		return server;
	}

	/**
	 * @return the player
	 */
	protected Player getPlayer() {
		return player;
	}

	public abstract void firstRound();
	public abstract void secondRound();

	/** Implements the algorithm for taking a turn
	 * This is called AFTER the dice are rolled and BEFORE the turn is ended
	 */
	public void takeTurn() {
		
	}
	
	/** Determine what to do with a trade that was offered to you
	 * Called on the receiver of the trade (if it is an AI)
	 * @param trade
	 * @returns whether or not the trade should be accepted. False by default.
	 */
	public boolean tradeOffered(TradeOffer trade) {
		return false;
	}
	
	
	/**
	 * Called when you need to discard
	 */
	public abstract void discard();
	
	/**
	 * Called when you rolled a 7 and need to move the robber
	 */
	public abstract void robber();

}
