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
		new CreateGame("Steve", "steve");
		new JoinGame(3, CatanColor.PURPLE);
		new AddThreePlayersToGame();
		new FirstTwoTurns("Steve", "steve");
		
		PlayerReference zero = new PlayerReference(modelFacade.getGameHeader(), 0);
		PlayerReference one = new PlayerReference(modelFacade.getGameHeader(), 1);
		PlayerReference two = new PlayerReference(modelFacade.getGameHeader(), 2);
		PlayerReference three = new PlayerReference(modelFacade.getGameHeader(), 3);

		serverProxy.rollDice(zero, 6);
	}

}
