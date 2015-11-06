package client.testing;

import java.util.List;

import org.json.simple.JSONObject;

import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.JoinGameException;
import shared.exceptions.SchemaMismatchException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.ModelFacade;
import shared.model.PlayerReference;
import client.communication.DataConverter;
import client.misc.ClientManager;

public class FirstTwoTurns {

	private int gameID;
	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();

	public static void main(String[] args) throws ServerException, UserException, SchemaMismatchException, JoinGameException{
		new FirstTwoTurns("Sam", "sam");
	}
	
	public FirstTwoTurns(String username, String password) throws ServerException, UserException, SchemaMismatchException, JoinGameException{
		Session user = serverProxy.login("Steve", "steve");
		serverProxy.joinGame(user, 3, CatanColor.PURPLE);
		List<GameHeader> games = serverProxy.getGameList();
		modelFacade.setGameInfo(DataConverter.convertHeaderToInfo(games.get(3)));
		gameID = modelFacade.getGameHeader().getId();
		JSONObject model = serverProxy.getModel(gameID, -1);
		JSONObject turnTracker = (JSONObject) model.get("turnTracker");
		Long currentTurn = (Long) turnTracker.get("currentTurn");
		String status = (String) turnTracker.get("status");
		PlayerReference zero = new PlayerReference(modelFacade.getGameHeader(), 0);
		PlayerReference one = new PlayerReference(modelFacade.getGameHeader(), 1);
		PlayerReference two = new PlayerReference(modelFacade.getGameHeader(), 2);
		PlayerReference three = new PlayerReference(modelFacade.getGameHeader(), 3);

		int i = 0;
		boolean second = false;
		while(i < 8){
			System.out.println("Current Turn: " + currentTurn);
			System.out.println("Status:       " + status);
			System.out.println("");
			
			if(second){
				currentTurn = currentTurn + 4;
			}
			
			switch (currentTurn.intValue()){
				case 0:	makeMove(zero, 2L, 0L, "SW", 2L, 0L, "SW");
						break;
				case 1:	makeMove(one, -2L, 0L, "S", -2L, 0L, "SW");
						break;
				case 2:	makeMove(two, 2L, -2L, "S", 2L, -2L, "SW");
						break;
				case 3:	makeMove(three, 0L, 0L, "S", 0L, 0L, "SW");
						second = true;
						break;
				case 4:	makeMove(three, 0L, 0L, "SE", 0L, 0L, "E");
						break;
				case 5:	makeMove(two, 2L, -2L, "SE", 2L, -2L, "E");
						break;
				case 6:	makeMove(one, -2L, 0L, "SE", -2L, 0L, "E");
						break;
				case 7:	makeMove(zero, -1L, -1L, "N", -1L, -1L, "NE");
						break;
			}
			
			model = serverProxy.getModel(gameID, -1);
			turnTracker = (JSONObject) model.get("turnTracker");
			currentTurn = (Long) turnTracker.get("currentTurn");
			status = (String) turnTracker.get("status");
			++i;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void makeMove(PlayerReference player, Long x, Long y, String direction,
						 Long x2, Long y2, String direction2) throws ServerException, UserException, SchemaMismatchException{
		JSONObject location = new JSONObject();
		location.put("x", x);
		location.put("y", y);
		location.put("direction", direction);
		EdgeLocation edgeLocation = new EdgeLocation(location);
		location = new JSONObject();
		location.put("x", x2);
		location.put("y", y2);
		location.put("direction", direction2);
		VertexLocation vertexLocation = new VertexLocation(location);
		serverProxy.buildRoad(player, gameID, edgeLocation, true);
		serverProxy.buildSettlement(player, gameID, vertexLocation, true);
		serverProxy.finishTurn(player, gameID);
	}
}
