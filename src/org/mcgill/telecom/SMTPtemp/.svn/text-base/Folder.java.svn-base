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

import java.io.*;
import java.util.*;
import javax.swing.*;

import org.mcgill.telecom.base.ComponentType;
import org.mcgill.telecom.base.FolderListener;
import org.mcgill.telecom.base.InterfaceJPanel;

public interface Folder extends ComponentType{
	// Meta-info
	public String getDescription();

	// Miscellaneous 
	public boolean moveToTrash();
	//public void init(Preferences Config); // TODO: TEMP
	public String getFolderName();
	public void addFolderListener(FolderListener Target);
	public void removeFolderListener(FolderListener Target);
	public void setCommStatusListener(CommStatusListener Target);

	// GUI Interface methods
	public InterfaceJPanel getInterface();	
	public Icon getIcon();

	// Blocked methods
	public void open() throws IOException,FolderException;
	public void close() throws IOException,FolderException;	
	public Enumeration getAllEmails();
	public int getEmailCount();
	public EmailID insertEmail(EmailID TargetID) throws IOException,FolderException;
	public void deleteEmail(EmailID TargetID) throws IOException,FolderException;
	public String getRawMessage(EmailID TargetID) throws IOException,FolderException;
	public String getHeader(EmailID TargetID,String HeaderName) throws IOException,FolderException;
	public String getAllHeaders(EmailID TargetID) throws IOException,FolderException;
	public int getStatus(EmailID TargetID,String Type) throws IOException,FolderException;
	public void setStatus(EmailID TargetID,String Type,int Value) throws IOException,FolderException;
	public Object getPart(EmailID TargetID,PartStruct Part) throws IOException,FolderException;
	public PartStruct getPartStruct(EmailID TargetID) throws IOException,FolderException;
}