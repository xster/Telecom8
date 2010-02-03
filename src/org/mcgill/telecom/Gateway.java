package org.mcgill.telecom;
import java.io.IOException;
import java.net.*;

public class Gateway {
	ServerSocket server;
	Socket clientConnection;
	
	private void listen(){
		try{
			server = new ServerSocket(1337);
			System.out.println("Waiting for connection ...");
			clientConnection = server.accept();
		}
		catch(IOException ex){
			
		}		
	}
	
	public void main(String args[]){
		listen();
	}
}
