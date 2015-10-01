package client.communication;

import static org.junit.Assert.*;

import java.util.List;

import org.json.simple.JSONObject;
import org.junit.Test;

import shared.communication.GameHeader;
import shared.definitions.CatanColor;
import shared.locations.VertexLocation;
import shared.model.CatanModel;
import shared.model.ModelFacade;
import shared.model.PlayerReference;
import shared.model.TurnTracker;

public class ServerProxyTest {
	ServerProxy SP;

	@SuppressWarnings("unchecked")
	@Test
	public void testOne() {
		SP = new ServerProxy();
		try{
			SP.register("Steve", "steve");
			GameHeader game = SP.createGame("test", true, true, true);
			SP.joinGame(game.getId(), CatanColor.PURPLE);
			System.out.println("Registered Steve");
			
			SP.register("Justin", "123");
			SP.joinGame(game.getId(), CatanColor.BLUE);
			System.out.println("Registered Justin");
			
			SP.register("Jordan", "Jordan");
			SP.joinGame(game.getId(), CatanColor.GREEN);
			System.out.println("Registered Jordan");
			
			SP.register("Grant", "abc_123");
			SP.joinGame(game.getId(), CatanColor.ORANGE);
			System.out.println("Registered Grant");
			
			JSONObject modelJSON = SP.getModel(0);
			
			CatanModel test = new CatanModel();
			ModelFacade facade = new ModelFacade(test);
			facade.updateFromJSON(modelJSON);
			CatanModel model = facade.getCatanModel();
			TurnTracker turnTracker = model.getTurnTracker();
			
			SP.login("Steve", "steve");
			System.out.println("Logged in as Steve");
			SP.joinGame(game.getId(), CatanColor.PURPLE);
			System.out.println("Joined Game");

			
			PlayerReference user = turnTracker.getCurrentPlayer();
			JSONObject location = new JSONObject();
			location.put("x", 0L);
			location.put("y", 0L);
			location.put("direction", "NW");
			VertexLocation vertexLocation = new VertexLocation(location);
			modelJSON = SP.buildSettlement(user, vertexLocation, true);
			System.out.println("Steve Built a Settlement");
			facade.updateFromJSON(modelJSON);
			model = facade.getCatanModel();
			turnTracker = model.getTurnTracker();
			
			SP.login("Justin", "123");
			SP.joinGame(game.getId(), CatanColor.BLUE);
			
			user = turnTracker.getCurrentPlayer();
			location = new JSONObject();
			location.put("x", 0L);
			location.put("y", 0L);
			location.put("direction", "E");
			vertexLocation = new VertexLocation(location);
			modelJSON = SP.buildSettlement(user, vertexLocation, true);
			System.out.println("Justin Built a Settlement");
		}
		catch(Exception e){
			e.printStackTrace();
			fail("Test1");
		}
	}
}
