package client.testing;

import shared.communication.IServer;
import shared.exceptions.GameInitializationException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import client.misc.ClientManager;

public class CreateGame {

	private IServer serverProxy = ClientManager.getServer();

	public static void main(String[] args) throws UserException, ServerException, GameInitializationException{
		new CreateGame().run("Sam", "sam");
	}
		
	public int run(String username, String password) throws GameInitializationException, UserException, ServerException{
		serverProxy.login(username, password);
		return(serverProxy.createGame("Test", false, false, false)).getId();
	}
}
