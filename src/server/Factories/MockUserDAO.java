package server.Factories;

import server.DAOs.DatabaseException;
import server.DAOs.IUserDAO;
import server.model.User;

public class MockUserDAO implements IUserDAO {

	@Override
	public void addUser(User user) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUser(User user) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUserPassword(User user) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public User getUser(User user) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}
