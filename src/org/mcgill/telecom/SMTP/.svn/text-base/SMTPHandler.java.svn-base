package org.mcgill.telecom.SMTP;

import org.mcgill.telecom.Main;

public class SMTPHandler {
	
	private static SMTPServer _server = null;
	
	public static void smtp(String message){
		Main.println("Running SMTP test");
		_server = new SMTPServer();
		
		try{
			_server.connect();
		}catch(Exception e){
			_server.disconnect();
			Main.println("SendMail Agent: An error occured while trying to connect to the SMTP Server.\nPlease make sure your computer is online/your SMTP Server configuration preferences are correct!");
		}

		// Send an e-mail
		try {
			EmailItem item = new EmailItem();
			item.setMessage(message);
			_server.sendEmail(new EmailItem());
		} catch (Exception e) {
			Main.println(e);
		}
		
		_server.disconnect();		
	}	
	
}
