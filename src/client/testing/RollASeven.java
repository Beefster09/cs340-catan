package client.testing;

import java.util.List;

import org.json.simple.JSONObject;

import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.definitions.CatanColor;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.HexLocation;
import shared.model.ModelFacade;
import shared.model.PlayerReference;
import client.communication.DataConverter;
import client.misc.ClientManager;

public class RollASeven {

	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();

	public static void main(String[] args) throws ServerException, UserException, JoinGameException{
		new RollASeven("Sam", "sam");
	}
	
	public RollASeven(String username, String password) throws ServerException, UserException, JoinGameException{
		serverProxy.login("Steve", "steve");
		serverProxy.joinGame(3, CatanColor.PURPLE);
		List<GameHeader> games = serverProxy.getGameList();
		modelFacade.setGameInfo(DataConverter.convertHeaderToInfo(games.get(3)));
		JSONObject model = serverProxy.getModel(-1);
		JSONObject turnTracker = (JSONObject) model.get("turnTracker");
		Long currentTurn = (Long) turnTracker.get("currentTurn");
		String status = (String) turnTracker.get("status");
		PlayerReference zero = new PlayerReference(modelFacade.getGameHeader(), 0);			
		PlayerReference nullPlayer = PlayerReference.getDummyPlayerReference(-1);
		HexLocation hexLocation = new HexLocation(1,-2);
		
		if(currentTurn == 0 && status.equals("Rolling")){
			serverProxy.rollDice(zero, 7);
			serverProxy.robPlayer(zero, hexLocation, nullPlayer);
		}
		else{
			System.out.println("It wasn't your turn");
		}			
	}
}
