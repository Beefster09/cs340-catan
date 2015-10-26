package client.maritime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONObject;

import shared.communication.IServer;
import shared.definitions.*;
import shared.model.Board;
import shared.model.CatanModel;
import shared.model.ModelFacade;
import shared.model.Player;
import shared.model.PlayerReference;
import shared.model.Port;
import shared.model.ResourceList;
import client.base.*;
import client.misc.ClientManager;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController {

	private IMaritimeTradeOverlay tradeOverlay;
	private IServer server;
	private ModelFacade modelFacade;
	private PlayerReference localPlayer;
	private Board board;
	private List<ResourceType> typesOfResources;
	
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		
		super(tradeView);
		typesOfResources = new ArrayList<ResourceType>();
		typesOfResources.add(ResourceType.BRICK);
		typesOfResources.add(ResourceType.ORE);
		typesOfResources.add(ResourceType.SHEEP);
		typesOfResources.add(ResourceType.WHEAT);
		typesOfResources.add(ResourceType.WOOD);

		setTradeOverlay(tradeOverlay);
	}
	
	public IMaritimeTradeView getTradeView() {
		
		return (IMaritimeTradeView)super.getView();
	}
	
	public IMaritimeTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IMaritimeTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	
	/**
	 *Gets called by the view when the roll dice button is pressed
	 *To Do (not necessarily in this class):
	 *Create and array of possible resource trade options (Resource types of a sufficent amount to trade) 
	 *and pass them into the overlay in .showGiveOptions(resurce[])
	 *disable trade button in view
	 *showModal on tradeOverlay
	 */
	@Override
	public void startTrade() {
		server = ClientManager.getServer();
		modelFacade = ClientManager.getModel();
		localPlayer = ClientManager.getLocalPlayer();
		board = ClientManager.getModel().getCatanModel().getMap();
		List<Player> players = modelFacade.getCatanModel().getPlayers();
		Player thisPlayer = players.get(localPlayer.getIndex());
		ResourceList resources = thisPlayer.getResources();
		Collection<Port> ports = board.getPorts();
		Set<ResourceType> resourceSet = new TreeSet<ResourceType>();
		int tradeRatio = 4;
		for(Port port : ports){
			PlayerReference owner = board.getPortOwner(port);
			if(owner == null){
				continue;
			}
			if(owner.getIndex() == localPlayer.getIndex()){
				if(port.getRatio() != 3){
					if(resources.count(port.getResource()) >= 2){
						resourceSet.add(port.getResource());
					}
				}
				else{
					tradeRatio = 3;
				}
			}
		}
		
		for(ResourceType type : typesOfResources){
			if(resources.count(type) >= tradeRatio){
				resourceSet.add(type);
			}
		}
		ResourceType[] resourceArray = new ResourceType[resourceSet.size()];
		int i = 0;
		for(ResourceType type : resourceSet){
			resourceArray[i] = type;
			++i;
		}
		getTradeOverlay().showGiveOptions(resourceArray);
		getTradeOverlay().setTradeEnabled(false);
		getTradeOverlay().showModal();
	}

	
	
	
	/**
	 * Verify there is a resource to give and recieve
	 * Send request to server
	 * closeModal
	 * */
	@Override
	public void makeTrade() {

		getTradeOverlay().closeModal();
	}

	
	/**
	 * closeModal()
	 */
	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	
	/**
	 * check to make sure bank has the resource
	 * call selectGetOption() in tradeOverlay
	 * enable/disable trade button based on validity 
	 */
	@Override
	public void setGetResource(ResourceType resource) {
		

	}

	/**
	 * find out trade ratio for resource to give
	 * call selectGiveOption() in tradeOverlay
	 */
	@Override
	public void setGiveResource(ResourceType resource) {
		int ratio = 4;
		ResourceList resouces = modelFacade.getCatanModel().getPlayers().get(ClientManager.getLocalPlayer().getIndex()).getResources();
		Collection<Port> ports = board.getPorts();
		for(Port port : ports){
			if(port.getRatio() == 3){
				if(board.getPortOwner(port) == null){
					continue;
				}
				if(board.getPortOwner(port).equals(ClientManager.getLocalPlayer())){
					ratio = 3;
				}
			}
			else if(port.getResource().equals(resource)){
				if(board.getPortOwner(port) == null){
					continue;
				}
				if(board.getPortOwner(port).equals(ClientManager.getLocalPlayer())){
					ratio = 2;
					break;
				}
			}
		}
		getTradeOverlay().selectGiveOption(resource, ratio);
	}

	
	/**
	 * disable trade button
	 * redisplay the trade Options for getting resource
	 */
	@Override
	public void unsetGetValue() {

	}

	
	/**
	 * disable trade button
	 * redisplay the trade Options for getting resource
	 */
	@Override
	public void unsetGiveValue() {

	}
}

