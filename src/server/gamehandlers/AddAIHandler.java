package server.gamehandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import server.ai.AIType;
import server.communication.Server;
import server.interpreter.ExchangeConverter;
import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.ServerException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AddAIHandler extends AbstractGameHandler implements HttpHandler {

	IServer server = Server.getSingleton();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		arg0.getResponseHeaders().set("Content-type:", "application/text");
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			JSONObject json = ExchangeConverter.toJSON(arg0);
			UUID gameUUID = UUID.fromString((String)json.get("game"));
			AIType aiType = AIType.fromString((String)json.get("type"));
			server.addAIPlayer(gameUUID, aiType);
			
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write("Success!");
			output.flush();
			arg0.getResponseBody().close();
		} catch (Exception e) {
			e.printStackTrace();
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			arg0.getResponseBody().close();
		}
	}

}
