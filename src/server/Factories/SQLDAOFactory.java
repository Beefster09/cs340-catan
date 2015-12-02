package server.Factories;

import java.io.File;
import java.sql.*;

import server.DAOs.*;

/**
 * This factory contains references to all the
 * DAOs necessary to store the necessary model into
 * a SQL based, relational database.
 * @author jchip
 *
 */
public class SQLDAOFactory implements IDAOFactory {

	private static final String DATABASE_DIRECTORY = "database";
	private static final String DATABASE_FILE = "RecordIndexer.sqlite";
	private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_DIRECTORY +
												File.separator + DATABASE_FILE;
	
	IUserDAO userDAO;
	IGameDAO gameDAO;
	ICommandDAO commandDAO;
	
	Connection connection;
	
	public SQLDAOFactory() {
		try {
			SQLDatabase.initialize();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		SQLDatabase db = new SQLDatabase();
		userDAO = new SQLUserDAO(db);
		gameDAO = new SQLGameDAO(db);
		commandDAO = new SQLCommandDAO(db);
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
		return commandDAO;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void startTransaction() throws DatabaseException {
		try {
			connection = DriverManager.getConnection(DATABASE_URL);
			connection.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not connect to database. Make sure " + 
					DATABASE_FILE + " is available in ./" + DATABASE_DIRECTORY, e);
		}
	}
	
	public void endTransaction(boolean commit) {
		try {
			if (commit) {
				connection.commit();
			}
			else {
				connection.rollback();
			}
		}
		catch(SQLException e) {
			System.out.println("Could not end transaction.");
			e.printStackTrace();
		}
		finally {
			safeClose(connection);
			connection = null;
		}
	}
	
	public static void safeClose(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			}
			catch (SQLException e) {
				System.out.println("Could not close connection");
				e.getStackTrace();
			}
		}
	}
	
	public static void safeClose(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				System.out.println("Could not close connection");
				e.getStackTrace();
			}
		}
	}
	
	public static void safeClose(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				System.out.println("Could not close connection");
				e.getStackTrace();
			}
		}
	}
	
	public static void safeClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				System.out.println("Could not close connection");
				e.getStackTrace();
			}
		}
	}

}
