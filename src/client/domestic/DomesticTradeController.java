package client.domestic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.java_cup.internal.runtime.Symbol;

import shared.definitions.*;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;
import shared.model.Player;
import shared.model.PlayerReference;
import shared.model.ResourceTradeList;
import shared.model.TradeOffer;
import shared.model.TurnTracker;
import client.base.*;
import client.data.PlayerInfo;
import client.misc.*;


/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController {

	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;
	private ModelFacade modelFacade = ClientManager.getModel();
	
	private Map<ResourceType, Integer> offered = new HashMap<ResourceType, Integer>();
	private Map<ResourceType, Integer> wanted = new HashMap<ResourceType, Integer>();
	private ResourceType resourceToReceive;
	private ResourceType resourceToSend;
	PlayerInfo[] playerInfos;
	private int indexToTradeWith = -1;
	
	//We should only set the players on the View once.  This is to check for that
	private boolean initialized = false;
	
	/**
	 * DomesticTradeController constructor
	 * 
	 * @param tradeView Domestic trade view (i.e., view that contains the "Domestic Trade" button)
	 * @param tradeOverlay Domestic trade overlay (i.e., view that lets the user propose a domestic trade)
	 * @param waitOverlay Wait overlay used to notify the user they are waiting for another player to accept a trade
	 * @param acceptOverlay Accept trade overlay which lets the user accept or reject a proposed trade
	 */
	public DomesticTradeController(IDomesticTradeView tradeView, IDomesticTradeOverlay tradeOverlay,
									IWaitView waitOverlay, IAcceptTradeOverlay acceptOverlay) {

		super(tradeView);
		
		setTradeOverlay(tradeOverlay);
		setWaitOverlay(waitOverlay);
		setAcceptOverlay(acceptOverlay);
		
		
	}
	
	public IDomesticTradeView getTradeView() {
		
		return (IDomesticTradeView)super.getView();
	}

	public IDomesticTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IDomesticTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	public IWaitView getWaitOverlay() {
		return waitOverlay;
	}

	public void setWaitOverlay(IWaitView waitView) {
		this.waitOverlay = waitView;
	}

	public IAcceptTradeOverlay getAcceptOverlay() {
		return acceptOverlay;
	}

	public void setAcceptOverlay(IAcceptTradeOverlay acceptOverlay) {
		this.acceptOverlay = acceptOverlay;
	}

	@Override
	public void startTrade() {

		assert ClientManager.getModel().getCatanModel().getTurnTracker().equals(TurnStatus.Playing);
		getTradeOverlay().showModal();
		getTradeOverlay().setTradeEnabled(false);

		Map<ResourceType, Integer> hand = getLocalHand();
		
		for(Map.Entry<ResourceType, Integer> entry : hand.entrySet()) {
			
			if(entry.getValue() == 0)
				getTradeOverlay().setResourceAmountChangeEnabled(entry.getKey(), false, false);
			else
				getTradeOverlay().setResourceAmountChangeEnabled(entry.getKey(), true, false);
		}
	}

	@Override
	public void decreaseResourceAmount(ResourceType resource) {
		
		if (resourceToReceive != null && 
				resourceToReceive.equals(resource)) {
			int current;
			if (wanted.get(resource) != null) {
				current = wanted.get(resource) - 1;
				wanted.put(resource, current);
				getTradeOverlay().setResourceAmount(resource, Integer.toString(current));
				
				if (current <= 0) {
					getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
				}
			}
		}
		
		if (offered.get(resource) != null) {
			int current = offered.get(resource)-1;
			offered.put(resource, current);
			getTradeOverlay().setResourceAmount(resource, Integer.toString(current));
			if (current == 0) {
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
			}
		}
		
		setTradeState();
	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {

		if (resourceToReceive != null && 
				resourceToReceive.equals(resource)) {
			int current;
			if (wanted.get(resource) != null) {
				current = wanted.get(resource) + 1;
			}
			else {
				current = 1;
			}
			wanted.put(resource, current);
			getTradeOverlay().setResourceAmount(resource, Integer.toString(current));
			
			getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
			if (current >= 15) {
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
			}
		}
		else {
			int current;
			if (offered.get(resource) != null) {
				current = offered.get(resource) + 1;
			}
			else {
				current = 1;
			}
			offered.put(resource, current);
			
			getTradeOverlay().setResourceAmount(resource, Integer.toString(current));
			
			if (getLocalHand().get(resource) >= current) {
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
			}
		}
		
		setTradeState();
			
	}
	
	private Map<ResourceType, Integer> getLocalHand() {
		int playerID = modelFacade.getLocalPlayer().getPlayerID();
		Player player = modelFacade.getCatanModel().getPlayers().get(playerID);

		return player.getResources().getResources();
	}
	
	private void setTradeState() {
		if (canTrade()){
			getTradeOverlay().setStateMessage("Trade");
			getTradeOverlay().setTradeEnabled(true);
		}
		else if (indexToTradeWith == -1) {
			getTradeOverlay().setStateMessage("Must select a player to trade with");
			getTradeOverlay().setTradeEnabled(false);
		}
		else {
			getTradeOverlay().setStateMessage("Must send and receive resources");
			getTradeOverlay().setTradeEnabled(false);
		}
	}
	
	private boolean canTrade() {
		if (indexToTradeWith == -1)
			return false;
		
		boolean hasOffered = false;
		for(Map.Entry<ResourceType, Integer> entry : offered.entrySet()) {
			if (entry.getValue() > 0)
				hasOffered = true;
		}
		if (!hasOffered)
			return false;
		
		boolean hasReceive = false;
		for(Map.Entry<ResourceType, Integer> entry : wanted.entrySet()) {
			if (entry.getValue() > 0)
				hasReceive = true;
		}
		if (!hasReceive)
			return false;
		
		return true;
		
	}

	@Override
	public void sendTradeOffer() {

		PlayerReference receiver = null;
		
		PlayerInfo playerToTradeWith = playerInfos[indexToTradeWith];
		List<Player> players = ClientManager.getModel().getCatanModel().getPlayers();
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getPlayerID() == playerToTradeWith.getId()) {
				receiver = players.get(i).getReference();
			}
		}
		//If we can't find the player...things are seriously wrong.
		if (receiver == null)
			assert 1 == 0;
		
		ResourceTradeList offer = new ResourceTradeList();
		offer.setOffered(offered);
		offer.setWanted(wanted);
		
		PlayerReference user = ClientManager.getLocalPlayer();
		
		try {
			ClientManager.getServer().offerTrade(user, offer, receiver);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		
		getTradeOverlay().closeModal();
		getWaitOverlay().showModal();
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {
		indexToTradeWith = playerIndex-1;
		setTradeState();
	}

	@Override
	public void setResourceToReceive(ResourceType resource) {
		resourceToReceive = resource;
		offered.put(resource, 0);
		wanted.put(resource, 0);
		getTradeOverlay().setResourceAmount(resource, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);

		if (resourceToSend != null &&
				resourceToSend.equals(resource)) {
			resourceToSend = null;
		}
		
		setTradeState();
	}

	@Override
	public void setResourceToSend(ResourceType resource) {
		resourceToSend = resource;
		
		offered.put(resource, 0);
		wanted.put(resource, 0);
		getTradeOverlay().setResourceAmount(resource, "0");
		if (getLocalHand().get(resource) > 0)
			getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
		else {
			getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
		
			if (resourceToReceive != null &&
					resourceToReceive.equals(resource)) {
				resourceToReceive = null;
			}
		}
		setTradeState();
	}

	@Override
	public void unsetResource(ResourceType resource) {
		wanted.put(resource, 0);
		offered.put(resource, 0);
	}

	@Override
	public void cancelTrade() {
		wanted = new HashMap<ResourceType, Integer>();
		offered = new HashMap<ResourceType, Integer>();
		getTradeOverlay().closeModal();
	}

	@Override
	public void acceptTrade(boolean willAccept) {
		try {
			ClientManager.getServer().respondToTrade(ClientManager.getLocalPlayer(), willAccept);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		getAcceptOverlay().closeModal();
	}
	
	@Override
	public void turnTrackerChanged(TurnTracker turnTracker) {
		if (turnTracker.getCurrentPlayer().getIndex() == 
				ClientManager.getLocalPlayer().getIndex() &&
				turnTracker.getStatus().equals(TurnStatus.Playing)) {
			getTradeView().enableDomesticTrade(true);
		}
		else {
			getTradeView().enableDomesticTrade(false);
		}
	}

	@Override
	public void playersChanged(List<Player> players) {
		if (initialized || players.size() <= 3) {
			return;
		}
		playerInfos = new PlayerInfo[players.size()-1];
		int j = 0;
		for (int i = 0; i < players.size(); i++) {
			if (i != ClientManager.getLocalPlayer().getIndex()) {
				playerInfos[j] = new PlayerInfo(players.get(i));
				j++;
			}
		}
		getTradeOverlay().setPlayers(playerInfos);
		initialized = true;
	}
	
	@Override
	public void tradeOfferChanged(TradeOffer offer) {

		if (offer == null) {
			if (getWaitOverlay().isModalShowing()) {
				getWaitOverlay().closeModal();
			}
		}
	
		int localPlayerID = ClientManager.getLocalPlayer().getPlayer().getPlayerID();
		if (offer.getReceiver().getPlayer().getPlayerID() == localPlayerID) {
			
			this.getAcceptOverlay().setPlayerName(offer.getSender().getPlayer().getName());
			
			Map<ResourceType, Integer> offeredCards = offer.getOffer().getOffered();
			for(Map.Entry<ResourceType, Integer> entry : offeredCards.entrySet()) {
				this.getAcceptOverlay().addGetResource(entry.getKey(), entry.getValue());
			}
			
			Map<ResourceType, Integer> wantedCards = offer.getOffer().getWanted();
			for(Map.Entry<ResourceType, Integer> entry : wantedCards.entrySet()) {
				this.getAcceptOverlay().addGiveResource(entry.getKey(), entry.getValue());
			}
			
			getAcceptOverlay().setAcceptEnabled(true);
			getAcceptOverlay().showModal();
		}
	}
	
	
}

