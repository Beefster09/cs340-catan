package client.map;

import shared.definitions.PieceType;
import shared.exceptions.InvalidActionException;

public class YourTurnState extends MapControllerState {

	public YourTurnState(MapController controller) {
		super(controller);
	}

	@Override
	public MapControllerState playSoldierCard() throws InvalidActionException {
		return new MoveRobberState(getController());
	}

	@Override
	public MapControllerState playRoadBuildingCard()
			throws InvalidActionException {
		getView().startDrop(PieceType.ROAD, getYourColor(), true);
		return new RoadBuildingCardState(getController());
	}

	@Override
	public MapControllerState startMove(PieceType pieceType, boolean isFree,
			boolean allowDisconnected) throws InvalidActionException {
		MapControllerState nextState;
		boolean cancelable = true;
		switch(pieceType) {
		case ROAD:
			nextState = new BuildRoadState(getController());
			break;
		case SETTLEMENT:
			if (isFree && allowDisconnected) {
				cancelable = false;
				nextState = new BuildStartingSettlementState(getController());
			}
			else {
				nextState = new BuildSettlementState(getController());
			}
			break;
		case CITY:
			nextState = new BuildCityState(getController());
			break;
		case ROBBER:
			cancelable = false;
			nextState = new MoveRobberState(getController(), false);
			break;
		default:
			throw new IllegalArgumentException("Invalid piece type.");
		}
		getView().startDrop(pieceType, getYourColor(), cancelable);
		return nextState;
	}
	
	

}
