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

import org.mcgill.telecom.Main;
import org.mcgill.telecom.base.FolderConstants;

// An Object of this Class represents the background process (Thread) where newly-composed Messages are sent to the Mail Server
public class SendMailProcess implements Runnable{
	Object Lock=new Object();
	Vector SendQueue=new Vector();
	Thread SendMailThread;
	boolean RequestCancel;
	SMTPServer Server;
	
	public SendMailProcess(SMTPServer Server){
		this.Server=Server;

		SendMailThread=new Thread(this);
		SendMailThread.start();				
	}

	public void sendMail(OutgoingEmail Target,CommStatusListener Listener){
		SendQueue.addElement(new QueueItem(Target,Listener));
		synchronized(Lock){Lock.notifyAll();}
	}

	public void kill(){
		SendMailThread.stop();
		Server.disconnect();
	}

	public void run(){
		String RawMessage;
		EmailID TheEmailID;
		Folder SentFolder;

		while(true){
			if (SendQueue.size()==0) synchronized(Lock){
				try{
					Lock.wait();
				}catch(InterruptedException e){
					Main.println("SendMail Agent: Thread Interrupted!");					
				}
			}

//			// Open Sent Folder
//			SentFolder=null;
//			if (Mailpuccino.getUserPrefs().getValue("General.EnableSentFolder","false").equals("true")){
//				SentFolder=FolderManager.getSentFolder();
//				try{
//					if (SentFolder!=null) FolderManager.open(SentFolder);
//				}catch(Exception e){
//					new MessageDialog("SendMail Agent","An error occured while trying to open the Sent Folder \""+SentFolder.getFolderName()+"\"",Mailpuccino.getResourceImageIcon("Exclaim.gif"),e);
//					SentFolder=null;
//				}
//			}

			try{
				Server.connect();
			}catch(Exception e){
				Server.disconnect();SendQueue.removeAllElements();
				Main.println("SendMail Agent: An error occured while trying to connect to the SMTP Server.\nPlease make sure your computer is online/your SMTP Server configuration preferences are correct!");
				continue;
			}

			while (!SendQueue.isEmpty()){
				try{
					Server.sendEmail((QueueItem)SendQueue.elementAt(0),RawMessage=((QueueItem)SendQueue.elementAt(0)).Target.getRawMessage());
//					TheEmailID=new RawMessageEmailID(RawMessage);
//					TheEmailID.setStatus(FolderConstants.STATUS_READ,1);
//					if (SentFolder!=null) SentFolder.insertEmail(TheEmailID);
					RawMessage=null;
					TheEmailID=null;
				}finally{
					SendQueue.removeElementAt(0);
					continue;
				}
			}

			Server.disconnect();

//			try{
//				if (SentFolder!=null) FolderManager.close(SentFolder);
//			}catch(Throwable e){
//				Main.println("SendMail Agent: An error occured while closing the Sent Folder \""+SentFolder.getFolderName()+"\".")
//				SentFolder=null;
//			}
		}
	}

	static class QueueItem{
		public OutgoingEmail Target;
		public CommStatusListener Listener;
		
		public QueueItem(OutgoingEmail Target,CommStatusListener Listener){
			this.Target=Target;
			this.Listener=Listener;
		}
	}
}