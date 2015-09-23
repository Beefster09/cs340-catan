package shared.model;

import java.util.Map;

import shared.definitions.ResourceType;
import shared.exceptions.InsufficientResourcesException;

import java.util.*;

/** A ResourceList where all counts must be non-negative
 * and all mutations are <i>zero-sum</i> between two instances.
 * (i.e. cards should never spontaneously appear or disappear.
 * Rather, they can only be transfered from one ResourceList
 * to another, not created nor destroyed.)
 * @author beefster
 *
 */
public class ResourceList {
	private Map<ResourceType, Integer> resources;
	
	/** Creates an empty ResourceList- e.g. for a player
	 * 
	 */
	public ResourceList() {
		
	}
	
	/** Creates a ResourceList with a specific number of each resource- e.g. 
	 * for the bank at the beginning of a game.
	 * @param count the amount to have of each resource
	 * @throws IllegalArgumentException if count is negative.
	 */
	public ResourceList(int count) throws IllegalArgumentException {
		if (count < 0) {
			throw new IllegalArgumentException("ResourceLists may not have negative counts.");
		}
		for (ResourceType type : ResourceType.values()) {
			resources.put(type, count);
		}
	}
	
	/** Creates a ResourceList with the given resource amounts. Values will be copied.
	 * @param counts a map representing the counts for each resource
	 * @throws IllegalArgumentException if any of the counts are negative.
	 */
	public ResourceList(Map<ResourceType, Integer> counts) throws IllegalArgumentException {
		for (int count : counts.values()) {
			if (count < 0) throw new IllegalArgumentException("ResourceLists may not have negative counts.");
		}
		resources = new HashMap<>(counts);
	}

	/** Counts the total number of cards in this ResourceList
	 * @return The number of cards of all types
	 * @pre none
	 * @post none
	 */
	public int count() {
		int total = 0;
		for (int count : resources.values()) {
			total += count;
		}
		return total;
	}
	
	/** Counts the total number of cards of the given type in this ResourceList
	 * @param type
	 * @return The number of cards of the given type
	 * @pre none
	 * @post none
	 */
	public int count(ResourceType type) {
		//TODO: incomplete; values might be null
		return resources.get(type);
	}

	/** Transfers cards from one ResourceList to another
	 * @param destination the ResourceList to transfer to
	 * @param type the Type of resource to transfer
	 * @param amount the number of cards to transfer
	 * @pre This ResourceList has at least amount cards of the given type in it
	 * @post This list's number of cards will decrease by amount and the destination
	 * list will increase by amount
	 * @throws InsufficientResourcesException if the precondition is not met.
	 */
	public void transferTo(ResourceList destination, ResourceType type, int amount)
			throws InsufficientResourcesException {
		if (count(type) < amount) throw new InsufficientResourcesException();
		this.resources.put(type, this.resources.get(type) - amount);
		destination.resources.put(type, destination.resources.get(type) + amount);
	}

}
