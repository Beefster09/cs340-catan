package client.resources;

import java.util.*;

import shared.definitions.*;
import shared.model.*;

import client.base.*;
import client.misc.ClientManager;

/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements
		IResourceBarController {

	private Map<ResourceBarElement, IAction> elementActions;

	public ResourceBarController(IResourceBarView view) {

		super(view);

		elementActions = new HashMap<ResourceBarElement, IAction>();
	}

	@Override
	public IResourceBarView getView() {
		return (IResourceBarView) super.getView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shared.model.AbstractModelListener#playersChanged(java.util.List)
	 */
	@Override
	public void playersChanged(List<Player> players) {
		Player localPlayer = ClientManager.getLocalPlayer().getPlayer();

		// Set the counts
		ResourceList resources = localPlayer.getResources();
		
		IResourceBarView view = getView();

		view.setElementAmount(ResourceBarElement.BRICK,
				resources.count(ResourceType.BRICK));
		view.setElementAmount(ResourceBarElement.WOOD,
				resources.count(ResourceType.WOOD));
		view.setElementAmount(ResourceBarElement.WHEAT,
				resources.count(ResourceType.WHEAT));
		view.setElementAmount(ResourceBarElement.ORE,
				resources.count(ResourceType.ORE));
		view.setElementAmount(ResourceBarElement.SHEEP,
				resources.count(ResourceType.SHEEP));

		int numRoads = localPlayer.getRoads();
		int numSettlements = localPlayer.getSettlements();
		int numCities = localPlayer.getCities();
		
		view.setElementAmount(ResourceBarElement.ROAD, numRoads);
		view.setElementAmount(ResourceBarElement.SETTLEMENT, numSettlements);
		view.setElementAmount(ResourceBarElement.CITY, numCities);

		if (numRoads >= 1
				&& resources.count(ResourceType.BRICK) >= 1
				&& resources.count(ResourceType.WOOD) >= 1) {
			view.setElementEnabled(ResourceBarElement.ROAD, true);
		}
		else {
			view.setElementEnabled(ResourceBarElement.ROAD, false);
		}
		if (numSettlements >= 1
				&& resources.count(ResourceType.BRICK) >= 1
				&& resources.count(ResourceType.WOOD) >= 1
				&& resources.count(ResourceType.SHEEP) >= 1
				&& resources.count(ResourceType.WHEAT) >= 1) {
			view.setElementEnabled(ResourceBarElement.SETTLEMENT, true);
		}
		else {
			view.setElementEnabled(ResourceBarElement.SETTLEMENT, false);
		}
		if (numCities >= 1
				&& resources.count(ResourceType.ORE) >= 3
				&& resources.count(ResourceType.WHEAT) >= 2) {
			view.setElementEnabled(ResourceBarElement.CITY, true);
		}
		else {
			view.setElementEnabled(ResourceBarElement.CITY, false);
		}

		getView().setElementAmount(ResourceBarElement.SOLDIERS,
				localPlayer.getSoldiers());
		
		
	}

	/**
	 * Sets the action to be executed when the specified resource bar element is
	 * clicked by the user
	 * 
	 * @param element
	 *            The resource bar element with which the action is associated
	 * @param action
	 *            The action to be executed
	 */
	public void setElementAction(ResourceBarElement element, IAction action) {

		elementActions.put(element, action);
	}

	@Override
	public void buildRoad() {
		executeElementAction(ResourceBarElement.ROAD);
	}

	@Override
	public void buildSettlement() {
		executeElementAction(ResourceBarElement.SETTLEMENT);
	}

	@Override
	public void buildCity() {
		executeElementAction(ResourceBarElement.CITY);
	}

	@Override
	public void buyCard() {
		executeElementAction(ResourceBarElement.BUY_CARD);
	}

	@Override
	public void playCard() {
		executeElementAction(ResourceBarElement.PLAY_CARD);
	}

	private void executeElementAction(ResourceBarElement element) {

		if (elementActions.containsKey(element)) {

			IAction action = elementActions.get(element);
			action.execute();
		}
	}

}
