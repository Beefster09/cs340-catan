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
	public void addGame(UUID uuid, ModelFacade model) {
	}

	@Override
	public void removeGame(UUID gameUUID) {
	}

	@Override
	public void updateGamebyUUID(UUID gameUUID, ModelFacade model) {
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
