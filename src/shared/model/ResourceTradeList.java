package shared.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import shared.definitions.*;
import shared.exceptions.InsufficientResourcesException;
import shared.exceptions.TradeException;

/** An immutable representation of an exchange of resources.
 * @author beefster
 *
 */
public class ResourceTradeList {
	
	Map<ResourceType, Integer> offered;
	Map<ResourceType, Integer> wanted;
	
	/**
	 * @return a copy of the offered resources, as a Map
	 */
	public Map<ResourceType, Integer> getOffered() {
		return new HashMap<>(offered);
	}

	/**
	 * @return a copy of the wanted resources, as a Map
	 */
	public Map<ResourceType, Integer> getWanted() {
		return new HashMap<>(wanted);
	}

	/** Makes this trade between two ResourceLists
	 * @param offerer the ResourceList to trade from
	 * @param receiver the ResourceList to trade to
	 * @pre <ul>
	 * <li>The offerer has at least what is offered</li>
	 * <li>the receiver has at least what is wanted.</li>
	 * </ul>
	 * @post The offered resources will be taken from the offerer and given to the receiver
	 * and the wanted resources will be taken from the receiver and given to the offerer.
	 * @throws TradeException if either of the preconditions are not met
	 */
	public void makeExchange(ResourceList offerer, ResourceList receiver) throws TradeException {
		// Exception check. You can't just use transferTo and re-purpose the exception because
		// trades are all-or-nothing transactions, and the transferTo could cause this method to
		// be stopped part way through the trade (obviously bad)
		for (Entry<ResourceType, Integer> resource : offered.entrySet()) {
			if (offerer.count(resource.getKey()) < resource.getValue()) throw new TradeException();
		}
		for (Entry<ResourceType, Integer> resource : wanted.entrySet()) {
			if (receiver.count(resource.getKey()) < resource.getValue()) throw new TradeException();
		}
		
		// Implementation
		for (Entry<ResourceType, Integer> resource : offered.entrySet()) {
			try {
				offerer.transferTo(receiver, resource.getKey(), resource.getValue());
			} catch (InsufficientResourcesException e) {
				// This should never happen! This is a critical error.
				e.printStackTrace();
				assert false;
			}
		}
		for (Entry<ResourceType, Integer> resource : wanted.entrySet()) {
			try {
				receiver.transferTo(offerer, resource.getKey(), resource.getValue());
			} catch (InsufficientResourcesException e) {
				// This should never happen! This is a critical error.
				e.printStackTrace();
				assert false;
			}
		}
		
	}

}
