package server.communication;

import java.util.List;

import org.json.simple.JSONObject;

import server.ai.AIType;
import server.commands.CatanCommand;
import server.logging.LogLevel;
import shared.communication.Command;
import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.exceptions.GameInitializationException;
import shared.exceptions.GamePersistenceException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.JoinGameException;
import shared.exceptions.NotYourTurnException;
import shared.exceptions.ServerException;
import shared.exceptions.TradeException;
import shared.exceptions.UserException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanModel;
import shared.model.PlayerReference;
import shared.model.ResourceList;
import shared.model.ResourceTradeList;

public class Server implements IServer {

	List<CatanCommand> commands;
	
	@Override
	public Session login(String username, String password)
			throws UserException, ServerException {
		System.out.println("logging in");
		return null;
	}

	@Override
	public Session register(String username, String password)
			throws UserException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GameHeader> getGameList() throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameHeader createGame(String name, boolean randomTiles,
			boolean randomNumbers, boolean randomPorts)
			throws GameInitializationException, UserException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean joinGame(Session player, int gameID, CatanColor color)
			throws JoinGameException, ServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveGame(int gameID, String filename)
			throws GamePersistenceException, UserException, ServerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadGame(String filename) throws ServerException, UserException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject getModel(int gameID, int version) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject resetGame(int gameID) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Command> getCommands(int gameID) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject executeCommands(int gameID, List<Command> commands)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAIPlayer(int gameID, AIType type) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAITypes() throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject sendChat(PlayerReference user, int gameID, String message)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject rollDice(PlayerReference user, int gameID, int number)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject robPlayer(PlayerReference user, int gameID,
			HexLocation newRobberLocation, PlayerReference victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject buyDevCard(PlayerReference user, int gameID)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject yearOfPlenty(PlayerReference user, int gameID,
			ResourceType type1, ResourceType type2) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject roadBuilding(PlayerReference user, int gameID,
			EdgeLocation road1, EdgeLocation road2) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject soldier(PlayerReference user, int gameID,
			HexLocation newRobberLocation, PlayerReference victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject monopoly(PlayerReference user, int gameID,
			ResourceType type) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject monument(PlayerReference user, int gameID)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject buildRoad(PlayerReference user, int gameID,
			EdgeLocation location, boolean free) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject buildSettlement(PlayerReference user, int gameID,
			VertexLocation location, boolean free) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject buildCity(PlayerReference user, int gameID,
			VertexLocation location) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject offerTrade(PlayerReference user, int gameID,
			ResourceTradeList offer, PlayerReference receiver)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject respondToTrade(PlayerReference user, int gameID,
			boolean accept) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject maritimeTrade(PlayerReference user, int gameID,
			ResourceType inResource, ResourceType outResource, int ratio)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject discardCards(PlayerReference user, int gameID,
			ResourceList cards) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject finishTurn(PlayerReference user, int gameID)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeLogLevel(LogLevel level) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		
	}

}
