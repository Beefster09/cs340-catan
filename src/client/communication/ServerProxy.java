package client.communication;

import java.util.List;

import org.json.simple.JSONObject;

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

/**
 * A proxy that acts as the server to the client. Contains all the methods
 * that can be called on the server.  Each method bundles the given variables
 * and gives it to the ClientCommunicator to send through the HTTP protocol.
 * @author jchip
 *
 */
public class ServerProxy implements IServer {

	private ClientCommunicator commuincator = new ClientCommunicator();
	
	@SuppressWarnings("unchecked")
	@Override
	public Session login(String username, String password)
			throws UserException, ServerException {
		if(username == null || password == null){
			throw new UserException();
		}
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/user/login");
		o.put("requestType", "POST");
		o.put("username", username);
		o.put("password", password);
		JSONObject returned = commuincator.send(o);
		String returnedUsername = (String) returned.get("name");
		String returnedPassword = (String) returned.get("password");
		int playerID = (Integer) returned.get("playerID");
		return new Session(returnedUsername, returnedPassword, playerID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Session register(String username, String password)
			throws UserException, ServerException {
		if(username == null || password == null){
			throw new UserException();
		}
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/user/register");
		o.put("requestType", "POST");
		o.put("username", username);
		o.put("password", password);
		JSONObject returned = commuincator.send(o);
		String returnedUsername = (String) returned.get("name");
		String returnedPassword = (String) returned.get("password");
		int playerID = (Integer) returned.get("playerID");
		return new Session(returnedUsername, returnedPassword, playerID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GameHeader> getGameList() throws ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/games/list");
		o.put("requestType", "GET");
		JSONObject returned = commuincator.send(o);
		String title = (String) returned.get("title");
		int id = (int) returned.get("id");
		List<JSONObject> players = (List<JSONObject>) returned.get("players");
		for(JSONObject json : players){
			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public GameHeader createGame(Session user, String name,
			boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws GameInitializationException, ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/games/create");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Session joinGame(Session user, int gameID, CatanColor color)
			throws JoinGameException, ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/games/join");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveGame(int gameID, String filename)
			throws GamePersistenceException, ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/games/save");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadGame(String filename) throws GamePersistenceException,
			ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/games/load");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel getModel(Session user, int version)
			throws ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/model");
		o.put("requestType", "GET");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel resetGame(Session user) throws ServerException,
			GameInitializationException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/reset");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Command> getCommands(Session user) throws ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/commands");
		o.put("requestType", "GET");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel executeCommands(Session user, List<Command> commands)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/commands");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addAIPlayer(Session user, AIType type) throws ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/addAI");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AIType> getAITypes(Session user) throws ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/listAI");
		o.put("requestType", "GET");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel sendChat(Session user, String message)
			throws ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/sendChat");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel rollDice(Session user, int number)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/rollNumber");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel robPlayer(Session user, HexLocation newRobberLocation,
			PlayerReference victim) throws ServerException,
			InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/robPlayer");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel buyDevCard(Session user) throws ServerException,
			InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buyDevCard");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel yearOfPlenty(Session user, ResourceType type1,
			ResourceType type2) throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Year_of_Plenty");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel roadBuilding(Session user, EdgeLocation road1,
			EdgeLocation road2) throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Road_Building");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatanModel soldier(Session user, HexLocation newRobberLocation,
			PlayerReference victim) throws ServerException,
			InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Solder");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public CatanModel monopoly(Session user, ResourceType type)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Monopoly");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public CatanModel buildRoad(Session user, EdgeLocation location,
			boolean free) throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buildRoad");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public CatanModel buildSettlement(Session user, VertexLocation location,
			boolean free) throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buildSettlement");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public CatanModel buildCity(Session user, VertexLocation location)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buildCity");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public CatanModel offerTrade(Session user, ResourceList offer)
			throws ServerException, NotYourTurnException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/offerTrade");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public CatanModel respondToTrade(Session user, boolean accept)
			throws ServerException, TradeException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/acceptTrade");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public CatanModel maritimeTrade(Session user, ResourceType inResource,
			ResourceType outResource, int ratio) throws ServerException,
			InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/maritimeTrade");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public CatanModel discardCards(Session user, ResourceList cards)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/discardCards");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public CatanModel finishTurn(Session user) throws ServerException,
			InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/finishTurn");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public void changeLogLevel(LogLevel level) throws ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
		
	}

	@Override
	public CatanModel monument(Session user) throws ServerException,
			InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Monument");
		o.put("requestType", "GET");
		JSONObject returned = commuincator.send(o);
	}}
