package server.DAOs;

import java.util.UUID;

import server.model.User;

/**
 * This interface defines all needed operations to enable interaction
 * with a database for users
 * @author jchip
 *
 */
public interface IUserDAO {

	public boolean addUser(User user);
	public boolean deleteUser(User user);
	public boolean updateUserbyUUID(UUID playerUUID, User user);
	public User getUser(UUID playerUUID);
}
