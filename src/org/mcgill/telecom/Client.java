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
				json = composeMessage(command);
				if (json != null){
					sendMessage(json.toString());
					print(receive.readObject());
				}
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
	
	private JSONObject composeMessage(String command){
		try{
			JSONObject message = new JSONObject().put("command", command);
			if (command.equals("sendmail")){
				String query = ask("To: ");
				message.put("to", query);
				query = ask("Subject: ");
				message.put("subject", query);
				query = "";
				print("Please enter email body.\n(End message with a '.' on a new line)");
				String last;
				do{
					last = ask("");
					query += last + "\n";
				}while(!last.equals("."));
				message.put("body", query);
			}
			else if (command.startsWith("header")){
				String[] split = command.split(" ");
				if (split.length == 2 && split[1].matches("^\\d+$")){
					message.put("command", split[0]);
					message.put("email", Integer.parseInt(split[1]));
				}
				else{
					print("Please specify the header to retrieve\nie [header 1]");
					return null;
				}
			}
			return message;
		}
		catch (JSONException ex){
			print("Bad command");
			return null;
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
