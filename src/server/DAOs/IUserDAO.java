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

	public void addUser(User user) throws DatabaseException;
	public void deleteUser(User user) throws DatabaseException;
	public void updateUserPassword(User user) throws DatabaseException;
	public User getUser(UUID playerUUID) throws DatabaseException;
}
