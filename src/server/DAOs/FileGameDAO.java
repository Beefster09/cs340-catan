package server.DAOs;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import shared.model.ModelFacade;

/**
 * This class contains implementation for storing, deleting, and updating
 * games in a file-based database system.
 * @author jchip
 *
 */
public class FileGameDAO implements IGameDAO {

	@Override
	public boolean addGame(ModelFacade model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeGame(UUID gameUUID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateGamebyUUID(UUID gameUUID, ModelFacade model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ModelFacade getGame(UUID gameUUID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<UUID, ModelFacade> getAllGames() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}
