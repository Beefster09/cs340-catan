package server.gamehandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.Server;
import server.interpreter.ExchangeConverter;
import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.ServerException;

/**
 * Handles join requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class JoinHandler extends AbstractGameHandler implements HttpHandler {
	
	IServer server = Server.getSingleton();
//	IServer server = new MockServer();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			if(!super.checkCookies(arg0, server)){
				throw new ServerException();
			}
			JSONObject json = ExchangeConverter.toJSON(arg0);
			UUID gameUUID = UUID.fromString((String)json.get("id"));
			CatanColor color = CatanColor.getColorFromString((String) json.get("color"));
			
			Session player = this.getPlayerSessionFromCookie(arg0);
			boolean success = server.joinGame(player, gameUUID, color);
			String outputMsg = "";
			if (success)
				outputMsg = "Success";
			else
				outputMsg = "Failed";
			
			StringBuilder str = new StringBuilder();
			str.append("Catan.game=");
			str.append(gameUUID);
			str.append(";Path=/;");
			String cookie = str.toString();
			arg0.getResponseHeaders().add("Set-cookie", cookie);
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(outputMsg);
			output.flush();
			arg0.getResponseBody().close();
		} catch (Exception e) {
			e.printStackTrace();
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			arg0.getResponseBody().close();
		}
	}
}