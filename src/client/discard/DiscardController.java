package client.discard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shared.communication.IServer;
import shared.definitions.*;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;
import shared.model.Player;
import shared.model.PlayerReference;
import shared.model.ResourceList;
import client.base.*;
import client.misc.*;


/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController {

	private IWaitView waitView;
	Map<ResourceType, Integer> cardsToBeDiscarded = initializecardsToBeDiscarded();
	private IServer serverProxy = ClientManager.getServer();
	private ModelFacade modelFacade = ClientManager.getModel();
	boolean maxedOut = false;

	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting for other players to discard
	 *disable discardbutton in the getDiscardView()
	 *
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		
		super(view);
		
		this.waitView = waitView;
		
	}

	public IDiscardView getDiscardView() {
		return (IDiscardView)super.getView();
	}
	
	public IWaitView getWaitView() {
		return waitView;
	}

	
	/**
	 * add recource to a list to be discarded
	 * getDiscardView().setStateMessage(message); where message is 
	 * how many out of required amount have been selected
	 * enable discard button if enough cards (but not more) have been selected
	 */
	@Override
	public void increaseAmount(ResourceType resource) {
		
		Map<ResourceType, Integer> hand = getLocalHand();
		
		int temp = cardsToBeDiscarded.get(resource);
		temp++;
		cardsToBeDiscarded.put(resource, temp);
		
		int discardNum = getTotalCardsToDiscard();
		
		getDiscardView().setResourceDiscardAmount(resource, temp);
		
		getDiscardView().setStateMessage( discardCount() + "/" + discardNum);
		
		setEnables(resource, hand, temp);
		
		checkIfMaxedOut(resource, hand, discardNum);
	}





	private void checkIfMaxedOut(ResourceType resource, Map<ResourceType, Integer> hand, int maxCards) {
		
		if(discardCount() >= maxCards) {
			
			for(Map.Entry<ResourceType, Integer> entry : hand.entrySet()) {
				
				if(cardsToBeDiscarded.get(entry.getKey()) == 0)
					getDiscardView().setResourceAmountChangeEnabled(entry.getKey(), false, false);
				else
					getDiscardView().setResourceAmountChangeEnabled(entry.getKey(), false, true);
				
			}
			getDiscardView().setDiscardButtonEnabled(true);
			maxedOut = true;
		}
		
	}



	/**
	 * undoes things you did in increase function
	 */
	@Override
	public void decreaseAmount(ResourceType resource) {
		
		Map<ResourceType, Integer> hand = getLocalHand();
		
		int temp = cardsToBeDiscarded.get(resource);
		temp--;
		cardsToBeDiscarded.put(resource, temp);
		
		int discardNum = getTotalCardsToDiscard();
		
		getDiscardView().setResourceDiscardAmount(resource, temp);
		
		getDiscardView().setStateMessage( discardCount() + "/" + discardNum);
		
		setEnables(resource, hand, temp);
		
		checkIfWasMaxedOut(hand);
	}

	private void checkIfWasMaxedOut(Map<ResourceType, Integer> hand) {
		
		if(maxedOut) {
			
			for(Map.Entry<ResourceType, Integer> entry : cardsToBeDiscarded.entrySet()) {
				
				setEnables(entry.getKey(), hand, entry.getValue());
			}
			getDiscardView().setDiscardButtonEnabled(false);
			maxedOut = false;
		}
		
	}



	/**
	 * send request to server with a map of resource types and how many of each are discarded
	 */
	@Override
	public void discard() {
		try {
			
			int playerID = modelFacade.getLocalPlayer().getPlayerID();
			PlayerReference localPlayer = modelFacade.getCatanModel().getPlayers().get(playerID).getReference();
			ResourceList cards = new ResourceList(cardsToBeDiscarded);
			
			serverProxy.discardCards(localPlayer, cards);
			
			getDiscardView().closeModal();
			
		} catch (ServerException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private Map<ResourceType, Integer> initializecardsToBeDiscarded() {
		
		Map<ResourceType, Integer> cardsToBeDiscarded = new HashMap<ResourceType, Integer>();
		
		cardsToBeDiscarded.put(ResourceType.BRICK, 0);
		cardsToBeDiscarded.put(ResourceType.WOOD, 0);
		cardsToBeDiscarded.put(ResourceType.SHEEP, 0);
		cardsToBeDiscarded.put(ResourceType.ORE, 0);
		cardsToBeDiscarded.put(ResourceType.WHEAT, 0);
		
		return cardsToBeDiscarded;
	}
	
	private int getTotalCardsToDiscard() {
		
		int playerID = modelFacade.getLocalPlayer().getPlayerID();
		Player player = modelFacade.getCatanModel().getPlayers().get(playerID);
		ResourceList hand = player.getResources();
		
		int discardRequirement = hand.count()/2;
		return discardRequirement;
	}
	
	private int discardCount() {
		int total = 0;
		for (int count : cardsToBeDiscarded.values()) {
			total += count;
		}
		return total;
	}
	
	private Map<ResourceType, Integer> getLocalHand() {
		
		int playerID = modelFacade.getLocalPlayer().getPlayerID();
		Player player = modelFacade.getCatanModel().getPlayers().get(playerID);
		return player.getResources().getResources();
		
	}
	
	private void setEnables(ResourceType resource, Map<ResourceType, Integer> hand, int temp) {
		if(temp >= hand.get(resource))
			getDiscardView().setResourceAmountChangeEnabled(resource, false, true);
		
		if(temp <= 0)
			getDiscardView().setResourceAmountChangeEnabled(resource, true, false);
		
		if(temp > 0 && temp < hand.get(resource))
			getDiscardView().setResourceAmountChangeEnabled(resource, true, true);
		
		if(hand.get(resource) == 0)
			getDiscardView().setResourceAmountChangeEnabled(resource, false, false);
		
	}

	
}

