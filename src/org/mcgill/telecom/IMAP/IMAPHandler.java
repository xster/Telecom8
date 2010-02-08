package org.mcgill.telecom.IMAP;

import java.io.IOException;

import org.mcgill.telecom.Main;
import org.mcgill.telecom.SMTP.EmailItem;
import org.mcgill.telecom.SMTP.SMTPServer;

public class IMAPHandler {
	private static IMAPServer _server = null;
	
	public static void imap(){
		Main.println("Running IMAP test");
		_server = new IMAPServer();
		
		try{
			_server.connect();
		}catch(Exception e){
			_server.disconnect(true);
			Main.println("SendMail Agent: An error occured while trying to connect to the IMAP Server.\nPlease make sure your computer is online/your IMAP Server configuration preferences are correct!");
		}
		
		try{
			_server.login();
			_server.listFolders();
			_server.selectMailbox("INBOX");
		}catch(Exception e){
			_server.disconnect(true);
			Main.println("SendMail Agent: An error occured while trying to login to the IMAP Server.");
		}		
				
		_server.disconnect(true);		
	}
}
