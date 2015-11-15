package server.movehandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import client.communication.MockServer;
import server.interpreter.ExchangeConverter;
import shared.communication.IServer;
import shared.exceptions.SchemaMismatchException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.VertexLocation;

/**
 * Handles buildSettlement requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class BuildSettlementHandler extends AbstractMoveHandler implements HttpHandler {

	IServer server = new MockServer();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			int gameID = super.checkCookies(arg0, server);
			if(gameID == -1){
				throw new ServerException();
			}
			JSONObject json = ExchangeConverter.toJSON(arg0);
			
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject)parser.parse((String)json.get("vertexLocation"));
			
			VertexLocation location = new VertexLocation(jsonObject);
			boolean free = (boolean)json.get("free");
			int index = (int)(long)json.get("playerIndex");
			String gson = server.buildSettlement(index, gameID, location, free);
			
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(gson.toString());
			output.flush();
			arg0.getResponseBody().close();
		} catch (ParseException | ServerException | UserException | SchemaMismatchException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			arg0.getResponseBody().close();
		}
	}

}
