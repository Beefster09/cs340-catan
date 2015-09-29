package shared.exceptions;

/**
 * A general exception that occurs when a player is trying to perform an illegal action.
 * @author Jordan
 *
 */
public class InvalidActionException extends Exception {

	public InvalidActionException(String string) {
		super(string);
	}

	public InvalidActionException() {
		super();
	}

}
