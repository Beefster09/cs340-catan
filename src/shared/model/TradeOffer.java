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

	
}
