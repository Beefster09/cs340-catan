package shared.model;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public class ModelFacadeTest {

	private CatanModel model;
	private ModelFacade m;

	
	@Before
	public void setup() throws IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		Reader r = new BufferedReader(new FileReader("json_test.json"));
		Object parseResult = parser.parse(r);
		JSONObject json = ((JSONObject) parseResult);
		
		model = new CatanModel();
		m = new ModelFacade(model);
		m.updateFromJSON(json);
	}
	
	@Test
	public void testUpdateFromJSON() {
		
		
		//m.updateFromJSON(json);
	}
	
	@Test
	public void testCanRoll() {
			
		CatanModel model = new CatanModel();
		
		
		//test if it's players current turn and he hasn't rolled
		PlayerReference currentPlayer = model.getTurnTracker().getCurrentPlayer();
		boolean can = m.canRoll(currentPlayer);
		assertTrue(can);
		
		//test if it is player's turn but he has already rolled
		m.doRoll(currentPlayer);
		can = m.canRoll(currentPlayer);
		assertFalse(can);
		
		//test if its not player's current turn
		PlayerReference otherPlayer = new PlayerReference(m.getCatanModel(), 3);
		can = m.canRoll(otherPlayer);
		assertFalse(can);
		}
	
	@Test
	public void testCanRob() {
		
		//test if hex is desert
		HexLocation hexLoc = new HexLocation(0, 0);
		boolean can = m.canRob(hexLoc);
		
		//test if hex is anything else
	}
	
	@Test 
	public void testDoRob() {
		
		m.doRob();
	}
	
	@Test
	public void testCanFinishTurn() {
		
		//test if current player has not rolled
		boolean can = m.canFinishTurn();
		assertFalse(can);
		
		//test if current player has rolled
	}
	
	@Test
	public void testDoFinishTurn() {
		
		boolean did = m.doFinishTurn();
	}
	
	@Test
	public void testCanBuyDevelopmentCard() {
		
		
		//test empty hand
		boolean can = m.canBuyDevelopmentCard();
		assertFalse(can);
		
		//test hand with sufficient resources
	}
	
	@Test
	public void testDoBuyDevelopmentCard() {
		
		boolean did = m.doBuyDevelopmentCard();
	}
	
	@Test
	public void testCanBuildRoad() {
		
		
		HexLocation hexLoc = new HexLocation(0, 0);
		EdgeLocation edgeLoc = new EdgeLocation(hexLoc, EdgeDirection.North);
		boolean can = m.canBuildRoad(edgeLoc);
		
		//test no connecting municipality or road
		assertFalse(can);
		
		//test connecting road
		
		//test connecting municipality
	}

	@Test
	public void testDoBuildRoad() {
		
		boolean did = m.doBuildRoad();
	}
	
	@Test
	public void testCanBuildSettlement() {
		
		HexLocation hexLoc = new HexLocation(0, 0);
		VertexLocation vertexLoc = new VertexLocation(hexLoc, VertexDirection.East);
		boolean can = m.canBuildSettlement(vertexLoc);
		
		//test no connection
		assertFalse(can);
		
		//test with connecting road, but adjacent municiaplity
		
		//test connecting road
	}
	
	@Test
	public void testDoBuildSettlement() {
		
		HexLocation hexLoc = new HexLocation(0, 0);
		VertexLocation vertexLoc = new VertexLocation(hexLoc, VertexDirection.East);
		boolean did = m.doBuildSettlement(vertexLoc);
	}
	
	@Test
	public void testCanYearOfPlenty() {
		
		//test with empty hand
		boolean can = m.canYearOfPlenty();
		assert(false);
		
		//test with yearOfPlenty card in hand
	}
	
	@Test
	public void testDoYearOfPlenty() {
		
		boolean did = m.doYearOfPlenty();
	}
	
	@Test
	public void testCanRoadBuildingCard() {
		
		//test with empty hand
		boolean can = m.canRoadBuildingCard();
		assert(false);
		
		//test with roadBuilding card in hand
	}
	
	@Test
	public void testDoRoadBuildingCard() {
		
		boolean did = m.doRoadBuildCard();
	}
	
	@Test
	public void testCanSoldier() {
		
		//test with empty hand
		boolean can = m.canSoldier();
		assert(false);
		
		//test with soldier card in hand
	}
	
	@Test
	public void testDoSoldier() {
		
		boolean did = m.doSoldier();
	}
	
	@Test
	public void testCanMonopoly() {
		
		//Player currentPlayer = model.getTurnTracker().getCurrentPlayer().getPlayer();
		//DevCardList devCards = new DevCardList(0, 0, 1); 
		//currentPlayer.setOldDevCards(devCards);
		
		//test with empty hand
		boolean can = m.canMonopoly();
		assert(false);
		
		//test with monopoly card in hand
	}
	
	@Test
	public void testDoMonopoly() {
		
		boolean did = m.doMonopoly();
	}
	
	@Test
	public void testCanMonument() {
		
		//test with empty hand
		boolean can = m.canMonument();
		assert(false);
		
		//test with monument card in hand
	}
	
	@Test
	public void testDoMonument() {
		
		boolean did = m.doMonument();
	}
	
	@Test
	public void testCanOfferTrade() {
		//test with insufficient cards in hand for trade
		
		//test with sufficient cards in hand for trade
		boolean can = m.canOfferTrade();
	}
	
	@Test
	public void testDoOfferTrade() {
		
		boolean did = m.doOfferTrade();
	}
	
	@Test
	public void testCanAcceptTrade() {
		
		//test with insufficient cards in hand for trade
		
		//test with sufficient cards in hand for trade
		
		boolean can = m.canAcceptTrade();
	}
	
	@Test
	public void testDoAcceptTrade() {
		
		boolean did = m.doAcceptTrade();
	}
	
	@Test
	public void testCanMaritimeTrade() {
		
		//test without municipality on port
		boolean can = m.canMaritimeTrade();
		assert(false);
		
		//test with municipality on port
	}
	
	@Test
	public void testDoMaritimeTrade() {
		
		boolean did = m.doMaritimeTrade();
	}
	
	@Test
	public void testCanDiscardCards() {
		
		//still don't know how to do this one
		boolean can = m.canDiscardCards();
	}
	
	@Test
	public void testDoDiscardCards() {
		
		boolean did = m.doDiscardCards();
	}
}
