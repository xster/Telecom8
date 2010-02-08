package org.mcgill.telecom;
import java.net.*;
import java.io.*;
import org.json.*;

public class Client {
	Socket serverConnection;
	ObjectOutputStream send;
	ObjectInputStream receive;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public Client(){
		try{
			serverConnection = new Socket("localhost", 1337);
			System.out.println("Connected to gateway");
			send = new ObjectOutputStream(serverConnection.getOutputStream());
			send.flush();
			receive = new ObjectInputStream(serverConnection.getInputStream());
			
			do{
				String username = ask("Username: ");
				String password = ask("Password: ");

				sendMessage(new String[] {username, password});

				String response = (String) receive.readObject();
				
				if ("Connected".equals(response)){
					System.out.println("Gateway connected!\n");
					prompt();
					System.exit(0);
				}
				else{
					System.out.println("Invalid login\n");
				}
			}while(true);
		}
		catch(Exception ex){
			System.out.println("Can't connect to server");
		}
	}
	
	public void prompt(){
		String command;
		JSONObject json;
		try{
			command = ask("Please enter command:\n>");
			while (!command.equals("exit")){
				json = new JSONObject().put("command", command);
				sendMessage(json.toString());
				print(receive.readObject());
				command = ask("Please enter command:\n>");
			}
		}
		catch (Exception ex){
			System.out.println("Lost connection");
		}
		System.out.println("Exiting...");
		
	}
	
	private String ask(String query){
		System.out.print(query);
		try{
			return reader.readLine();
		}
		catch(IOException ex){
			return "";
		}
	}
	
	private void print(Object text){
		try{
			System.out.println((String) text);
		}
		catch(Exception ex){
			System.out.println("Not text!\n");
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
        System.out.println("Run Client");
		new Client();
	}
}
