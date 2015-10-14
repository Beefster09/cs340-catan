package client.map;

import client.data.RobPlayerInfo;
import shared.definitions.PieceType;
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

	/**
	 * @param edge
	 * @return the state to transition to after this runs
	 * @throws InvalidActionException
	 */
	public MapControllerState placeRoad(EdgeLocation edge) throws InvalidActionException {
		throw new InvalidActionException("You cannot build a road at this time.");
	}
	
	/**
	 * @param vertex
	 * @return the state to transition to after this runs
	 * @throws InvalidActionException
	 */
	public MapControllerState placeSettlement(VertexLocation vertex) throws InvalidActionException {
		throw new InvalidActionException("You cannot build a settlement at this time.");
	}
	
	/**
	 * @param vertex
	 * @return the state to transition to after this runs
	 * @throws InvalidActionException
	 */
	public MapControllerState placeCity(VertexLocation vertex) throws InvalidActionException {
		throw new InvalidActionException("You cannot build a city at this time.");
	}
	
	/**
	 * @param hex
	 * @return the state to transition to after this runs
	 * @throws InvalidActionException
	 */
	public MapControllerState placeRobber(HexLocation hex) throws InvalidActionException {
		throw new InvalidActionException("You cannot move the robber at this time.");
	}
	
	/**
	 * @return the state to transition to after this runs
	 * @throws InvalidActionException
	 */
	public MapControllerState playSoldierCard() throws InvalidActionException {	
		throw new InvalidActionException("You cannot play a soldier card at this time.");
	}
	
	/**
	 * @return the state to transition to after this runs
	 * @throws InvalidActionException
	 */
	public MapControllerState playRoadBuildingCard() throws InvalidActionException {	
		throw new InvalidActionException("You cannot play a road building card at this time.");
	}
	
	/**
	 * @param victim
	 * @return the state to transition to after this runs
	 * @throws InvalidActionException
	 */
	public MapControllerState robPlayer(RobPlayerInfo victim) throws InvalidActionException {	
		throw new InvalidActionException("You cannot rob at this time.");
	}
	
	/**
	 * @param pieceType
	 * @param isFree
	 * @param allowDisconnected
	 * @return the state to transition to after this runs
	 * @throws InvalidActionException
	 */
	public MapControllerState startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected)
			throws InvalidActionException {
		throw new InvalidActionException("You cannot start a move at this time.");
	}

	/**
	 * @return the state to transition to after this runs
	 * @throws InvalidActionException
	 */
	public MapControllerState cancelMove()
			throws InvalidActionException {
		throw new InvalidActionException("There is nothing to cancel.");
	}
}
