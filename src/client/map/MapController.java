package client.map;

import java.util.*;

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
	private MapControllerState state;
	private ModelFacade model;
	
	public MapController(IMapView view, IRobView robView) {
		
		super(view);
		
		setRobView(robView);
		
		initFromModel();
	}
	
	public IMapView getView() {
		
		return (IMapView)super.getView();
	}
	
	private IRobView getRobView() {
		return robView;
	}
	
	private void setRobView(IRobView robView) {
		this.robView = robView;
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
		return true;
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		
		return true;
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		
		return true;
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		
		return true;
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		
		try {
			state = state.placeRoad(edgeLoc);
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeSettlement(VertexLocation vertLoc) {
		
		try {
			state = state.placeSettlement(vertLoc);
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeCity(VertexLocation vertLoc) {
		try {
			state = state.placeCity(vertLoc);
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
	
}

