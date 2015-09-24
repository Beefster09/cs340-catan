package shared.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import shared.definitions.*;
import shared.exceptions.InsufficientResourcesException;
import shared.exceptions.SchemaMismatchException;
import shared.exceptions.TradeException;

/** An immutable representation of an exchange of resources.
 * @author beefster
 *
 */
public class ResourceTradeList {
	
	Map<ResourceType, Integer> offered;
	Map<ResourceType, Integer> wanted;
	
	public static void main(String[] args) throws Exception {
		JSONParser parser = new JSONParser();
		Reader r = new BufferedReader(new FileReader("trade.json"));
		Object parseResult = parser.parse(r);
		ResourceTradeList trade = new ResourceTradeList((JSONObject) parseResult);

		System.out.println(parseResult);
		System.out.println(trade);
		
	}
	
	public ResourceTradeList(JSONObject json) throws SchemaMismatchException {
		offered = new HashMap<>();
		wanted = new HashMap<>();
		try {
			for (ResourceType type : ResourceType.values()) {
				String key = type.toString().toLowerCase();
				if (json.containsKey(key)) {
					int count = (int) (long) json.get(key);
					if (count > 0) {
						offered.put(type, count);
					}
					if (count < 0) {
						wanted.put(type, -count);
					}
				}
				else {
					throw new SchemaMismatchException("A resource count is missing from the " +
							"given JSONObject:\n" + json.toJSONString());
				}
			}
		} catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not match the expected schema" +
					"for a ResourceTradeList:\n" + json.toJSONString());
		}
	}
	
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResourceTradeList [offered=" + offered + ", wanted=" + wanted
				+ "]";
	}

}
