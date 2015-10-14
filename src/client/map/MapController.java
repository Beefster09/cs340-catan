package client.map;

import java.util.*;

import shared.definitions.*;
import shared.exceptions.InvalidActionException;
import shared.locations.*;
import shared.model.ModelFacade;
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
		
		Random rand = new Random();

		
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
			state.placeRoad(edgeLoc);
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeSettlement(VertexLocation vertLoc) {
		
		try {
			state.placeSettlement(vertLoc);
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeCity(VertexLocation vertLoc) {
		try {
			state.placeCity(vertLoc);
		}
		catch (InvalidActionException e) {
			
		}
	}

	public void placeRobber(HexLocation hexLoc) {
		try {
			state.placeRobber(hexLoc);
			getRobView().showModal();
		}
		catch (InvalidActionException e) {
			
		}		
	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {	
		
		getView().startDrop(pieceType, CatanColor.ORANGE, true);
		
		// TODO: set state
	}
	
	public void cancelMove() {
		
	}
	
	public void playSoldierCard() {	
		try {
			state.playSoldierCard();
		}
		catch (InvalidActionException e) {
			
		}
	}
	
	public void playRoadBuildingCard() {	
		try {
			state.playRoadBuildingCard();
		}
		catch (InvalidActionException e) {
			
		}
	}
	
	public void robPlayer(RobPlayerInfo victim) {	
		try {
			state.robPlayer(victim);
		}
		catch (InvalidActionException e) {
			
		}
	}
	
}

