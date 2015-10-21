package client.map;

import java.util.*;

import shared.communication.IServer;
import shared.definitions.*;
import shared.exceptions.InvalidActionException;
import shared.locations.*;
import shared.model.*;
import client.base.*;
import client.data.*;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController {
	
	private IRobView robView;
	private IServer server;
	private ModelFacade model;
	private PlayerReference you;
	
	private MapControllerState state;
	
	public MapController(IMapView view, IRobView robView, IServer server,
			ModelFacade model, PlayerReference you) {
		
		super(view);
		
		setRobView(robView);
		setServer(server);
		
		this.model = model;
		this.you = you;
		
		initFromModel();
		
		// Default state until you can control things on the map.
		// Active before the game starts and when it isn't your turn.
		// It does NOTHING but throw exceptions. Always.
		state = new NullState(this);
	}
	
	public MapController(IMapView view, IRobView robView) {
		
		super(view);
		
		setRobView(robView);
		
		initHardCoded();
	}

	protected void initHardCoded() {
		
		//<temp>
		
		Random rand = new Random();

		for (int x = 0; x <= 3; ++x) {
			
			int maxY = 3 - x;			
			for (int y = -3; y <= maxY; ++y) {				
				int r = rand.nextInt(HexType.values().length);
				HexType hexType = HexType.values()[r];
				HexLocation hexLoc = new HexLocation(x, y);
				getView().addHex(hexLoc, hexType);
				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
						CatanColor.RED);
				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
						CatanColor.BLUE);
				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
						CatanColor.ORANGE);
				getView().placeSettlement(new VertexLocation(hexLoc,  VertexDirection.NorthWest), CatanColor.GREEN);
				getView().placeCity(new VertexLocation(hexLoc,  VertexDirection.NorthEast), CatanColor.PURPLE);
			}
			
			if (x != 0) {
				int minY = x - 3;
				for (int y = minY; y <= 3; ++y) {
					int r = rand.nextInt(HexType.values().length);
					HexType hexType = HexType.values()[r];
					HexLocation hexLoc = new HexLocation(-x, y);
					getView().addHex(hexLoc, hexType);
					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
							CatanColor.RED);
					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
							CatanColor.BLUE);
					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
							CatanColor.ORANGE);
					getView().placeSettlement(new VertexLocation(hexLoc,  VertexDirection.NorthWest), CatanColor.GREEN);
					getView().placeCity(new VertexLocation(hexLoc,  VertexDirection.NorthEast), CatanColor.PURPLE);
				}
			}
		}
		
		PortType portType = PortType.BRICK;
		getView().addPort(new EdgeLocation(new HexLocation(0, 3), EdgeDirection.North), portType);
		getView().addPort(new EdgeLocation(new HexLocation(0, -3), EdgeDirection.South), portType);
		getView().addPort(new EdgeLocation(new HexLocation(-3, 3), EdgeDirection.NorthEast), portType);
		getView().addPort(new EdgeLocation(new HexLocation(-3, 0), EdgeDirection.SouthEast), portType);
		getView().addPort(new EdgeLocation(new HexLocation(3, -3), EdgeDirection.SouthWest), portType);
		getView().addPort(new EdgeLocation(new HexLocation(3, 0), EdgeDirection.NorthWest), portType);
		
		getView().placeRobber(new HexLocation(0, 0));
		
		getView().addNumber(new HexLocation(-2, 0), 2);
		getView().addNumber(new HexLocation(-2, 1), 3);
		getView().addNumber(new HexLocation(-2, 2), 4);
		getView().addNumber(new HexLocation(-1, 0), 5);
		getView().addNumber(new HexLocation(-1, 1), 6);
		getView().addNumber(new HexLocation(1, -1), 8);
		getView().addNumber(new HexLocation(1, 0), 9);
		getView().addNumber(new HexLocation(2, -2), 10);
		getView().addNumber(new HexLocation(2, -1), 11);
		getView().addNumber(new HexLocation(2, 0), 12);
		
		//</temp>
	}

	// TODO: listener that affects the state of this object (player order)
	
	public IMapView getView() {
		
		return (IMapView)super.getView();
	}
	
	private IRobView getRobView() {
		return robView;
	}
	
	private void setRobView(IRobView robView) {
		this.robView = robView;
	}

	public IServer getServer() {
		return server;
	}

	private void setServer(IServer server) {
		this.server = server;
	}
	
	public CatanColor getYourColor() {
		return you.getPlayer().getColor();
	}
	
	private boolean isYourTurn() {
		return you.equals(model.getCurrentPlayer());
	}
	
	protected void initFromModel() {
		
		IMapView view = getView();
		
		Board board = model.getCatanModel().getMap();
		
		for (Hex hex : board.getHexes()) {
			HexType type = HexType.fromResourceType(hex.getResource());
			view.addHex(hex.getLocation(), type);
			if (type != HexType.DESERT) {
				view.addNumber(hex.getLocation(), hex.getNumber());
			}
		}
		
		for (Port port : board.getPorts()) {
			view.addPort(port.getLocation(), PortType.fromResourceType(port.getResource()));
		}
		
		for (Road road : board.getRoads()) {
			CatanColor color = road.getOwner().getPlayer().getColor();
			view.placeRoad(road.getLocation(), color);
		}
		
		for (Municipality town : board.getMunicipalities()) {
			CatanColor color = town.getOwner().getPlayer().getColor();
			switch (town.getType()) {
			case SETTLEMENT:
				view.placeSettlement(town.getLocation(), color);
				break;
			case CITY:
				view.placeCity(town.getLocation(), color);
				break;
			default:
				break;
			}
		}
		
		view.placeRobber(board.getRobberLocation());
		
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		return state.canPlaceRoad(edgeLoc);
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		return state.canPlaceSettlement(vertLoc);
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		return state.canPlaceCity(vertLoc);
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		return state.canMoveRobber(hexLoc);
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		
		try {
			state = state.placeRoad(edgeLoc);
			getView().placeRoad(edgeLoc, you.getPlayer().getColor());
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeSettlement(VertexLocation vertLoc) {
		
		try {
			state = state.placeSettlement(vertLoc);
			getView().placeSettlement(vertLoc, you.getPlayer().getColor());
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeCity(VertexLocation vertLoc) {
		try {
			state = state.placeCity(vertLoc);
			getView().placeCity(vertLoc, you.getPlayer().getColor());
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeRobber(HexLocation hexLoc) {
		try {
			state = state.placeRobber(hexLoc);
			getRobView().showModal();
		}
		catch (InvalidActionException e) {
			
		}		
	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {	
		try {
			state = state.startMove(pieceType, isFree, allowDisconnected);
			getView().startDrop(pieceType, CatanColor.ORANGE, true);
		}
		catch (InvalidActionException e) {
			
		}	
	}
	
	public void cancelMove() {
		try {
			state = state.cancelMove();
		}
		catch (InvalidActionException e) {
			
		}
	}
	
	public void playSoldierCard() {	
		try {
			state = state.playSoldierCard();
		}
		catch (InvalidActionException e) {
			
		}
	}
	
	public void playRoadBuildingCard() {	
		try {
			state = state.playRoadBuildingCard();
		}
		catch (InvalidActionException e) {
			
		}
	}
	
	public void robPlayer(RobPlayerInfo victim) {	
		try {
			state = state.robPlayer(victim);
		}
		catch (InvalidActionException e) {
			
		}
	}

	public ModelFacade getModel() {
		return model;
	}

	public PlayerReference getYourself() {
		return you;
	}

	public void robDialog() {
		getRobView().showModal();
	}
	
}

