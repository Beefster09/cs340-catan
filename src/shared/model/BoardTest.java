package shared.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.definitions.MunicipalityType;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public class BoardTest {
	
	private Board board;
	private PlayerReference red, green, blue;

	private List<Hex> hexList;
	private List<Port> portList;
	private List<Road> roadList;
	private List<Municipality> townList;
	
	private static final ResourceType[] resources = {
		ResourceType.WHEAT,
		
		ResourceType.BRICK,
		ResourceType.SHEEP,
		null, // Desert
		ResourceType.ORE,
		ResourceType.WOOD,
		ResourceType.SHEEP,

		ResourceType.WHEAT,
		ResourceType.WOOD,
		ResourceType.WOOD,
		ResourceType.ORE,
		ResourceType.BRICK,
		ResourceType.WHEAT,
		ResourceType.SHEEP,
		ResourceType.BRICK,
		ResourceType.SHEEP,
		ResourceType.WHEAT,
		ResourceType.ORE,
		ResourceType.WOOD
	};
	
	private static final int[] numbers = {
		4,
		9, 5, 0, 11, 6, 3,
		4, 11, 2, 6, 10, 3, 8, 9, 12, 10, 5, 8
	};

	// All tests should be on the same board.
	@Before
	public void setUp() throws Exception {
		hexList = new ArrayList<Hex>();
		portList = new ArrayList<Port>();
		roadList = new ArrayList<Road>();
		townList = new ArrayList<Municipality>();
		
		red = new PlayerReference(null, 0);
		green = new PlayerReference(null, 1);
		blue = new PlayerReference(null, 2);
		
		int hexIndex=0;
		for (HexLocation location: HexLocation.locationsWithinRadius(2)) {
			hexList.add(new Hex(location, resources[hexIndex], numbers[hexIndex]));
			++hexIndex;
		}

		portList.add(new Port(new EdgeLocation( 0,-2, EdgeDirection.North), ResourceType.WOOD));
		portList.add(new Port(new EdgeLocation( 1,-2, EdgeDirection.NorthEast), ResourceType.WHEAT));
		portList.add(new Port(new EdgeLocation( 2,-1, EdgeDirection.NorthEast), null));
		portList.add(new Port(new EdgeLocation( 2, 0, EdgeDirection.SouthEast), ResourceType.SHEEP));
		portList.add(new Port(new EdgeLocation( 1, 1, EdgeDirection.South), null));
		portList.add(new Port(new EdgeLocation(-1, 2, EdgeDirection.South), ResourceType.BRICK));
		portList.add(new Port(new EdgeLocation(-2, 2, EdgeDirection.SouthWest), null));
		portList.add(new Port(new EdgeLocation(-2, 1, EdgeDirection.NorthWest), ResourceType.ORE));
		portList.add(new Port(new EdgeLocation(-1,-1, EdgeDirection.NorthWest), null));
		
		roadList.add(new Road(new EdgeLocation( 0,-1, EdgeDirection.NorthEast), red));		
		roadList.add(new Road(new EdgeLocation( 0, 0, EdgeDirection.SouthEast), red));		
		roadList.add(new Road(new EdgeLocation( 0, 0, EdgeDirection.South),     red));		
		roadList.add(new Road(new EdgeLocation( 0, 0, EdgeDirection.SouthWest), red));			
		roadList.add(new Road(new EdgeLocation(-1, 1, EdgeDirection.SouthEast), red));
		townList.add(new Municipality(new VertexLocation(-1, 2, VertexDirection.NorthEast),
				MunicipalityType.SETTLEMENT, red));
		townList.add(new Municipality(new VertexLocation( 1,-2, VertexDirection.West),
				MunicipalityType.SETTLEMENT, red));
		townList.add(new Municipality(new VertexLocation( 0, 0, VertexDirection.East),
				MunicipalityType.CITY, red));

		roadList.add(new Road(new EdgeLocation(-1, 1, EdgeDirection.North),     green));
		roadList.add(new Road(new EdgeLocation(-1, 1, EdgeDirection.NorthWest), green));
		roadList.add(new Road(new EdgeLocation(-1, 0, EdgeDirection.SouthWest), green));
		roadList.add(new Road(new EdgeLocation( 1,-1, EdgeDirection.SouthEast), green));
		roadList.add(new Road(new EdgeLocation(-2, 0, EdgeDirection.NorthEast), green));
		roadList.add(new Road(new EdgeLocation(-2, 0, EdgeDirection.SouthEast), green));
		roadList.add(new Road(new EdgeLocation(-1, 1, EdgeDirection.SouthWest), green));
		roadList.add(new Road(new EdgeLocation(-1, 1, EdgeDirection.South),     green));
		roadList.add(new Road(new EdgeLocation(-1,-1, EdgeDirection.NorthWest), green));
		townList.add(new Municipality(new VertexLocation(-2, 1, VertexDirection.NorthEast),
				MunicipalityType.SETTLEMENT, green));
		townList.add(new Municipality(new VertexLocation(-2, 1, VertexDirection.SouthEast),
				MunicipalityType.SETTLEMENT, green));
		townList.add(new Municipality(new VertexLocation( 1,-1, VertexDirection.East),
				MunicipalityType.SETTLEMENT, green));

		roadList.add(new Road(new EdgeLocation( 2, 0, EdgeDirection.North),     blue));
		roadList.add(new Road(new EdgeLocation( 2, 0, EdgeDirection.NorthEast), blue));
		roadList.add(new Road(new EdgeLocation( 2, 0, EdgeDirection.SouthEast), blue));
		roadList.add(new Road(new EdgeLocation( 2,-1, EdgeDirection.SouthEast), blue));
		roadList.add(new Road(new EdgeLocation( 1, 0, EdgeDirection.North),     blue));
		roadList.add(new Road(new EdgeLocation( 1, 0, EdgeDirection.NorthEast), blue));
		townList.add(new Municipality(new VertexLocation(-1,-1, VertexDirection.SouthEast),
				MunicipalityType.SETTLEMENT, blue));
		townList.add(new Municipality(new VertexLocation( 2,-1, VertexDirection.East),
				MunicipalityType.SETTLEMENT, blue));
		townList.add(new Municipality(new VertexLocation( 2, 0, VertexDirection.NorthWest),
				MunicipalityType.SETTLEMENT, blue));
		townList.add(new Municipality(new VertexLocation( 2, 0, VertexDirection.SouthEast),
				MunicipalityType.CITY, blue));
		
		board = new Board(2, hexList, portList, roadList, townList, new HexLocation(1,1));
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testConstructor() {
		// Make sure the stock/test board is constructed correctly
		int hexIndex=0;
		for (HexLocation location: HexLocation.locationsWithinRadius(2)) {
			Hex hex = board.getHexAt(location);
			assertEquals(numbers[hexIndex], hex.getNumber());
			assertEquals(resources[hexIndex], hex.getResource());
			++hexIndex;
		}

		Collection<Port> ports = board.getPorts();
		Collection<Road> roads = board.getRoads();
		Collection<Municipality> towns = board.getMunicipalities();

		// Check that there are the right number of ports, roads, and towns
		assertEquals(portList.size(), ports.size());
		assertEquals(roadList.size(), roads.size());
		assertEquals(townList.size(), towns.size());
		
		assertEquals(ResourceType.WOOD, board.getPortAt(new EdgeLocation( 0,-2, EdgeDirection.North)).getResource());
		assertEquals(ResourceType.WHEAT, board.getPortAt(new EdgeLocation( 1,-2, EdgeDirection.NorthEast)).getResource());
		assertEquals(null, board.getPortAt(new EdgeLocation( 2,-1, EdgeDirection.NorthEast)).getResource());
		assertEquals(ResourceType.SHEEP, board.getPortAt(new EdgeLocation( 2, 0, EdgeDirection.SouthEast)).getResource());
		assertEquals(null, board.getPortAt(new EdgeLocation( 1, 1, EdgeDirection.South)).getResource());
		assertEquals(ResourceType.BRICK, board.getPortAt(new EdgeLocation(-1, 2, EdgeDirection.South)).getResource());
		assertEquals(null, board.getPortAt(new EdgeLocation(-2, 2, EdgeDirection.SouthWest)).getResource());
		assertEquals(ResourceType.ORE, board.getPortAt(new EdgeLocation(-2, 1, EdgeDirection.NorthWest)).getResource());
		assertEquals(null, board.getPortAt(new EdgeLocation(-1,-1, EdgeDirection.NorthWest)).getResource());
		
		// TODO test for fails
	}
	
	@Test
	public void testGetHexesByNumber() {
		assertEquals(1, board.getHexesByNumber(2).size());
		assertEquals(1, board.getHexesByNumber(12).size());
		
		for (int i=3; i<=11; ++i) {
			if (i == 7) continue;
			assertEquals(2, board.getHexesByNumber(i).size());
		}
		
		// TODO: this needs work.
	}

	@Test
	public void testGetPortAtVertexLocation() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPortOwner() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOwnerOfPortAt() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanBuildRoadAt() {
		assertFalse(board.canBuildRoadAt(red,
				new EdgeLocation( 0, 0, EdgeDirection.South))); // already your road there
		assertFalse(board.canBuildRoadAt(red,
				new EdgeLocation(-1, 0, EdgeDirection.South))); // already opponent's road there
		assertFalse(board.canBuildRoadAt(green,
				new EdgeLocation(-1,-2, EdgeDirection.SouthWest))); // just outside board, but still connected

		assertTrue(board.canBuildRoadAt(green,
				new EdgeLocation( 0, 0, EdgeDirection.NorthWest)));
		assertTrue(board.canBuildRoadAt(red,
				new EdgeLocation( 0, 0, EdgeDirection.NorthWest)));
		assertTrue(board.canBuildRoadAt(blue,
				new EdgeLocation( 0,-1, EdgeDirection.NorthWest))); // No road, only a settlement (can't technically happen... whatever)
		
		assertFalse(board.canBuildRoadAt(green,
				new EdgeLocation( 0, 1, EdgeDirection.SouthWest))); // opponent's settlement is in the way
	}

	@Test
	public void testCanBuildSettlement() {
		assertFalse(board.canBuildSettlement(red, 
				new VertexLocation(-1, 1, VertexDirection.SouthEast))); // already your settlement there
		assertFalse(board.canBuildSettlement(red, 
				new VertexLocation(-1, 10, VertexDirection.SouthEast))); // outside the board
		assertFalse(board.canBuildSettlement(red, 
				new VertexLocation(0,0, VertexDirection.East))); // already your city there
		assertFalse(board.canBuildSettlement(blue, 
				new VertexLocation(0,0, VertexDirection.East))); // already another player's city there
		assertFalse(board.canBuildSettlement(red, 
				new VertexLocation(1,0, VertexDirection.SouthWest))); // no roads to location
		assertFalse(board.canBuildSettlement(green, 
				new VertexLocation(1,0, VertexDirection.NorthEast))); // distance rule
		assertFalse(board.canBuildSettlement(blue, 
				new VertexLocation(0,0, VertexDirection.West))); // roads belong to another player
		assertFalse(board.canBuildSettlement(red, 
				new VertexLocation(-1,-1, VertexDirection.West))); // roads belong to another player
		
		assertTrue(board.canBuildSettlement(green, 
				new VertexLocation(0,0, VertexDirection.West)));
		assertTrue(board.canBuildSettlement(red, 
				new VertexLocation(0,0, VertexDirection.West)));
		assertTrue(board.canBuildSettlement(green, 
				new VertexLocation(-1,-1, VertexDirection.West)));
	}

	@Test
	public void testCanBuildCity() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanMoveRobberTo() {
		assertFalse(board.canMoveRobberTo(new HexLocation(1, 0))); // Desert
		assertFalse(board.canMoveRobberTo(new HexLocation(1, 1))); // Same place as before
		assertFalse(board.canMoveRobberTo(new HexLocation(20, 20))); // Off the board

		assertTrue(board.canMoveRobberTo(new HexLocation(2, 0)));
		assertTrue(board.canMoveRobberTo(new HexLocation(0,-2)));
	}

}
