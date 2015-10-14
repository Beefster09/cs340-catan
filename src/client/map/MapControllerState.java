package client.map;

import client.data.RobPlayerInfo;
import shared.exceptions.InvalidActionException;
import shared.locations.*;
import shared.model.ModelFacade;

public abstract class MapControllerState {
	
	private ModelFacade model;
	
	public MapControllerState(ModelFacade model) {
		this.model = model;
	}

	public ModelFacade getModel() {
		return model;
	}

	public void placeRoad(EdgeLocation edge) throws InvalidActionException {
		throw new InvalidActionException("You cannot build a road at this time.");
	}
	
	public void placeSettlement(VertexLocation vertex) throws InvalidActionException {
		throw new InvalidActionException("You cannot build a settlement at this time.");
	}
	
	public void placeCity(VertexLocation vertex) throws InvalidActionException {
		throw new InvalidActionException("You cannot build a city at this time.");
	}
	
	public void placeRobber(HexLocation hex) throws InvalidActionException {
		throw new InvalidActionException("You cannot move the robber at this time.");
	}
	
	public void playSoldierCard() throws InvalidActionException {	
		throw new InvalidActionException("You cannot play a soldier card at this time.");
	}
	
	public void playRoadBuildingCard() throws InvalidActionException {	
		throw new InvalidActionException("You cannot play a road building card at this time.");
	}
	
	public void robPlayer(RobPlayerInfo victim) throws InvalidActionException {	
		throw new InvalidActionException("You cannot rob at this time.");
	}
	
}
