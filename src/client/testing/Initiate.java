package client.testing;

import shared.communication.IServer;
import shared.definitions.CatanColor;
import shared.exceptions.GameInitializationException;
import shared.exceptions.JoinGameException;
import shared.exceptions.SchemaMismatchException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;
import shared.model.PlayerReference;
import client.misc.ClientManager;

public class Initiate {
	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();

	public static void main(String[] args) throws UserException, ServerException, JoinGameException, GameInitializationException, SchemaMismatchException{
		new Initiate();
	}
	
	public Initiate() throws UserException, ServerException, JoinGameException, GameInitializationException, SchemaMismatchException{
		new Register("Steve", "steve");
		int gameID = new CreateGame().run("Steve", "steve");
		new JoinGame(null, 3, CatanColor.PURPLE);
		new AddThreePlayersToGame();
		new FirstTwoTurns("Steve", "steve");
		
		PlayerReference zero = new PlayerReference(modelFacade.getGameHeader(), 0);

		serverProxy.rollDice(zero, gameID, 6);
	}

}
