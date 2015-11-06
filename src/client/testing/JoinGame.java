package client.testing;

import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import client.misc.ClientManager;

public class JoinGame {

	private IServer serverProxy = ClientManager.getServer();
	public static void main(String[] args) throws JoinGameException, ServerException{
		new JoinGame(null, 3, CatanColor.RED);
	}
	
	public JoinGame(Session user, int game, CatanColor color) throws JoinGameException, ServerException{
		serverProxy.joinGame(user, game, color);
	}
}
