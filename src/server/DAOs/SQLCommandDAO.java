package server.DAOs;

import java.util.logging.Logger;

import server.commands.ICatanCommand;

public class SQLCommandDAO implements ICommandDAO {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private SQLDatabase db;
	
	/**
	 * Creates an instance of a CellDAO to interact with the database.
	 */
	public SQLCommandDAO(SQLDatabase db) {
		this.db = db;
	}

	@Override
	public void addCommand(ICatanCommand command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCommand(ICatanCommand command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAll() {
		// TODO Auto-generated method stub

	}

}
