package server.gamehandlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.MockServer;
import server.communication.Server;
import server.interpreter.ExchangeConverter;
import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;

/**
 * Handles list requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class ListHandler implements HttpHandler {

	IServer server = new MockServer();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
//			JSONObject json = ExchangeConverter.toJSON(arg0);
			List<GameHeader> headers = server.getGameList();
			
			
//			JSONObject header = new JSONObject();
//			JSONArray array = new JSONArray();
//			for (GameHeader game : headers) {
//				JSONObject item = new JSONObject();
//				item.put("title", game.getTitle());
//				item.put("UUID", game.getTitle());
//				item.put("players", game.getPlayers());
//				
//				array.add(item);
//			}
//			
//			header.put("game", array);
//			System.out.println(header.toString());
//			StringBuilder str = new StringBuilder();
//			str.append("catan.user=");
//			str.append(URLEncoder.encode(header.toJSONString()));
//			str.append(";Path=/;");
//			String cookie = str.toString();
//			arg0.getResponseHeaders().add("Set-cookie", cookie);
//			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
//			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
//			//output.write(header.toJSONString());
//			JSONObject.writeJSONString(header, output);
//			output.flush();
//			arg0.getResponseBody().close();
			
		} catch (UserException e) {
			
		} catch (ServerException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			e.printStackTrace();
		}
	}

}
