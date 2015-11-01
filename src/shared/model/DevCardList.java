package shared.model;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.exceptions.InsufficientResourcesException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.SchemaMismatchException;

/**
 * Manages the development cards that a player or the bank has.
 * Amounts must be non-negative
 */
public class DevCardList {
	private Map<DevCardType, Integer> cards;

	/** Creates an empty DevCardList
	 * 
	 */
	public DevCardList() {
		cards = new HashMap<DevCardType, Integer>();
	}
	
	public static DevCardList fromJSONObject(JSONObject json) throws SchemaMismatchException {
		DevCardList self = new DevCardList();
		self.cards = new HashMap<>();
		try {
			for (DevCardType type : DevCardType.values()) {
				String key = type.toString();
				if (json.containsKey(key)) {
					self.cards.put(type, (int) (long) json.get(key));
				}
				else {
					throw new SchemaMismatchException("A card count is missing from the " +
							"given JSONObject:\n" + json.toJSONString());
				}
			}
		} catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not match the expected schema" +
					"for a DevCardList:\n" + json.toJSONString());
		}
		return self;
	}
	
	/** Creates a DevCardList with the specified amounts of cards
	 * @param cards a Map<DevCardType, Integer> specifying the quantities of each type of card.
	 * @pre all of the amounts in cards are non-negative
	 * @post a DevCardList will be created with a copy of the specified card amounts
	 * @throws IllegalArgumentException if any of the amounts are negative
	 */
	public DevCardList(Map<DevCardType, Integer> cards) throws IllegalArgumentException {
		// TODO validate
		this.cards = new HashMap<>(cards);
	}
	
	/** Creates a DevCardList with specified amounts of things
	 * @param soldiers the number of Soldiers to put in this DevCardList
	 * @param special the number of Road Building, Monopoly, and Year of Plenty cards
	 * @param monuments the number of Monuments
	 * @pre all the parameters are non-negative
	 * @post a DevCardList will be created with the specified card amounts
	 * @throws IllegalArgumentException if any of parameters are negative
	 */
	public DevCardList(int soldiers, int special, int monuments) throws IllegalArgumentException {
		cards = new HashMap<DevCardType, Integer>();
		for (DevCardType type : DevCardType.values()) {
			String cardName = type.toString().toLowerCase();
			if (cardName == "soldier")
				cards.put(DevCardType.SOLDIER, soldiers);
			else if (cardName == "monuments")
				cards.put(type, monuments);
			else
				cards.put(type, special);
		}
	}
	
	/** Gives a count of all cards of all types
	 * @return the number of cards in this DevCardList
	 * @pre none
	 * @post none
	 */
	public int count() {
		int total = 0;
		for (int count : cards.values()) {
			total += count;
		}
		return total;
	}
	
	/** Gives a count of all cards of the given type
	 * @param type the type
	 * @return the number of cards of the type in this DevCardList
	 * @pre none
	 * @post none
	 */
	public int count(DevCardType type) {
		
		return cards.get(type);
	}
	
	/** Transfers a card from this DevCardList to another
	 * @param destination the DevCardList to transfer to
	 * @param type the type of card you want to transfer
	 * @pre count(type) >= 1
	 * @post the count of the specified type will be decreased on this DevCardList
	 * and increased on the destination DevCardList
	 * @throws InvalidActionException if there isn't a card of the type to transfer
	 */
	public void transferCardTo(DevCardList destination, DevCardType type) throws InvalidActionException {
		if (count(type) < 1) throw new InsufficientResourcesException();
		this.cards.put(type, this.cards.get(type) - 1);
		destination.cards.put(type, destination.cards.get(type) + 1);
	}
	

	/** Transfers a random card from this DevCardList to another, with a uniform
	 * distribution based on counts
	 * @param destination the DevCardList to transfer to
	 * @pre count() >= 1
	 * @post the count of the randomly determined type will be decreased on this 
	 * DevCardList and increased on the destination DevCardList
	 * @throws InvalidActionException if there are no cards in this DevCardList
	 */
	public void transferRandomCardTo(DevCardList destination) throws InvalidActionException {
		
	}
	
	/** Uses a card of the given type
	 * @param type The type of the card to use
	 * @pre count(type) >= 1
	 * @post the count of the card's type will decrease by 1 and the appropriate action will be taken
	 * @throws InvalidActionException if this DevCardList does not have at least 1 card of the type
	 */
	public void useCard(DevCardType type) throws InvalidActionException {
		
	}
	
	public Map<DevCardType, Integer> getCards() {
		return cards;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cards == null) ? 0 : cards.hashCode());
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
		DevCardList other = (DevCardList) obj;
		if (cards == null) {
			if (other.cards != null)
				return false;
		} else if (!cards.equals(other.cards))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DevCardList [cards=" + cards + "]";
	}
	
	
}