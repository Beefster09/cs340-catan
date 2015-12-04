package server.DAOs;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
		ModelFacade returnGame = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		
		try {
			String query = "select * from games where uuid = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, gameUUID.toString());
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				String uuid = rs.getString(2);
				Blob game = rs.getBlob(3);
				
				returnGame = new ModelFacade(username, password);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get cells", e);
		}
		finally {
			SQLDatabase.safeClose(rs);
			SQLDatabase.safeClose(stmt);
		}
		return returnUser;
	}

	@Override
	public List<ModelFacade> getAllGames() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}
