package server.telnet;

public interface ITelnetInterpreter {
	
	/**
	 * @param command
	 * @param args
	 * @return true if the connection should be kept open, false if it should be closed
	 */
	boolean interpret(String line);
	
	void onOpen();
	void onClose();
	void prompt();

}
