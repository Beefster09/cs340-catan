package server.DAOs;

import java.util.Map;
import java.util.UUID;

import shared.model.ModelFacade;

/**
 * This interface defines all needed operations to enable interaction
 * with a database for users
 * @author jchip
 *
 */
public interface IGameDAO {

	public boolean addGame(ModelFacade model) throws DatabaseException;
	public boolean removeGame(UUID gameUUID) throws DatabaseException;
	public boolean updateGamebyUUID(UUID gameUUID, ModelFacade model) throws DatabaseException;
	public ModelFacade getGame(UUID gameUUID) throws DatabaseException;
	public Map<UUID, ModelFacade> getAllGames() throws DatabaseException;
}
