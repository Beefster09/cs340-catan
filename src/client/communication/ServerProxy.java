package client.communication;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

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
import shared.exceptions.SchemaMismatchException;
import shared.exceptions.UserException;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import client.misc.ClientManager;
import shared.model.ResourceList;
import shared.model.ResourceTradeList;

/**
 * A proxy that acts as the server to the client. Contains all the methods
 * that can be called on the server.  Each method bundles the given variables
 * and gives it to the ClientCommunicator to send through the HTTP protocol.
 * @author jchip
 *
 */
public class ServerProxy implements IServer {
		
	//Get the Singleton for this class
	//private static IServer instance = new ServerProxy();
	@Deprecated
	public static IServer getInstance(){
		return ClientManager.getServer();
	}
	
	public static void main(String[] args) throws JoinGameException, ServerException, UserException {
		ServerProxy test = new ServerProxy("localhost",8081);
		Session player = test.login("Sam", "sam");
		if (test.joinGame(player, 0, CatanColor.BLUE)) {
			VertexLocation loc = new VertexLocation(0, 0, VertexDirection.East);
			test.buildCity(0, 0, loc);
			test.sendChat(0, 0, "test");
		}
	}

	private ClientCommunicator communicator = new ClientCommunicator();
	private String host = null;
	private int port = -1;
	private Gson gson;

