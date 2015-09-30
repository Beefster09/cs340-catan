package shared.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

public class BoardTest {
	
	private Board board;

	List<Hex> hexList;
	List<Port> portList;
	List<Road> roadList;
	List<Municipality> townList;
	
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
	}
	
	@Test
	public void testGetHexesByNumber() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	@Test
	public void testCanBuildSettlement() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanBuildCity() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanMoveRobberTo() {
		fail("Not yet implemented");
	}

}
