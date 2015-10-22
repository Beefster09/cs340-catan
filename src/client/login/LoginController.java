package client.login;

import client.base.*;

import java.util.logging.*;

import client.communication.ServerProxy;
import client.misc.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import shared.communication.IServer;
import shared.communication.Session;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.ModelFacade;


/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController {

	private IMessageView messageView;
	private IAction loginAction;
	private IServer serverProxy = ServerProxy.getInstance();
	private ModelFacade modelFacade = ModelFacade.getInstance();
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * LoginController constructor
	 * 
	 * @param view Login view
	 * @param messageView Message view (used to display error messages that occur during the login process)
	 */
	public LoginController(ILoginView view, IMessageView messageView) {

		super(view);
		
		this.messageView = messageView;
	}
	
	public ILoginView getLoginView() {
		
		return (ILoginView)super.getView();
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	
	/**
	 * Sets the action to be executed when the user logs in
	 * 
	 * @param value The action to be executed when the user logs in
	 */
	public void setLoginAction(IAction value) {
		
		loginAction = value;
	}
	
	/**
	 * Returns the action to be executed when the user logs in
	 * 
	 * @return The action to be executed when the user logs in
	 */
	public IAction getLoginAction() {
		
		return loginAction;
	}

	@Override
	public void start() {
		
		getLoginView().showModal();
	}

	@Override
	public void signIn() {
		
		// TODO: log in user
		String username = getLoginView().getLoginUsername();
		String password = getLoginView().getLoginPassword();

		try {
			Session player = serverProxy.login(username, password);
			modelFacade.setLocalPlayer(player);
			logger.log(Level.INFO, "Login was successful");
			// If log in succeeded
			getLoginView().closeModal();
			loginAction.execute();
		} catch (UserException e) {
			messageView.setTitle("Invalid credentials");
			messageView.setMessage("Invalid username/password. Please try again.");
			messageView.showModal();
		} catch (ServerException e) {
			messageView.setTitle("Server Error");
			messageView.setMessage("Unable to reach server at this point");
			messageView.showModal();
		}

	}

	@Override
	public void register() {
		
		String username = getLoginView().getRegisterUsername();
		String password = getLoginView().getRegisterPassword();
		String repeatPassword = getLoginView().getRegisterPasswordRepeat();
		
		//Check if the passwords are the same
		if (!password.equals(repeatPassword)) {
			messageView.setTitle("Passwords differ");
			messageView.setMessage("The password are different. Please try again.");
			messageView.showModal();
			return;
		}
		
		try {
			Session player = serverProxy.register(username, password);
			modelFacade.setLocalPlayer(player);
			System.out.println("Register was successful");
			logger.log(Level.INFO, "Register was successful");
			// If register succeeded
			getLoginView().closeModal();
			loginAction.execute();
		} catch (UserException e) {
			messageView.setTitle("Username taken");
			messageView.setMessage("User already exists. Please try again.");
			messageView.showModal();
		} catch (ServerException e) {
			messageView.setTitle("Server Error");
			messageView.setMessage("Unable to reach server at this point");
			messageView.showModal();
		}
	}

}

