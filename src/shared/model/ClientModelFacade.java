package shared.model;

import java.util.ArrayList;
import java.util.logging.*;

import shared.exceptions.GameInitializationException;
import client.communication.ServerPoller;
import client.data.GameInfo;


public class ClientModelFacade extends ModelFacade {
		static final Logger log = Logger.getLogger( ClientModelFacade.class.getName() );
		
		private ServerPoller poller;
		
		public ClientModelFacade() throws GameInitializationException {
			//when does any of this get initialized?
			this(new CatanModel());
		}
		
		public ClientModelFacade(CatanModel startingModel) {
			super(startingModel);
			
			listeners = new ArrayList<>();
		}
		
		public void setGameInfo(GameInfo header) {
			model.setHeader(header);
		}
		
		public GameInfo getGameInfo() {
			return model.getGameInfo();
		}
		
		// It's implemented a little differently client-side
		@Override
		public synchronized void rollDice(PlayerReference player) {
			Player currentPlayer = getCurrentPlayer().getPlayer();
			
			currentPlayer.setHasRolled(true);
		}
		
		public void notifyGameFinished() {
			if (poller.isRunning()) {
				poller.stop();
			}
			try {
				this.model = new CatanModel();
			} catch (GameInitializationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (IModelListener listener : listeners) {
				try {
					listener.gameFinished();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void setPoller(ServerPoller poller) {
			this.poller = poller;
			
		}
}
