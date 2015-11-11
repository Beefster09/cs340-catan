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
		// TODO Auto-generated method stub
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
	public boolean joinGame(int gameID, CatanColor color)
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
	public JSONObject getModel(int version) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject resetGame() throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Command> getCommands() throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject executeCommands(List<Command> commands)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAIPlayer(AIType type) throws ServerException, UserException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAITypes() throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject sendChat(PlayerReference user, String message)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject rollDice(PlayerReference user, int number)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject robPlayer(PlayerReference user,
			HexLocation newRobberLocation, PlayerReference victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject buyDevCard(PlayerReference user) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject yearOfPlenty(PlayerReference user, ResourceType type1,
			ResourceType type2) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject roadBuilding(PlayerReference user, EdgeLocation road1,
			EdgeLocation road2) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject soldier(PlayerReference user,
			HexLocation newRobberLocation, PlayerReference victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject monopoly(PlayerReference user, ResourceType type)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject monument(PlayerReference user) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject buildRoad(PlayerReference user, EdgeLocation location,
			boolean free) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject buildSettlement(PlayerReference user,
			VertexLocation location, boolean free) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject buildCity(PlayerReference user, VertexLocation location)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject offerTrade(PlayerReference user, ResourceTradeList offer,
			PlayerReference receiver) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject respondToTrade(PlayerReference user, boolean accept)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject maritimeTrade(PlayerReference user,
			ResourceType inResource, ResourceType outResource, int ratio)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject discardCards(PlayerReference user, ResourceList cards)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject finishTurn(PlayerReference user) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeLogLevel(LogLevel level) throws ServerException,
			UserException {
		// TODO Auto-generated method stub
		
	}

}
