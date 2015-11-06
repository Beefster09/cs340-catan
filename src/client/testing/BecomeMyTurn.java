package client.testing;

import java.util.List;

import org.json.simple.JSONObject;

import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;
import shared.model.PlayerReference;
import client.communication.DataConverter;
import client.misc.ClientManager;

public class BecomeMyTurn {
	
	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();
	
	public static void main(String[] args) throws UserException, ServerException, JoinGameException{
		new BecomeMyTurn("Sam", "sam");
	}
	
	public BecomeMyTurn(String username, String password) throws UserException, ServerException, JoinGameException{
		Session user = serverProxy.login("Steve", "steve");
		serverProxy.joinGame(user, 3, CatanColor.PURPLE);
		List<GameHeader> games = serverProxy.getGameList();
		modelFacade.setGameInfo(DataConverter.convertHeaderToInfo(games.get(3)));
		int gameID = modelFacade.getGameHeader().getId();
		JSONObject model = serverProxy.getModel(gameID, -1);
		JSONObject turnTracker = (JSONObject) model.get("turnTracker");
		Long currentTurn = (Long) turnTracker.get("currentTurn");
		String status = (String) turnTracker.get("status");
		PlayerReference one = new PlayerReference(modelFacade.getGameHeader(), 1);
		PlayerReference two = new PlayerReference(modelFacade.getGameHeader(), 2);
		PlayerReference three = new PlayerReference(modelFacade.getGameHeader(), 3);
			
		while(currentTurn != 0){
			System.out.println("Current Turn: " + currentTurn);
			System.out.println("Status:       " + status);
			System.out.println("");
			
			if(status.equals("Rolling")){
				switch (currentTurn.intValue()){
					case 1:	serverProxy.rollDice(one, gameID, 6);
							break;
					case 2:	serverProxy.rollDice(two, gameID, 6);
							break;
					case 3:	serverProxy.rollDice(three, gameID, 6);
							break;
				}
				model = serverProxy.getModel(gameID, -1);
				turnTracker = (JSONObject) model.get("turnTracker");
				currentTurn = (Long) turnTracker.get("currentTurn");
				status = (String) turnTracker.get("status");
			}
			else if(status.equals("Playing")){
				switch (currentTurn.intValue()){
					case 1:	serverProxy.finishTurn(one, gameID);
							break;
					case 2:	serverProxy.finishTurn(two, gameID);
							break;
					case 3:	serverProxy.finishTurn(three, gameID);
							break;
				}
				model = serverProxy.getModel(gameID, -1);
				turnTracker = (JSONObject) model.get("turnTracker");
				currentTurn = (Long) turnTracker.get("currentTurn");
				status = (String) turnTracker.get("status");
			}
		}			
	}

}
