package server.DAOs;

import java.util.List;
import java.util.UUID;

import shared.communication.GameHeader;
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
	public List<GameHeader> getGameList();
	
}
