package shared.exceptions;

/**
 * Occurs when one player attempts to trade more resources than he has available to offer.
 * @author Jordan
 *
 */
public class TradeException extends InsufficientResourcesException {

	public TradeException(String string) {
		super(string);
	}

	public TradeException() {
		super();
	}

}
