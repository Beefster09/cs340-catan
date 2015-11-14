package server.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

import server.ai.AIType;
import server.logging.LogLevel;
import shared.communication.Command;
import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.PlayerHeader;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.exceptions.GameInitializationException;
import shared.exceptions.GamePersistenceException;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.PlayerReference;
import shared.model.ResourceList;
import shared.model.ResourceTradeList;

public class MockServer implements IServer {

	@Override
	public Session login(String username, String password)
			throws UserException, ServerException {

		Session fakeUser = new Session("Sam", "sam", 0);

		return fakeUser;
	}

	@Override
	public Session register(String username, String password)
			throws UserException, ServerException {

		Session fakeUser = new Session("Fake", "fake", 1);
		return fakeUser;
	}

	@Override
	public List<GameHeader> getGameList() throws ServerException, UserException {
		
		List<GameHeader> headers = new ArrayList<GameHeader>();
		for (int i = 0; i < 5; i++) {
			List<PlayerHeader> players = new ArrayList<PlayerHeader>();
			players.add(new PlayerHeader(CatanColor.BLUE, "Sam", UUID.randomUUID()));
			GameHeader tempGame = new GameHeader("test"+Integer.toString(i),UUID.randomUUID(),players);
			headers.add(tempGame);
		}
		
		return headers;
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
	public String sendChat(PlayerReference user, int gameID, String message) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String rollDice(PlayerReference user, int gameID, int number) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String robPlayer(PlayerReference user, int gameID, HexLocation newRobberLocation, PlayerReference victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buyDevCard(PlayerReference user, int gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String yearOfPlenty(PlayerReference user, int gameID, ResourceType type1, ResourceType type2)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String roadBuilding(PlayerReference user, int gameID, EdgeLocation road1, EdgeLocation road2)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String soldier(PlayerReference user, int gameID, HexLocation newRobberLocation, PlayerReference victim)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String monopoly(PlayerReference user, int gameID, ResourceType type) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String monument(PlayerReference user, int gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildRoad(PlayerReference user, int gameID, EdgeLocation location, boolean free)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildSettlement(PlayerReference user, int gameID, VertexLocation location, boolean free)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildCity(PlayerReference user, int gameID, VertexLocation location)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String offerTrade(PlayerReference user, int gameID, ResourceTradeList offer, PlayerReference receiver)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String respondToTrade(PlayerReference user, int gameID, boolean accept) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String maritimeTrade(PlayerReference user, int gameID, ResourceType inResource, ResourceType outResource,
			int ratio) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String discardCards(PlayerReference user, int gameID, ResourceList cards)
			throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String finishTurn(PlayerReference user, int gameID) throws ServerException, UserException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeLogLevel(LogLevel level) throws ServerException, UserException {
		// TODO Auto-generated method stub
		
	}
}