package server.movehandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.Server;
import server.interpreter.ExchangeConverter;
import shared.communication.IServer;
import shared.exceptions.SchemaMismatchException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

/**
 * Handles buyDevCard requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class BuildStartingPiecesHandler extends AbstractMoveHandler implements HttpHandler {

	IServer server = Server.getSingleton();
//	IServer server = new MockServer();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String address = arg0.getRequestURI().toString();
		logger.log(Level.INFO, "Connection to " + address + " established.");

		try{
			UUID gameUUID = super.checkCookies(arg0, server);
			if(gameUUID == null){
				throw new ServerException();
			}
			JSONObject json = ExchangeConverter.toJSON(arg0);
			
			JSONParser parser = new JSONParser();
			
			JSONObject jsonObject = (JSONObject)parser.parse((String)json.get("settlementLocation"));
			VertexLocation settlementLocation = new VertexLocation(jsonObject);
			boolean settlementFree = (boolean)json.get("settlementFree");
			
			jsonObject = (JSONObject)parser.parse((String)json.get("roadLocation"));
			EdgeLocation roadLocation = new EdgeLocation(jsonObject);
			boolean roadFree = (boolean)json.get("roadFree");
			
			UUID index = UUID.fromString((String)json.get("playerIndex"));
			
			String gson = server.buildStartingPieces(index, gameUUID, settlementLocation, roadLocation);
			
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(gson.toString());
			output.flush();
			arg0.getResponseBody().close();
		} catch (ParseException | ServerException | UserException | SchemaMismatchException e) {
			e.printStackTrace();
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 500);
			arg0.getResponseBody().close();
		}
	}

}
