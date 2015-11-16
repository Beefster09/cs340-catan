package server.movehandlers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import client.communication.MockServer;
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
			int versionID = Integer.valueOf(address.substring(20));
			
			String header = server.getModel(gameID, versionID);
			
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			OutputStreamWriter output = new OutputStreamWriter(arg0.getResponseBody());
			output.write(header);
			output.flush();
			arg0.getResponseBody().close();
			
		} catch (ServerException | UserException e) {
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}

}