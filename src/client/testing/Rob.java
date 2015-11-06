package client.testing;

import java.util.List;

import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.HexLocation;
import shared.model.ModelFacade;
import shared.model.PlayerReference;
import client.communication.DataConverter;
import client.misc.ClientManager;

public class Rob {

	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();
	
	public static void main(String[] args) throws JoinGameException, ServerException, UserException{
		new Rob("Sam", "sam");
	}
	
	public Rob(String username, String password) throws JoinGameException, ServerException, UserException{
		Session user = serverProxy.login("Steve", "steve");
		serverProxy.joinGame(user, 3, CatanColor.PURPLE);
		List<GameHeader> games = serverProxy.getGameList();
		modelFacade.setGameInfo(DataConverter.convertHeaderToInfo(games.get(3)));
		int gameID = modelFacade.getGameHeader().getId();
		PlayerReference zero = new PlayerReference(modelFacade.getGameHeader(), 0);			
		PlayerReference nullPlayer = PlayerReference.getDummyPlayerReference(-1);
		HexLocation hexLocation = new HexLocation(1,-2);
		
		serverProxy.robPlayer(zero, gameID, hexLocation, nullPlayer);
	}

}
