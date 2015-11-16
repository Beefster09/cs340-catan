package server.movehandlers;

import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import shared.communication.IServer;
import shared.communication.Session;

import com.sun.net.httpserver.HttpExchange;

public abstract class AbstractMoveHandler {

	@SuppressWarnings("deprecation")
	public UUID checkCookies(HttpExchange exchange, IServer server){
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
			UUID userID = UUID.fromString((String) cookie.get("playerUUID"));
			
			Session user = server.login(username, password);
			
			if(userID != user.getPlayerUUID()){
				return null;
			}
			return UUID.fromString(gameCookie);
		}
		catch(Exception e){
			return null;
		}
	}
}
