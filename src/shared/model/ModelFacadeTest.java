package shared.model;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import server.cheat.CheatEnabledDice;
import shared.IDice;
import shared.communication.*;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.MunicipalityType;
import shared.definitions.ResourceType;
import shared.exceptions.GameInitializationException;
import shared.exceptions.InsufficientResourcesException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.NotYourTurnException;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public class ModelFacadeTest {

	private CatanModel model;
	private ModelFacade m;
	private CheatEnabledDice dice;
	
	private PlayerReference justin, steve, jordan, grant;
	
	@Before
	public void setup() throws Exception {
		
		dice = new CheatEnabledDice();
		model = new CatanModel();
		m = new ModelFacade(model, dice);
		//m.updateFromJSON(json.toString());
		model.setHeader(new GameHeader("Dummy Game", 
				UUID.fromString("3d4f073d-7acd-4cf8-8b81-5eb097b58d79"),
				new ArrayList<PlayerHeader>()));
		
		// Put in some players
		m.addPlayer("Justin", CatanColor.GREEN);
		m.addPlayer("Steve", CatanColor.BLUE);
		m.addPlayer("Jordan", CatanColor.ORANGE);
		m.addPlayer("Grant", CatanColor.RED);
		
		List<Player> players = model.getPlayers();
		justin = players.get(0).getReference();
		steve = players.get(1).getReference();
		jordan = players.get(2).getReference();
		grant = players.get(3).getReference();
		
		//System.out.println(model.getTurnTracker());
		
		// Place some starting crap
		m.buildStartingPieces(justin,
				new VertexLocation(0, 2, VertexDirection.NorthEast),
				new EdgeLocation(0, 1, EdgeDirection.SouthEast));
		m.buildStartingPieces(steve,
				new VertexLocation(1, -1, VertexDirection.NorthEast),
				new EdgeLocation(1, -1, EdgeDirection.North));
		m.buildStartingPieces(jordan,
				new VertexLocation(0, 2, VertexDirection.NorthEast),
				new EdgeLocation(0, 1, EdgeDirection.SouthWest));
		m.buildStartingPieces(grant,
				new VertexLocation(0, 2, VertexDirection.NorthEast),
				new EdgeLocation(0, 1, EdgeDirection.SouthWest));
		
		m.buildStartingPieces(grant,
				new VertexLocation(0, 2, VertexDirection.NorthEast),
				new EdgeLocation(0, 1, EdgeDirection.SouthWest));
		m.buildStartingPieces(jordan,
				new VertexLocation(0, 2, VertexDirection.NorthEast),
				new EdgeLocation(0, 1, EdgeDirection.SouthWest));
		m.buildStartingPieces(steve,
				new VertexLocation(0, 2, VertexDirection.NorthEast),
				new EdgeLocation(0, 1, EdgeDirection.SouthWest));
		m.buildStartingPieces(justin,
				new VertexLocation(0, 2, VertexDirection.NorthEast),
				new EdgeLocation(0, 1, EdgeDirection.SouthWest));
		
		System.out.println(model.getTurnTracker());
	}
	
	
	@Test
	public void testCanRoll() {
		
		//test if it's players current turn and he hasn't rolled
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();
		assertTrue(m.canRoll(currentPlayer));
		
		//test if it is player's turn but he has already rolled
		Player cur = currentPlayer.getPlayer();
		cur.setHasRolled(true);
		assertFalse(m.canRoll(currentPlayer));
		
		//test if its not player's current turn
		PlayerReference otherPlayer = new PlayerReference(m.getCatanModel(), 3);
		assertFalse(m.canRoll(otherPlayer));
	}
	
	@Test
	public void testRollDice() throws NotYourTurnException {
		dice.enqueueRoll(8);
		
		PlayerReference currentPlayer = m.getCurrentPlayer();
		
		m.rollDice(currentPlayer);
		
		//TODO: test that resources have been correctly distributed
	}
	
	@Test
	public void testCanRob() {
		
		//test if robber is already there
		HexLocation hexLoc = m.getCatanModel().getMap().getRobberLocation();
		assertFalse(m.canMoveRobberTo(hexLoc));
		
		//test if hex is desert
		hexLoc = m.getCatanModel().getMap().getDesertLocation();
		assertFalse(m.canMoveRobberTo(hexLoc));
		
		//test any other hex
		assertTrue(m.canMoveRobberTo(new HexLocation(0, 0)));
		
	}
	
	@Test 
	public void testDoRob() {
		
		//m.rob();
	}
	
	@Test
	public void testCanFinishTurn() throws NotYourTurnException {
		
		dice.enqueueRoll(8);
		
		//test if current player has not rolled
		assertFalse(m.canFinishTurn());
		
		//test if current player has rolled
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();

		m.rollDice(currentPlayer);
		assertTrue(m.canFinishTurn());
		
	}
	
	@Test
	public void testDoFinishTurn() throws InvalidActionException {
		dice.enqueueRoll(8);
		
		PlayerReference currentPlayer = m.getCurrentPlayer();

		m.rollDice(currentPlayer);
		m.finishTurn(currentPlayer);
	}
	
	@Test
	public void testCanBuyDevelopmentCard() throws InvalidActionException {
		
		
		//test insufficient resources
		assertFalse(m.canBuyDevelopmentCard());
		
		//test hand with sufficient resources
		Player currentPlayer = m.getCurrentPlayer().getPlayer();
		ResourceList hand = currentPlayer.getResources();
		ResourceList bank = model.getBank().getResources();
		bank.transfer(hand, ResourceType.WHEAT, 1);
		bank.transfer(hand, ResourceType.ORE, 1);
		bank.transfer(hand, ResourceType.SHEEP, 1);
		assertTrue(m.canBuyDevelopmentCard());
		
		// Test with empty devcard deck
		DevCardList dump = new DevCardList();
		model.getBank().getDevCards().transferAll(dump);
		assertFalse(m.canBuyDevelopmentCard());
		
		// Not your turn
		assertFalse(m.canBuyDevelopmentCard(new PlayerReference(model, 3)));
	}
	
	@Test
	public void testDoBuyDevelopmentCard() throws Exception {
		
		int expectedTotal = model.getBank().getDevCards().count();

		PlayerReference playerRef = m.getCurrentPlayer();
		Player player = playerRef.getPlayer();
		ResourceList hand = player.getResources();
		ResourceList bank = model.getBank().getResources();

		int wheatCount = hand.count(ResourceType.WHEAT);
		int oreCount = hand.count(ResourceType.ORE);
		int sheepCount = hand.count(ResourceType.SHEEP);
		
		bank.transfer(hand, ResourceType.WHEAT, 1);
		bank.transfer(hand, ResourceType.ORE, 1);
		bank.transfer(hand, ResourceType.SHEEP, 1);
		
		m.buyDevelopmentCard(playerRef);
		
		// verify that there are the correct number of devcards overall.
		assertEquals(expectedTotal, model.getBank().getDevCards().count() +
				player.getNewDevCards().count());
		// And the correct resources were expended.
		assertEquals(wheatCount, hand.count(ResourceType.WHEAT));
		assertEquals(oreCount, hand.count(ResourceType.ORE));
		assertEquals(sheepCount, hand.count(ResourceType.SHEEP));
	}
	
	@Test
	public void testCanBuildRoad() {
		
		//test no connecting municipality or road
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();
		Map<EdgeLocation, Road> roads = m.getCatanModel().getMap().getRoadMap();
		HexLocation hexLoc = new HexLocation(0, 0);
		EdgeLocation edgeLoc = new EdgeLocation(hexLoc, EdgeDirection.North);
		assertFalse(m.canBuildRoad(edgeLoc));
		
		//test connecting road
		Road road = new Road(edgeLoc, currentPlayer);
		roads.put(road.getLocation(), road);
		m.getCatanModel().getMap().setRoads(roads);
		edgeLoc = new EdgeLocation(hexLoc, EdgeDirection.NorthEast);
		assertTrue(m.canBuildRoad(edgeLoc));
		
		//test connecting municipality
		Map<VertexLocation, Municipality> municipalities = m.getCatanModel().getMap().getMunicipalityMap();
		hexLoc = new HexLocation(1, 1);
		VertexLocation vertLoc = new VertexLocation(hexLoc, VertexDirection.East);
		Municipality municipality = new Municipality(vertLoc, MunicipalityType.SETTLEMENT, currentPlayer);
		municipalities.put(municipality.getLocation(), municipality);
		m.getCatanModel().getMap().setMunicipalities(municipalities);
		edgeLoc = new EdgeLocation(hexLoc, EdgeDirection.NorthEast);
		assertTrue(m.canBuildRoad(edgeLoc));
		
	}

	@Test
	public void testDoBuildRoad() throws Exception {
		
		m.buildRoad(null, null);
	}
	
	@Test
	public void testCanBuildSettlement() {
		
		HexLocation hexLoc = new HexLocation(0, 0);
		VertexLocation vertexLoc = new VertexLocation(hexLoc, VertexDirection.East);
		boolean can = m.canBuildSettlement(vertexLoc);
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();
		Map<EdgeLocation, Road> roads = m.getCatanModel().getMap().getRoadMap();
		EdgeLocation edgeLoc = new EdgeLocation(hexLoc, EdgeDirection.NorthEast);
		
		//test no connection
		assertFalse(can);
		
		//test connecting road
		Road road = new Road(edgeLoc, currentPlayer);
		roads.put(road.getLocation(), road);
		m.getCatanModel().getMap().setRoads(roads);
		can = m.canBuildSettlement(vertexLoc);
		assertTrue(can);
		
		//test with connecting road, but adjacent municipality
		Map<VertexLocation, Municipality> municipalities = m.getCatanModel().getMap().getMunicipalityMap();
		Municipality municipality = new Municipality(vertexLoc, MunicipalityType.SETTLEMENT, currentPlayer);
		municipalities.put(municipality.getLocation(), municipality);
		m.getCatanModel().getMap().setMunicipalities(municipalities);
		vertexLoc = new VertexLocation(hexLoc, VertexDirection.NorthEast);
		can = m.canBuildSettlement(vertexLoc);
		assertFalse(can);
		
	}
	
	@Test
	public void testCanBuildCity() throws Exception {
		
		//test nothing at current location
		HexLocation hexLoc = new HexLocation(0, 0);
		VertexLocation vertexLoc = new VertexLocation(hexLoc, VertexDirection.East);
		assertFalse(m.canBuildCity(vertexLoc));
		
		//test settlement at current location
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();
		Municipality municipality = new Municipality(vertexLoc, MunicipalityType.SETTLEMENT, currentPlayer);
		Map<VertexLocation, Municipality> municipalities = m.getCatanModel().getMap().getMunicipalityMap();
		municipalities.put(municipality.getLocation(), municipality);
		m.getCatanModel().getMap().setMunicipalities(municipalities);
		
		// Still not enough resources
		assertFalse(m.canBuildCity(municipality.getLocation()));

		ResourceList hand = m.getCurrentPlayer().getHand();
		ResourceList bank = model.getBank().getResources();
		
		bank.transfer(hand, ResourceType.WHEAT, 2);
		bank.transfer(hand, ResourceType.ORE, 3);
		
		assertTrue(m.canBuildCity(municipality.getLocation()));
	}
	
	@Test
	public void testDoBuildSettlement() throws InvalidActionException {
		
		HexLocation hexLoc = new HexLocation(0, 0);
		VertexLocation vertexLoc = new VertexLocation(hexLoc, VertexDirection.East);
		m.buildSettlement(m.getCurrentPlayer(), vertexLoc);
	}
	
	@Test
	public void testCanYearOfPlenty() throws InvalidActionException {
		
		//test with empty hand
		assertFalse(m.canYearOfPlenty(m.getCurrentPlayer(), ResourceType.BRICK, ResourceType.WOOD));
		
		//test with yearOfPlenty card in hand
		Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.YEAR_OF_PLENTY);
		//can = m.canYearOfPlenty();
		assertTrue(m.canYearOfPlenty(m.getCurrentPlayer(), ResourceType.BRICK, ResourceType.WOOD));
	}
	
	@Test
	public void testDoYearOfPlenty() {
		
		//m.doYearOfPlenty();
	}
	
	@Test
	public void testCanRoadBuildingCard() throws InvalidActionException {
		//boolean can = m.canRoadBuildingCard();
		//assertFalse(can);
		
		//test with roadBuilding card in hand
		Player currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.ROAD_BUILD);
		//can = m.canRoadBuildingCard();
		//assertTrue(can);
	}
	
	@Test
	public void testDoRoadBuildingCard() {
		
		//m.doRoadBuildCard();
	}
	
	@Test
	public void testCanSoldier() throws InvalidActionException {
		
		//test with empty hand
		//boolean can = m.canSoldier();
		//assertFalse(can);
				
		//test with soldier card in hand
		Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.SOLDIER);
		//can = m.canSoldier();
		//assertTrue(can);
	}
	
	@Test
	public void testDoSoldier() {
		
		//m.soldier();
	}
	
	@Test
	public void testCanMonopoly() throws InvalidActionException {
		
		//test with empty hand
		//boolean can = m.canMonopoly();
		//assertFalse(can);
						
		//test with monopoly card in hand
		Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.MONOPOLY);
		//can = m.canMonopoly();
		//assertTrue(can);
	}
	
	@Test
	public void testDoMonopoly() {
		
		//m.doMonopoly();
	}
	
	@Test
	public void testCanMonument() throws InvalidActionException {
		
		//test with empty hand
		//boolean can = m.canMonument();
		//assertFalse(can);
						
		//test with monument card in hand
		Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.MONUMENT);
		//can = m.canMonument();
		//assertTrue(can);
	}
	
	@Test
	public void testDoMonument() {
		
		//m.monument();
	}
	
	@Test
	public void testCanOfferTrade() throws InsufficientResourcesException {
		//test with insufficient cards in hand for trade
		//boolean can = m.canOfferTrade();
		//assertFalse(can);
		
		//test with sufficient cards in hand for trade
		TradeOffer tradeOffer = m.getCatanModel().getTradeOffer();
		Player offeringPlayer = tradeOffer.getSender().getPlayer();
		ResourceList hand = offeringPlayer.getResources();
		ResourceList bank = m.getCatanModel().getBank().getResources();
		bank.transfer(hand, ResourceType.BRICK, 1);
		bank.transfer(hand, ResourceType.WHEAT, 1);
		bank.transfer(hand, ResourceType.WOOD, 1);
		bank.transfer(hand, ResourceType.SHEEP, 1);
		bank.transfer(hand, ResourceType.ORE, 1);
		//can = m.canOfferTrade();
		//assertTrue(can);
	}
	
	@Test
	public void testDoOfferTrade() {
		
		//m.offerTrade();
	}
	
	@Test
	public void testCanAcceptTrade() throws InsufficientResourcesException {
			
		TradeOffer tradeOffer = m.getCatanModel().getTradeOffer();
		Player acceptingPlayer = tradeOffer.getReceiver().getPlayer();
		
		//test with insufficient cards in hand for trade
		Map<ResourceType, Integer> wanted = new HashMap<ResourceType, Integer>();
		wanted.put(ResourceType.ORE, 1);
		tradeOffer.getOffer().setWanted(wanted);
		
		boolean can = m.canAcceptTrade();
		assertFalse(can);
		
		//test with sufficient cards in hand for trade
		
		ResourceList hand = acceptingPlayer.getResources();
		ResourceList bank = m.getCatanModel().getBank().getResources();
		bank.transfer(hand, ResourceType.BRICK, 1);
		bank.transfer(hand, ResourceType.WHEAT, 1);
		bank.transfer(hand, ResourceType.WOOD, 1);
		bank.transfer(hand, ResourceType.SHEEP, 1);
		bank.transfer(hand, ResourceType.ORE, 1);
		can = m.canAcceptTrade();
		assertTrue(can);
		
	}
	
	@Test
	public void testDoAcceptTrade() {
		
		//m.acceptTrade();
	}
	
	@Test
	public void testCanMaritimeTrade() {
		
		//test without municipality on port
		boolean can = m.ownsPort();
		assertFalse(can);
		
		//test with municipality on port
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();
		HexLocation hexLoc = new HexLocation(0, 3);
		VertexLocation vertexLoc = new VertexLocation(hexLoc, VertexDirection.NorthEast);
		Map<VertexLocation, Municipality> municipalities = m.getCatanModel().getMap().getMunicipalityMap();
		Municipality municipality = new Municipality(vertexLoc, MunicipalityType.SETTLEMENT, currentPlayer);
		municipalities.put(municipality.getLocation(), municipality);
		m.getCatanModel().getMap().setMunicipalities(municipalities);
		can = m.ownsPort();
		assertTrue(can);
		
	}
	
	@Test
	public void testDoMaritimeTrade() {
		
		//m.maritimeTrade();
	}
	

}
