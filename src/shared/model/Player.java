package shared.model;

import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
	// Meta-information
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
	
	public static void main(String[] args) throws Exception {
/*		JSONParser parser = new JSONParser();
		Reader r = new BufferedReader(new FileReader("player.json"));
		Object parseResult = parser.parse(r);
		Player player = new Player(null, (JSONObject) parseResult);

		System.out.println(parseResult);
		System.out.println(player);  */
	}
	

	public Player(CatanModel game) {
		this.game = game;
		// Do some stuff?
	}
	
	public Player(CatanModel game, int index) {
		this.game = game;
		playerIndex = index;
	}

	public Player(CatanModel game, JSONObject json) throws SchemaMismatchException {
		this.game = game;
		
		try {
			playerIndex	= (int) (long) json.get("playerIndex");
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
		return new PlayerReference(game, playerIndex);
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
	
	

}
