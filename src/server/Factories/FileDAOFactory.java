package server.Factories;

import server.DAOs.FileGameDAO;
import server.DAOs.FileUserDAO;
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
	
	IUserDAO userDAO;
	IGameDAO gameDAO;

	public FileDAOFactory(){
		userDAO = new FileUserDAO();
		gameDAO = new FileGameDAO();
	}
	
	@Override
	public IUserDAO getUserDAO() {
		return userDAO;
	}

	@Override
	public IGameDAO getGameDAO() {
		return gameDAO;
	}

	@Override
	public ICommandDAO getCommandDAO() {
		return null;
	}

	@Override
	public void startTransaction() {}

	@Override
	public void endTransaction(boolean commit) {}

}
