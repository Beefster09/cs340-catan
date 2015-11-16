package server.gamehandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import client.communication.MockServer;
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
	
	IServer server = new MockServer();
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
			UUID gameID = this.checkGameCookies(arg0, server);
			CatanColor color = CatanColor.getColorFromString((String) json.get("color"));
			
			Session player = this.getPlayerSessionFromCookie(arg0);
			boolean success = server.joinGame(player, gameID, color);
			String outputMsg = "";
			if (success)
				outputMsg = "Success";
			else
				outputMsg = "Failed";
			
			StringBuilder str = new StringBuilder();
			str.append("Catan.game=");
			str.append(gameID);
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
	public UUID checkGameCookies(HttpExchange exchange, IServer server){
		List<String> cookies = exchange.getRequestHeaders().get("Cookie");
		if(cookies.size() != 1){
			return null;
		}
		
		JSONParser parser = new JSONParser();
		
		String cookieEncoded = cookies.get(0);
		String cookieDecoded = URLDecoder.decode(cookieEncoded);
		cookieDecoded = cookieDecoded.substring(11);
		int locationOfSemicolon = cookieDecoded.indexOf(';');
		String userCookie = cookieDecoded.substring(0,locationOfSemicolon);
		String gameCookie = cookieDecoded.substring(locationOfSemicolon);;
				
		try{
			JSONObject cookie = (JSONObject) parser.parse(userCookie);
			String username = (String) cookie.get("name");
			String password = (String) cookie.get("password");
			int userID = (int)(long)cookie.get("playerID");
			
			Session user = server.login(username, password);
			
			if(userID != user.getPlayerID()){
				return null;
			}
			return UUID.fromString(gameCookie);
		}
		catch(Exception e){
			return null;
		}
	}
}
