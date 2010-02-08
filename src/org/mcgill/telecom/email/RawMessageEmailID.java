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
package org.mcgill.telecom.email;

import java.io.*;
import java.util.*;

import org.mcgill.telecom.SMTPtemp.EmailID;
import org.mcgill.telecom.SMTPtemp.FolderException;
import org.mcgill.telecom.SMTPtemp.PartStruct;

public class RawMessageEmailID extends EmailID{
//	String RawMessage=null;
//	PartStruct TheStruct=null;
//	Hashtable StatusFlags=new Hashtable();
//	int HeaderEnd;
//
//	public RawMessageEmailID(String RawMessage){
//		this.RawMessage=RawMessage;
//		HeaderEnd=Mailpuccino.search(0,RawMessage.length(),RawMessage,"\r\n\r\n")+4;
//	}
//
//	public String getRawMessage() throws IOException,FolderException{return RawMessage;}
//	public String getHeader(String HeaderName) throws IOException,FolderException{return LocalFolder.parseHeader(0,HeaderEnd,RawMessage,HeaderName);}
//	public String getAllHeaders() throws IOException,FolderException{return RawMessage.substring(0,HeaderEnd);}
//	public void setStatus(String Type,int Value) throws IOException,FolderException{StatusFlags.put(Type,new Integer(Value));}
//	public Object getPart(PartStruct Part) throws IOException,FolderException{return LocalFolder.getPartData(RawMessage,Part);}
//	public PartStruct getPartStruct() throws IOException,FolderException{return ((TheStruct==null) ? TheStruct=LocalFolder.buildPartStruct(RawMessage,0,RawMessage.length()) : TheStruct);}
//	public void delete() throws IOException,FolderException{}
//	public boolean equals(Object Target){return RawMessage.equals(((RawMessageEmailID)Target).RawMessage);}
//
//	public int getStatus(String Type) throws IOException,FolderException{
//		Integer Val=(Integer)StatusFlags.get(Type);
//		return (Val==null) ? 0 : Val.intValue();
//	}
}