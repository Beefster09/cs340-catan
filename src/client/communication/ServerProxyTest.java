package client.communication;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.Test;

import shared.communication.GameHeader;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.PlayerReference;
import shared.model.ResourceList;
import shared.model.ResourceTradeList;

public class ServerProxyTest {
	ServerProxy SP;

	@SuppressWarnings("unchecked")
	@Test
	public void testOne() {
		SP = new ServerProxy();
		try{
			SP.register("Steve", "steve");
			System.out.println("Registered Steve");
			GameHeader game = SP.createGame("test", false, false, false);
			System.out.println("Created game");
			
			SP.getGameList();
			System.out.println("Got the list of games");
			SP.joinGame(game.getId(), CatanColor.PURPLE);
			System.out.println("Steve joined the game");
			
			SP.register("Justin", "123");
			System.out.println("Registered Justin");
			SP.joinGame(game.getId(), CatanColor.BLUE);
			System.out.println("Justin joined the game");
			
			SP.register("Jordan", "Jordan");
			System.out.println("Registered Jordan");
			SP.joinGame(game.getId(), CatanColor.GREEN);
			System.out.println("Jordan joined the game");
			
			SP.register("Grant", "abc_123");
			System.out.println("Registered Grant");
			SP.joinGame(game.getId(), CatanColor.ORANGE);
			System.out.println("Grant joined the game");
						
			PlayerReference steve = new PlayerReference(null, 0);
			PlayerReference justin = new PlayerReference(null, 1);
			PlayerReference jordan = new PlayerReference(null, 2);
			PlayerReference grant = new PlayerReference(null, 3);
			
			SP.login("Steve", "steve");
			System.out.println("Steve logged in");
			SP.joinGame(game.getId(), CatanColor.PURPLE);
			System.out.println("Steve rejoined the game");

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
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), -1L);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), -1L);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), -1L);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), 0L);
			ResourceTradeList trade = new ResourceTradeList(tradeJSON);
			
			SP.offerTrade(steve, trade, jordan);
			System.out.println("Steve offered trade to Jordan");
			SP.respondToTrade(jordan, true);
			System.out.println("Jordan accepted trade");
			
			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), -1L);
			trade = new ResourceTradeList(tradeJSON);
			
			SP.offerTrade(steve, trade, justin);
			System.out.println("Steve offered trade to Justin");
			SP.respondToTrade(justin, false);
			System.out.println("Justin declined trade");
	
			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 1L);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), -1L);
			trade = new ResourceTradeList(tradeJSON);

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
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");
			
			SP.rollDice(justin, 4);
			System.out.println("Justin rolled a 4");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 4);
			System.out.println("Jordan rolled a 4");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");
			
			SP.rollDice(grant, 4);
			System.out.println("Grant rolled a 4");

			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), -1L);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 1L);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), 0L);
			trade = new ResourceTradeList(tradeJSON);
			
			SP.offerTrade(grant, trade, justin);
			System.out.println("Grant offered trade to Justin");
			SP.respondToTrade(justin, true);
			System.out.println("Justin accepted trade");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			
			SP.rollDice(steve, 7);
			System.out.println("Steve rolled a 7");
						
			JSONObject resources = new JSONObject();
			resources.put(ResourceType.BRICK.toString().toLowerCase(), 4L);
			resources.put(ResourceType.ORE.toString().toLowerCase(), 0L);
			resources.put(ResourceType.SHEEP.toString().toLowerCase(), 0L);
			resources.put(ResourceType.WHEAT.toString().toLowerCase(), 0L);
			resources.put(ResourceType.WOOD.toString().toLowerCase(), 0L);

			ResourceList cards = new ResourceList(resources);
			SP.discardCards(steve, cards);
			System.out.println("Steve discarded 4 Brick");
			
			HexLocation hexLocation = new HexLocation(-2,1);
			SP.robPlayer(steve, hexLocation, grant);
			System.out.println("Steve moved the robber and robbed Grant");
			location = new JSONObject();
			location.put("x", 0L);
			location.put("y", 0L);
			location.put("direction", "NE");
			edgeLocation = new EdgeLocation(location);
			SP.buildRoad(steve, edgeLocation, false);
			System.out.println("Steve built a 5th road");
			SP.sendChat(steve, "Why did nobody do anything last round?");
			System.out.println("Steve sent a chat");
			
			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), -1L);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), 1L);
			trade = new ResourceTradeList(tradeJSON);

			SP.offerTrade(steve, trade, justin);
			System.out.println("Steve offered trade to Justin");
			SP.sendChat(jordan, "You took all of our resources!");
			System.out.println("Jordan sent a chat");
			SP.respondToTrade(justin, true);
			System.out.println("Justin accepted trade");
			
			location = new JSONObject();
			location.put("x", 0L);
			location.put("y", 0L);
			location.put("direction", "E");
			vertexLocation = new VertexLocation(location);

			SP.buildSettlement(steve, vertexLocation, false);
			System.out.println("Steve built a 4th settlement");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");
			
			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 9);
			System.out.println("Jordan rolled a 9");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 9);
			System.out.println("Grant rolled a 9");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");

			SP.rollDice(steve, 11);
			System.out.println("Steve rolled an 11");
			SP.buildCity(steve, vertexLocation);
			System.out.println("Steve built a city");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");
			
			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 9);
			System.out.println("Jordan rolled a 9");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 11);
			System.out.println("Grant rolled an 11");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			
			SP.rollDice(steve, 9);
			System.out.println("Steve rolled a 9");
			
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", -1L);
			location.put("direction", "NW");
			vertexLocation = new VertexLocation(location);
			
			SP.buildCity(steve, vertexLocation);
			System.out.println("Steve built a 2nd city");
			
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", 1L);
			location.put("direction", "NW");
			vertexLocation = new VertexLocation(location);
			
			SP.buildCity(steve, vertexLocation);
			System.out.println("Steve built a 3rd city");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");
			
			
			//////this is where I finished keeping track of things//////////////
			
			SP.rollDice(justin, 9);
			System.out.println("Justin rolled a 9");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 9);
			System.out.println("Jordan rolled a 9");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 9);
			System.out.println("Grant rolled a 9");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			
			SP.rollDice(steve, 9);
			System.out.println("Steve rolled a 9");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");

			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 11);
			System.out.println("Jordan rolled an 11");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 11);
			System.out.println("Grant rolled an 11");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			
			SP.rollDice(steve, 11);
			System.out.println("Steve rolled an 11");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");

			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 11);
			System.out.println("Jordan rolled an 11");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 11);
			System.out.println("Grant rolled an 11");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			
			SP.rollDice(steve, 11);
			System.out.println("Steve rolled an 11");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");

			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 11);
			System.out.println("Jordan rolled an 11");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 11);
			System.out.println("Grant rolled an 11");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			/////////////////////////////////////////////////////////////////
			SP.rollDice(steve, 10);
			System.out.println("Steve rolled a 10");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");

			SP.rollDice(justin, 10);
			System.out.println("Justin rolled a 10");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 10);
			System.out.println("Jordan rolled a 10");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 10);
			System.out.println("Grant rolled a 10");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			
			SP.rollDice(steve, 10);
			System.out.println("Steve rolled a 10");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");
			
			SP.rollDice(justin, 10);
			System.out.println("Justin rolled a 10");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 10);
			System.out.println("Jordan rolled a 10");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 10);
			System.out.println("Grant rolled a 10");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			
			SP.rollDice(steve, 10);
			System.out.println("Steve rolled a 10");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");

			SP.rollDice(justin, 10);
			System.out.println("Justin rolled a 10");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 10);
			System.out.println("Jordan rolled a 10");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 10);
			System.out.println("Grant rolled a 10");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			
			SP.rollDice(steve, 9);
			System.out.println("Steve rolled a 9");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");
			
			SP.rollDice(justin, 9);
			System.out.println("Justin rolled a 9");
			SP.finishTurn(justin);
			System.out.println("Justin finished his turn");
			
			SP.rollDice(jordan, 9);
			System.out.println("Jordan rolled a 9");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his turn");

			SP.rollDice(grant, 9);
			System.out.println("Grant rolled a 9");
			SP.finishTurn(grant);
			System.out.println("Grant finished his turn");
			////////////////////////////////////////////////////////////////////
			SP.rollDice(steve, 11);
			System.out.println("Steve rolled an 11");
			SP.finishTurn(steve);
			System.out.println("Steve finished his turn");



		}
		catch(Exception e){
			e.printStackTrace();
			fail("Test1");
		}
	}
}
