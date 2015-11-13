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

import com.google.gson.Gson;
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
			JSONObject json = ExchangeConverter.toJSON(arg0);
			List<GameHeader> headers = server.getGameList();
			
			Gson gson = new Gson();
			

			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(gson.toJson(headers));
			output.flush();
			arg0.getResponseBody().close();
			
		} catch (UserException e) {
			
		} catch (ServerException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			e.printStackTrace();
		} catch (ParseException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			e.printStackTrace();
		}
	}

}
