package shared.model;

import java.util.*;

import shared.communication.GameHeader;
import shared.definitions.CatanColor;

/** Represents an immutable reference to a player in a game.
 * This class's purpose is to make JSON serialization easier.
 * @author Justin
 *
 */
public class PlayerReference {
	private static final int INVALID_INDEX = -1337;
	//private CatanModel game;
	private int playerIndex; // Old compatibility
	private UUID playerUUID;
	
	// This is intentionally package-private (i.e. no modifier). Really, only Players
	// should ever create PlayerReferences
	/** Old server compatible constructor
	 * @param game
	 * @param playerIndex
	 */
	private PlayerReference(int playerIndex) {
		this.playerIndex = playerIndex;
		playerUUID = Player.generateUUID(-1, playerIndex);
	}
	
	// Needed for backwards compatibility
	public PlayerReference(CatanModel game, int playerIndex) {
		assert game != null;
		this.playerIndex = playerIndex;
		playerUUID = game.getPlayers().get(playerIndex).getUUID();
	}
	
	// This is for debugging with the old server.
	public static PlayerReference getDummyPlayerReference(int playerIndex) {
		return new PlayerReference(playerIndex);
	}
	
	public PlayerReference(UUID uuid) {
		playerUUID = uuid;
		playerIndex = INVALID_INDEX;
	}
	
	public PlayerReference(UUID uuid, int index) {
		playerUUID = uuid;
		playerIndex = index;
	}
	
	public PlayerReference(String string) {
		playerUUID = UUID.fromString(string);
	}

	/** Gets the player that this object references.
	 * @return the player 'pointed' to by this PlayerReference
	 */
	public Player getPlayer() {
		return Player.getPlayerByUUID(playerUUID);
	}
	
	public int getIndex() {
		if (playerIndex == INVALID_INDEX) {
			return playerIndex = getPlayer().getPlayerIndex();
		}
		else return playerIndex;
	}
	
	public CatanColor getColor() {
		return getPlayer().getColor();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return playerUUID.hashCode();
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
		if (!playerUUID.equals(other.playerUUID))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlayerReference [playerIndex=" + playerIndex + ", playerUUID="
				+ playerUUID + "]";
	}
	
}
