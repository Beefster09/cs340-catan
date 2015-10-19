package client.map;

import shared.definitions.CatanColor;
import shared.exceptions.InvalidActionException;
import shared.locations.VertexLocation;

public class BuildStartingSettlementState extends MapControllerState {

	public BuildStartingSettlementState(MapController controller) {
		super(controller);
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation loc) {
		return getModel().canBuildStartingSettlement(loc);
	}

	@Override
	public MapControllerState placeSettlement(VertexLocation vertex)
			throws InvalidActionException {
		getView().placeSettlement(vertex, getYourColor());
		return new BuildStartingRoadState(getController(), vertex);
	}

}
