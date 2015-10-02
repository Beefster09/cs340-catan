package client.communication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		
		return new Session("SAM", "sam", 1);
	}

	@Override
	public Session register(String username, String password)
			throws UserException, ServerException {
		
		return new Session("JOE", "joe", 1);
	}

	@Override
	public List<GameHeader> getGameList() throws ServerException,
			InvalidActionException {
		
		List<GameHeader> returnList = new ArrayList<GameHeader>();
		List<PlayerHeader> players = new ArrayList<PlayerHeader>();
		players.add(new PlayerHeader(CatanColor.BLUE, "Jim", 1));
		GameHeader returnGame = new GameHeader("GameTest", 1, players);
		returnList.add(returnGame);
		return returnList;
	}

	@Override
	public GameHeader createGame(String name, boolean randomTiles,
			boolean randomNumbers, boolean randomPorts)
			throws GameInitializationException, InvalidActionException,
			ServerException {
		
		List<PlayerHeader> players = new ArrayList<PlayerHeader>();
		players.add(new PlayerHeader(CatanColor.BLUE, "Jim", 1));
		GameHeader returnGame = new GameHeader("GameTest", 1, players);
		
		return returnGame;
	}

	@Override
	public boolean joinGame(int gameID, CatanColor color)
			throws JoinGameException, ServerException {
		
		return true;
	}

	@Override
	public void saveGame(int gameID, String filename)
			throws GamePersistenceException, InvalidActionException,
			ServerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadGame(String filename) throws ServerException,
			InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject getModel(int version) throws ServerException,
			InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject resetGame() throws ServerException,
			InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public List<Command> getCommands() throws ServerException,
			InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject executeCommands(List<Command> commands)
			throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAIPlayer(AIType type) throws ServerException,
			InvalidActionException {
		
		//return "Success";
		
	}

	@Override
	public List<String> getAITypes() throws ServerException,
			InvalidActionException {
		
		List<String> aiTypes = new ArrayList<String>();
		aiTypes.add("LARGEST_ARMY");
		return aiTypes;
	}

	@Override
	public JSONObject sendChat(PlayerReference user, String message)
			throws ServerException, InvalidActionException {
		// TODO Auto-generated method stub
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
		
	}

	@Override
	public JSONObject rollDice(PlayerReference user, int number)
			throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject robPlayer(PlayerReference user,
			HexLocation newRobberLocation, PlayerReference victim)
			throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
		
	}

	@Override
	public JSONObject buyDevCard(PlayerReference user) throws ServerException,
			InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject yearOfPlenty(PlayerReference user, ResourceType type1,
			ResourceType type2) throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject roadBuilding(PlayerReference user, EdgeLocation road1,
			EdgeLocation road2) throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject soldier(PlayerReference user,
			HexLocation newRobberLocation, PlayerReference victim)
			throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject monopoly(PlayerReference user, ResourceType type)
			throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject monument(PlayerReference user) throws ServerException,
			InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject buildRoad(PlayerReference user, EdgeLocation location,
			boolean free) throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject buildSettlement(PlayerReference user,
			VertexLocation location, boolean free) throws ServerException,
			InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject buildCity(PlayerReference user, VertexLocation location)
			throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject offerTrade(PlayerReference user, ResourceTradeList offer,
			PlayerReference receiver) throws ServerException,
			InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject respondToTrade(PlayerReference user, boolean accept)
			throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject maritimeTrade(PlayerReference user,
			ResourceType inResource, ResourceType outResource, int ratio)
			throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject discardCards(PlayerReference user, ResourceList cards)
			throws ServerException, InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public JSONObject finishTurn(PlayerReference user) throws ServerException,
			InvalidActionException {
		
		try {
			JSONParser parser = new JSONParser();
			Reader r = new BufferedReader(new FileReader("json_test.json"));
			Object parseResult = parser.parse(r);
			return ((JSONObject) parseResult);
		} catch (IOException | ParseException e) {
		}
		return null;
	}

	@Override
	public void changeLogLevel(LogLevel level) throws ServerException,
			InvalidActionException {
		// TODO Auto-generated method stub
		
	}

}
