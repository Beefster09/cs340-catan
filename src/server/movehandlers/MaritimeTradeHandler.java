package server.movehandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.IExtendedServer;
import client.communication.MockServer;
import server.communication.Server;
import server.interpreter.ExchangeConverter;
import shared.communication.IServer;
import shared.definitions.ResourceType;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.PlayerReference;

/**
 * Handles maritimeTrade requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class MaritimeTradeHandler extends AbstractMoveHandler implements HttpHandler {

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
			int index = (int)json.get("playerIndex");
			int gameIndex = this.checkCookies(arg0);
			int ratio = (int)json.get("ratio");
			ResourceType inputResource = (ResourceType)json.get("inputResource");
			ResourceType outputResource = (ResourceType)json.get("outputResource");
			
			String gson = server.maritimeTrade(index, gameIndex, inputResource, outputResource, ratio);
			
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(gson.toString());
			output.flush();
			arg0.getResponseBody().close();
		} catch (ParseException | ServerException | UserException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			arg0.getResponseBody().close();
		}
	}
}
