/* 
 * Mailpuccino Free Java Email Client
 * http://members.xoom.com/konget/mailpuccino/dev/
 * Copyright (C) 2000 Kong Eu Tak

 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 */
package org.mcgill.telecom.SMTPtemp;

import java.util.*;
import java.io.*;
import java.net.*;

import org.mcgill.telecom.Main;
import org.mcgill.telecom.SMTP.MonitoredOutputStream;
import org.mcgill.telecom.SMTP.exceptions.SMTPErrorResponseException;
import org.mcgill.telecom.SMTP.exceptions.SMTPException;
import org.mcgill.telecom.SMTP.exceptions.SMTPLoginFailedException;
import org.mcgill.telecom.SMTP.exceptions.SMTPUnconnectedException;

// An Object of this Class represents the server to which the Messages that the user has created and sent are delivered to
public class SMTPServer{
	String _lastReply;
	Socket _socketServer=null;
	BufferedReader _serverResponseStream=null;
	BufferedWriter _serverOutputStream=null;
	MonitoredOutputStream _outputStreamMonitor=null;
	
	public SMTPServer(){}

	public String getServerName(){return Main.SMTP_SERVER;}
	public int getPort(){return Main.SMTP_PORT;}

	public void sendEmail(SendMailProcess.QueueItem SendItem,String RawMessage) throws IOException,SMTPException{

		SendItem.Listener.updateStatus("Preparing to send message...");
		try{
			if (sendCommand("RSET")!=250) throw new SMTPErrorResponseException(_lastReply.substring(4));
			if (sendCommand("MAIL FROM:"+SendItem.Target.getHeader("Reply-To"))!=250) throw new SMTPErrorResponseException(_lastReply.substring(4));
			for (int i=0;i<3;i++) walkAddressList(SendItem.Target.getRecipients(i));
			if (sendCommand("DATA")!=354) throw new SMTPErrorResponseException(_lastReply.substring(4));
			if (sendRawMessage(RawMessage,SendItem.Listener)!=250) throw new SMTPErrorResponseException(_lastReply.substring(4));
			SendItem.Listener.done("Message sent successfully !");
		}catch(IOException e){
			SendItem.Listener.abort("Error occured, aborting send !");
			throw e;
		}catch(SMTPException e){
			SendItem.Listener.abort("Error occured, aborting send !");
			throw e;
		}
	}

	void walkAddressList(AddressList Target) throws IOException,SMTPException{
		Object Current;
		int Reply;
		
		for (Enumeration AllEntries=Target.getMembers().elements();AllEntries.hasMoreElements();){
			if ((Current=AllEntries.nextElement()) instanceof AddressList){
				walkAddressList((AddressList)Current);
			}else{
				Reply=sendCommand("RCPT TO:"+((Address)Current).getEmailAddress());
				if (Reply!=250&&Reply!=251) throw new SMTPErrorResponseException(_lastReply.substring(4));
			}
		}
	}

	public void connect() throws IOException,SMTPException{
		_socketServer=new Socket(getServerName(),getPort());
		_serverResponseStream=new BufferedReader(new InputStreamReader(_socketServer.getInputStream()));		
		_serverOutputStream=new BufferedWriter(new OutputStreamWriter(_outputStreamMonitor=new MonitoredOutputStream(_socketServer.getOutputStream(),Main.SMTP_GRANULARITY)));		
		if (Integer.parseInt(getResponse().substring(0,3))!=220) throw new SMTPLoginFailedException(_lastReply.substring(4));
		if (sendCommand("HELO Me")!=250) throw new SMTPLoginFailedException(_lastReply.substring(4));
	}

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

	int sendCommand(String commandString) throws IOException,SMTPException{
		if (_socketServer==null) throw new SMTPUnconnectedException("Not Connected to SMTP server!");
		Main.println("Telecom sendCommand: " + commandString);
		_serverOutputStream.write(commandString);
		_serverOutputStream.write("\r\n");
		_serverOutputStream.flush();
		return Integer.parseInt(getResponse().substring(0,3));
	}

	int sendRawMessage(String Data,CommStatusListener Status) throws IOException,SMTPException{
		int Pos=0,NextPos=Data.indexOf("\r\n.");
		
		if (_socketServer==null) throw new SMTPUnconnectedException("Not Connected to SMTP server !");

		// For status monitoring
		_outputStreamMonitor.resetCounter();
//		Status.beginIO(_outputStreamMonitor,Data.length(),"Sending: ");

		while(NextPos!=-1){
			_serverOutputStream.write(Data.substring(Pos,NextPos));
			_serverOutputStream.write("\r\n..");
			Pos=NextPos+3;
			NextPos=Data.indexOf("\r\n.",NextPos+1);
		}
		_serverOutputStream.write(Data.substring(Pos));
		_serverOutputStream.write("\r\n.\r\n");
		_serverOutputStream.flush();
		return Integer.parseInt(getResponse().substring(0,3));
	}

	String getResponse() throws IOException{
		do{
			_lastReply=_serverResponseStream.readLine();
			Main.println("Server: " + _lastReply);
		} while(_lastReply.charAt(3)=='-');
		return _lastReply;
	}
}