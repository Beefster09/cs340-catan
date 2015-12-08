package shared.exceptions;

/**
 * Occurs when an error occurs on the server.
 * Can also signify that the client can't make contact with the server.
 * @author Jordan
 *
 */
public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ServerException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ServerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ServerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ServerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
