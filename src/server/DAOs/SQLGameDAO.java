package server.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import server.model.User;
import shared.model.ModelFacade;

/**
 * This class contains implementation for storing, deleting, and updating
 * games in a SQL-based relational database system. 
 * @author jchip
 *
 */
public class SQLGameDAO implements IGameDAO {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private SQLDatabase db;
	
	/**
	 * Creates an instance of a CellDAO to interact with the database.
	 */
	public SQLGameDAO(SQLDatabase db) {
		this.db = db;
	}

	@Override
	public boolean addGame(ModelFacade model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeGame(UUID gameUUID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateGamebyUUID(UUID gameUUID, ModelFacade model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ModelFacade getGame(UUID gameUUID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<UUID, ModelFacade> getAllGames() throws DatabaseException {
		Map<UUID, ModelFacade> games = new HashMap<UUID, ModelFacade>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			String query = "select * from games";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString(2));
				ModelFacade game = (ModelFacade) rs.getBlob(3);
				
				games.put(uuid, game);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get games", e);
		}
		finally {
			SQLDatabase.safeClose(rs);
			SQLDatabase.safeClose(stmt);
		}
		return games;
	}

}
