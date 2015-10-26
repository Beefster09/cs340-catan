package shared.model;

import java.io.*;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import shared.exceptions.SchemaMismatchException;

/**
 * Manages the current trade offer between two players
 */
public class TradeOffer {
	private PlayerReference sender;
	private PlayerReference receiver;
	private ResourceTradeList offer;
	
	public static void main(String[] args) throws Exception {
		List<Player> players = new ArrayList<>();
		players.add(new Player(null, 0));
		players.add(new Player(null, 1));
		players.add(new Player(null, 2));
		players.add(new Player(null, 3));
		
		JSONParser parser = new JSONParser();
		Reader r = new BufferedReader(new FileReader("tradeoffer.json"));
		Object parseResult = parser.parse(r);
		TradeOffer trade = new TradeOffer(players, (JSONObject) parseResult);

		System.out.println(parseResult);
		System.out.println(trade);
	}

	public TradeOffer() {
		
	}
	
	public TradeOffer(List<Player> players, JSONObject json) throws SchemaMismatchException {
		try {
			offer = new ResourceTradeList((JSONObject) json.get("offer"));
			sender = players.get((int) (long) json.get("sender")).getReference();
			receiver = players.get((int) (long) json.get("receiver")).getReference();
		}
		catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a TradeOffer:\n" + json.toJSONString());
		}
	}

	/**
	 * @return the sender
	 */
	public PlayerReference getSender() {
		return sender;
	}

	/**
	 * @return the receiver
	 */
	public PlayerReference getReceiver() {
		return receiver;
	}

	/**
	 * @return the offer
	 */
	public ResourceTradeList getOffer() {
		return offer;
	}
	
	/** Accepts the offer
	 * @pre None
	 * @post The offer is accepted and will be carried out.
	 */
	public void accept() {
		
	}
	
	/** Declines the offer
	 * @pre None
	 * @post The offer is declined and gameplay continues as before.
	 */
	public void decline() {
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TradeOffer [sender=" + sender + ", receiver=" + receiver
				+ ", offer=" + offer + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offer == null) ? 0 : offer.hashCode());
		result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
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
		TradeOffer other = (TradeOffer) obj;
		if (offer == null) {
			if (other.offer != null)
				return false;
		} else if (!offer.equals(other.offer))
			return false;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}


	
}
