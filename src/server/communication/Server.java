package server.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

import server.ai.AIType;
import server.commands.CatanCommand;
import server.commands.ICatanCommand;
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
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanModel;
import shared.model.ModelFacade;
import shared.model.ResourceList;
import shared.model.ResourceTradeList;

public class Server implements IServer {

	Map<UUID,ModelFacade> models = new HashMap<UUID,ModelFacade>();
	
	@Override
	public Session login(String username, String password) throws UserException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session register(String username, String password) throws UserException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GameHeader> getGameList() throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameHeader createGame(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws GameInitializationException, UserException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean joinGame(Session player, int gameID, CatanColor color) throws JoinGameException, ServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveGame(int gameID, String filename) throws GamePersistenceException, UserException, ServerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadGame(String filename) throws ServerException, UserException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getModel(int gameID, int version) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String resetGame(int gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Command> getCommands(int gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String executeCommands(int gameID, List<Command> commands) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAIPlayer(int gameID, AIType type) throws ServerException, UserException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAITypes() throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendChat(int user, int gameID, String message) throws ServerException, UserException {
		// TODO Auto-generated method stub
		try {
			ICatanCommand command = new CatanCommand("doSendChat",message);
			ModelFacade tempModel = new ModelFacade();
			command.execute(tempModel);
			return null;
		} catch (NoSuchMethodException | SecurityException | InvalidActionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String rollDice(int user, int gameID, int number) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String robPlayer(int user, int gameID, HexLocation newRobberLocation, int victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buyDevCard(int user, int gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String yearOfPlenty(int user, int gameID, ResourceType type1, ResourceType type2)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String roadBuilding(int user, int gameID, EdgeLocation road1, EdgeLocation road2)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String soldier(int user, int gameID, HexLocation newRobberLocation, int victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String monopoly(int user, int gameID, ResourceType type) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String monument(int user, int gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildRoad(int user, int gameID, EdgeLocation location, boolean free)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildSettlement(int user, int gameID, VertexLocation location, boolean free)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildCity(int user, int gameID, VertexLocation location)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String offerTrade(int user, int gameID, ResourceTradeList offer, int receiver)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String respondToTrade(int user, int gameID, boolean accept) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String maritimeTrade(int user, int gameID, ResourceType inResource, ResourceType outResource,
			int ratio) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String discardCards(int user, int gameID, ResourceList cards)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String finishTurn(int user, int gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeLogLevel(LogLevel level) throws ServerException, UserException {
		// TODO Auto-generated method stub
		
	}
	

}