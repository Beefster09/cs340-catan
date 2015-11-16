package shared.model;

import java.util.*;

import org.json.simple.JSONObject;
import shared.definitions.ResourceType;
import shared.exceptions.SchemaMismatchException;
import shared.exceptions.TradeException;

/**
 * Manages the current trade offer between two players
 */
public class TradeOffer {
	private PlayerReference sender;
	private PlayerReference receiver;
	private ResourceTradeList offer;

	public TradeOffer() {
		
	}
	
	public TradeOffer(List<Player> players, JSONObject json) throws SchemaMismatchException {
		try {
			offer = new ResourceTradeList((JSONObject) json.get("offer"));
			sender = new PlayerReference((String) json.get("sender"));
			receiver = new PlayerReference((String) json.get("receiver"));
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
	
	public boolean isPossible() {
		ResourceList sendHand = sender.getPlayer().getResources();
		ResourceList recHand = receiver.getPlayer().getResources();
		
		for (Map.Entry<ResourceType, Integer> offered :
			offer.getOffered().entrySet()) {
			if (sendHand.count(offered.getKey()) < offered.getValue()) {
				return false;
			}
		}
		
		for (Map.Entry<ResourceType, Integer> wanted :
			offer.getWanted().entrySet()) {
			if (recHand.count(wanted.getKey()) < wanted.getValue()) {
				return false;
			}
		}
		
		return true;
	}
	
	/** Accepts the offer
	 * @throws TradeException 
	 * @pre None
	 * @post The offer is accepted and will be carried out.
	 */
	public void makeTrade() throws TradeException {
		offer.makeExchange(sender.getPlayer().getResources(), receiver.getPlayer().getResources());
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
