package client.communication;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import shared.communication.GameHeader;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.exceptions.GameInitializationException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;

public class ServerProxyTest {

	private ServerProxy p;
	
	@Test
	public void testLogin() throws UserException, ServerException {
		String username = "John";
		String password = "password";
		
		//needs to ensure that username and password are already
		//stored on the server
		Session first = p.login(username, password);
	}

	@Test
	public void testRegister() throws UserException, ServerException {
		String username = "John";
		String password = "password";
		
		//needs to ensure that username is valid
		//I forget exactly what counts as valid and doesn't
		Session first = p.register(username, password);
		
		
	}
	
	@Test
	public void testGetGameList() throws UserException, ServerException {
		
		//ensures that getGameList returns list of all current games
		List<GameHeader> gameHeaders= p.getGameList();
		
		
	}
	
	@Test
	public void testCreateGame() throws UserException, ServerException, GameInitializationException {
		
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		String name = "Fun Game!";
		boolean randomTiles = false;
		boolean randomNumbers = false;
		boolean randomPorts = false;
		
		//ensures that game is created when appropriate parameters are passed in.
		p.createGame(user, name, randomTiles, randomNumbers, randomPorts);
	}
	
	@Test
	public void testJoinGame() throws UserException, ServerException {
		
		String username = "John";
		String password = "password";
		
		Session user = p.register(username, password);
		
		int gameID = 0;
		CatanColor color;
		//color.getColorFromString("red");
		//p.joinGame(user, gameID, color);
	}
	
}
