package org.mcgill.telecom.IMAP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.mcgill.telecom.Main;

public class IMAPServer {
	private Socket _socketServer = null;
	private DataInputStream _serverInputStream;
	private DataOutputStream _serverOutputStream;
	private MonitoredInputStream _monitoredInputStream;
	
	private int _commandCount = 0;

	/**
	 * Connect to the IMAP Server
	 */
	public void connect() throws IOException,FolderException{
		if (_socketServer==null){
//			if (CommStatus!=null) CommStatus.updateStatus("Connecting to \""+Config.getValue("Server","")+"\" ...");
			_socketServer=new Socket(Main.IMAP_SERVER, Main.IMAP_PORT);
			_serverInputStream=new DataInputStream(new BufferedInputStream(_monitoredInputStream=new MonitoredInputStream(_socketServer.getInputStream(),Main.IMAP_GRANULARITY)));
			_serverOutputStream=new DataOutputStream(new BufferedOutputStream(_socketServer.getOutputStream()));
			// Read in Server's greeting
			getResponse();
//			selectMailbox();
			
//			fireIMAPEvent(IMAPListener.CONNECTED);
//			if (fireReset) fireFolderEvent(FolderListener.RESET,null);
		}
	}
	
	/**
	 * Disconnect from the IMAP Server
	 * @param quick
	 */
	public void disconnect(boolean quick){
		if (_socketServer!=null){
//			if (CommStatus!=null) CommStatus.updateStatus("Disconnected.");
			try{
//				if (!quick) sendCommandAndHandle("LOGOUT");
				_socketServer.close();
			}catch(Throwable e){}
			_socketServer=null;
			
			Main.println("Disconnected");
		}
//		fireIMAPEvent(IMAPListener.DISCONNECTED);
	}	
	
	/**
	 * Login to IMAP Server
	 */
	public void login() throws IOException, FolderException
	{		
		if (sendCommandAndHandle("LOGIN "+Main.MAIL_ADDRESS+" "+Main.MAIL_PASSWORD) != 0) 
			new FolderException("Unable to login to IMAP Server, please check your Username and/or Password for \""+ Main.MAIL_ADDRESS);
	}
	
	/**
	 * Select Mailbox
	 */
	public void selectMailbox(String mailbox) throws IOException, FolderException
	{		
		if (sendCommandAndHandle("SELECT "+mailbox) != 0) 
			new FolderException("Error selecting: " + mailbox);
	}
	
	/**
	 * List all folders and sub-folders
	 */
	public void listFolders() throws IOException, FolderException
	{
		if (sendCommandAndHandle("list \"\" \"*\"") != 0); 
			new FolderException("Error listing folders");		
	}
	
	
	//=============================================================
	// Implementation
	//=============================================================
	
	/**
	 * Get a response from the IMAP Server
	 */
	private String getResponse() throws IOException{
		String response;
		try{
			response=readLine();

			if (response.charAt(response.length()-1)=='}'){
				int i=0,j=0,k=0;
				byte literal[];
				StringBuffer cummulativeStringBuffer=new StringBuffer(response);
	
				while(cummulativeStringBuffer.charAt(cummulativeStringBuffer.length()-1)=='}'){
					i=cummulativeStringBuffer.length()-2;j=0;k=1;
					while(cummulativeStringBuffer.charAt(i)!='{'){
						j+=(cummulativeStringBuffer.charAt(i--)-'0')*k;
						k*=10;
					}

					_monitoredInputStream.resetCounter();
//					if (CommStatus!=null&&j>ShowStatusThreshold) CommStatus.beginIO(MonitoredInput,j,"Downloading: ");
					try{
						literal=new byte[j];
						_serverInputStream.readFully(literal);
					}finally{
//						if (CommStatus!=null&&j>ShowStatusThreshold) CommStatus.done("Download Complete");
					}

					cummulativeStringBuffer.append(new String(literal));
					cummulativeStringBuffer.append(readLine());
				}

				response=cummulativeStringBuffer.toString();
			}
			Main.println("IMAP Server: "+((response.length()>1024) ? ("["+response.length()+" BYTES RESPONSE]") : response));
		}catch(IOException e){
			disconnect(true);
			throw e;			
		}
		return response;
	}		

	/**
	 * Read a line from the server input stream
	 */
	private String readLine() throws IOException{
		StringBuffer temp=new StringBuffer(64);
		byte Current=0,Prev=0;
		while(true){
			Prev=Current;Current=_serverInputStream.readByte();
			if (Current=='\n'&&Prev=='\r') break;
			if (Prev=='\r') temp.append('\r');
			if (Current!='\r') temp.append((char)Current);
		}
		return temp.toString();
	}
	
	/**
	 * Send a command to the IMAP Server
	 * @param cmdString Command to be sent
	 */
	private synchronized void sendCommand(String cmdString) throws IOException,FolderException{
		if (_socketServer==null) throw new FolderException("Please connect to the IMAP server to perform this operation !");
		try{
			_serverOutputStream.write('t');
			_serverOutputStream.writeBytes(Integer.toString(++_commandCount));
			_serverOutputStream.write(' ');
			_serverOutputStream.writeBytes(cmdString);
			_serverOutputStream.writeBytes("\r\n");
			_serverOutputStream.flush();
		}catch(IOException e){
			disconnect(true);
			throw e;
		}
		
		// Print out specific info for login
		if (cmdString.indexOf("LOGIN")==0)
			Main.println("Telecom: t"+Integer.toString(_commandCount)+" "+cmdString.substring(0,cmdString.lastIndexOf(' '))+" ****");
		else
			Main.println("Telecom: t"+Integer.toString(_commandCount)+" "+cmdString);
	}
	
	/**
	 * Sends a command and handles it
	 * @param cmdString Command sent
	 * @return 0 = OK, 1 = NO, 2 = BAD
	 */
	private int sendCommandAndHandle(String cmdString) throws IOException,FolderException{
		int completionStatus = 0;
		
		synchronized(this){
			if (cmdString!=null) sendCommand(cmdString);
			while(true){				
				String response=getResponse();
				String[] responseArray = response.split("\\s");
				
				// Select/Fetch/List
				if (response.charAt(0)=='*'){
					
				}
				// Normal command
				else if (response.charAt(0)=='t'){
					if (responseArray[1].equalsIgnoreCase("OK")){
						completionStatus=0;
					}else if (responseArray[1].equalsIgnoreCase("NO")){
						completionStatus=1;
					}else if (responseArray[1].equalsIgnoreCase("BAD")){
						completionStatus=2;
					}
					break;
				} else {
					break;
				}
			}
		}

		return completionStatus;
	}	
	
}
