package shared.model;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import shared.communication.*;
import shared.definitions.DevCardType;
import shared.definitions.MunicipalityType;
import shared.definitions.ResourceType;
import shared.exceptions.InsufficientResourcesException;
import shared.exceptions.InvalidActionException;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public class ClientModelFacadeTest {

	private CatanModel model;
	private ClientModelFacade m;

	
	@Before
	public void setup() throws IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		Reader r = new BufferedReader(new FileReader("json_test.json"));
		Object parseResult = parser.parse(r);
		JSONObject json = ((JSONObject) parseResult);
		
		model = new CatanModel();
		model.setHeader(new GameHeader("Dummy Game", 
				UUID.fromString("3d4f073d-7acd-4cf8-8b81-5eb097b58d79"),
				new ArrayList<PlayerHeader>()));
		m = new ClientModelFacade(model);
		m.updateFromJSON(json.toJSONString());
	}
	
	
	@Test
	public void testCanRoll() {
		
		//test if it's players current turn and he hasn't rolled
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();
		boolean can = m.canRoll(currentPlayer);
		assertTrue(can);
		
		//test if it is player's turn but he has already rolled
		Player cur = currentPlayer.getPlayer();
		cur.setHasRolled(true);
		can = m.canRoll(currentPlayer);
		assertFalse(can);
		
		//test if its not player's current turn
		PlayerReference otherPlayer = new PlayerReference(m.getCatanModel(), 3);
		can = m.canRoll(otherPlayer);
		assertFalse(can);
		}
	
	@Test
	public void testCanRob() {
		
		//test if robber is already there
		HexLocation hexLoc = m.getCatanModel().getMap().getRobberLocation();
		
		boolean can = m.canMoveRobberTo(hexLoc);
		assertFalse(can);
		
		//test if hex is desert
		hexLoc = m.getCatanModel().getMap().getDesertLocation();
		can = m.canMoveRobberTo(hexLoc);
		assertFalse(can);
		
		//test any other hex
		hexLoc = new HexLocation(0, 0);
		can = m.canMoveRobberTo(hexLoc);
		assertTrue(can);
		
	}
	
	@Test 
	public void testDoRob() {
		
		//m.rob();
	}
	
	@Test
	public void testCanFinishTurn() {
		
		//test if current player has not rolled
		boolean can = m.canFinishTurn();
		assertTrue(can);
		
		//test if current player has rolled
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();
		m.rollDice(currentPlayer);
		can = m.canFinishTurn();
		assertFalse(can);
		
	}
	
	@Test
	public void testDoFinishTurn() {
		
		//m.finishTurn();
	}
	
	@Test
	public void testCanBuyDevelopmentCard() throws InsufficientResourcesException {
		
		
		//test insufficient resources
		boolean can = m.canBuyDevelopmentCard();
		assertFalse(can);
		
		//test hand with sufficient resources
		Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		ResourceList hand = currentPlayer.getResources();
		ResourceList bank = model.getBank().getResources();
		bank.transferTo(hand, ResourceType.WHEAT, 1);
		bank.transferTo(hand, ResourceType.ORE, 1);
		bank.transferTo(hand, ResourceType.SHEEP, 1);
		can = m.canBuyDevelopmentCard();
		assertTrue(can);
		
	}
	
	@Test
	public void testDoBuyDevelopmentCard() {
		
		m.buyDevelopmentCard();
	}
	
	@Test
	public void testCanBuildRoad() {
		
		//test no connecting municipality or road
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();
		Map<EdgeLocation, Road> roads = m.getCatanModel().getMap().getRoadMap();
		HexLocation hexLoc = new HexLocation(0, 0);
		EdgeLocation edgeLoc = new EdgeLocation(hexLoc, EdgeDirection.North);
		boolean can = m.canBuildRoad(edgeLoc);
		assertFalse(can);
		
		//test connecting road
		Road road = new Road(edgeLoc, currentPlayer);
		roads.put(road.getLocation(), road);
		m.getCatanModel().getMap().setRoads(roads);
		edgeLoc = new EdgeLocation(hexLoc, EdgeDirection.NorthEast);
		can = m.canBuildRoad(edgeLoc);
		assertTrue(can);
		
		//test connecting municipality
		Map<VertexLocation, Municipality> municipalities = m.getCatanModel().getMap().getMunicipalityMap();
		hexLoc = new HexLocation(1, 1);
		VertexLocation vertLoc = new VertexLocation(hexLoc, VertexDirection.East);
		Municipality municipality = new Municipality(vertLoc, MunicipalityType.SETTLEMENT, currentPlayer);
		municipalities.put(municipality.getLocation(), municipality);
		m.getCatanModel().getMap().setMunicipalities(municipalities);
		edgeLoc = new EdgeLocation(hexLoc, EdgeDirection.NorthEast);
		can = m.canBuildRoad(edgeLoc);
		assertTrue(can);
		
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
	public void testCanBuildCity() {
		
		//test nothing at current location
		HexLocation hexLoc = new HexLocation(0, 0);
		VertexLocation vertexLoc = new VertexLocation(hexLoc, VertexDirection.East);
		boolean can = m.canBuildCity(vertexLoc);
		assertFalse(can);
		
		//test settlement at current location
		PlayerReference currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer();
		Municipality municipality = new Municipality(vertexLoc, MunicipalityType.SETTLEMENT, currentPlayer);
		Map<VertexLocation, Municipality> municipalities = m.getCatanModel().getMap().getMunicipalityMap();
		municipalities.put(municipality.getLocation(), municipality);
		m.getCatanModel().getMap().setMunicipalities(municipalities);
		can = m.canBuildCity(municipality.getLocation());
		assertTrue(can);
	}
	
	@Test
	public void testDoBuildSettlement() {
		
		HexLocation hexLoc = new HexLocation(0, 0);
		VertexLocation vertexLoc = new VertexLocation(hexLoc, VertexDirection.East);
		//m.buildSettlement(vertexLoc);
	}
	
	@Test
	public void testCanYearOfPlenty() throws InvalidActionException {
		
		//test with empty hand
		//boolean can = m.canYearOfPlenty();
		//assertFalse(can);
		
		//test with yearOfPlenty card in hand
		Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.YEAR_OF_PLENTY);
		//can = m.canYearOfPlenty();
		//assertTrue(can);
	}
	
	@Test
	public void testDoYearOfPlenty() {
		
		m.doYearOfPlenty();
	}
	
	@Test
	public void testCanRoadBuildingCard() throws InvalidActionException {
		
		//test with empty hand
		boolean can = m.canRoadBuildingCard();
		assertFalse(can);
		
		//test with roadBuilding card in hand
		Player currentPlayer = m.getCatanModel().getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.ROAD_BUILD);
		can = m.canRoadBuildingCard();
		assertTrue(can);
	}
	
	@Test
	public void testDoRoadBuildingCard() {
		
		m.doRoadBuildCard();
	}
	
	@Test
	public void testCanSoldier() throws InvalidActionException {
		
		//test with empty hand
		boolean can = m.canSoldier();
		assertFalse(can);
				
		//test with soldier card in hand
		Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.SOLDIER);
		can = m.canSoldier();
		assertTrue(can);
	}
	
	@Test
	public void testDoSoldier() {
		
		m.doSoldier();
	}
	
	@Test
	public void testCanMonopoly() throws InvalidActionException {
		
		//test with empty hand
		boolean can = m.canMonopoly();
		assertFalse(can);
						
		//test with monopoly card in hand
		Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.MONOPOLY);
		can = m.canMonopoly();
		assertTrue(can);
	}
	
	@Test
	public void testDoMonopoly() {
		
		m.doMonopoly();
	}
	
	@Test
	public void testCanMonument() throws InvalidActionException {
		
		//test with empty hand
		boolean can = m.canMonument();
		assertFalse(can);
						
		//test with monument card in hand
		Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		DevCardList hand = currentPlayer.getOldDevCards();
		DevCardList bank = m.getCatanModel().getBank().getDevCards();
		bank = new DevCardList(1,1,1);
		bank.transferCardTo(hand, DevCardType.MONUMENT);
		can = m.canMonument();
		assertTrue(can);
	}
	
	@Test
	public void testDoMonument() {
		
		m.doMonument();
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
		bank.transferTo(hand, ResourceType.BRICK, 1);
		bank.transferTo(hand, ResourceType.WHEAT, 1);
		bank.transferTo(hand, ResourceType.WOOD, 1);
		bank.transferTo(hand, ResourceType.SHEEP, 1);
		bank.transferTo(hand, ResourceType.ORE, 1);
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
		bank.transferTo(hand, ResourceType.BRICK, 1);
		bank.transferTo(hand, ResourceType.WHEAT, 1);
		bank.transferTo(hand, ResourceType.WOOD, 1);
		bank.transferTo(hand, ResourceType.SHEEP, 1);
		bank.transferTo(hand, ResourceType.ORE, 1);
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
