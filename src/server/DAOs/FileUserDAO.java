package server.DAOs;

import java.util.List;
import java.util.UUID;

import server.model.User;

/**
 * This class contains implementation for storing, deleting, and updating
 * users in a file-based database system.
 * @author jchip
 *
 */
public class FileUserDAO implements IUserDAO {

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserPassword(User user) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User getUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUsers() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}


}
