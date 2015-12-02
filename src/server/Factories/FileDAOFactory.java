package server.Factories;

import server.DAOs.ICommandDAO;
import server.DAOs.IGameDAO;
import server.DAOs.IUserDAO;

/**
 * This factory contains references to all the
 * DAOs necessary to store the necessary model into
 * a basic file-based database.
 * @author jchip
 *
 */
public class FileDAOFactory implements IDAOFactory {

	@Override
	public IUserDAO getUserDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGameDAO getGameDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICommandDAO getCommandDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endTransaction(boolean commit) {
		// TODO Auto-generated method stub
		
	}

}
