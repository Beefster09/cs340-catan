package shared.exceptions;

/**
 * Occurs when an error happens during the initialization of the game.
 * @author Jordan
 *
 */
public class GameInitializationException extends Exception {

	public GameInitializationException(String string) {
		super(string);
	}

	public GameInitializationException() {
		super();
	}

}
