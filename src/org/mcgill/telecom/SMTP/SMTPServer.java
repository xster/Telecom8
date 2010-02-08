package org.mcgill.telecom.SMTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.mcgill.telecom.Main;
import org.mcgill.telecom.SMTP.exceptions.SMTPErrorResponseException;
import org.mcgill.telecom.SMTP.exceptions.SMTPException;
import org.mcgill.telecom.SMTP.exceptions.SMTPLoginFailedException;
import org.mcgill.telecom.SMTP.exceptions.SMTPUnconnectedException;

public class SMTPServer {
	String _lastReply;
	Socket _socketServer=null;
	BufferedReader _serverResponseStream=null;
	BufferedWriter _serverOutputStream=null;
	MonitoredOutputStream _outputStreamMonitor=null;
	
	private String getServerName(){return Main.SMTP_SERVER;}
	private int getPort(){return Main.SMTP_PORT;}
	private String getResponse() throws IOException{
		do{
			_lastReply=_serverResponseStream.readLine();
			Main.println("Server: " + _lastReply);
		} while(_lastReply.charAt(3)=='-');
		return _lastReply;
	}
	
	/**
	 * Send an E-mail
	 */
	public void sendEmail(EmailItem emailItem) throws IOException,SMTPException{

//		SendItem.Listener.updateStatus("Preparing to send message...");
		try{
			if (sendCommand("RSET")!=250) throw new SMTPErrorResponseException(_lastReply.substring(4));
			
			// Login
			if (sendCommand("AUTH LOGIN")!=334) throw new SMTPErrorResponseException(_lastReply.substring(4));
			String tmpEncoded64 = Base64.encodeString(Main.MAIL_ADDRESS);			
			if (sendCommand(tmpEncoded64)!=334) throw new SMTPErrorResponseException(_lastReply.substring(4));
			tmpEncoded64 = Base64.encodeString(Main.MAIL_PASSWORD);
			if (sendCommand(tmpEncoded64)!=235) throw new SMTPErrorResponseException(_lastReply.substring(4));			
							
			if (sendCommand("MAIL FROM:"+emailItem.FROM)!=250) throw new SMTPErrorResponseException(_lastReply.substring(4));
			int tmp = sendCommand("RCPT TO:"+emailItem.TO);
			if (tmp!=250 && tmp!=251) throw new SMTPErrorResponseException(_lastReply.substring(4));			
			
			if (sendCommand("DATA")!=354) throw new SMTPErrorResponseException(_lastReply.substring(4));
			if (sendCommand(emailItem.getRawMessage())!=250) throw new SMTPErrorResponseException(_lastReply.substring(4));
			Main.println("Message sent successfully!");
			
		}catch(IOException e){
			Main.println("Error occured, aborting send e-mail!");
			throw e;
		}catch(SMTPException e){
			Main.println("Error occured, aborting send e-mail!");
			throw e;
		}
	}	
	
	/**
	 * Connect to the SMTP Server
	 */
	public void connect() throws IOException,SMTPException{
		_socketServer=new Socket(getServerName(),getPort());
		_serverResponseStream=new BufferedReader(new InputStreamReader(_socketServer.getInputStream()));		
		_serverOutputStream=new BufferedWriter(new OutputStreamWriter(_outputStreamMonitor=new MonitoredOutputStream(_socketServer.getOutputStream(),Main.SMTP_GRANULARITY)));		
		if (Integer.parseInt(getResponse().substring(0,3))!=220) throw new SMTPLoginFailedException(_lastReply.substring(4));
		if (sendCommand("HELO Me")!=250) throw new SMTPLoginFailedException(_lastReply.substring(4));
	}
	
	/**
	 * Disconnect from the SMTP Server
	 */
	public void disconnect(){
		if (_socketServer==null) return;
		try{
			sendCommand("QUIT");
			_socketServer.close();
			_socketServer=null;
		}catch(Throwable e){
			Main.println(e);
		}
	}	
	
	/**
	 * Send a command to the SMTP Server
	 */
	private int sendCommand(String commandString) throws IOException,SMTPException{
		if (_socketServer==null) throw new SMTPUnconnectedException("Not Connected to SMTP server!");
		Main.println("Telecom sendCommand: " + commandString);
		_serverOutputStream.write(commandString);
		_serverOutputStream.write("\r\n");
		_serverOutputStream.flush();
		return Integer.parseInt(getResponse().substring(0,3));
	}

}
