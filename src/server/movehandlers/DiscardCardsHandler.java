package server.movehandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.MockServer;
import server.communication.Server;
import server.interpreter.ExchangeConverter;
import shared.communication.IServer;

/**
 * Handles discard requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class DiscardCardsHandler implements HttpHandler {

	IServer server = new MockServer();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			JSONObject json = ExchangeConverter.toJSON(arg0);
			/*
			 * Extract needed information from JSON, and call the appropriate server method.
			 */
//			String name = (String) json.get("name");
//			boolean randomTiles = (boolean) json.get("randomTiles");
//			boolean randomNumbers = (boolean) json.get("randomNumbers");
//			boolean randomPorts = (boolean) json.get("randomPorts");
//			
//			GameHeader game = server.createGame(name, randomTiles, randomNumbers, randomPorts);
			
			JSONObject header = new JSONObject();
			/*
			 * Put necessary information into JSON object to return
			 */
//			header.put("game", game);
			
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(header.toString());
			output.flush();
			arg0.getResponseBody().close();
		} catch (ParseException e) {
			
		}
	}
}
