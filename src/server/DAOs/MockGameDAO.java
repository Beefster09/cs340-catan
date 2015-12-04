package server.DAOs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import shared.communication.GameHeader;
import shared.model.ModelFacade;

public class MockGameDAO implements IGameDAO {

	@Override
	public boolean addGame(ModelFacade model) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeGame(UUID gameUUID) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateGamebyUUID(UUID gameUUID, ModelFacade model)
			throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ModelFacade getGame(UUID gameUUID) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GameHeader> getGameList() {
		return new ArrayList<>();
	}

}
