package server.movehandlers;

import java.net.URLDecoder;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import server.communication.Server;
import shared.communication.Session;

import com.sun.net.httpserver.HttpExchange;

public abstract class AbstractMoveHandler {

	@SuppressWarnings("deprecation")
	public int checkCookies(HttpExchange exchange){
		List<String> cookies = exchange.getRequestHeaders().get("Cookie");
		if(cookies.size() != 1){
			return -1;
		}
		
		JSONParser parser = new JSONParser();
		
		String cookieEncoded = cookies.get(0);
		String cookieDecoded = URLDecoder.decode(cookieEncoded);
		cookieDecoded = cookieDecoded.substring(11);
		int difference = 14;
		if(cookieDecoded.charAt(cookieDecoded.length() -2) != '='){
			++difference;
		}
		
		String gameID = cookieDecoded.substring(cookieDecoded.length() - difference + 13);
		cookieDecoded = cookieDecoded.substring(0, cookieDecoded.length()-difference);
		
		try{
			JSONObject cookie = (JSONObject) parser.parse(cookieDecoded);
			Server server = new Server();
			String username = (String) cookie.get("name");
			String password = (String) cookie.get("password");
			int userID = (int)(long)cookie.get("playerID");
			
			Session user = server.login(username, password);
			
			if(userID != user.getPlayerID()){
				return -1;
			}
			return Integer.valueOf(gameID);
		}
		catch(Exception e){
			return -1;
		}
	}
}
