package server.movehandlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.JSONParser;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.communication.Server;
import shared.communication.IServer;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;

/**
 * Handles getModel requests by communicating with the Server Facade,
 * and sends the response back through the httpExchange.
 * @author Jordan
 *
 */
public class ModelHandler extends AbstractMoveHandler implements HttpHandler {

	IServer server = Server.getSingleton();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		arg0.getResponseHeaders().set("Content-type:", "application/text");
		URI uri = arg0.getRequestURI();
		logger.log(Level.INFO, "Connection to " + uri + " established.");

		try{
			UUID gameUUID = null;
			if(gameUUID == null){
				List<String> gameInfo = arg0.getRequestHeaders().get("game");
				if(gameInfo.size() != 1){
					throw new ServerException();
				}
								
				String gameString = gameInfo.get(0);

				gameUUID = UUID.fromString(gameString);
			}
			String query = uri.getQuery();
			int versionID = Integer.parseInt(query.substring(query.indexOf('=') + 1));
			
			String model = server.getModel(gameUUID, versionID);
			
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			if (model != null) {
				OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
				output.write(model);
				output.flush();
				arg0.getResponseBody().close();
			}
			
		} catch (Exception e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			e.printStackTrace();
		}
	}

}
