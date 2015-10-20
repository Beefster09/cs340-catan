package client.communication;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.Test;

import server.ai.AIType;
import shared.communication.GameHeader;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.exceptions.GameInitializationException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.JoinGameException;
import shared.exceptions.NameAlreadyInUseException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.PlayerReference;
import shared.model.ResourceList;
import shared.model.ResourceTradeList;

public class ServerProxyTest {
	ServerProxy SP;
	Long start;
	Long finish;

	@SuppressWarnings("unchecked")
	@Test
	public void testOne() {
		start = System.currentTimeMillis();
		SP = new ServerProxy();
		try{
			JSONObject gameJSON = new JSONObject();
			gameJSON.put("title", "Yes");
			gameJSON.put("id", 0L);
			JSONObject playerJSON = new JSONObject();
			playerJSON.put("id", 0L);
			playerJSON.put("name", "blah");
			playerJSON.put("color", "red");
			List<JSONObject> newList = new ArrayList<JSONObject>();
			newList.add(playerJSON);
			gameJSON.put("players", newList);
			GameHeader game = new GameHeader(gameJSON);
			try{
				SP.register("Steve", "steve");
				System.out.println("Registered Steve");
				game = SP.createGame("test", false, false, false);
				System.out.println("Created game");
			}
			catch(NameAlreadyInUseException e){
				SP.login("Steve", "steve");
				System.out.println("Steve logged in");
				game = SP.createGame("test", false, false, false);
				System.out.println("Created game");
			}
			SP.getGameList();
			System.out.println("Got the list of games");
			SP.joinGame(game.getId(), CatanColor.PURPLE);
			System.out.println("Steve joined the game");
				
			try{	
				SP.register("Justin", "123");
				System.out.println("Registered Justin");
				SP.joinGame(game.getId(), CatanColor.BLUE);
				System.out.println("Justin joined the game");
			}
			catch(NameAlreadyInUseException e){
				SP.login("Justin", "123");
				System.out.println("Justin logged in");
				SP.joinGame(game.getId(), CatanColor.BLUE);
				System.out.println("Justin joined the game");
			}

			try{
				SP.register("Jordan", "Jordan");
				System.out.println("Registered Jordan");
				SP.joinGame(game.getId(), CatanColor.GREEN);
				System.out.println("Jordan joined the game");
			}
			catch(NameAlreadyInUseException e){
				SP.login("Jordan", "Jordan");
				System.out.println("Jordan logged in");
				SP.joinGame(game.getId(), CatanColor.GREEN);
				System.out.println("Jordan joined the game");
			}
			
			try{
				SP.register("Grant", "abc_123");
				System.out.println("Registered Grant");
				SP.joinGame(game.getId(), CatanColor.ORANGE);
				System.out.println("Grant joined the game");
			}
			catch(NameAlreadyInUseException e){
				SP.login("Grant", "abc_123");
				System.out.println("Grant logged in");
				SP.joinGame(game.getId(), CatanColor.ORANGE);
				System.out.println("Grant joined the game");
			}

			PlayerReference steve = PlayerReference.getDummyPlayerReference(0);
			PlayerReference justin = PlayerReference.getDummyPlayerReference(1);
			PlayerReference jordan = PlayerReference.getDummyPlayerReference(2);
			PlayerReference grant = PlayerReference.getDummyPlayerReference(3);
			
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
			System.out.println("Grant Built 2nd Road");
			SP.buildSettlement(grant, vertexLocation, true);
			System.out.println("Grant Built 2nd Settlement");
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
			System.out.println("Jordan Built 2nd Road");
			SP.buildSettlement(jordan, vertexLocation, true);
			System.out.println("Jordan Built 2nd Settlement");
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
			System.out.println("Justin Built 2nd Road");
			SP.buildSettlement(justin, vertexLocation, true);
			System.out.println("Justin Built 2nd Settlement");
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
			System.out.println("Steve Built 2nd Road");
			SP.buildSettlement(steve, vertexLocation, true);
			System.out.println("Steve Built 2nd Settlement");
			SP.finishTurn(steve);
			System.out.println("Steve finished his Turn");
			
			JSONObject model = SP.getModel(0);
			System.out.println("Obtained Model");
			
			assertTrue(checkCards(model, steve, 1, 1, 0, 1, 0));
			assertTrue(checkCards(model, justin, 0, 1, 1, 1, 0));
			assertTrue(checkCards(model, jordan, 1, 0, 1, 0, 1));
			assertTrue(checkCards(model, grant, 0, 0, 2, 0, 1));
			assertTrue(checkLongestRoad(model, -1));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 4);
			System.out.println("Steve rolled a 4");
			location = new JSONObject();
			location.put("x", 0L);
			location.put("y", -1L);
			location.put("direction", "NE");
			edgeLocation = new EdgeLocation(location);
			SP.buildRoad(steve, edgeLocation, false);
			System.out.println("Steve built 3rd road");
			
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", -2L);
			location.put("direction", "NW");
			edgeLocation = new EdgeLocation(location);
			SP.buildRoad(steve, edgeLocation, false);
			System.out.println("Steve built 4th road");
			
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
			System.out.println("Steve built 3rd settlement");
			SP.finishTurn(steve);
			System.out.println("Steve finished his 1st turn");
			
			SP.rollDice(justin, 4);
			System.out.println("Justin rolled a 4");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 1st turn");
			
			SP.rollDice(jordan, 4);
			System.out.println("Jordan rolled a 4");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 1st turn");
			
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
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 1st turn");
			
			assertTrue(checkCards(model, steve, 6, 3, 0, 0, 0));
			assertTrue(checkCards(model, justin, 0, 4, 0, 2, 1));
			assertTrue(checkCards(model, jordan, 0, 0, 0, 0, 0));
			assertTrue(checkCards(model, grant, 0, 0, 3, 0, 0));
			assertTrue(checkLongestRoad(model, -1));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 7);
			System.out.println("Steve rolled a 7");
						
