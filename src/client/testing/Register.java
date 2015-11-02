package client.testing;

import shared.communication.IServer;
import shared.exceptions.GameInitializationException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;
import client.misc.ClientManager;

public class Register {

	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();

	public static void main(String[] args) throws UserException, ServerException{
		new Register("Steve", "steve");
	}
	
	public Register(String username, String password) throws UserException, ServerException{
		serverProxy.register(username, password);
	}

}
