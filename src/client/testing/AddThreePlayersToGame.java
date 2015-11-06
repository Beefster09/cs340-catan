package client.testing;

import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import client.misc.ClientManager;

public class AddThreePlayersToGame {

	private IServer serverProxy = ClientManager.getServer();

	public static void main(String[] args) throws UserException, ServerException, JoinGameException{
		new AddThreePlayersToGame();
	}
	
	public AddThreePlayersToGame() throws UserException, ServerException, JoinGameException{
		Session user = serverProxy.login("Sam", "sam");
		serverProxy.joinGame(user, 3, CatanColor.RED);
		serverProxy.login("Brooke", "brooke");
		serverProxy.joinGame(user, 3, CatanColor.BLUE);
		serverProxy.login("Pete", "pete");
		serverProxy.joinGame(user, 3, CatanColor.GREEN);
	}
}
