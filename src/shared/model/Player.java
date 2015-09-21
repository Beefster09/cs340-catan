package shared.model;

import shared.definitions.CatanColor;

public class Player {
	
	private CatanColor color;
	private String name;
	
	private DevCardList newDevCards;
	private DevCardList oldDevCards;
	private ResourceList resources;
	
	private boolean playedDevCard;
	private boolean discarded;
	
	private int cities;
	private int roads;
	private int settlements;
	private int soldiers;
	private int monuments;
	private int victoryPoints;
	
	private int playerIndex;
	private int playerID;

	public Player() {
		
	}
	
	/** Gives a PlayerReference that refers to this player. 
	 * <p>Gives a PlayerReference such that a call to getPlayer() on the result of this 
	 * function will return this instance of Player</p>
	 * @return a corresponding PlayerReference
	 */
	public PlayerReference getReference() {
		return null;
	}

	/**
	 * @return the resources
	 */
	public ResourceList getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(ResourceList resources) {
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
	public boolean isDiscarded() {
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
	public boolean isPlayedDevCard() {
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
	
	

}
