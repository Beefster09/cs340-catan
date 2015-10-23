package client.resources;

import java.util.*;

import shared.definitions.*;
import shared.model.*;

import client.base.*;
import client.misc.ClientManager;


/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements IResourceBarController {

	private Map<ResourceBarElement, IAction> elementActions;
	
	public ResourceBarController(IResourceBarView view) {

		super(view);
		
		elementActions = new HashMap<ResourceBarElement, IAction>();
	}

	@Override
	public IResourceBarView getView() {
		return (IResourceBarView)super.getView();
	}

	/* (non-Javadoc)
	 * @see shared.model.AbstractModelListener#playersChanged(java.util.List)
	 */
	@Override
	public void playersChanged(List<Player> players) {
		System.out.println("Updating Resource Counts...");
		
		Player localPlayer = ClientManager.getLocalPlayer().getPlayer();
		
		// Set the counts
		ResourceList resources = localPlayer.getResources();
		
		getView().setElementAmount(ResourceBarElement.BRICK, resources.count(ResourceType.BRICK));
		getView().setElementAmount(ResourceBarElement.WOOD, resources.count(ResourceType.WOOD));
		getView().setElementAmount(ResourceBarElement.WHEAT, resources.count(ResourceType.WHEAT));
		getView().setElementAmount(ResourceBarElement.ORE, resources.count(ResourceType.ORE));
		getView().setElementAmount(ResourceBarElement.SHEEP, resources.count(ResourceType.SHEEP));

		getView().setElementAmount(ResourceBarElement.ROAD, localPlayer.getRoads());
		getView().setElementAmount(ResourceBarElement.SETTLEMENT, localPlayer.getSettlements());
		getView().setElementAmount(ResourceBarElement.CITY, localPlayer.getCities());
		
		// TODO: set the dev card stuff?
	}

	/**
	 * Sets the action to be executed when the specified resource bar element is clicked by the user
	 * 
	 * @param element The resource bar element with which the action is associated
	 * @param action The action to be executed
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

