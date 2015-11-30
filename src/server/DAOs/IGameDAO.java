package server.DAOs;

import java.util.UUID;

import shared.model.ModelFacade;

/**
 * This interface defines all needed operations to enable interaction
 * with a database for users
 * @author jchip
 *
 */
public interface IGameDAO {

	public boolean addGame(ModelFacade model);
	public boolean removeGame(UUID gameUUID);
	public boolean updateGamebyUUID(UUID gameUUID, ModelFacade model);
	public ModelFacade getGame(UUID gameUUID);
}
