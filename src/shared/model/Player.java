package shared.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;

import shared.communication.GameHeader;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.exceptions.InsufficientResourcesException;
import shared.exceptions.SchemaMismatchException;

/**
 * Represents a player in a given game and everything associated with that player
 * @author Jordan
 *
 */
public class Player {
	// Table for retrieving players by UUID
	private static Map<UUID, Player> playerTable = new HashMap<>();
	
	// Meta-information
	private UUID uuid; // For PlayerReferences
	
	private CatanModel game; // This is so you can get PlayerReferences.
	private int playerIndex;
	
	private int playerID = -1337; // Arbitrary default.
	private String name;
	private CatanColor color;
	
	private DevCardList newDevCards;
	private DevCardList oldDevCards;
	private ResourceList resources;
	
	private boolean playedDevCard 	= false;
	private boolean discarded 		= false;
	private boolean hasRolled 		= false;
	
	private int cities 			= 4;
	private int settlements 	= 5;
	private int roads 			= 15;
	private int soldiers		= 0;
	private int monuments		= 0;
	private int victoryPoints 	= 0;	
	
	static UUID generateUUID(int gameid, int index) {
		byte[] data = new byte[8];

		data[0] = (byte)(gameid & 0xFF);
		data[1] = (byte)((gameid >> 8) & 0xFF);
		data[2] = (byte)((gameid >> 16) & 0xFF);
		data[3] = (byte)((gameid >> 24) & 0xFF);
		data[4] = (byte)(index & 0xFF);
		data[5] = (byte)((index >> 8) & 0xFF);
		data[6] = (byte)((index >> 16) & 0xFF);
		data[7] = (byte)((index >> 24) & 0xFF);
		
		return UUID.nameUUIDFromBytes(data);
	}

	static UUID generateUUID(CatanModel game, int index) {
		return generateUUID(game.getHeader().getId(), index);
	}

	static UUID generateUUID(GameHeader header, int index) {
		return generateUUID(header.getId(), index);
	}
	
	private void assignUUID(CatanModel game, int index) {
		uuid = generateUUID(game, index);
		playerTable.put(uuid, this);
	}
	
	public Player(CatanModel game, int index) {
		this.game = game;
		playerIndex = index;
		assignUUID(game, index);
	}

