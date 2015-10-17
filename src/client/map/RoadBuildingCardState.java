package client.map;

import shared.exceptions.InvalidActionException;
import shared.locations.EdgeLocation;

public class RoadBuildingCardState extends MapControllerState {
	
	private EdgeLocation firstRoad = null;

	public RoadBuildingCardState(MapController controller) {
		super(controller);
	}

	@Override
	public MapControllerState placeRoad(EdgeLocation edge)
			throws InvalidActionException {
		if (firstRoad == null) {
			firstRoad = edge;
			getView().placeRoad(edge, getYourColor());
			return this;
		}
		else {
			getView().placeRoad(edge, getYourColor());
			// TODO: build the roads via road building action
			return new YourTurnState(getController());
		}
	}

	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		if (firstRoad == null) {
			return new YourTurnState(getController());
		}
		else {
			firstRoad = null;
			return this;
		}
	}

	@Override
	public boolean canPlaceRoad(EdgeLocation loc) {
		if (firstRoad == null) {
			return getModel().canBuildRoad(loc);
		}
		else {
			return getModel().canBuild2Roads(firstRoad, loc);
		}
	}

}
