package org.mcgill.telecom;

import org.mcgill.telecom.IMAP.IMAPHandler;
import org.mcgill.telecom.SMTP.SMTPHandler;

public class Main {

	public final static String MAIL_ADDRESS = "john.terry1@gmx.com";
	public final static String MAIL_PASSWORD = "testing1234";
	
	public final static String POP3_SERVER = "pop.gmx.com";
	
	public final static String SMTP_SERVER = "mail.gmx.com";
	public final static int SMTP_PORT = 587;
	public final static int SMTP_GRANULARITY = 2048;
	
	public final static String IMAP_SERVER = "imap.gmx.com";
	public final static int IMAP_PORT = 143;
	public final static int IMAP_GRANULARITY = 4096;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = new String(args[0]);
		if (s.equals("c"))
		{
			Client c = new Client();
		}
		else if (s.equals("g"))
		{
			Gateway g = new Gateway();
		}
		else if (s.equals("smtp"))
		{
			SMTPHandler.smtp("test");
		}
		else if (s.equals("imap"))
		{
			IMAPHandler.imap();
		}		
	}
	
	/**
	 * Print to console.
	 * @param string
	 */
	public static void println(Object string){
		System.out.println(string);
	}
	


}
