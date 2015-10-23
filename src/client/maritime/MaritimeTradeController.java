package client.maritime;

import shared.definitions.*;
import client.base.*;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController {

	private IMaritimeTradeOverlay tradeOverlay;
	
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		
		super(tradeView);

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

