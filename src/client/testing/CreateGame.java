package client.testing;

import shared.communication.IServer;
import shared.exceptions.GameInitializationException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;
import client.misc.ClientManager;

public class CreateGame {

	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();

	public static void main(String[] args) throws UserException, ServerException, GameInitializationException{
		new CreateGame("Sam", "sam");
	}
	
	public CreateGame(String username, String password) throws UserException, ServerException, GameInitializationException{
		serverProxy.login(username, password);
		serverProxy.createGame("Test", false, false, false);
	}
}
