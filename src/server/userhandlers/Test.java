package server.userhandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import server.communication.ServerCommunicator;
import shared.communication.GameHeader;
import shared.communication.PlayerHeader;
import shared.communication.Session;
import client.communication.ServerProxy;

public class Test {

	public static void main(String[] args) throws Exception{
		String[] arg = new String[1];
		arg[0] = "8081";
		ServerCommunicator.main(arg);
		new Test();
		System.exit(0);
	}
	
	public Test() throws Exception{
		ServerProxy server = new ServerProxy("localhost", 8081);
				
		Session user = server.login("Sam", "sam");
		System.out.println(user.getUsername());
		user = server.register("Steve", "steve");
		System.out.println(user.getUsername());
		GameHeader game = server.createGame("blah", true, true, true);
		System.out.println(game.getTitle());
	}
}