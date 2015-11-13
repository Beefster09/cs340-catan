package server.userhandlers;

import server.communication.ServerCommunicator;
import client.communication.ServerProxy;

public class Test {

	public static void main(String[] args) throws Exception{
		String[] arg = new String[1];
		arg[0] = "8081";
		ServerCommunicator.main(arg);
		new Test();
	}
	
	public Test() throws Exception{
		ServerProxy server = new ServerProxy("localhost", 8081);
		
		server.login("Sam", "sam");
		server.register("Steve", "steve");
	}
}
