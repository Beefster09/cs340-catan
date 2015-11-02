package client.testing;

import shared.communication.IServer;
import shared.definitions.CatanColor;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;
import client.misc.ClientManager;

public class AddThreePlayersToGame {

	private IServer serverProxy = ClientManager.getServer();

	public static void main(String[] args) throws UserException, ServerException, JoinGameException{
		new AddThreePlayersToGame();
	}
	
	public AddThreePlayersToGame() throws UserException, ServerException, JoinGameException{
		serverProxy.login("Sam", "sam");
		serverProxy.joinGame(3, CatanColor.RED);
		serverProxy.login("Brooke", "brooke");
		serverProxy.joinGame(3, CatanColor.BLUE);
		serverProxy.login("Pete", "pete");
		serverProxy.joinGame(3, CatanColor.GREEN);
	}
}
