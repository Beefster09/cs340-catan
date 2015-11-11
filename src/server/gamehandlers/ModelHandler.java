package server.gamehandlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.Server;

/**
 * Handles getModel requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class ModelHandler implements HttpHandler {

	Server server;
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		// TODO Auto-generated method stub

	}

}
