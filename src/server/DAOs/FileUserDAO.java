package server.DAOs;

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
	public boolean addUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUserbyUUID(UUID playerUUID, User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getUser(UUID playerUUID) {
		// TODO Auto-generated method stub
		return null;
	}

}
