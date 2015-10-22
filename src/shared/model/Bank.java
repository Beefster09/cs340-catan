package shared.model;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import shared.definitions.DevCardType;
import shared.exceptions.SchemaMismatchException;

/**
 * Represents the bank of resources available for purchase during the game.
 * @author Jordan
 *
 */
public class Bank {
	
	private final int NUM_MONOPOLY_CARDS = 2;
	private final int NUM_MONUMENT_CARDS = 5;
	private final int NUM_ROAD_BUILD_CARDS = 2;
	private final int NUM_SOLDIER_CARDS = 14;
	private final int NUM_YEAR_OF_PLENTY_CARDS = 2;

	private ResourceList resources;
	private DevCardList devCards;

	/** Create a bank with the default (standard) amounts of cards
	 * 
	 */
	public Bank() {
		resources = new ResourceList(19);
		
		Map<DevCardType, Integer> cards = new HashMap<DevCardType, Integer>();
		cards.put(DevCardType.MONOPOLY, NUM_MONOPOLY_CARDS);
		cards.put(DevCardType.MONUMENT, NUM_MONUMENT_CARDS);
		cards.put(DevCardType.ROAD_BUILD, NUM_ROAD_BUILD_CARDS);
		cards.put(DevCardType.SOLDIER, NUM_SOLDIER_CARDS);
		cards.put(DevCardType.YEAR_OF_PLENTY, NUM_YEAR_OF_PLENTY_CARDS);
		
		devCards = new DevCardList(cards);
	}
	
	/** Create a bank with the given resources and development cards
	 * @param resources the ResourceList to use
	 * @param devCards the DevCardList to use
	 */
	public Bank(ResourceList resources, DevCardList devCards) {
		super();
		this.resources = resources;
		this.devCards = devCards;
	}
	
	public Bank(JSONObject json) throws SchemaMismatchException {
		resources = ResourceList.fromJSONObject(json);
	}
	
	/**
	 * @return the resources
	 */
	public ResourceList getResources() {
		return resources;
	}
	/**
	 * @return the devCards
	 */
	public DevCardList getDevCards() {
		return devCards;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((devCards == null) ? 0 : devCards.hashCode());
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
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
		Bank other = (Bank) obj;
		if (devCards == null) {
			if (other.devCards != null)
				return false;
		} else if (!devCards.equals(other.devCards))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		return true;
	}
	
}
