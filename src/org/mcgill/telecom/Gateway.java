package org.mcgill.telecom;
import java.net.*;
import java.io.*;
import org.json.*;
import org.mcgill.telecom.IMAP.*;

public class Gateway {
	ServerSocket server;
	Socket clientConnection;
	ObjectOutputStream send;
	ObjectInputStream receive;
	
	public Gateway(){
		try{
			server = new ServerSocket(1337);
			while(true){
				System.out.println("\nWaiting for connection ...");
				clientConnection = server.accept();
				System.out.println(clientConnection.getInetAddress() + " has connected");
				send = new ObjectOutputStream(clientConnection.getOutputStream());
				send.flush();
				receive = new ObjectInputStream(clientConnection.getInputStream());
			
				try {
					do{
						String[] login = (String[]) receive.readObject();
						Boolean success = IMAPHandler.login(login[0], login[1]);
						if (!success){sendMessage("Wrong login");}
					}while(!success)
	
					sendMessage("Connected");
					prompt();
				
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		catch(IOException ex){
			System.out.println("Error getting connection");
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
			else if (command.equals("sendmail")){
				return "To: " + json.getString("to") + "\nSubject: " + json.getString("subject") + "\nBody:" + json.getString("body");
			}
			else if (command.equals("header")){
				return "header " + json.getString("email");
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
