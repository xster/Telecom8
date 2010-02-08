package org.mcgill.telecom;
import java.net.*;
import java.io.*;
import org.json.*;

public class Gateway {
	ServerSocket server;
	Socket clientConnection;
	ObjectOutputStream send;
	ObjectInputStream receive;
	
	public Gateway(){
		try{
			server = new ServerSocket(1337);
			System.out.println("Waiting for connection ...");
			clientConnection = server.accept();
			System.out.println(clientConnection.getInetAddress() + " has connected");
			send = new ObjectOutputStream(clientConnection.getOutputStream());
			send.flush();
			receive = new ObjectInputStream(clientConnection.getInputStream());
			
			try {
				String[] login = (String[]) receive.readObject();
				System.out.print(login[0] + " " + login[1] + "\n");
				
				sendMessage("Connected");
				prompt();
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		catch(IOException ex){
			
		}		
	}
	
	private void prompt(){
		JSONObject json;
		try{
			Boolean first = true;
			while (true){
				System.out.println("Waiting for command...");
				json = new JSONObject((String) receive.readObject());
				sendMessage(interpretCommand(json));
			}
		}
		catch(Exception ex){
			System.out.println("Lost connection");
		}
		
		System.out.println("Disconnecting...");
	}
	
	private String interpretCommand(JSONObject json){
		try{
			String command = json.getString("command");
			System.out.println(command);
			if (command.equals("folders")){
				return "folder\nfolder\n";
			}
			else{
				return "nothing";
			}
		}
		catch (JSONException ex){
			return "Broken command";
		}
	}
	
	private void sendMessage(Object message){
		try{
			send.writeObject(message);
			send.flush();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		System.out.println("Run Gateway");
		new Gateway();
	}
}
