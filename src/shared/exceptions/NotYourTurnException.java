package shared.exceptions;

/**
 * Occurs when a player attempts to perform a turn-specific action when
 * it is not their turn.
 * @author Jordan
 *
 */
public class NotYourTurnException extends InvalidActionException {

	public NotYourTurnException(String string) {
		super(string);
		// TODO Auto-generated constructor stub
	}

}
