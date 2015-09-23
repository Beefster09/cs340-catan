package client.communication;

import java.util.Timer;
import java.util.TimerTask;

import shared.communication.IServer;



/**
 * @author Justin Snyder
 * A ServerPoller periodically hits the server to get the status of a particular game.
 */
public class ServerPoller {
	/**
	 * The default polling interval (1 second)
	 */
	public final static long DEFAULT_POLL_INTERVAL = 1000;
	
	private IServer server;
	private Timer poller = null;

	/**
	 * Creates the Poller
	 * @param server the server or server proxy object to poll.
	 * @pre the server passed in is valid
	 * @post a ServerPoller object will be created.
	 */
	public ServerPoller(IServer server) {
		this.server = server;	
	}
	
	/** Tells you whether this poller is running or not
	 * @return true if the poller is running
	 * @pre None
	 * @post None
	 */
	public boolean isRunning() {
		return poller != null;
	}

	/**
	 * Creates a thread for polling at the specified interval and begins polling.
	 * @param interval The polling rate, in ms.
	 * @pre The poller is not running and the interval is positive
	 * @post The poller will be running
	 */
	public void start(long interval) {
		poller = new Timer(true); // Create a new timer to run as a daemon
		
		poller.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
			
		}, 0, interval);
	}
	
	/**
	 * Creates a thread for polling using the default poll interval
	 * @pre The poller is not running
	 * @post The poller will be running
	 */
	public void start() {
		start(DEFAULT_POLL_INTERVAL);
	}

	/**
	 * Stops the polling. <br/>
	 * <b>Make sure to call this when you are done polling, in order
	 * to prevent resource leaks (threads <i>are</i>, in fact,
	 * a limited resource.) </b>
	 * @pre the poller is running
	 * @post the poller will not be running
	 */
	public void stop() {
		poller.cancel();
		poller = null;
	}

	/**
	 * Gets the server object that this ServerPoller uses
	 * @return the server or server proxy object that this ServerPoller uses
	 * @pre None
	 * @post None
	 */
	public IServer getServer() {
		return server;
		
	}

}

