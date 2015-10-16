package client.map;

import shared.definitions.PieceType;
import shared.exceptions.InvalidActionException;

public class YourTurnState extends MapControllerState {

	public YourTurnState(MapController controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see client.map.MapControllerState#playSoldierCard()
	 */
	@Override
	public MapControllerState playSoldierCard() throws InvalidActionException {
		// TODO: play the soldier card
		return new MoveRobberState(getController());
	}

	/* (non-Javadoc)
	 * @see client.map.MapControllerState#playRoadBuildingCard()
	 */
	@Override
	public MapControllerState playRoadBuildingCard()
			throws InvalidActionException {
		// TODO server interaction
		return new BuildFreeRoadsState(2, getController());
	}

	/* (non-Javadoc)
	 * @see client.map.MapControllerState#startMove(shared.definitions.PieceType, boolean, boolean)
	 */
	@Override
	public MapControllerState startMove(PieceType pieceType, boolean isFree,
			boolean allowDisconnected) throws InvalidActionException {
		// TODO Auto-generated method stub
		return super.startMove(pieceType, isFree, allowDisconnected);
	}
	
	

}
