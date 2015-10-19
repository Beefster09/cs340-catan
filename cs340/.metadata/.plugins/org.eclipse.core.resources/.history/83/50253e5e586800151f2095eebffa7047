package client.communication;

import java.util.List;

import server.ai.AIType;
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

/**
 * A fake server that will send back hard-coded information
 * This class is to be used for testing purposes only.
 * @author jchip
 *
 */
public class MockServer implements IServer {

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
	public List<GameHeader> getGameList() throws ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameHeader createGame(Session user, String name,
			boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws GameInitializationException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session joinGame(Session user, int gameID, CatanColor color)
			throws JoinGameException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveGame(int gameID, String filename)
			throws GamePersistenceException, ServerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadGame(String filename) throws GamePersistenceException,
			ServerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CatanModel getModel(Session user, int version)
			throws ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel resetGame(Session user) throws ServerException,
			GameInitializationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Command> getCommands(Session user) throws ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel executeCommands(Session user, List<Command> commands)
			throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AIType> getAITypes(Session user) throws ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel sendChat(Session user, String message)
			throws ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel rollDice(Session user, int number)
			throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel robPlayer(Session user, HexLocation newRobberLocation,
			PlayerReference victim) throws ServerException,
			InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel buyDevCard(Session user) throws ServerException,
			InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel yearOfPlenty(Session user, ResourceType type1,
			ResourceType type2) throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel roadBuilding(Session user, EdgeLocation road1,
			EdgeLocation road2) throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel soldier(Session user, HexLocation newRobberLocation,
			PlayerReference victim) throws ServerException,
			InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel monopoly(Session user, ResourceType type)
			throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel buildRoad(Session user, EdgeLocation location,
			boolean free) throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel buildSettlement(Session user, VertexLocation location,
			boolean free) throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel buildCity(Session user, VertexLocation location)
			throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel respondToTrade(Session user, boolean accept)
			throws ServerException, TradeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel maritimeTrade(Session user, ResourceType inResource,
			ResourceType outResource, int ratio) throws ServerException,
			InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel discardCards(Session user, ResourceList cards)
			throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel finishTurn(Session user) throws ServerException,
			InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAIPlayer(Session user, AIType type) {
		// TODO Auto-generated method stub
	}

	@Override
	public void changeLogLevel(LogLevel level) {
		// TODO Auto-generated method stub
	}

	@Override
	public CatanModel offerTrade(Session user, ResourceList offer)
			throws ServerException, NotYourTurnException {
		// TODO Auto-generated method stub
		return null;
	}

}
