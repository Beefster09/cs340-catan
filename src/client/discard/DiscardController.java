package client.discard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shared.communication.IServer;
import shared.definitions.*;
import shared.model.ModelFacade;
import shared.model.Player;
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
		
		int temp = cardsToBeDiscarded.get(resource);
		temp++;
		cardsToBeDiscarded.put(resource, temp);
		
		int discardNum = getHowManyLeftToDiscard();
		
		getDiscardView().setStateMessage("More than 7 cards. Discard " + discardNum + " more");
		
	}

	/**
	 * undoes things you did in increase function
	 */
	@Override
	public void decreaseAmount(ResourceType resource) {
		
		int temp = cardsToBeDiscarded.get(resource);
		temp--;
		cardsToBeDiscarded.put(resource, temp);
		
		int discardNum = getHowManyLeftToDiscard();
		
		getDiscardView().setStateMessage("More than 7 cards. Discard " + discardNum + " more");
		
		getDiscardView().setStateMessage("HI");
	}

	/**
	 * send request to server with a map of resource types and how many of each are discarded
	 */
	@Override
	public void discard() {
		
		getDiscardView().closeModal();
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
	
	private int getHowManyLeftToDiscard() {
		
		int playerID = modelFacade.getLocalPlayer().getPlayerID();
		List<Player> players = modelFacade.getCatanModel().getPlayers();
		Player player = modelFacade.getCatanModel().getPlayers().get(playerID);
		ResourceList hand = player.getResources();
		int discardRequirement = hand.count()/2;
		return discardRequirement - discardCount();
	}
	
	private int discardCount() {
		int total = 0;
		for (int count : cardsToBeDiscarded.values()) {
			total += count;
		}
		return total;
	}
}

