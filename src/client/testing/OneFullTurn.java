package client.testing;

import java.util.List;

import org.json.simple.JSONObject;

import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.GameInitializationException;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;
import shared.model.PlayerReference;
import client.communication.DataConverter;
import client.misc.ClientManager;

public class OneFullTurn {

	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();

	public static void main(String[] args) throws UserException, ServerException, GameInitializationException, JoinGameException{
		new OneFullTurn("Sam", "sam");
	}
	
	public OneFullTurn(String username, String password) throws UserException, ServerException, JoinGameException{
		Session user = serverProxy.login("Steve", "steve");
		serverProxy.joinGame(user, 3, CatanColor.PURPLE);
		List<GameHeader> games = serverProxy.getGameList();
		modelFacade.setGameInfo(DataConverter.convertHeaderToInfo(games.get(3)));
		int gameID = modelFacade.getGameHeader().getId();
		JSONObject model = serverProxy.getModel(gameID, -1);
		JSONObject turnTracker = (JSONObject) model.get("turnTracker");
		Long currentTurn = (Long) turnTracker.get("currentTurn");
		String status = (String) turnTracker.get("status");
		PlayerReference zero = new PlayerReference(modelFacade.getGameHeader(), 0);

		if(currentTurn == 0){
			if(status.equals("Rolling")){
				serverProxy.rollDice(zero, gameID, 6);
			}
			serverProxy.finishTurn(zero, gameID);
		}
		new BecomeMyTurn(username, password);
		serverProxy.rollDice(zero, gameID, 6);
	}

}
