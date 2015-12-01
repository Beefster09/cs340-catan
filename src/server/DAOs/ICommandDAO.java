package server.DAOs;

import server.commands.ICatanCommand;

public interface ICommandDAO {

	public void addCommand(ICatanCommand command);
	public void deleteCommand(ICatanCommand command);
	public void getAll();
	
}
