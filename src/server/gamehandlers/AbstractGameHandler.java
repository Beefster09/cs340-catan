package server.gamehandlers;

import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import server.communication.Server;
import shared.communication.IServer;
import shared.communication.Session;

import com.sun.net.httpserver.HttpExchange;

public abstract class AbstractGameHandler {

	@SuppressWarnings("deprecation")
	public boolean checkCookies(HttpExchange exchange, IServer server){
		List<String> cookies = exchange.getRequestHeaders().get("Cookie");
		if(cookies.size() != 1){
			return false;
		}
		
		JSONParser parser = new JSONParser();
		
		String cookieEncoded = cookies.get(0);
		String cookieDecoded = URLDecoder.decode(cookieEncoded);
		cookieDecoded = cookieDecoded.substring(11);
		
		try{
			JSONObject cookie = (JSONObject) parser.parse(cookieDecoded);
			String username = (String) cookie.get("name");
			String password = (String) cookie.get("password");
			UUID userID = UUID.fromString((String) cookie.get("playerID"));
			
			Session user = server.login(username, password);
			
			if(userID != user.getPlayerUUID()){
				return false;
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public Session getPlayerSessionFromCookie(HttpExchange arg0) {
		List<String> cookies = arg0.getRequestHeaders().get("Cookie");
		if(cookies.size() != 1){
			return null;
		}
		
		JSONParser parser = new JSONParser();
		
		String cookieEncoded = cookies.get(0);
		String cookieDecoded = URLDecoder.decode(cookieEncoded);
		cookieDecoded = cookieDecoded.substring(11);
		
		try{
			JSONObject cookie = (JSONObject) parser.parse(cookieDecoded);
			Server server = new Server();
			String username = (String) cookie.get("name");
			String password = (String) cookie.get("password");
			UUID userID = UUID.fromString((String) cookie.get("playerID"));
			
			Session user = server.login(username, password);
			
			if(userID != user.getPlayerUUID()){
				return null;
			}
			return user;
		}
		catch(Exception e){
			return null;
		}
	}
}
