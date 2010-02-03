package org.mcgill.telecom;
import java.net.*;
import java.io.*;

public class Gateway {
	ServerSocket server;
	Socket clientConnection;
	ObjectOutputStream send;
	ObjectInputStream receive;
	
	private void listen(){
		try{
			server = new ServerSocket(1337);
			System.out.println("Waiting for connection ...");
			clientConnection = server.accept();
			System.out.println(clientConnection.getInetAddress().getHostName() + " has connected");
			send = new ObjectOutputStream(clientConnection.getOutputStream());
			send.flush();
			receive = new ObjectInputStream(clientConnection.getInputStream());
			
			try {
				String[] login = (String[]) receive.readObject();
				System.out.print(login[0]);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		catch(IOException ex){
			
		}		
	}
	
	public void main(String args[]){
		listen();
	}
}
