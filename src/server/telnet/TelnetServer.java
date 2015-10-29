package server.telnet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TelnetServer implements Runnable {
	
	public static final int DEFAULT_PORT = 2323;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		TelnetServer telnet = new TelnetServer(new SimpleTelnetInterpreterFactory());
		System.out.println("Starting Telnet Server on port " + DEFAULT_PORT);
		telnet.run();
	}
	
	private ServerSocket serverSocket;
	private ITelnetInterpreterFactory interpreterFactory;
	
	public TelnetServer(int port, ITelnetInterpreterFactory interpreterFactory) throws IOException {
		serverSocket = new ServerSocket(port);
		this.interpreterFactory = interpreterFactory;
	}
	
	public TelnetServer(ITelnetInterpreterFactory interpreter) throws IOException {
		this(DEFAULT_PORT, interpreter);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("Accepted Telnet connection from "
						+ socket.getInetAddress().toString() + ":" + socket.getPort());
				
				OutputStream os = socket.getOutputStream();
				ITelnetInterpreter interpreter = interpreterFactory.getInterpreter(os);
				
				interpreter.onOpen();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				while (interpreter.interpret(br.readLine()));
				
				interpreter.onClose();
				
				System.out.println("Closing telnet connection from "
						+ socket.getInetAddress().toString() + ":" + socket.getPort());
				
				socket.close();
				
				return;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
