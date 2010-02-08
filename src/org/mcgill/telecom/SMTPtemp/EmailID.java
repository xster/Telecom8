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

public abstract class EmailID{
	public Folder TargetFolder;

	public String getRawMessage() throws IOException,FolderException{return TargetFolder.getRawMessage(this);}
	public String getHeader(String HeaderName) throws IOException,FolderException{return TargetFolder.getHeader(this,HeaderName);}
	public String getAllHeaders() throws IOException,FolderException{return TargetFolder.getAllHeaders(this);}
	public int getStatus(String Type) throws IOException,FolderException{return TargetFolder.getStatus(this,Type);}
	public void setStatus(String Type,int Value) throws IOException,FolderException{TargetFolder.setStatus(this,Type,Value);}
	public Object getPart(PartStruct Part) throws IOException,FolderException{return TargetFolder.getPart(this,Part);}
	public PartStruct getPartStruct() throws IOException,FolderException{return TargetFolder.getPartStruct(this);}
	public void delete() throws IOException,FolderException{TargetFolder.deleteEmail(this);}
	public boolean equals(Object Target){return ((EmailID)Target).TargetFolder==TargetFolder;}
}