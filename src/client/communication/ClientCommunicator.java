package client.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import shared.exceptions.ServerException;
import shared.exceptions.UserException;

/**
 * 
 * @author Steven Pulsipher
 * Communication between client and server
 */
public class ClientCommunicator {

	/**
	 * creates a new ClientCommunicator
	 */
	public ClientCommunicator(){
	}
	
	/**
	 * Sends to the server
	 * @param o the JSON Object that is going to be sent
	 * @pre JSON Object is valid, and contains a location to be sent as well as "Get" or "Post"
	 * @post Response from the server will be given
	 * @return response object from server
	 * @throws ServerException
	 * @throws UserException 
	 */
	public JSONObject send(JSONObject o) throws ServerException, UserException{
		try{
			URL url = new URL((String) o.get("url"));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod((String) o.get("requestType"));
			con.setDoOutput(true);
			con.connect();
			
			OutputStreamWriter output = new OutputStreamWriter(con.getOutputStream());
			output.write(o.toString());
			output.flush();
			
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK){
				InputStream input = con.getInputStream();
				int len = 0;
				
				byte[] buffer = new byte[1024];
				StringBuilder str = new StringBuilder();
				while(-1 != (len = input.read(buffer))){
					str.append(new String(buffer, 0, len));
				}
				JSONParser parser = new JSONParser();
				JSONObject JSONOutput = (JSONObject) parser.parse(str.toString());
				return JSONOutput;
			}
			throw new UserException();
		}
		catch(IOException | ParseException e){
			throw new ServerException();
		}
	}
}