	public Player(CatanModel game, JSONObject json) throws SchemaMismatchException {
		this.game = game;
		
		try {
			playerIndex	= (int) (long) json.get("playerIndex");
			assignUUID(game, playerIndex);
			playerID	= (int) (long) json.get("playerID");
			
			name = (String) json.get("name");
			color = CatanColor.getColorFromString((String) json.get("color"));
			
			resources = ResourceList.fromJSONObject((JSONObject) json.get("resources")); 
			newDevCards = DevCardList.fromJSONObject((JSONObject) json.get("newDevCards")); 
			oldDevCards = DevCardList.fromJSONObject((JSONObject) json.get("oldDevCards")); 

			playedDevCard	= (boolean) json.get("playedDevCard");
			discarded		= (boolean) json.get("discarded");
			
			settlements 	= (int) (long) json.get("settlements");
			cities 			= (int) (long) json.get("cities");
			roads 			= (int) (long) json.get("roads");
			soldiers		= (int) (long) json.get("soldiers");
			monuments		= (int) (long) json.get("monuments");
			victoryPoints	= (int) (long) json.get("victoryPoints");
		}
		catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a Player:\n" + json.toJSONString());
		}
	}
	
	/** Gives a PlayerReference that refers to this player. 
	 * <p>Gives a PlayerReference such that a call to getPlayer() on the result of this 
	 * function will return this instance of Player</p>
	 * @return a corresponding PlayerReference
	 */
	public PlayerReference getReference() {
		return new PlayerReference(uuid);
	}
	
	public static Player getPlayerByUUID(UUID uuid) {
		if (playerTable.containsKey(uuid)) {
			return playerTable.get(uuid);
		}
		else {
			throw new IllegalArgumentException("Unrecognized UUID: " + uuid.toString());
		}
	}

	/**
	 * @return the resources
	 */
	public ResourceList getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 * @throws InsufficientResourcesException 
	 */
	public void setResources(ResourceList resources) throws InsufficientResourcesException {
		for (ResourceType type : ResourceType.values()) {
			int numResourceCardsForPlayer = resources.count(type);
			if (numResourceCardsForPlayer > 19)
				throw new InsufficientResourcesException();
			if (game.getBank().getResources().count(type) + numResourceCardsForPlayer > 19)
				throw new InsufficientResourcesException();
		}
		this.resources = resources;
	}

	/**
	 * @return the cities
	 */
	public int getCities() {
		return cities;
	}

	/**
	 * @param cities the cities to set
	 */
	public void setCities(int cities) {
		this.cities = cities;
	}

	/**
	 * @return the roads
	 */
	public int getRoads() {
		return roads;
	}

	/**
	 * @param roads the roads to set
	 */
	public void setRoads(int roads) {
		this.roads = roads;
	}

	/**
	 * @return the settlements
	 */
	public int getSettlements() {
		return settlements;
	}

	/**
	 * @param settlements the settlements to set
	 */
	public void setSettlements(int settlements) {
		this.settlements = settlements;
	}

	/**
	 * @return the soldiers
	 */
	public int getSoldiers() {
		return soldiers;
	}

	/**
	 * @param soldiers the soldiers to set
	 */
	public void setSoldiers(int soldiers) {
		this.soldiers = soldiers;
	}

	/**
	 * @return the color
	 */
	public CatanColor getColor() {
		return color;
	}

	/**
	 * @return the discarded
	 */
	public boolean hasDiscarded() {
		return discarded;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the newDevCards
	 */
	public DevCardList getNewDevCards() {
		return newDevCards;
	}

	/**
	 * @return the oldDevCards
	 */
	public DevCardList getOldDevCards() {
		return oldDevCards;
	}

	/**
	 * @return the playedDevCard
	 */
	public boolean hasPlayedDevCard() {
		return playedDevCard;
	}

	/**
	 * @return the monuments
	 */
	public int getMonuments() {
		return monuments;
	}

	/**
	 * @return the victoryPoints
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * @return the playerIndex
	 */
	public int getPlayerIndex() {
		return playerIndex;
	}

	/**
	 * @return the playerID
	 */
	public int getPlayerID() {
		return playerID;
	}

	public boolean hasRolled() {
		return hasRolled;
	}

	public void setHasRolled(boolean hasRolled) {
		this.hasRolled = hasRolled;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player [game=" + game + ", playerIndex=" + playerIndex
				+ ", playerID=" + playerID + ", name=" + name + ", color="
				+ color + ", newDevCards=" + newDevCards + ", oldDevCards="
				+ oldDevCards + ", resources=" + resources + ", playedDevCard="
				+ playedDevCard + ", discarded=" + discarded + ", cities="
				+ cities + ", settlements=" + settlements + ", roads=" + roads
				+ ", soldiers=" + soldiers + ", monuments=" + monuments
				+ ", victoryPoints=" + victoryPoints + "]";
	}


	public void setOldDevCards(DevCardList devCards) {
		this.oldDevCards = devCards;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cities;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + (discarded ? 1231 : 1237);
		result = prime * result + (hasRolled ? 1231 : 1237);
		result = prime * result + monuments;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((newDevCards == null) ? 0 : newDevCards.hashCode());
		result = prime * result + ((oldDevCards == null) ? 0 : oldDevCards.hashCode());
		result = prime * result + (playedDevCard ? 1231 : 1237);
		result = prime * result + playerID;
		result = prime * result + playerIndex;
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
		result = prime * result + roads;
		result = prime * result + settlements;
		result = prime * result + soldiers;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + victoryPoints;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (cities != other.cities)
			return false;
		if (color != other.color)
			return false;
		if (discarded != other.discarded)
			return false;
		if (hasRolled != other.hasRolled)
			return false;
		if (monuments != other.monuments)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (newDevCards == null) {
			if (other.newDevCards != null)
				return false;
		} else if (!newDevCards.equals(other.newDevCards))
			return false;
		if (oldDevCards == null) {
			if (other.oldDevCards != null)
				return false;
		} else if (!oldDevCards.equals(other.oldDevCards))
			return false;
		if (playedDevCard != other.playedDevCard)
			return false;
		if (playerID != other.playerID)
			return false;
		if (playerIndex != other.playerIndex)
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		if (roads != other.roads)
			return false;
		if (settlements != other.settlements)
			return false;
		if (soldiers != other.soldiers)
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (victoryPoints != other.victoryPoints)
			return false;
		return true;
	}

}
