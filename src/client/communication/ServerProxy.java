package client.communication;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import client.communication.ClientManager;

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
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.PlayerReference;
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
	

	private ClientCommunicator commuincator = new ClientCommunicator();
	
	public static void main(String[] args) throws UserException, ServerException, InvalidActionException, GameInitializationException, IllegalArgumentException, JoinGameException{
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Session login(String username, String password)
			throws UserException, ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/user/login");
		o.put("requestType", "POST");
		o.put("username", username);
		o.put("password", password);
		JSONObject returned = commuincator.login(o);
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
		o.put("url","http://localhost:8081/user/register");
		o.put("requestType", "POST");
		o.put("username", username);
		o.put("password", password);
		JSONObject returned = commuincator.login(o);
		String returnedName = (String) returned.get("name");
		String returnedPassword = (String) returned.get("password");
		int playerID = ((Long)returned.get("playerID")).intValue();
		return new Session(returnedName, returnedPassword, playerID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GameHeader> getGameList() throws ServerException, InvalidActionException {
		try{
			JSONObject o = new JSONObject();
			o.put("url","http://localhost:8081/games/list");
			o.put("requestType", "GET");
			JSONObject returned = commuincator.preJoin(o);
				
			List<GameHeader> returnList = new ArrayList<GameHeader>();
			List<JSONObject> listOfGames = (List<JSONObject>) returned.get("games");
			for(JSONObject game : listOfGames){
				String title = (String) game.get("title");
				int id = ((Long)game.get("id")).intValue();
				List<JSONObject> players = (List<JSONObject>) game.get("players");
				List<PlayerHeader> playerHeaders = new ArrayList<PlayerHeader>();
				for(JSONObject json : players){
					if(json.isEmpty()){
						playerHeaders.add(null);
						continue;
					}
					CatanColor playerColor = CatanColor.getColorFromString((String)json.get("color"));
					String playerName = (String) json.get("name");
					int playerID = ((Long)json.get("id")).intValue();
					playerHeaders.add(new PlayerHeader(playerColor, playerName, playerID));
				}
				returnList.add(new GameHeader(title, id, playerHeaders));
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
			throws GameInitializationException, ServerException, InvalidActionException {
		try{
			JSONObject o = new JSONObject();
			o.put("url","http://localhost:8081/games/create");
			o.put("requestType", "POST");
			o.put("name", name);
			o.put("randomTiles", randomTiles);
			o.put("randomNumbers", randomNumbers);
			o.put("randomPorts", randomPorts);
			
			JSONObject returned = commuincator.preJoin(o);
			
			String title = (String)returned.get("title");
			int id = ((Long)returned.get("id")).intValue();
			List<JSONObject> players = (List<JSONObject>) returned.get("players");
			List<PlayerHeader> playerHeader = new ArrayList<PlayerHeader>();
			for(JSONObject player : players){
				if(player.isEmpty()){
					playerHeader.add(null);
					continue;
				}
				String playerName = (String)player.get("name");
				int playerID = ((Long)player.get("id")).intValue();
				CatanColor playerColor = CatanColor.getColorFromString((String)player.get("color"));
				playerHeader.add(new PlayerHeader(playerColor, playerName, playerID));
			}
			return new GameHeader(title, id, playerHeader);
		}
		catch(InvalidActionException e){
			System.out.println("There was a typo somewhere in order for me to get here!!!");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean joinGame(int gameID, CatanColor color)
			throws JoinGameException, ServerException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/games/join");
		o.put("requestType", "POST");
		o.put("id", gameID);
		o.put("color", (color.toString()).toLowerCase());
		
		JSONObject returned = commuincator.joinGame(o);
		
		if(returned.get("success").equals("Success")){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveGame(int gameID, String filename)
			throws GamePersistenceException, ServerException, InvalidActionException{
		try{
			JSONObject o = new JSONObject();
			o.put("url","http://localhost:8081/games/save");
			o.put("requestType", "POST");
			o.put("id", gameID);
			o.put("name", filename);
			commuincator.preJoin(o);
		}
		catch(GameInitializationException e){
			System.out.println("Another typo");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadGame(String filename)
			throws ServerException, InvalidActionException {
		try{
			JSONObject o = new JSONObject();
			o.put("url","http://localhost:8081/games/load");
			o.put("requestType", "POST");
			o.put("name", filename);
			commuincator.preJoin(o);
		}
		catch(GameInitializationException e){
			System.out.println("I don't even know");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getModel(int version)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/model?version=" + version);
		o.put("requestType", "GET");
		o.put("version", version);

		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject resetGame()
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/reset");
		o.put("requestType", "POST");
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Command> getCommands()
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/commands");
		o.put("requestType", "GET");
		JSONObject returned = commuincator.send(o);
		//TODO figure out how the list is returned
		return (List<Command>) returned.get("commands");
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject executeCommands(List<Command> commands)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/commands");
		o.put("requestType", "POST");
		o.put("commands", commands);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addAIPlayer(AIType type)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/addAI");
		o.put("requestType", "POST");
		o.put("AIType", type.toString());
		commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAITypes()
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/listAI");
		o.put("requestType", "GET");
		JSONObject returned = commuincator.send(o);
		
		return (List<String>)returned.get("list");
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject sendChat(PlayerReference user, String message)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/sendChat");
		o.put("requestType", "POST");
		o.put("playerIndex", user.getIndex());
		o.put("type", "sendChat");
		o.put("content", message);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject rollDice(PlayerReference user, int number)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/rollNumber");
		o.put("requestType", "POST");
		o.put("type", "rollNumber");
		o.put("playerIndex",user.getIndex());
		o.put("number", number);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject robPlayer(PlayerReference user, HexLocation newRobberLocation,
			PlayerReference victim)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/robPlayer");
		o.put("requestType", "POST");
		o.put("type", "robPlayer");
		o.put("playerIndex", user.getIndex());
		o.put("victimIndex", victim.getIndex());
		JSONObject location = new JSONObject();
		location.put("x", newRobberLocation.getX());
		location.put("y", newRobberLocation.getY());
		o.put("location", location);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject buyDevCard(PlayerReference user)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buyDevCard");
		o.put("requestType", "POST");
		o.put("type", "buyDevCard");
		o.put("playerIndex", user.getIndex());
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject yearOfPlenty(PlayerReference user, ResourceType type1,
			ResourceType type2)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Year_of_Plenty");
		o.put("requestType", "POST");
		o.put("type", "Year_of_Plenty");
		o.put("playerIndex", user.getIndex());
		o.put("resource1", type1.toString().toLowerCase());
		o.put("resource2", type2.toString().toLowerCase());
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject roadBuilding(PlayerReference user, EdgeLocation road1,
			EdgeLocation road2)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Road_Building");
		o.put("requestType", "POST");
		o.put("type", "Road_Building");
		o.put("playerIndex", user.getIndex());
		JSONObject firstRoad = road1.toJSONObject();
		JSONObject secondRoad = road2.toJSONObject();
		o.put("spot1", firstRoad);
		o.put("spot2", secondRoad);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject soldier(PlayerReference user, HexLocation newRobberLocation,
			PlayerReference victim)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Soldier");
		o.put("requestType", "POST");
		o.put("type", "Soldier");
		o.put("playerIndex", user.getIndex());
		o.put("victimIndex", victim.getIndex());
		JSONObject location = new JSONObject();
		location.put("x", newRobberLocation.getX());
		location.put("y", newRobberLocation.getY());
		o.put("location", location);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject monopoly(PlayerReference user, ResourceType type)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Monopoly");
		o.put("requestType", "POST");
		o.put("type", "Monopoly");
		o.put("resource", type.toString().toLowerCase());
		o.put("playerIndex", user.getIndex());
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject buildRoad(PlayerReference user, EdgeLocation location,
			boolean free)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buildRoad");
		o.put("requestType", "POST");
		o.put("type", "buildRoad");
		o.put("playerIndex", user.getIndex());
		JSONObject roadLocation = location.toJSONObject();		
		o.put("roadLocation", roadLocation);
		o.put("free", free);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject buildSettlement(PlayerReference user, VertexLocation location,
			boolean free)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buildSettlement");
		o.put("requestType", "POST");
		o.put("type", "buildSettlement");
		o.put("playerIndex", user.getIndex());
		JSONObject vertexLocation = location.toJSONObject();
		o.put("vertexLocation", vertexLocation);
		o.put("free", free);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject buildCity(PlayerReference user, VertexLocation location)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buildCity");
		o.put("requestType", "POST");
		o.put("type", "buildCity");
		o.put("playerIndex", user.getIndex());
		JSONObject vertexLocation = location.toJSONObject();
		o.put("vertexLocation", vertexLocation);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject offerTrade(PlayerReference user, ResourceTradeList offer,
			PlayerReference receiver)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/offerTrade");
		o.put("requestType", "POST");
		o.put("type", "offerTrade");
		o.put("playerIndex", user.getIndex());
		o.put("offer", offer.toJSONObject());
		o.put("receiver", receiver.getIndex());
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject respondToTrade(PlayerReference user, boolean accept)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/acceptTrade");
		o.put("requestType", "POST");
		o.put("type", "acceptTrade");
		o.put("playerIndex", user.getIndex());
		o.put("willAccept", accept);
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject maritimeTrade(PlayerReference user, ResourceType inResource,
			ResourceType outResource, int ratio)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/maritimeTrade");
		o.put("requestType", "POST");
		o.put("type", "maritimeTrade");
		o.put("playerIndex", user.getIndex());
		o.put("ratio", ratio);
		o.put("inputResource", inResource.toString().toLowerCase());
		o.put("outputResource", outResource.toString().toLowerCase());
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject discardCards(PlayerReference user, ResourceList cards)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/discardCards");
		o.put("requestType", "POST");
		o.put("type", "discardCards");
		o.put("playerIndex", user.getIndex());
		o.put("discardedCards", cards.toJSONObject());
		
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject finishTurn(PlayerReference user)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/finishTurn");
		o.put("requestType", "POST");
		o.put("type", "finishTurn");
		o.put("playerIndex", user.getIndex());
		return commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void changeLogLevel(LogLevel level)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/");
		o.put("requestType", "POST");
		o.put("logLevel", level.toString());
		commuincator.send(o);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject monument(PlayerReference user)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Monument");
		o.put("requestType", "POST");
		o.put("type", "Monument");
		o.put("playerIndex", user.getIndex());
		
		return commuincator.send(o);
	}}
