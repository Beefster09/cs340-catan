package client.communication;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.Test;

import shared.communication.GameHeader;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.PlayerReference;
import shared.model.ResourceTradeList;

public class ServerProxyTest {
	ServerProxy SP;

	@SuppressWarnings("unchecked")
	@Test
	public void testOne() {
		SP = new ServerProxy();
		try{
			SP.register("Steve", "steve");
			GameHeader game = SP.createGame("test", false, false, false);
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
						
			PlayerReference steve = new PlayerReference(null, 0);
			PlayerReference justin = new PlayerReference(null, 1);
			PlayerReference jordan = new PlayerReference(null, 2);
			PlayerReference grant = new PlayerReference(null, 3);
			
			SP.login("Steve", "steve");
			SP.joinGame(game.getId(), CatanColor.PURPLE);

			JSONObject location = new JSONObject();
			location.put("x", 0L);
			location.put("y", 1L);
			location.put("direction", "NE");
			EdgeLocation edgeLocation = new EdgeLocation(location);
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", 1L);
			location.put("direction", "NW");
			VertexLocation vertexLocation = new VertexLocation(location);
			SP.buildRoad(steve, edgeLocation, true);
			System.out.println("Steve Built a Road");
			SP.buildSettlement(steve, vertexLocation, true);
			System.out.println("Steve Built a Settlement");
			SP.finishTurn(steve);
			System.out.println("Steve finished his Turn");
			
//			SP.login("Justin", "123");
//			SP.joinGame(game.getId(), CatanColor.BLUE);
			
			location = new JSONObject();
			location.put("x", -1L);
			location.put("y", 2L);
			location.put("direction", "NW");
			edgeLocation = new EdgeLocation(location);
			location = new JSONObject();
			location.put("x", -1L);
			location.put("y", 2L);
			location.put("direction", "W");
			vertexLocation = new VertexLocation(location);
			SP.buildRoad(justin, edgeLocation, true);
			System.out.println("Justin Built a Road");
			SP.buildSettlement(justin, vertexLocation, true);
			System.out.println("Justin Built a Settlement");
			SP.finishTurn(justin);
			System.out.println("Justin finished his Turn");
			
//			SP.login("Jordan", "Jordan");
//			SP.joinGame(game.getId(), CatanColor.GREEN);
			
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", 1L);
			location.put("direction", "NE");
			edgeLocation = new EdgeLocation(location);
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", 1L);
			location.put("direction", "E");
			vertexLocation = new VertexLocation(location);
			SP.buildRoad(jordan, edgeLocation, true);
			System.out.println("Jordan Built a Road");
			SP.buildSettlement(jordan, vertexLocation, true);
			System.out.println("Jordan Built a Settlement");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his Turn");

//			SP.login("Grant", "abc_123");
//			SP.joinGame(game.getId(), CatanColor.ORANGE);
			
			location = new JSONObject();
			location.put("x", -2L);
			location.put("y", 2L);
			location.put("direction", "N");
			edgeLocation = new EdgeLocation(location);
			location = new JSONObject();
			location.put("x", -2L);
			location.put("y", 2L);
			location.put("direction", "NW");
			vertexLocation = new VertexLocation(location);
			SP.buildRoad(grant, edgeLocation, true);
			System.out.println("Grant Built a Road");
			SP.buildSettlement(grant, vertexLocation, true);
			System.out.println("Grant Built a Settlement");
			SP.finishTurn(grant);
			System.out.println("Grant finished his Turn");
			
			location = new JSONObject();
			location.put("x", -1L);
			location.put("y", 1L);
			location.put("direction", "NW");
			edgeLocation = new EdgeLocation(location);
			location = new JSONObject();
			location.put("x", -1L);
			location.put("y", 1L);
			location.put("direction", "NW");
			vertexLocation = new VertexLocation(location);
			SP.buildRoad(grant, edgeLocation, true);
			System.out.println("Grant Built a 2nd Road");
			SP.buildSettlement(grant, vertexLocation, true);
			System.out.println("Grant Built a 2nd Settlement");
			SP.finishTurn(grant);
			System.out.println("Grant finished his Turn");
			
//			SP.login("Jordan", "Jordan");
//			SP.joinGame(game.getId(), CatanColor.GREEN);
			
			location = new JSONObject();
			location.put("x", 2L);
			location.put("y", 0L);
			location.put("direction", "NW");
			edgeLocation = new EdgeLocation(location);
			location = new JSONObject();
			location.put("x", 2L);
			location.put("y", 0L);
			location.put("direction", "NW");
			vertexLocation = new VertexLocation(location);
			SP.buildRoad(jordan, edgeLocation, true);
			System.out.println("Jordan Built a 2nd Road");
			SP.buildSettlement(jordan, vertexLocation, true);
			System.out.println("Jordan Built a 2nd Settlement");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his Turn");

//			SP.login("Justin", "123");
//			SP.joinGame(game.getId(), CatanColor.BLUE);
			
			location = new JSONObject();
			location.put("x", -1L);
			location.put("y", 2L);
			location.put("direction", "N");
			edgeLocation = new EdgeLocation(location);
			location = new JSONObject();
			location.put("x", -1L);
			location.put("y", 2L);
			location.put("direction", "NE");
			vertexLocation = new VertexLocation(location);
			SP.buildRoad(justin, edgeLocation, true);
			System.out.println("Justin Built a 2nd Road");
			SP.buildSettlement(justin, vertexLocation, true);
			System.out.println("Justin Built a 2nd Settlement");
			SP.finishTurn(justin);
			System.out.println("Justin finished his Turn");

//			SP.login("Steve", "steve");
//			SP.joinGame(game.getId(), CatanColor.PURPLE);

			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", -1L);
			location.put("direction", "NW");
			edgeLocation = new EdgeLocation(location);
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", -1L);
			location.put("direction", "NW");
			vertexLocation = new VertexLocation(location);
			SP.buildRoad(steve, edgeLocation, true);
			System.out.println("Steve Built a 2nd Road");
			SP.buildSettlement(steve, vertexLocation, true);
			System.out.println("Steve Built a 2nd Settlement");
			SP.finishTurn(steve);
			System.out.println("Steve finished his Turn");

			SP.rollDice(steve, 4);
			System.out.println("Steve rolled a 4");
			location = new JSONObject();
			location.put("x", 0L);
			location.put("y", -1L);
			location.put("direction", "NE");
			edgeLocation = new EdgeLocation(location);
			SP.buildRoad(steve, edgeLocation, false);
			System.out.println("Steve built a 3rd road");
			
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", -2L);
			location.put("direction", "NW");
			edgeLocation = new EdgeLocation(location);
			SP.buildRoad(steve, edgeLocation, false);
			System.out.println("Steve built a 4th road");
			
			JSONObject tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 1);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), 1);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 1);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), 0);
			
			ResourceTradeList trade = new ResourceTradeList(tradeJSON);
			
			SP.offerTrade(steve, trade, jordan);
			System.out.println("Steve offered trade to Jordan");
			SP.respondToTrade(jordan, true);
			System.out.println("Jordan accepted trade");
			
			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), 0);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 0);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), 1);
			
			trade = new ResourceTradeList(tradeJSON);
			
			SP.offerTrade(steve, trade, justin);
			System.out.println("Steve offered trade to Justin");
			SP.respondToTrade(justin, false);
			System.out.println("Justin declined trade");
	
			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), -1);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), 0);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 0);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), 1);

			SP.offerTrade(steve, trade, justin);
			System.out.println("Steve offered trade to Justin");
			SP.respondToTrade(justin, true);
			System.out.println("Justin accepted trade");
			
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", -2L);
			location.put("direction", "NW");
			vertexLocation = new VertexLocation(location);
			SP.buildSettlement(steve, vertexLocation, false);
			System.out.println("Steve built a 3rd settlement");

		}
		catch(Exception e){
			e.printStackTrace();
			fail("Test1");
		}
	}
}
