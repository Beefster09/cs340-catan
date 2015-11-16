package server.movehandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import client.communication.MockServer;
import server.interpreter.ExchangeConverter;
import shared.communication.IServer;
import shared.definitions.ResourceType;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;

/**
 * Handles yearOfPlenty requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class YearOfPlentyHandler extends AbstractMoveHandler implements HttpHandler {

	IServer server = new MockServer();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			UUID gameID = super.checkCookies(arg0, server);
			if(gameID == -1){
				throw new ServerException();
			}
			JSONObject json = ExchangeConverter.toJSON(arg0);
			/*
			 * Extract needed information from JSON, and call the appropriate server method.
			 */
			UUID index = (int)(long)json.get("playerIndex");
			ResourceType type1 = ResourceType.fromString((String)json.get("resource1"));
			ResourceType type2 = ResourceType.fromString((String)json.get("resource2"));
			
			String gson = server.yearOfPlenty(index, gameID, type1, type2);
			
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(gson.toString());
			output.flush();
			arg0.getResponseBody().close();
		} catch (ParseException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			e.printStackTrace();
		} catch (ServerException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			e.printStackTrace();
		} catch (UserException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			e.printStackTrace();
		}
	}

}