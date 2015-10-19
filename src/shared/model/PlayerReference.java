package shared.model;

/** Represents an immutable reference to a player in a game.
 * This class's purpose is to make JSON serialization easier.
 * @author Justin
 *
 */
public class PlayerReference {
	// TODO FUTURE: use UUIDs as indices into a global Player table
	private CatanModel game;
	private int playerIndex;
	
	// This is intentionally package-private (i.e. no modifier). Really, only Players
	// should ever create PlayerReferences
	/**
	 * @param game
	 * @param playerIndex
	 */
	PlayerReference(CatanModel game, int playerIndex) {
		super();
		this.game = game;
		this.playerIndex = playerIndex;
	}
	
	// This is for debugging with the old server.
	public static PlayerReference getDummyPlayerReference(int playerIndex) {
		return new PlayerReference(null, playerIndex);
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
	
	// TODO: incomplete... needed by boards;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + playerIndex;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerReference other = (PlayerReference) obj;
		if (playerIndex != other.playerIndex)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlayerReference [playerIndex=" + playerIndex + "]";
	}
}
