package server.gamehandlers;

import java.net.URLDecoder;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import server.communication.Server;
import shared.communication.Session;

import com.sun.net.httpserver.HttpExchange;

public abstract class AbstractGameHandler {

	@SuppressWarnings("deprecation")
	public boolean checkCookies(HttpExchange exchange){
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
			Server server = new Server();
			String username = (String) cookie.get("name");
			String password = (String) cookie.get("password");
			int userID = (int)(long)cookie.get("playerID");
			
			Session user = server.login(username, password);
			
			if(userID != user.getPlayerID()){
				return false;
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
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
			int userID = (int)(long)cookie.get("playerID");
			
			Session user = server.login(username, password);
			
			if(userID != user.getPlayerID()){
				return null;
			}
			return user;
		}
		catch(Exception e){
			return null;
		}
	}
}
