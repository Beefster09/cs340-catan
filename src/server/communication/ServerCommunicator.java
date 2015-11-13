package server.communication;

import com.sun.net.httpserver.*;

/**
 * Runs the server, binds it to a port, and creates contexts.
 * @author Grant
 *
 */
public class ServerCommunicator {

	private static int SERVER_PORT_NUMBER = 8082;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	private HttpServer server;
	
	public void run() {	
	
		server.setExecutor(null); // use the default executor
		
		server.createContext("/Login", login);
		server.createContext("/Register", register);
		server.createContext("/GetGameList", getGameList);
		server.createContext("/CreateGame", createGame);
		server.createContext("/JoinGame", joinGame);
		server.createContext("/SaveGame", saveGame);
		server.createContext("/LoadGame", loadGame);
		server.createContext("/GetModel", getModel);
		server.createContext("/ResetGame", resetGame);
		server.createContext("/GetCommands", getCommands);
		server.createContext("/ExecuteCommands", executeCommands);
		server.createContext("/AddAIPlayer", addAIPlayer);
		server.createContext("/GetAITypes", getAITypes);
		server.createContext("/SendChat", sendChat);
		server.createContext("/RollDice", rollDice);
		server.createContext("/RobPlayer", robPlayer);
		server.createContext("/BuyDevCard", buyDevCard);
		server.createContext("/YearOfPlenty", yearOfPlenty);
		server.createContext("/RoadBuilding", roadBuilding);
		server.createContext("/Soldier", soldier);
		server.createContext("/Monopoly", monopoly);
		server.createContext("/Monument", monument);
		server.createContext("/BuildRoad", buildRoad);
		server.createContext("/BuildSettlement", buildSettlement);
		server.createContext("/BuildCity", buildCity);
		server.createContext("/OfferTrade", offerTrade);
		server.createContext("/RespondToTrade", respondToTrade);
		server.createContext("/MaritimeTrade", maritimeTrade);
		server.createContext("/DiscardCards", discardCards);
		server.createContext("/FinishTurn", finishTurn);
		server.createContext("/ChangeLogLevel", changeLogLevel);
		
		server.start();
	}
	public static void main(String[] args) {
		String port = args[0];
		SERVER_PORT_NUMBER = Integer.parseInt(port);
		new ServerCommunicator().run();
	}
}
