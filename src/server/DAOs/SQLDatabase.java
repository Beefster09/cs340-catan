package server.DAOs;

import java.io.File;
import java.sql.*;
import java.util.logging.*;

public class SQLDatabase {
	
	private static final String DATABASE_DIRECTORY = "db";
	private static final String DATABASE_FILE = "Catan.sqlite";
	private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_DIRECTORY +
												File.separator + DATABASE_FILE;

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	public static void initialize() throws DatabaseException {
		try {
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}
		catch(ClassNotFoundException e) {
			
			DatabaseException serverEx = new DatabaseException("Could not load database driver", e);
			
			logger.throwing("server.database.Database", "initialize", serverEx);

			throw serverEx; 
		}
	}
	
	Connection connection;
	
	public SQLDatabase() {
		connection = null;
	}
	
	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Begins a transaction on the database.  A transaction allows to commit multiple
	 * statements without committing them to the database.  Once all the statements
	 * needed have been queued, use endTransaction(true) to commit them to the database.
	 * @throws DatabaseException
	 */
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
