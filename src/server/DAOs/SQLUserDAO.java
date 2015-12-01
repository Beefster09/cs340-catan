package server.DAOs;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

import server.model.User;

/**
 * This class contains implementation for storing, deleting, and updating
 * users in a SQL-based relational database system. 
 * @author jchip
 *
 */
public class SQLUserDAO implements IUserDAO {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private SQLDatabase db;
	
	/**
	 * Creates an instance of a CellDAO to interact with the database.
	 */
	public SQLUserDAO(SQLDatabase db) {
		this.db = db;
	}
	
	@Override
	public void addUser(User user) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		try {
			String query = "insert into user (username, password) values (?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not add cell");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not add cell", e);
		}
		finally{
			SQLDatabase.safeClose(keyRS);
			SQLDatabase.safeClose(stmt);
		}
	}

	@Override
	public void deleteUser(User user) throws DatabaseException {
		PreparedStatement stmt = null;	
		try {
			String query = "delete from user where username = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete user");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete user", e);
		}
		finally {
			SQLDatabase.safeClose(stmt);
		}
	}

	@Override
	public void updateUserPassword(User user) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "update user set password = ? where username = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update user");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update cell", e);
		}
		finally {
			SQLDatabase.safeClose(rs);
			SQLDatabase.safeClose(stmt);
		}
	}

	@Override
	public User getUser(UUID playerUUID) {
		// TODO Auto-generated method stub
		return null;
	}

}
