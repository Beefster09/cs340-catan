package server.DAOs;

import java.util.UUID;

import shared.model.ModelFacade;

/**
 * This class contains implementation for storing, deleting, and updating
 * games in a SQL-based relational database system. 
 * @author jchip
 *
 */
public class SQLGameDAO implements IGameDAO {

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

}
