package client.map;

import java.util.*;

import shared.communication.IServer;
import shared.definitions.*;
import shared.exceptions.InvalidActionException;
import shared.locations.*;
import shared.model.*;
import client.base.*;
import client.communication.ClientManager;
import client.data.*;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController {
	
	private IRobView robView;
	
	private MapControllerState state;
	
	public MapController(IMapView view, IRobView robView) {
		
		super(view);
		
		setRobView(robView);
		
		initFromModel();
		
		// Default state until you can control things on the map.
		// Active before the game starts and when it isn't your turn.
		// It does NOTHING but throw exceptions. Always.
		state = new NullState(this);
		
		getModel().registerListener(listener);
	}

	IModelListener listener = new AbstractModelListener() {

		@Override
		public void mapChanged(Board newMap) {
			
			// Assume (for now) that only pieces will change
			refreshPieces();
		}

		@Override
		public void turnChanged(TurnTracker turnTracker) {
			if (isYourTurn()) {
				state = new YourTurnState(MapController.this);
			}
			else {
				state = new NullState(MapController.this);
			}
		}
		
	};
	
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
		return ClientManager.getServer();
	}
	
	public CatanColor getYourColor() {
		return ClientManager.getLocalPlayer().getPlayer().getColor();
	}
	
	private boolean isYourTurn() {
		return ClientManager.getLocalPlayer().equals(getModel().getCurrentPlayer());
	}
	
	protected void initFromModel() {

		buildBoard(getModel().getCatanModel().getMap());
		placePieces(getModel().getCatanModel().getMap());
		
	}

	private void buildBoard(Board board) {
		IMapView view = getView();
		
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
	}
		
	private void placePieces(Board board) {
		IMapView view = getView();
		
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
			getView().placeRoad(edgeLoc, getYourColor());
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeSettlement(VertexLocation vertLoc) {
		
		try {
			state = state.placeSettlement(vertLoc);
			getView().placeSettlement(vertLoc, getYourColor());
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeCity(VertexLocation vertLoc) {
		try {
			state = state.placeCity(vertLoc);
			getView().placeCity(vertLoc, getYourColor());
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
		return ClientManager.getModel();
	}

	public PlayerReference getYourself() {
		return ClientManager.getLocalPlayer();
	}

	public void robDialog() {
		getRobView().showModal();
	}

	public void refreshPieces() {
		getView().removeAllPieces();
		
		placePieces(getModel().getCatanModel().getMap());
	}
	
}

