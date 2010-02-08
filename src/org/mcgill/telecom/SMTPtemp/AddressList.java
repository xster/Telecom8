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

public class AddressList implements Saveable{
	String Name;
	Vector Members;

	public AddressList(){this("");}
	
	public AddressList(String Name){
		this.Name=Name;
		Members=new Vector();
	}
	
	public String getName(){return Name;}
	public Vector getMembers(){return Members;}	
	public void addMember(Object Target){Members.addElement(Target);}

	public String toString(){return Name+" <Mailing List>";}

	// Saveable Interface
	public void load(InputStream Input) throws IOException{
		DataInputStream InputData=(Input instanceof DataInputStream) ? (DataInputStream)Input : new DataInputStream(Input);
		byte StringData[];
		InputData.readFully(StringData=new byte[InputData.readInt()]);
		Name=new String(StringData);
		
		Members=new Vector();
		int MemberCount=InputData.readInt();
		for (int i=0;i<MemberCount;i++){
			if (InputData.readInt()==0){
				Address ToLoad=new Address();
				ToLoad.load(InputData);
				Members.addElement(ToLoad);
			}else{
				AddressList ToLoad=new AddressList();
				ToLoad.load(InputData);
				Members.addElement(ToLoad);
			}
		}
	}

	public void save(OutputStream Output) throws IOException{
		DataOutputStream OutputData=(Output instanceof DataOutputStream) ? (DataOutputStream)Output : new DataOutputStream(Output);
		OutputData.writeInt(Name.length());
		OutputData.writeBytes(Name);
		
		OutputData.writeInt(Members.size());
		for (int i=0;i<Members.size();i++){
			if (Members.elementAt(i) instanceof Address){
				OutputData.writeInt(0);
				((Address)Members.elementAt(i)).save(OutputData);
			}else{
				OutputData.writeInt(1);
				((AddressList)Members.elementAt(i)).save(OutputData);
			}
		}
	}
}