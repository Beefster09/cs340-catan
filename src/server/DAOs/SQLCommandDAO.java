package server.DAOs;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import server.commands.CommandSerializationException;
import server.commands.CommandSerializer;
import server.commands.ICatanCommand;

public class SQLCommandDAO implements ICommandDAO {

	private static Logger logger = Logger.getLogger("Catan-SQL");
	
	public static void main(String[] args) {
		SQLDatabase database = new SQLDatabase();
	}

	private SQLDatabase db;
	
	/**
	 * Creates an instance of a CellDAO to interact with the database.
	 */
	public SQLCommandDAO(SQLDatabase db) {
		this.db = db;
	}

	@Override
	public void addCommand(UUID gameid, ICatanCommand command) throws DatabaseException {
		try {
			Connection conn = db.getConnection();
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO commands (gameid, command) " +
					"VALUES (?, ?)");
			
			Blob cmdBlob = conn.createBlob();
			cmdBlob.setBytes(0, CommandSerializer.serializeBytes(command));

			stmt.setString(1, gameid.toString());
			stmt.setBlob(2, cmdBlob);
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Failed to store the command.");
			}
			
			stmt.close();
			
			logger.fine("Saved command to game " + gameid.toString());
		} catch (SQLException | CommandSerializationException e) {
			throw new DatabaseException("Failed to store the command.", e);
		}
	}

	@Override
	public void clearCommands(UUID gameid) throws DatabaseException {
		try {
			PreparedStatement stmt = db.getConnection().prepareStatement(
					"DELETE FROM commands WHERE gameid = ?");
			
			stmt.setString(1, gameid.toString());

			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Failed to delete commands from the game.");
			}
			
			stmt.close();

			logger.fine("Cleared commands from game " + gameid.toString());
		} catch (SQLException e) {
			throw new DatabaseException("Failed to delete commands from the game.", e);
		}
	}

	@Override
	public List<ICatanCommand> getAll(UUID gameid) throws DatabaseException {
		try {
			List<ICatanCommand> commands = new ArrayList<>();
			PreparedStatement stmt = db.getConnection().prepareStatement(
					"SELECT command FROM commands WHERE gameid = ? ORDER BY id");
			
			stmt.setString(1, gameid.toString());
			
			ResultSet results = stmt.executeQuery();
			
			while(results.next()) {
				Blob cmdBlob = results.getBlob(1);
				commands.add(CommandSerializer.deserializeBytes(
						cmdBlob.getBytes(0, (int) cmdBlob.length())));
			}
			
			results.close();
			stmt.close();
			
			logger.fine("Obtained " + commands.size() + " commands for game " + gameid.toString());
			
			return commands;
		} catch (SQLException | CommandSerializationException e) {
			throw new DatabaseException("Failed to get commands for the game.", e);
		}
	}

}
