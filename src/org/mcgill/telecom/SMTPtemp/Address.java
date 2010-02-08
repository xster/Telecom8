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

public class Address implements Saveable{
	String Name,EmailAddress;

	public Address(){}
	
	public Address(String Name,String EmailAddress){
		this.Name=Name;
		this.EmailAddress=EmailAddress;
	}

	public void setName(String Name){this.Name=Name;}
	public String getName(){return Name;}
	public void setEmailAddress(String EmailAddress){this.EmailAddress=EmailAddress;}
	public String getEmailAddress(){return EmailAddress;}
	public String toString(){return Name+" <"+EmailAddress+">";}
	public boolean equals(Object Target){return (Target instanceof Address)&&((Address)Target).EmailAddress.equals(EmailAddress);}

	// Saveable Interface
	public void load(InputStream Input) throws IOException{
		DataInputStream InputData=(Input instanceof DataInputStream) ? (DataInputStream)Input : new DataInputStream(Input);
		byte StringData[];
		InputData.readFully(StringData=new byte[InputData.readInt()]);
		Name=new String(StringData);
		InputData.readFully(StringData=new byte[InputData.readInt()]);
		EmailAddress=new String(StringData);
	}
	
	public void save(OutputStream Output) throws IOException{
		DataOutputStream OutputData=(Output instanceof DataOutputStream) ? (DataOutputStream)Output : new DataOutputStream(Output);
		OutputData.writeInt(Name.length());
		OutputData.writeBytes(Name);
		OutputData.writeInt(EmailAddress.length());
		OutputData.writeBytes(EmailAddress);
	}
	
	public static Address parseRecipient(String Rcpt){
		if (Rcpt==null) return null;
		Address TheAddress=new Address();
		int i,j;
		
		try{
			i=((j=Rcpt.lastIndexOf('>',Rcpt.length()-1))!=-1) ? Rcpt.lastIndexOf('<',j) : -1;
			if (j!=-1&&i!=-1) 
				TheAddress.EmailAddress=Rcpt.substring(i+1,j);
			else{
				i=Rcpt.length();
				TheAddress.EmailAddress="";
			}
			
			for (i--;i>0&&Rcpt.charAt(i)==' ';i--);
			if (Rcpt.charAt(i)=='\"') i--;
			for (j=0;j<i&&Rcpt.charAt(j)==' ';j++);
			if (Rcpt.charAt(j)=='\"') j++;
			TheAddress.Name=(j<=i) ? Rcpt.substring(j,i+1) : "";
		}catch(Exception e){
			if (TheAddress.Name==null) TheAddress.Name="";
			if (TheAddress.EmailAddress==null) TheAddress.EmailAddress="";
		}
		
		return TheAddress;
	}
}