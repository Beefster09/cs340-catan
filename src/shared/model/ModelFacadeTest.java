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

	private CatanModel model1;
	private ModelFacade m;

	
	@Before
	public void setup() throws IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		Reader r = new BufferedReader(new FileReader("json_test.json"));
		Object parseResult = parser.parse(r);
		JSONObject model = ((JSONObject) parseResult);
		
		model1 = new CatanModel();
		m = new ModelFacade(model1);
		m.updateFromJSON(model);
	}
	
	@Test
	public void testUpdateFromJSON() {
		
		JSONObject json = new JSONObject();
		m.updateFromJSON(json);
	}
	
	@Test
	public void testCanRoll() {
			
		CatanModel model = new CatanModel();
		
		PlayerReference player = new PlayerReference(model, 1);
		boolean can = m.canRoll(player);
	}
	
	@Test
	public void testCanRob() {
		
		HexLocation hexLoc = new HexLocation(0, 0);
		boolean can = m.canRob(hexLoc);
	}
	
	@Test 
	public void testDoRob() {
		
		m.doRob();
	}
	
	@Test
	public void testCanFinishTurn() {
		
		boolean can = m.canFinishTurn();
		
		assertFalse(can);
	}
	
	@Test
	public void testDoFinishTurn() {
		
		boolean did = m.doFinishTurn();
	}
	
	@Test
	public void testCanBuyDevelopmentCard() {
		
		boolean can = m.canBuyDevelopmentCard();
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
	}
	
	@Test
	public void testDoBuildSettlement() {
		
		HexLocation hexLoc = new HexLocation(0, 0);
		VertexLocation vertexLoc = new VertexLocation(hexLoc, VertexDirection.East);
		boolean did = m.doBuildSettlement(vertexLoc);
	}
	
	@Test
	public void testCanYearOfPlenty() {
		
		boolean can = m.canYearOfPlenty();
	}
	
	@Test
	public void testDoYearOfPlenty() {
		
		boolean did = m.doYearOfPlenty();
	}
	
	@Test
	public void testCanRoadBuildingCard() {
		
		boolean can = m.canRoadBuildingCard();
	}
	
	@Test
	public void testDoRoadBuildingCard() {
		
		boolean did = m.doRoadBuildCard();
	}
	
	@Test
	public void testCanSoldier() {
		
		boolean can = m.canSoldier();
	}
	
	@Test
	public void testDoSoldier() {
		
		boolean did = m.doSoldier();
	}
	
	@Test
	public void testCanMonopoly() {
		
		boolean can = m.canMonopoly();
	}
	
	@Test
	public void testDoMonopoly() {
		
		boolean did = m.doMonopoly();
	}
	
	@Test
	public void testCanMonument() {
		
		boolean can = m.canMonument();
	}
	
	@Test
	public void testDoMonument() {
		
		boolean did = m.doMonument();
	}
	
	@Test
	public void testCanOfferTrade() {
		
		boolean can = m.canOfferTrade();
	}
	
	@Test
	public void testDoOfferTrade() {
		
		boolean did = m.doOfferTrade();
	}
	
	@Test
	public void testCanAcceptTrade() {
		
		boolean can = m.canAcceptTrade();
	}
	
	@Test
	public void testDoAcceptTrade() {
		
		boolean did = m.doAcceptTrade();
	}
	
	@Test
	public void testCanMaritimeTrade() {
		
		boolean can = m.canMaritimeTrade();
	}
	
	@Test
	public void testDoMaritimeTrade() {
		
		boolean did = m.doMaritimeTrade();
	}
	
	@Test
	public void testCanDiscardCards() {
		
		boolean can = m.canDiscardCards();
	}
	
	@Test
	public void testDoDiscardCards() {
		
		boolean did = m.doDiscardCards();
	}
}