	public ServerProxy(String host, int port){
		this.host = host;
		this.port = port;
		gson = new Gson();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Session login(String username, String password)
			throws UserException, ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/user/login");
		o.put("requestType", "POST");
		o.put("username", username);
		o.put("password", password);
		JSONObject returned = communicator.login(o);
		String returnedName = (String) returned.get("name");
		String returnedPassword = (String) returned.get("password");
		int playerID = ((Long)returned.get("playerID")).intValue();
		return new Session(returnedName, returnedPassword, playerID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Session register(String username, String password)
			throws UserException, ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/user/register");
		o.put("requestType", "POST");
		o.put("username", username);
		o.put("password", password);
		JSONObject returned = communicator.login(o);
		String returnedName = (String) returned.get("name");
		String returnedPassword = (String) returned.get("password");
		int playerID = ((Long)returned.get("playerID")).intValue();
		return new Session(returnedName, returnedPassword, playerID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GameHeader> getGameList() throws ServerException, UserException {
		try{
			JSONObject o = new JSONObject();
			o.put("url","http://" + host + ":" + Integer.toString(port) + "/games/list");
			o.put("requestType", "GET");
			JSONObject returned = communicator.preJoin(o);
				
			List<GameHeader> returnList = new ArrayList<GameHeader>();
			List<JSONObject> listOfGames = (List<JSONObject>) returned.get("games");
			for(JSONObject game : listOfGames){
				try {
					returnList.add(new GameHeader(game));
				}
				catch (SchemaMismatchException e) {
					e.printStackTrace();
				}
			}
			return returnList;
		}
		catch(GameInitializationException e){
			System.out.println("How did I get to this exception???");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public GameHeader createGame(String name,
			boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws GameInitializationException, ServerException, UserException {
		try{
			JSONObject o = new JSONObject();
			o.put("url","http://" + host + ":" + Integer.toString(port) + "/games/create");
			o.put("requestType", "POST");
			o.put("name", name);
			o.put("randomTiles", randomTiles);
			o.put("randomNumbers", randomNumbers);
			o.put("randomPorts", randomPorts);
			
			JSONObject returned = communicator.preJoin(o);
			
			return new GameHeader(returned);
		}
		catch(UserException e){
			System.out.println("There was a typo somewhere in order for me to get here!!!");
		}
		catch (SchemaMismatchException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean joinGame(Session player, int gameID, CatanColor color)
			throws JoinGameException, ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/games/join");
		o.put("requestType", "POST");
		o.put("id", gameID);
		o.put("playerUUID", player.getPlayerID());
		o.put("color", (color.toString()).toLowerCase());
		
		JSONObject returned = communicator.joinGame(o);
		
		if(returned.get("success").equals("Success")){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveGame(int gameID, String filename)
			throws GamePersistenceException, ServerException, UserException{
		try{
			JSONObject o = new JSONObject();
			o.put("url","http://" + host + ":" + Integer.toString(port) + "/games/save");
			o.put("requestType", "POST");
			o.put("id", gameID);
			o.put("name", filename);
			communicator.preJoin(o);
		}
		catch(GameInitializationException e){
			System.out.println("Another typo");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadGame(String filename)
			throws ServerException, UserException {
		try{
			JSONObject o = new JSONObject();
			o.put("url","http://" + host + ":" + Integer.toString(port) + "/games/load");
			o.put("requestType", "POST");
			o.put("name", filename);
			communicator.preJoin(o);
		}
		catch(GameInitializationException e){
			System.out.println("I don't even know");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getModel(int gameID, int version)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/game/model?version=" + version);
		o.put("requestType", "GET");
		o.put("version", version);

		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String resetGame(int gameID)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/game/reset");
		o.put("requestType", "POST");
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Command> getCommands(int gameID)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/game/commands");
		o.put("requestType", "GET");
		JSONObject returned;
		try {
			returned = (JSONObject) new JSONParser().parse(communicator.send(o));
		} catch (ParseException e) {
			return new ArrayList<>();
		}
		return (List<Command>) returned.get("commands");
	}

	@SuppressWarnings("unchecked")
	@Override
	public String executeCommands(int gameID, List<Command> commands)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/game/commands");
		o.put("requestType", "POST");
		o.put("commands", commands);
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addAIPlayer(int gameID, AIType type)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/game/addAI");
		o.put("requestType", "POST");
		o.put("AIType", type.toString());
		communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAITypes()
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/game/listAI");
		o.put("requestType", "GET");
		JSONObject returned;
		try {
			returned = (JSONObject) new JSONParser().parse(communicator.send(o));
		} catch (ParseException e) {
			return new ArrayList<>();
		}
		
		return (List<String>)returned.get("list");
	}

	@SuppressWarnings("unchecked")
	@Override
	public String sendChat(int user, int gameID, String message)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/sendChat");
		o.put("requestType", "POST");
		o.put("playerIndex", user);
		o.put("type", "sendChat");
		o.put("content", message);
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String rollDice(int user, int gameID, int number)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/rollNumber");
		o.put("requestType", "POST");
		o.put("type", "rollNumber");
		o.put("playerIndex",user);
		o.put("number", number);
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String robPlayer(int user, int gameID, HexLocation newRobberLocation,
			int victim)
					throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/robPlayer");
		o.put("requestType", "POST");
		o.put("type", "robPlayer");
		o.put("playerIndex", user);
		o.put("victimIndex", victim);
		JSONObject location = new JSONObject();
		location.put("x", newRobberLocation.getX());
		location.put("y", newRobberLocation.getY());
		o.put("location", gson.toJson(location));
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String buyDevCard(int user, int gameID)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/buyDevCard");
		o.put("requestType", "POST");
		o.put("type", "buyDevCard");
		o.put("playerIndex", user);
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String yearOfPlenty(int user, int gameID, ResourceType type1,
			ResourceType type2)
					throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/Year_of_Plenty");
		o.put("requestType", "POST");
		o.put("type", "Year_of_Plenty");
		o.put("playerIndex", user);
		o.put("resource1", type1.toString().toLowerCase());
		o.put("resource2", type2.toString().toLowerCase());
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String roadBuilding(int user, int gameID, EdgeLocation road1,
			EdgeLocation road2)
					throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/Road_Building");
		o.put("requestType", "POST");
		o.put("type", "Road_Building");
		o.put("playerIndex", user);
		JSONObject firstRoad = road1.toJSONObject();
		JSONObject secondRoad = road2.toJSONObject();
		o.put("spot1", gson.toJson(firstRoad));
		o.put("spot2", gson.toJson(secondRoad));
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String soldier(int user, int gameID, HexLocation newRobberLocation,
			int victim)
					throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/Soldier");
		o.put("requestType", "POST");
		o.put("type", "Soldier");
		o.put("playerIndex", user);
		o.put("victimIndex", victim);
		JSONObject location = new JSONObject();
		location.put("x", newRobberLocation.getX());
		location.put("y", newRobberLocation.getY());
		o.put("location", gson.toJson(location));
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String monopoly(int user, int gameID, ResourceType type)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/Monopoly");
		o.put("requestType", "POST");
		o.put("type", "Monopoly");
		o.put("resource", type.toString().toLowerCase());
		o.put("playerIndex", user);
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String buildRoad(int user, int gameID, EdgeLocation location,
			boolean free)
					throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/buildRoad");
		o.put("requestType", "POST");
		o.put("type", "buildRoad");
		o.put("playerIndex", user);
		JSONObject roadLocation = location.toJSONObject();		
		o.put("roadLocation", gson.toJson(roadLocation));
		o.put("free", free);
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String buildSettlement(int user, int gameID, VertexLocation location,
			boolean free)
					throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/buildSettlement");
		o.put("requestType", "POST");
		o.put("type", "buildSettlement");
		o.put("playerIndex", user);
		JSONObject vertexLocation = location.toJSONObject();
		o.put("vertexLocation", gson.toJson(vertexLocation));
		o.put("free", free);
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String buildCity(int user, int gameID, VertexLocation location)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/buildCity");
		o.put("requestType", "POST");
		o.put("type", "buildCity");
		o.put("playerIndex", user);
		JSONObject vertexLocation = location.toJSONObject();
		o.put("vertexLocation", gson.toJson(vertexLocation));
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String offerTrade(int user, int gameID, ResourceTradeList offer,
			int receiver)
					throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/offerTrade");
		o.put("requestType", "POST");
		o.put("type", "offerTrade");
		o.put("playerIndex", user);
		o.put("offer", gson.toJson(offer.toJSONObject()));
		o.put("receiver", receiver);
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String respondToTrade(int user, int gameID, boolean accept)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/acceptTrade");
		o.put("requestType", "POST");
		o.put("type", "acceptTrade");
		o.put("playerIndex", user);
		o.put("willAccept", accept);
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String maritimeTrade(int user, int gameID, ResourceType inResource,
			ResourceType outResource, int ratio)
					throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/maritimeTrade");
		o.put("requestType", "POST");
		o.put("type", "maritimeTrade");
		o.put("playerIndex", user);
		o.put("ratio", ratio);
		o.put("inputResource", inResource.toString().toLowerCase());
		o.put("outputResource", outResource.toString().toLowerCase());
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String discardCards(int user, int gameID, ResourceList cards)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/discardCards");
		o.put("requestType", "POST");
		o.put("type", "discardCards");
		o.put("playerIndex", user);
		o.put("discardedCards", gson.toJson(cards.toJSONObject()));
		
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String finishTurn(int user, int gameID)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/finishTurn");
		o.put("requestType", "POST");
		o.put("type", "finishTurn");
		o.put("playerIndex", user);
		return communicator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void changeLogLevel(LogLevel level)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/");
		o.put("requestType", "POST");
		o.put("logLevel", level.toString());
		communicator.send(o);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public String monument(int user, int gameID)
			throws ServerException, UserException {
		JSONObject o = new JSONObject();
		o.put("url","http://" + host + ":" + Integer.toString(port) + "/moves/Monument");
		o.put("requestType", "POST");
		o.put("type", "Monument");
		o.put("playerIndex", user);
		
		return communicator.send(o);
	}}
