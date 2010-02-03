package org.mcgill.telecom;
import java.net.*;
import java.io.*;

public class Client {
	Socket serverConnection;
	ObjectOutputStream send;
	ObjectInputStream receive;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	private void connect(){
		try{
			serverConnection = new Socket("localhost", 1337);
			System.out.println("Connected to gateway");
			send = new ObjectOutputStream(serverConnection.getOutputStream());
			send.flush();
			receive = new ObjectInputStream(serverConnection.getInputStream());
			
			String username = ask("Username:");
			String password = ask("Password:");
			
			sendMessage(new String[] {username, password});
		}
		catch(IOException ex){}
	}
	
	private String ask(String query){
		System.out.println(query);
		try{
			return reader.readLine();
		}
		catch(IOException ex){
			return "";
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
	
	public void main(){
		connect();
	}
}
