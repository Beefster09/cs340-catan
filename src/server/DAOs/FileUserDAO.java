package server.DAOs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import server.model.User;

/**
 * This class contains implementation for storing, deleting, and updating
 * users in a file-based database system.
 * @author jchip
 *
 */
public class FileUserDAO implements IUserDAO {

	private String filePath;
	
	public FileUserDAO(){
		filePath = "fileStorage/users/users.txt";
	}
	
	@Override
	public void addUser(User user) {
		try{
			List<User> users = getAllUsers();
			users.add(user);
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(users);
			out.close();
			fileOut.close();
		}
		catch(IOException | DatabaseException e){
			e.printStackTrace();
		}		
	}

	//Not Needed
	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		
	}

	//Not Needed
	@Override
	public void updateUserPassword(User user) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	//Not Needed
	@Override
	public User getUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() throws DatabaseException {
		List<User> users = new ArrayList<User>();
		try{
			FileInputStream fileIn = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);

			users = (List<User>) in.readObject();
			
			in.close();
			fileIn.close();
		}
		catch(IOException e){
			System.out.println("File was empty");
		}
		catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		return users;
	}
}
