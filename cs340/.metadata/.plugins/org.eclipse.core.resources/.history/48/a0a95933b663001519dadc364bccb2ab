package shared.model;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.DevCardType;
import shared.exceptions.InvalidActionException;

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

}