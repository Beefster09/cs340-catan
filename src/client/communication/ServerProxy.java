package client.communication;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

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

/**
 * A proxy that acts as the server to the client. Contains all the methods
 * that can be called on the server.  Each method bundles the given variables
 * and gives it to the ClientCommunicator to send through the HTTP protocol.
 * @author jchip
 *
 */
public class ServerProxy implements IServer {

	private ClientCommunicator commuincator = new ClientCommunicator();
	
	public static void main(String[] args) throws UserException, ServerException, InvalidActionException, GameInitializationException, IllegalArgumentException, JoinGameException{
		ServerProxy SP = new ServerProxy();
		
		Session test;

		test = SP.login("Sam", "sam");
		System.out.println("Login:");
		System.out.println("Username: " + test.getUsername());
		System.out.println("Password: " + test.getPassword());
		System.out.println("ID:       " + test.getPlayerID() + "\n");

		try{
			test = SP.register("spulse4", "123456");
			System.out.println("Register:");
			System.out.println("Username: " + test.getUsername());
			System.out.println("Password: " + test.getPassword());
			System.out.println("ID:       " + test.getPlayerID() + "\n");
		}
		catch(UserException e){
			test = SP.login("spulse4", "123456");
			System.out.println("Login:");
			System.out.println("Username: " + test.getUsername());
			System.out.println("Password: " + test.getPassword());
			System.out.println("ID:       " + test.getPlayerID() + "\n");
		}
		
		List<GameHeader> test2 = SP.getGameList();
		for(GameHeader game : test2){
			System.out.println("GetGameList:");
			System.out.println("Title: " + game.getTitle());
			System.out.println("ID:    " + game.getId());
			System.out.println("Players:");
			for(PlayerHeader player : game.getPlayers()){
				if(player == null){
					System.out.println("   {}");
					continue;
				}
				System.out.println("   Name:  " + player.getName());
				System.out.println("   ID:    " + player.getId());
				System.out.println("   Color: " + player.getColor());
			}
			System.out.println();
		}
		
		GameHeader test3 = SP.createGame("Test3", true, true, true);
		System.out.println("CreateGame:");
		System.out.println("Title: " + test3.getTitle());
		System.out.println("ID:    " + test3.getId());
		System.out.println("Players:");
		for(PlayerHeader player : test3.getPlayers()){
			if(player == null){
				System.out.println("   {}");
				continue;
			}
			System.out.println("   Name:  " + player.getName());
			System.out.println("   ID:    " + player.getId());
			System.out.println("   Color: " + player.getColor());
		}
		System.out.println();
		
		boolean test4 = SP.joinGame(3, CatanColor.getColorFromString("red"));
		System.out.println("JoinGame");
		System.out.println(test4);

		test4 = SP.joinGame(4, CatanColor.getColorFromString("blue"));
		System.out.println(test4);
		System.out.println();
		
		SP.saveGame(4, "doneWithThis");
		SP.loadGame("doneWithThis");
}
	
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
		if(username == null || password == null){
			throw new UserException();
		}
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
		if(name == null){
			throw new InvalidActionException();
		}
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
		o.put("url","http://localhost:8081/game/model");
		o.put("requestType", "GET");
		o.put("version", version);
		JSONObject returned = commuincator.send(o);
		return returned;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject resetGame()
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/reset");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
		return returned;
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
		List<Command> returnList = (List<Command>) returned.get("commands");
		return returnList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject executeCommands(Session user, List<Command> commands)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/commands");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addAIPlayer(Session user, AIType type)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/addAI");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AIType> getAITypes(Session user)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/game/listAI");
		o.put("requestType", "GET");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject sendChat(Session user, String message)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/sendChat");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject rollDice(Session user, int number)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/rollNumber");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject robPlayer(Session user, HexLocation newRobberLocation,
			PlayerReference victim)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/robPlayer");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject buyDevCard(Session user)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buyDevCard");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject yearOfPlenty(Session user, ResourceType type1,
			ResourceType type2)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Year_of_Plenty");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject roadBuilding(Session user, EdgeLocation road1,
			EdgeLocation road2)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Road_Building");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject soldier(Session user, HexLocation newRobberLocation,
			PlayerReference victim)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Solder");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public JSONObject monopoly(Session user, ResourceType type)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Monopoly");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public JSONObject buildRoad(Session user, EdgeLocation location,
			boolean free)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buildRoad");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public JSONObject buildSettlement(Session user, VertexLocation location,
			boolean free)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buildSettlement");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public JSONObject buildCity(Session user, VertexLocation location)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/buildCity");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public JSONObject offerTrade(Session user, ResourceList offer)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/offerTrade");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public JSONObject respondToTrade(Session user, boolean accept)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/acceptTrade");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public JSONObject maritimeTrade(Session user, ResourceType inResource,
			ResourceType outResource, int ratio)
					throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/maritimeTrade");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public JSONObject discardCards(Session user, ResourceList cards)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/discardCards");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public JSONObject finishTurn(Session user)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/finishTurn");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
	}

	@Override
	public void changeLogLevel(LogLevel level)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/");
		o.put("requestType", "POST");
		JSONObject returned = commuincator.send(o);
		
	}

	@Override
	public JSONObject monument(Session user)
			throws ServerException, InvalidActionException {
		JSONObject o = new JSONObject();
		o.put("url","http://localhost:8081/moves/Monument");
		o.put("requestType", "GET");
		JSONObject returned = commuincator.send(o);
	}}
