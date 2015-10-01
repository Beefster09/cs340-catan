package client.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import shared.exceptions.GameInitializationException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.JoinGameException;
import shared.exceptions.NameAlreadyInUseException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;

/**
 * 
 * @author Steven Pulsipher
 * Communication between client and server
 */
public class ClientCommunicator {

	private String userCookie = null;
	private String gameCookie = null;
	private String cookies = null;
	
	/**
	 * creates a new ClientCommunicator
	 */
	public ClientCommunicator(){}
	
	
	@SuppressWarnings({ "deprecation" })
	public JSONObject login(JSONObject o)
			throws ServerException, UserException{
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
				JSONObject JSONOutput;
				
				String header = con.getHeaderField("Set-cookie");
				header = header.substring(0, header.length() - 8);
				String cutHeader = header.substring(11);
				JSONOutput = (JSONObject) parser.parse(URLDecoder.decode(cutHeader));
				userCookie = header;
				gameCookie = null;
				cookies = userCookie;
				
				return JSONOutput;
			}
			if(o.get("url").equals("http://localhost:8081/user/register")){
				throw new NameAlreadyInUseException();
			}
			throw new UserException();
		}
		catch(IOException | ParseException e){
			throw new ServerException();
		}
	}

	public JSONObject preJoin(JSONObject o)
			throws ServerException, InvalidActionException, GameInitializationException{		
		if(userCookie == null){
			throw new InvalidActionException();
		}
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
				JSONObject JSONOutput;
				
				if(str.charAt(0) == '['){
					str = new StringBuilder("{\"games\":" + str + "}");
				}
				if(str.charAt(0) != '{'){
					JSONOutput = new JSONObject();
					return JSONOutput;
				}
				JSONOutput = (JSONObject) parser.parse(str.toString());
				return JSONOutput;
			}
			if(o.get("url").equals("http://localhost:8081/games/create")){
				throw new GameInitializationException();
			}
			throw new InvalidActionException();
		}
		catch(IOException | ParseException e){
			throw new ServerException();
		}
	}
	@SuppressWarnings({ "unchecked" })
	public JSONObject joinGame(JSONObject o)
			throws ServerException, JoinGameException{
		if(userCookie == null){
			throw new JoinGameException();
		}
		try{
			URL url = new URL((String) o.get("url"));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Cookie", cookies);
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
				
				String header = con.getHeaderField("Set-cookie");
				header = header.substring(0, header.length() - 8);
				gameCookie = header;
				cookies = userCookie + "; " + gameCookie;
				
				JSONObject JSONOutput = new JSONObject();
				JSONOutput.put("success", str.toString());
		
				return JSONOutput;
			}
			
			throw new JoinGameException();
		}
		catch(IOException e){
			throw new ServerException();
		}
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
	public JSONObject send(JSONObject o)
			throws ServerException, InvalidActionException{
		if(userCookie == null || gameCookie == null){
			throw new InvalidActionException();
		}
		try{
			URL url = new URL((String) o.get("url"));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Cookie", cookies);
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
				JSONObject JSONOutput;
				if(str.charAt(0) == '['){
					str = new StringBuilder("{\"list\":" + str + "}");
				}
				JSONOutput = (JSONObject) parser.parse(str.toString());
				return JSONOutput;
			}
			InputStream input = con.getErrorStream();
			int len = 0;
			
			byte[] buffer = new byte[1024];
			StringBuilder str = new StringBuilder();
			while(-1 != (len = input.read(buffer))){
				str.append(new String(buffer, 0, len));
			}

			if(str.charAt(0) == '['){
				str = new StringBuilder("{\"list\":" + str + "}");
			}
			System.out.println(str.toString());
			throw new InvalidActionException();
		}
		catch(IOException | ParseException e){
			throw new ServerException();
		}
	}
}
