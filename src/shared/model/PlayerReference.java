package shared.model;

/** Represents an immutable reference to a player in a game.
 * This class's purpose is to make JSON serialization easier.
 * @author Justin
 *
 */
public class PlayerReference {
	// FUTURE: use UUIDs as indices into a global Player table
	private CatanModel game;
	private int playerIndex;
	
	// This is intentionally package-private. Really, only Players should ever
	// create PlayerReferences
	/**
	 * @param game
	 * @param playerIndex
	 */
	public PlayerReference(CatanModel game, int playerIndex) {
		super();
		this.game = game;
		this.playerIndex = playerIndex;
	}
	
	/** Gets the player that this object references.
	 * @return the player 'pointed' to by this PlayerReference
	 */
	public Player getPlayer() {
		return game.getPlayers().get(playerIndex);
	}
	
	public int getIndex() {
		return playerIndex;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlayerReference [playerIndex=" + playerIndex + "]";
	}
}
