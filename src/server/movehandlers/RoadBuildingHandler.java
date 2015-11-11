package server.movehandlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.Server;

/**
 * Handles roadBuilding requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class RoadBuildingHandler implements HttpHandler {

	Server server;
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			InputStream input = arg0.getRequestBody();
			int len = 0;
			
			byte[] buffer = new byte[1024];
			StringBuilder string = new StringBuilder();
			while(-1 != (len = input.read(buffer))){
				string.append(new String(buffer, 0, len));
			}
	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(string.toString());
			
			
		} catch (ParseException e) {
			
		}
	}

}
