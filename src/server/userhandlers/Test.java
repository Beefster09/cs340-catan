package server.userhandlers;

import java.util.List;
import server.communication.ServerCommunicator;
import shared.communication.GameHeader;
import shared.communication.Session;
import shared.definitions.CatanColor;
import client.communication.ServerProxy;

public class Test {

	public static void main(String[] args) throws Exception{
		String[] arg = new String[1];
		arg[0] = "8081";
		ServerCommunicator.main(arg);
		new Test();
		System.exit(0);
	}
	
	public Test() throws Exception{
		ServerProxy server = new ServerProxy("localhost", 8081);
			
		//user
		//login
		Session user = server.login("Sam", "sam");
		System.out.println(user.getUsername() + "\n");
		//register
		user = server.register("Steve", "steve");
		System.out.println(user.getUsername() + "\n");
		
		//game
		//createGame
		GameHeader game = server.createGame("blah", true, true, true);
		System.out.println(game.getTitle() + "\n");
		//getGameList
		List<GameHeader> games = server.getGameList();
		System.out.println("Size: " + games.size() + "\n");
		//joinGame
		boolean returned = server.joinGame(user, 0, CatanColor.RED);
		System.out.println(returned + "\n");
		
		//moves
		//AcceptTrade
		String model = server.respondToTrade(0, 0, true);
		System.out.println("Accept Trade:\n" + model.substring(5, 49) + "\n");
		//BuildCity
		model = server.buildCity(0, 0, null);
		System.out.println("Build City:\n" + model.substring(5, 49) + "\n");
		//BuildRoad
		model = server.buildRoad(0, 0, null, false);
		System.out.println("Build Road:\n" + model.substring(5, 49) + "\n");
		//BuildSettlement
		model = server.buildSettlement(0, 0, null, false);
		System.out.println("Build Settlement:\n" + model.substring(5, 49) + "\n");
		//BuyDevCard
		model = server.buyDevCard(0, 0);
		System.out.println("Buy Dev Card:\n" + model.substring(5, 49) + "\n");
		//DiscardCards
		model = server.discardCards(0, 0, null);
		System.out.println("Discard Cards:\n" + model.substring(5, 49) + "\n");
		//FinishTurn
		model = server.finishTurn(0, 0);
		System.out.println("Finish Turn:\n" + model.substring(5, 49) + "\n");
		//MaritimeTrade
		model = server.maritimeTrade(0, 0, null, null, 2);
		System.out.println("Maritime Trade:\n" + model.substring(5, 49) + "\n");
		//GetModel
		model = server.getModel(0, 0);
		System.out.println("Get Model:\n" + model.substring(5, 49) + "\n");
		//Monopoly
		model = server.monopoly(0, 0, null);
		System.out.println("Monopoly:\n" + model.substring(5, 49) + "\n");
		//Monument
		model = server.monument(0, 0);
		System.out.println("Monument:\n" + model.substring(5, 49) + "\n");
		//OfferTrade
		model = server.offerTrade(0, 0, null, 0);
		System.out.println("Offer Trade:\n" + model.substring(5, 49) + "\n");
		//RoadBuilding
		model = server.roadBuilding(0, 0, null, null);
		System.out.println("Road Building:\n" + model.substring(5, 49) + "\n");
		//RobPlayer
		model = server.robPlayer(0, 0, null, 0);
		System.out.println("Rob Player:\n" + model.substring(5, 49) + "\n");
		//RollNumber
		model = server.rollDice(0, 0, 3);
		System.out.println("Roll Dice:\n" + model.substring(5, 49) + "\n");
		//SendChat
		model = server.sendChat(0, 0, "Hey");
		System.out.println("Send Chat:\n" + model.substring(5, 49) + "\n");
		//Solier
		model = server.soldier(0, 0, null, 0);
		System.out.println("Solier:\n" + model.substring(5, 49) + "\n");
		//YearOfPlenty
		model = server.yearOfPlenty(0, 0, null, null);
		System.out.println("Year of Plenty:\n" + model.substring(5, 49) + "\n");
		
		System.out.println("\nFinished");
	}
}
