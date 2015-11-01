package client.testing;

import shared.communication.IServer;
import shared.definitions.CatanColor;
import shared.exceptions.GameInitializationException;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;
import client.misc.ClientManager;

public class JoinGame {

	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();

	public static void main(String[] args) throws JoinGameException, ServerException{
		new JoinGame(3, CatanColor.RED);
	}
	
	public JoinGame(int game, CatanColor color) throws JoinGameException, ServerException{
		serverProxy.joinGame(game, color);
	}
}
