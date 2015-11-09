package server.userhandlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.Server;
import shared.communication.Session;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;

/**
 * Handles register requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class RegisterHandler implements HttpHandler {

	Server server;
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@SuppressWarnings({ "deprecation", "unchecked" })
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
			String username = (String) json.get("username");
			String password = (String) json.get("password");
			Session user = server.register(username, password);
			
			
			JSONObject header = new JSONObject();
			header.put("name", user.getUsername());
			header.put("password", user.getPassword());
			header.put("playerID", user.getPlayerID());
			StringBuilder str = new StringBuilder();
			str.append("catan.user=");
			str.append(URLEncoder.encode(header.toJSONString()));
			str.append(";Path=/;");
			String cookie = str.toString();
			arg0.getResponseHeaders().add("Set-cookie", cookie);
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(header.toString());
			output.flush();
			arg0.getResponseBody().close();

		}
		catch(ParseException | UserException | ServerException e){
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			arg0.getResponseBody().close();
		}
	}

}
