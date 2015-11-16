package server.userhandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import client.communication.MockServer;
import server.interpreter.ExchangeConverter;
import shared.communication.IServer;
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

	IServer server = new MockServer();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			
			JSONObject json = ExchangeConverter.toJSON(arg0);
			
			String username = (String) json.get("username");
			String password = (String) json.get("password");
			Session user = server.register(username, password);
			
			
			JSONObject header = new JSONObject();
			header.put("name", user.getUsername());
			header.put("password", user.getPassword());
			header.put("playerUUID", user.getPlayerUUID());
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