			JSONObject resources = new JSONObject();
			resources.put(ResourceType.BRICK.toString().toLowerCase(), 4);
			resources.put(ResourceType.ORE.toString().toLowerCase(), 0);
			resources.put(ResourceType.SHEEP.toString().toLowerCase(), 0);
			resources.put(ResourceType.WHEAT.toString().toLowerCase(), 0);
			resources.put(ResourceType.WOOD.toString().toLowerCase(), 0);

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
			System.out.println("Steve built 5th road");

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
			System.out.println("Steve finished his 2nd turn");
			
			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");

			SP.finishTurn(justin);
			System.out.println("Justin finished his 2nd turn");

			SP.rollDice(jordan, 9);
			System.out.println("Jordan rolled a 9");

			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 2nd turn");

			SP.rollDice(grant, 9);
			System.out.println("Grant rolled a 9");

			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 2nd turn");

			assertTrue(checkCards(model, steve, 0, 0, 0, 4, 1));
			assertTrue(checkCards(model, justin, 0, 5, 2, 2, 0));
			assertTrue(checkCards(model, jordan, 0, 0, 0, 0, 0));
			assertTrue(checkCards(model, grant, 0, 0, 4, 0, 0));
			assertTrue(checkLongestRoad(model, -1));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 11);
			System.out.println("Steve rolled an 11");
			SP.buildCity(steve, vertexLocation);
			System.out.println("Steve built a city");
			SP.finishTurn(steve);
			System.out.println("Steve finished his 3rd turn");
			
			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 3rd turn");
			
			SP.rollDice(jordan, 9);
			System.out.println("Jordan rolled a 9");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 3rd turn");

			SP.rollDice(grant, 11);
			System.out.println("Grant rolled an 11");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 3rd turn");
			
			assertTrue(checkCards(model, steve, 0, 0, 0, 4, 4));
			assertTrue(checkCards(model, justin, 0, 5, 3, 2, 0));
			assertTrue(checkCards(model, jordan, 0, 0, 0, 0, 0));
			assertTrue(checkCards(model, grant, 0, 0, 5, 0, 0));
			assertTrue(checkLongestRoad(model, -1));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 9);
			System.out.println("Steve rolled a 9");

			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", -1L);
			location.put("direction", "NW");
			vertexLocation = new VertexLocation(location);
			
			SP.buildCity(steve, vertexLocation);
			System.out.println("Steve built 2nd city");

			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", 1L);
			location.put("direction", "NW");
			vertexLocation = new VertexLocation(location);
			
			SP.buildCity(steve, vertexLocation);
			System.out.println("Steve built 3rd city");

			SP.finishTurn(steve);
			System.out.println("Steve finished his 4th turn");
			
			SP.rollDice(justin, 9);
			System.out.println("Justin rolled a 9");

			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), -7L);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), 5L);
			trade = new ResourceTradeList(tradeJSON);

			SP.offerTrade(justin, trade, grant);
			System.out.println("Justin offered trade to Grant");
			SP.respondToTrade(grant, true);
			System.out.println("Grant accepted trade");
			
			SP.maritimeTrade(justin, ResourceType.SHEEP, ResourceType.BRICK, 4);
			System.out.println("Justin just maritime traded 4 sheep for a brick");
			SP.maritimeTrade(justin, ResourceType.SHEEP, ResourceType.BRICK, 4);
			System.out.println("Justin just maritime traded 4 sheep for a brick");
			SP.maritimeTrade(justin, ResourceType.SHEEP, ResourceType.BRICK, 4);
			System.out.println("Justin just maritime traded 4 sheep for a brick");

			SP.finishTurn(justin);
			System.out.println("Justin just finished his 4th turn");
			
			SP.rollDice(jordan, 9);
			System.out.println("Jordan rolled a 9");

			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), -3L);
			trade = new ResourceTradeList(tradeJSON);

			SP.offerTrade(jordan, trade, grant);
			System.out.println("Jordan offered trade to Grant");
			SP.respondToTrade(grant, true);
			System.out.println("Grant accepted trade");
			SP.maritimeTrade(jordan, ResourceType.WOOD, ResourceType.BRICK, 3);
			System.out.println("Jordan used his general port to trade 3 wood for 1 brick");

			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 4th turn");

			SP.rollDice(grant, 9);
			System.out.println("Grant rolled a 9");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 4th turn");
			
			assertTrue(checkCards(model, steve, 0, 0, 0, 13, 0));
			assertTrue(checkCards(model, justin, 3, 0, 2, 2, 0));
			assertTrue(checkCards(model, jordan, 1, 0, 0, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 2, 0, 0));
			assertTrue(checkLongestRoad(model, -1));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 9);
			System.out.println("Steve rolled a 9");
			SP.maritimeTrade(steve, ResourceType.ORE, ResourceType.BRICK, 2);
			System.out.println("Steve used his ore port to trade 2 ore for 1 brick");
			SP.maritimeTrade(steve, ResourceType.ORE, ResourceType.WOOD, 2);
			System.out.println("Steve used his ore port to trade 2 ore for 1 wood");
			
			location = new JSONObject();
			location.put("x", 1L);
			location.put("y", 0L);
			location.put("direction", "NW");
			edgeLocation = new EdgeLocation(location);
			
			SP.buildRoad(steve, edgeLocation, false);
			System.out.println("Steve built 6th road\nSteve has longest road");
			SP.finishTurn(steve);
			System.out.println("Steve finished his 5th turn");
			
			SP.rollDice(justin, 9);
			System.out.println("Justin rolled a 9");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 5th turn");
			
			SP.rollDice(jordan, 9);
			System.out.println("Jordan rolled a 9");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 5th turn");

			SP.rollDice(grant, 11);
			System.out.println("Grant rolled an 11");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 5th turn");

			assertTrue(checkCards(model, steve, 0, 0, 0, 21, 2));
			assertTrue(checkCards(model, justin, 3, 0, 5, 2, 0));
			assertTrue(checkCards(model, jordan, 1, 0, 0, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 5, 0, 0));
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 11);
			System.out.println("Steve rolled an 11");
			SP.finishTurn(steve);
			System.out.println("Steve finished his 6th turn");
			
			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 6th turn");
			
			SP.rollDice(jordan, 11);
			System.out.println("Jordan rolled an 11");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 6th turn");
			
			SP.rollDice(grant, 11);
			System.out.println("Grant rolled an 11");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 6th turn");
			
			assertTrue(checkCards(model, steve, 0, 0, 0, 21, 10));
			assertTrue(checkCards(model, justin, 3, 0, 5, 2, 0));
			assertTrue(checkCards(model, jordan, 1, 0, 0, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 5, 0, 0));
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 11);
			System.out.println("Steve rolled an 11");
			SP.finishTurn(steve);
			System.out.println("Steve finished his 7th turn");

			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 7th turn");
			
			SP.rollDice(jordan, 11);
			System.out.println("Jordan rolled an 11");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 7th turn");

			SP.rollDice(grant, 11);
			System.out.println("Grant rolled an 11");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 7th turn");

			assertTrue(checkCards(model, steve, 0, 0, 0, 21, 18));
			assertTrue(checkCards(model, justin, 3, 0, 5, 2, 0));
			assertTrue(checkCards(model, jordan, 1, 0, 0, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 5, 0, 0));
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 11);
			System.out.println("Steve rolled an 11");
			SP.finishTurn(steve);
			System.out.println("Steve finished his 8th turn");

			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 8th turn");
			
			SP.rollDice(jordan, 11);
			System.out.println("Jordan rolled an 11");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 8th turn");

			SP.rollDice(grant, 10);
			System.out.println("Grant rolled a 10");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 8th turn");
			
			assertTrue(checkCards(model, steve, 0, 0, 2, 21, 24));
			assertTrue(checkCards(model, justin, 3, 0, 5, 2, 0));
			assertTrue(checkCards(model, jordan, 1, 0, 1, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 6, 0, 0));
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 10);
			System.out.println("Steve rolled a 10");
			
			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), -5L);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), 0L);
			trade = new ResourceTradeList(tradeJSON);

			SP.offerTrade(steve, trade, justin);
			System.out.println("Steve offered trade to Justin");
			SP.respondToTrade(justin, true);
			System.out.println("Justin accepted trade");

			tradeJSON = new JSONObject();
			tradeJSON.put(ResourceType.BRICK.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.ORE.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.SHEEP.toString().toLowerCase(), -7L);
			tradeJSON.put(ResourceType.WHEAT.toString().toLowerCase(), 0L);
			tradeJSON.put(ResourceType.WOOD.toString().toLowerCase(), 0L);
			trade = new ResourceTradeList(tradeJSON);

			SP.offerTrade(steve, trade, grant);
			System.out.println("Steve offered trade to Grant");
			SP.respondToTrade(grant, true);
			System.out.println("Grant accepted trade");

			SP.buyDevCard(steve);
			System.out.println("Steve bought a development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 2nd development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 3rd development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 4th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 5th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 6th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 7th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 8th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 9th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 10th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 11th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 12th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 13th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 14th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 15th development card");

			SP.finishTurn(steve);
			System.out.println("Steve finished his 9th turn");
			
			SP.rollDice(justin, 10);
			System.out.println("Justin rolled a 10");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 9th turn");
			
			SP.rollDice(jordan, 10);
			System.out.println("Jordan rolled a 10");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 9th turn");
			
			SP.rollDice(grant, 10);
			System.out.println("Grant rolled a 10");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 9th turn");
			
			assertTrue(checkCards(model, steve, 0, 0, 7, 6, 9));
			assertTrue(checkCards(model, justin, 3, 0, 0, 2, 0));
			assertTrue(checkCards(model, jordan, 1, 0, 5, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 3, 0, 0));
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 10);
			System.out.println("Steve rolled a 10");

			SP.buyDevCard(steve);
			System.out.println("Steve bought 16th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 17th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 18th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 19th development card");
			SP.buyDevCard(steve);
			System.out.println("Steve bought 20th development card");			
			SP.finishTurn(steve);
			System.out.println("Steve finished his 10th turn");

			SP.rollDice(justin, 11);
			System.out.println("Justin rolled an 11");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 10th turn");
			
			SP.rollDice(jordan, 9);
			System.out.println("Jordan rolled a 9");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 10th turn");
			
			SP.rollDice(grant, 10);
			System.out.println("Grant rolled a 10");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 10th turn");
			
			assertTrue(checkCards(model, steve, 0, 0, 6, 5, 6));
			assertTrue(checkCards(model, justin, 3, 0, 1, 2, 0));
			assertTrue(checkCards(model, jordan, 1, 0, 7, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 6, 0, 0));
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 10);
			System.out.println("Steve rolled a 10");

			SP.buyDevCard(steve);
			System.out.println("Steve bought 21st development card");			
			SP.buyDevCard(steve);
			System.out.println("Steve bought 22nd development card");			
			SP.buyDevCard(steve);
			System.out.println("Steve bought 23rd development card");			
			SP.buyDevCard(steve);
			System.out.println("Steve bought 24th development card");			
			SP.buyDevCard(steve);
			System.out.println("Steve bought 25th development card");	

			SP.finishTurn(steve);
			System.out.println("Steve finished his 11th turn");
			
			SP.rollDice(justin, 8);
			System.out.println("Justin rolled an 8");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 11th turn");
			
			SP.rollDice(jordan, 8);
			System.out.println("Jordan rolled an 8");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 11th turn");

			SP.rollDice(grant, 8);
			System.out.println("Grant rolled an 8");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 11th turn");

			assertTrue(checkCards(model, steve, 0, 0, 3, 0, 1));
			assertTrue(checkCards(model, justin, 3, 0, 1, 2, 0));
			assertTrue(checkCards(model, jordan, 1, 0, 8, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 7, 0, 0));
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 8);
			System.out.println("Steve rolled an 8");
			
			SP.monopoly(steve, ResourceType.BRICK);
			System.out.println("Steve used monopoly on brick");
			SP.yearOfPlenty(steve, ResourceType.WOOD, ResourceType.WOOD);
			System.out.println("Steve used Year of Plenty to get two wood");
			
			location = new JSONObject();
			location.put("x", 0L);
			location.put("y", -2L);
			location.put("direction", "NE");
			edgeLocation = new EdgeLocation(location);

			location = new JSONObject();
			location.put("x", 0L);
			location.put("y", -2L);
			location.put("direction", "N");
			EdgeLocation secondEdgeLocation = new EdgeLocation(location);

			SP.roadBuilding(steve, edgeLocation, secondEdgeLocation);
			System.out.println("Steve used Road Builder to build two roads");
			
			hexLocation = new HexLocation(-1,-1);
			PlayerReference nullPlayer = PlayerReference.getDummyPlayerReference(-1);
			
			SP.soldier(steve, hexLocation, nullPlayer);
			System.out.println("Steve used a knight to move the robber to a blank space");

			SP.finishTurn(steve);
			System.out.println("Steve finished his 12th turn");
			
			SP.rollDice(justin, 8);
			System.out.println("Justin rolled an 8");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 12th turn");
			
			SP.rollDice(jordan, 8);
			System.out.println("Jordan rolled an 8");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 12th turn");

			SP.rollDice(grant, 8);
			System.out.println("Grant rolled an 8");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 12th turn");

			assertTrue(checkCards(model, steve, 4, 2, 3, 0, 1));
			assertTrue(checkCards(model, justin, 0, 0, 1, 2, 0));
			assertTrue(checkCards(model, jordan, 0, 0, 8, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 7, 0, 0));
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 8);
			System.out.println("Steve rolled an 8");

			hexLocation = new HexLocation(-2,0);
			SP.soldier(steve, hexLocation, nullPlayer);
			System.out.println("Steve used 2nd knight to move the robber to a blank space");

			SP.finishTurn(steve);
			System.out.println("Steve finished his 13th turn");
			
			SP.rollDice(justin, 8);
			System.out.println("Justin rolled an 8");
			SP.finishTurn(justin);
			System.out.println("Justin finished his 13th turn");
			
			SP.rollDice(jordan, 8);
			System.out.println("Jordan rolled an 8");
			SP.finishTurn(jordan);
			System.out.println("Jordan finished his 13th turn");

			SP.rollDice(grant, 8);
			System.out.println("Grant rolled an 8");
			model = SP.finishTurn(grant);
			System.out.println("Grant finished his 13th turn");

			assertTrue(checkCards(model, steve, 4, 2, 3, 0, 1));
			assertTrue(checkCards(model, justin, 0, 0, 1, 2, 0));
			assertTrue(checkCards(model, jordan, 0, 0, 8, 0, 0));
			assertTrue(checkCards(model, grant, 0, 2, 7, 0, 0));
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, -1));
			assertTrue(checkWinner(model, -1));

			SP.rollDice(steve, 8);
			System.out.println("Steve rolled an 8");

			hexLocation = new HexLocation(-1,1);
			model = SP.soldier(steve, hexLocation, nullPlayer);
			System.out.println("Steve used 3rd knight to move the robber to a blank space"
					+ "\nSteve has largest army");
			
			assertTrue(checkLongestRoad(model, 0));
			assertTrue(checkLargestArmy(model, 0));
			assertTrue(checkWinner(model, 12));
			
			System.out.println("Steve won the game");

			SP.monument(steve);
			System.out.println("Steve played a monument cards for 12 points");
			SP.monument(steve);
			System.out.println("Steve played 2nd monument cards for 13 points");
			SP.monument(steve);
			System.out.println("Steve played 3rd monument cards for 14 points");
			SP.monument(steve);
			System.out.println("Steve played 4th monument cards for 15 points");
			SP.monument(steve);
			System.out.println("Steve played 5th monument cards for 16 points");
									
			finish = System.currentTimeMillis();
			
			System.out.println(finish - start);
		}
		catch(Exception e){
			e.printStackTrace();
			fail("Test1");
		}
	}
	
	@Test
	public void testTwo() throws UserException, ServerException, GameInitializationException, InvalidActionException, JoinGameException{
		SP = new ServerProxy();
		SP.login("Sam", "sam");
		System.out.println("Sam logged in");
		
		GameHeader game = SP.createGame("AI Addition", true, true, true);
		System.out.println("Game created");
		SP.joinGame(game.getId(), CatanColor.PURPLE);
		System.out.println("Sam joined the game");
		
		List<String> AIList = SP.getAITypes();
		System.out.println("Got AITypes");
		
		AIType type = AIType.getTypeFromString(AIList.get(0));
		SP.addAIPlayer(type);
		System.out.println("Added AI to game");
		SP.addAIPlayer(type);
		System.out.println("Added AI to game");
		SP.addAIPlayer(type);
		System.out.println("Added AI to game\nGame is full");		
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkCards(JSONObject model, PlayerReference player, int brick, int wood, int sheep, int ore, int wheat){
		List<JSONObject> players = (List<JSONObject>) model.get("players");
		int playerIndex = player.getIndex();
		JSONObject currentPlayer = players.get(playerIndex);
		JSONObject resources = (JSONObject) currentPlayer.get("resources");
		int actualBrick = ((Long) resources.get("brick")).intValue();
		int actualWood = ((Long) resources.get("wood")).intValue();
		int actualSheep = ((Long) resources.get("sheep")).intValue();
		int actualOre = ((Long) resources.get("ore")).intValue();
		int actualWheat = ((Long) resources.get("wheat")).intValue();
		
		if(actualBrick != brick){
			System.out.println(player.getIndex() + " had " + actualBrick + " brick");
		}
		if(actualWood != wood){
			System.out.println(player.getIndex() + " had " + actualWood + " wood");
		}
		if(actualSheep != sheep){
			System.out.println(player.getIndex() + " had " + actualSheep + " sheep");
		}
		if(actualOre != ore){
			System.out.println(player.getIndex() + " had " + actualOre + " ore");
		}
		if(actualWheat != wheat){
			System.out.println(player.getIndex() + " had " + actualWheat + " wheat");
		}

		return (actualBrick == brick &&
				actualWood == wood &&
				actualSheep == sheep &&
				actualOre == ore &&
				actualWheat == wheat);
		
	}	
	
	public boolean checkLongestRoad(JSONObject model, int playerIndex){
		JSONObject turnTracker = (JSONObject) model.get("turnTracker");
		int player = ((Long) turnTracker.get("longestRoad")).intValue();
		if(player != playerIndex){
			System.out.println("player " + player + " has longest road.");
			return false;
		}
		return true;
	}
	
	public boolean checkLargestArmy(JSONObject model, int playerIndex){
		JSONObject turnTracker = (JSONObject) model.get("turnTracker");
		int player = ((Long) turnTracker.get("largestArmy")).intValue();
		if(player != playerIndex){
			System.out.println("player " + player + " has largest army.");
			return false;
		}
		return true;
	}
	
	public boolean checkWinner(JSONObject model, int playerIndex){
		int player = ((Long) model.get("winner")).intValue();
		if(player != playerIndex){
			System.out.println("player " + player + " won");
			return false;
		}
		return true;
	}

}
