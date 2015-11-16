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
import shared.model.PlayerReference;
import shared.model.ResourceList;
import shared.model.ResourceTradeList;

public class Server implements IServer {

	Map<UUID,ModelFacade> models = new HashMap<UUID,ModelFacade>();
	Map<UUID,Session> users = new HashMap<UUID,Session>();
	
	@Override
	public Session login(String username, String password) throws UserException, ServerException {
		// TODO Auto-generated method stub
		//VERY TEMPORARY, NEED VALIDATION HERE
		Session session = new Session(username,password,UUID.randomUUID());
		return session;
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
	public boolean joinGame(Session player, UUID gameID, CatanColor color) throws JoinGameException, ServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveGame(UUID gameID, String filename) throws GamePersistenceException, UserException, ServerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadGame(String filename) throws ServerException, UserException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getModel(UUID gameID, int version) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String resetGame(UUID gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Command> getCommands(UUID gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String executeCommands(UUID gameID, List<Command> commands) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAIPlayer(UUID gameID, AIType type) throws ServerException, UserException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAITypes() throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendChat(UUID user, UUID gameID, String message) throws ServerException, UserException {
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
	public String rollDice(UUID user, UUID gameID, int number) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String robPlayer(UUID user, UUID gameID, HexLocation newRobberLocation, UUID victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buyDevCard(UUID user, UUID gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String yearOfPlenty(UUID user, UUID gameID, ResourceType type1, ResourceType type2)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String roadBuilding(UUID user, UUID gameID, EdgeLocation road1, EdgeLocation road2)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String soldier(UUID user, UUID gameID, HexLocation newRobberLocation, UUID victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String monopoly(UUID user, UUID gameID, ResourceType type) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String monument(UUID user, UUID gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildRoad(UUID user, UUID gameID, EdgeLocation location, boolean free)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildSettlement(UUID user, UUID gameID, VertexLocation location, boolean free)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildCity(UUID user, UUID gameID, VertexLocation location)
			throws ServerException, UserException {
//		try {
//			ICatanCommand command = new CatanCommand("buildCity", PlayerReference.getDummyPlayerReference(user),location);
//			ModelFacade tempModel = new ModelFacade();
//			command.execute(tempModel);
//			return this.getModel(gameID, -1);
//		} catch (NoSuchMethodException | SecurityException | InvalidActionException e) {
//			e.printStackTrace();
//		}
//		return this.getModel(gameID, -1);
		return null;
	}

	@Override
	public String offerTrade(UUID user, UUID gameID, ResourceTradeList offer, UUID receiver)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String respondToTrade(UUID user, UUID gameID, boolean accept) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String maritimeTrade(UUID user, UUID gameID, ResourceType inResource, ResourceType outResource,
			int ratio) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String discardCards(UUID user, UUID gameID, ResourceList cards)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String finishTurn(UUID user, UUID gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeLogLevel(LogLevel level) throws ServerException, UserException {
		// TODO Auto-generated method stub
		
	}
	

}