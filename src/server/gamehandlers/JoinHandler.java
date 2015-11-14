package server.gamehandlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.IExtendedServer;
import client.communication.MockServer;
import server.communication.Server;
import server.interpreter.ExchangeConverter;
import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.GameInitializationException;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;

/**
 * Handles join requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class JoinHandler extends AbstractGameHandler implements HttpHandler {
	
	IServer server = new MockServer();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			JSONObject json = ExchangeConverter.toJSON(arg0);
			long temp = (long) json.get("id");
			int gameID = (int) temp;
			//int playerUUID = (int)json.get("playerUUID");
			CatanColor color = CatanColor.getColorFromString((String) json.get("color"));
			
			Session player = this.getPlayerSessionFromCookie(arg0);
			boolean success = server.joinGame(player, gameID, color);
			String outputMsg = "";
			if (success)
				outputMsg = "Success";
			else
				outputMsg = "Failed";
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 200);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(outputMsg);
			output.flush();
			arg0.getResponseBody().close();
		} catch (Exception e) {
			e.printStackTrace();
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			arg0.getResponseBody().close();
		}
	}
}
