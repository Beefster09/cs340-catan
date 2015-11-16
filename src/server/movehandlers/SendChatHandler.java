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

import client.communication.MockServer;
import server.communication.Server;
import server.interpreter.ExchangeConverter;
import shared.communication.IServer;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;

/**
 * Handles sendChat requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class SendChatHandler extends AbstractMoveHandler implements HttpHandler {

	IServer server = new MockServer();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			int gameID = super.checkCookies(arg0, server);
			if(gameID == -1){
				throw new ServerException();
			}
			JSONObject json = ExchangeConverter.toJSON(arg0);
			/*
			 * Extract needed information from JSON, and call the appropriate server method.
			 */
			int index = (int)(long)json.get("playerIndex");
			String message = (String)json.get("content");
			
			String gson = server.sendChat(index, gameID, message);
			//String gson = new Server().sendChat(index, gameID, message);
			
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(gson.toString());
			output.flush();
			arg0.getResponseBody().close();
		} catch (ParseException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			e.printStackTrace();
		} catch (ServerException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			e.printStackTrace();
		} catch (UserException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			e.printStackTrace();
		}
	}
}