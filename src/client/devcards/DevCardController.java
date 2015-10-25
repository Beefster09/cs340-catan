package client.devcards;

import java.util.ArrayList;
import java.util.List;

import shared.communication.IServer;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.DevCardList;
import shared.model.ModelFacade;
import client.base.*;
import client.misc.ClientManager;
import client.misc.IMessageView;


/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController {

	private IMessageView messageView;
	private IServer server = ClientManager.getServer();
	private ModelFacade model = ClientManager.getModel();
	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;
	
	/**
	 * DevCardController constructor
	 * 
	 * @param view "Play dev card" view
	 * @param buyCardView "Buy dev card" view
	 * @param soldierAction Action to be executed when the user plays a soldier card.  It calls "mapController.playSoldierCard()".
	 * @param roadAction Action to be executed when the user plays a road building card.  It calls "mapController.playRoadBuildingCard()".
	 */
	public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView, 
								IAction soldierAction, IAction roadAction) {

		super(view);
		
		this.buyCardView = buyCardView;
		this.soldierAction = soldierAction;
		this.roadAction = roadAction;
	}

	public IPlayDevCardView getPlayCardView() {
		return (IPlayDevCardView)super.getView();
	}

	public IBuyDevCardView getBuyCardView() {
		return buyCardView;
	}

	@Override
	public void startBuyCard() {
		
		getBuyCardView().showModal();
	}

	@Override
	public void cancelBuyCard() {
		
		getBuyCardView().closeModal();
	}

	@Override
	public void buyCard() {
		try {
			server.buyDevCard(ClientManager.getLocalPlayer());
		}
		catch (ServerException e){
			messageView.setTitle("Server Error");
			messageView.setMessage("Unable to reach server at this point");
			messageView.showModal();
			return;
		}
		catch (UserException e) {
			messageView.setTitle("User Error");
			messageView.setMessage("Unable to complete action at this time)");
			messageView.showModal();
			return;
		}
		getBuyCardView().closeModal();
	}

	@Override
	public void startPlayCard() {
		List<DevCardType> listOfDevCards = new ArrayList<DevCardType>();
		listOfDevCards.add(DevCardType.MONOPOLY);
		listOfDevCards.add(DevCardType.MONUMENT);
		listOfDevCards.add(DevCardType.ROAD_BUILD);
		listOfDevCards.add(DevCardType.SOLDIER);
		listOfDevCards.add(DevCardType.YEAR_OF_PLENTY);
		
		DevCardList devCards = model.getCatanModel().getPlayers().get(ClientManager.getLocalPlayer().getIndex()).getOldDevCards();

		for(DevCardType type : listOfDevCards){
			int count = devCards.count(type);
			if(count > 0){
				getPlayCardView().setCardEnabled(type, true);
				getPlayCardView().setCardAmount(type, count);
			}
			else{
				getPlayCardView().setCardEnabled(type, false);
				getPlayCardView().setCardAmount(type, count);
			}
		}
		getPlayCardView().showModal();
	}

	@Override
	public void cancelPlayCard() {

		getPlayCardView().closeModal();
	}

	@Override
	public void playMonopolyCard(ResourceType resource) {
		try {
			
			server.monopoly(ClientManager.getLocalPlayer(), resource);
		
		} catch (ServerException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void playMonumentCard() {
		
		try {
			server.monument(ClientManager.getLocalPlayer());
		} catch (ServerException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void playRoadBuildCard() {
		
		
		roadAction.execute();
	}

	@Override
	public void playSoldierCard() {
		
		
		
		soldierAction.execute();
	}

	@Override
	public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
		
	}

}

