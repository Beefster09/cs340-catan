package server.communication;

import static org.junit.Assert.*;

import java.util.List;

import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import server.ai.AIType;
import shared.communication.Command;
import shared.communication.GameHeader;
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
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.CatanModel;
import shared.model.ModelFacade;
import shared.model.PlayerReference;
import shared.model.ResourceList;

public class ServerTest {


	private Server p;
	private CatanModel testModel;
	private ModelFacade facade;
	
	@BeforeClass
	public void setup() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		int version = 1;
		testModel = facade.updateFromJSON(p.getModel(version));
	}
	
	@Test
	public void testLogin() throws UserException, ServerException {
		String username = "John";
		String password = "password";
		
		Session first = p.login(username, password);
	}

	@Test
	public void testRegister() throws UserException, ServerException {
		String username = "John";
		String password = "password";
		
		Session first = p.register(username, password);		
	}
	
	@Test
	public void testGetGameList() throws UserException, ServerException, InvalidActionException {
		
		List<GameHeader> gameHeaders= p.getGameList();
	}
	
	@Test
	public void testCreateGame() throws UserException, ServerException, GameInitializationException, InvalidActionException {
		
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		String name = "Fun Game!";
		boolean randomTiles = false;
		boolean randomNumbers = false;
		boolean randomPorts = false;
		
		p.createGame(name, randomTiles, randomNumbers, randomPorts);
	}
	
	@Test
	public void testJoinGame() throws UserException, ServerException, JoinGameException {
		
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		int gameID = 0;
		p.joinGame(gameID, CatanColor.BLUE);
	}
	
	
	@Test
	public void testSameGame() throws GamePersistenceException, ServerException, InvalidActionException {
		
		int gameID = 1;
		String filename = "Catan";
		p.saveGame(gameID, filename);
		
	}
	
	@Test
	public void testLoadGame() throws GamePersistenceException, ServerException, InvalidActionException {
		
		int gameID = 1;
		String filename = "Catan";
		p.saveGame(gameID, filename);
		
		p.loadGame(filename);
	}
	
	@Test
	public void testGetModel() throws UserException, ServerException, InvalidActionException{
		
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		int version = 1;
		JSONObject model = p.getModel(version);
	}
	
	@Test
	public void testResetGame() throws UserException, ServerException, GameInitializationException, InvalidActionException {
		
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		JSONObject model = p.resetGame();
	}
	
	@Test
	public void testGetCommands() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		List<Command> commands = p.getCommands();
	}
	
	@Test
	public void testExecuteCommands() throws UserException, ServerException, InvalidActionException {	
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		List<Command> commands = p.getCommands();
		
		JSONObject model = p.executeCommands(commands);
	}
	
	public void testAddAIPlayer() throws UserException, ServerException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		//p.addAIPlayer(user, type);
	}
	
	public void testGetAITypes() throws UserException, ServerException, InvalidActionException {
		
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		//p.addAIPlayer(user, type);
		
		List<String> AITypes = p.getAITypes();
	}
	
	@Test
	public void testSendChat() throws UserException, ServerException, InvalidActionException {
		
		String username = "John";
		String password = "password";
		
		//Session user = p.register(username, password);
		PlayerReference user = new PlayerReference(testModel, 1);
		
		String message = "yoyo wutup dawg";
		
		JSONObject model = p.sendChat(user, message);
	}
	
	@Test
	public void testRollDice() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		//Session user = p.register(username, password);
		PlayerReference user = new PlayerReference(testModel, 1);
		
		int number = 1;
		
		JSONObject model = p.rollDice(user, number);
	}
	
	@Test
	public void restRobPlayer() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		HexLocation newRobberLocation = new HexLocation(2, 2);
		
		
		PlayerReference victim = new PlayerReference(testModel, 1);
		
		model = p.robPlayer(user, newRobberLocation, victim);
	}
	
	@Test
	public void testBuyDevCard() throws UserException, ServerException, InvalidActionException {
		
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		CatanModel model = p.buyDevCard(user);
	}
	
	@Test
	public void testYearOfPlenty() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		CatanModel model = p.yearOfPlenty(user, ResourceType.BRICK, ResourceType.ORE);
	}
	
	@Test
	public void testRoadBuilding() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		HexLocation one = new HexLocation(1, 1);
		HexLocation two = new HexLocation(2, 2);
		
		EdgeLocation road1 = new EdgeLocation(one, EdgeDirection.North);
		EdgeLocation road2 = new EdgeLocation(two, EdgeDirection.North);
		
		CatanModel model = p.roadBuilding(user, road1, road2);
	}
	
	@Test
	public void testSolder() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		HexLocation newRobberLocation = new HexLocation(2, 2);
		
		int version = 1;
		CatanModel model = p.getModel(user, version);
		
		PlayerReference victim = new PlayerReference(model, 1);
		
		model = p.soldier(user, newRobberLocation, victim);
	}
	
	@Test
	public void testMonopoly() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		CatanModel model = p.monopoly(user, ResourceType.BRICK);
	}
	
	@Test
	public void testBuildRoad() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		HexLocation one = new HexLocation(1, 1);
		EdgeLocation location = new EdgeLocation(one, EdgeDirection.North);
		
		CatanModel model = p.buildRoad(user, location, true);
	}
	
	@Test
	public void testBuildSettlement() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		HexLocation one = new HexLocation(1, 1);
		VertexLocation location = new VertexLocation(one, VertexDirection.East);
		
		CatanModel model = p.buildSettlement(user, location, true);
	}
	
	@Test
	public void testBuildCity() throws UserException, ServerException, InvalidActionException {
		
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		HexLocation one = new HexLocation(1, 1);
		VertexLocation location = new VertexLocation(one, VertexDirection.East);
		
		CatanModel model = p.buildCity(user, location);
	}
	
	@Test
	public void testOfferTrade() throws UserException, ServerException, NotYourTurnException {	
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		ResourceList offer = new ResourceList();
		
		CatanModel model = p.offerTrade(user, offer);
	}
	
	@Test
	public void testRespondToTrade() throws UserException, ServerException, TradeException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		CatanModel model = p.respondToTrade(user, true);	
	}
	
	@Test
	public void testMaritimeTrade() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		int ratio = 3;
		
		CatanModel model = p.maritimeTrade(user, ResourceType.BRICK, ResourceType.WOOD, ratio);
	}
	
	@Test
	public void testDiscardCards() throws UserException, ServerException, InvalidActionException {
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		ResourceList cards = new ResourceList();
		
		CatanModel model = p.discardCards(user, cards);
	}
	
	@Test
	public void testFinishTurn() {
		String username = "John";
		String password = "password";
		
		//Session user = p.register(username, password);
		
		//CatanModel model = p.finishTurn(user);
		
	}
}
