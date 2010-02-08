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

import org.mcgill.telecom.Main;

public class PartStruct{
	static int[] Base64LookupTable=null;
	public String PartSpec=null;
	public String Type=null;
	public String SubType=null;
	public String Params=null;
	public String BodyID=null;
	public String BodyDescr=null;
	public String TransferEncoding=null;
	public Vector Parts=null;
	public int BodySize=0;

	public String toString(){
		return "PartSpec: "+PartSpec+"\r\nType: "+Type+"\r\nSubType: "+SubType+"\r\nParams: "+Params+"\r\nBodyID: "+BodyID+"\r\nBodyDescr: "+BodyDescr+"\r\nTransferEncoding: "+TransferEncoding+"\r\nBodySize: "+Integer.toString(BodySize)+"\r\nParts.size(): "+((Parts==null) ? "0" : Integer.toString(Parts.size()));
	}

	public void printEntireStruct(){
		recurStruct("");
	}

	public String getParam(String PName){
		if (Params==null) return null;
		String Temp=Params.toLowerCase();
		int End,Pos;

		if ((Pos=Temp.indexOf(PName.toLowerCase()+"="))!=0)
			if ((Pos=Temp.indexOf(";"+PName.toLowerCase()+"="))!=-1) Pos++;

		if (Pos==-1) return null; else Pos+=PName.length()+1;
		while (Params.charAt(Pos)==' '||Params.charAt(Pos)=='\t') Pos++;
		if (Params.charAt(Pos)=='\"') Pos++;
		End=Params.indexOf(';',Pos)-1;
		while (Params.charAt(End)==' ') End--;
		if (Params.charAt(End)=='\"') End--;

		return Params.substring(Pos,End+1);
	}

	void recurStruct(String Inden){
		Main.println(Inden+"Type/SubType: "+Type+"/"+SubType+", xfer-encd: "+TransferEncoding+", PartSpec: "+PartSpec);
		if (Parts!=null)
			for (int i=0;i<Parts.size();i++) ((PartStruct)Parts.elementAt(i)).recurStruct(Inden+" ");
	}

	public static String decodeQuotedPrintable(String RawMessage){
		String HexDigits="0123456789abcdef";
		int i,j,UnprintedWSP,End=RawMessage.length();
		StringBuffer Buffer=new StringBuffer();
		char A,B;

		for (UnprintedWSP=0,i=0;i<End;i++){
			if (RawMessage.charAt(i)==' '||RawMessage.charAt(i)=='\t'){
				UnprintedWSP++;
			}else{
				if (UnprintedWSP>0) Buffer.append(RawMessage.substring(i-UnprintedWSP,i));
				UnprintedWSP=0;
				if (RawMessage.charAt(i)=='='){
					A=Character.toLowerCase((char)RawMessage.charAt(i+1));
					B=Character.toLowerCase((char)RawMessage.charAt(i+2));
					if (HexDigits.indexOf(A)!=-1&&HexDigits.indexOf(B)!=-1){
						Buffer.append((char)(HexDigits.indexOf(A)*16+HexDigits.indexOf(B)));
						i+=2;
					}else{
						j=RawMessage.indexOf("\r\n",i);
						if (j>=0) i=j+1; else i=End;
					}
				}else if (RawMessage.charAt(i)==13&&i+1<End&&RawMessage.charAt(i+1)==10){
					UnprintedWSP=0;
					Buffer.append("\r\n");
					i+=1;
				}else Buffer.append(RawMessage.charAt(i));
			}
		}
		if (UnprintedWSP>0) Buffer.append(RawMessage.substring(i-UnprintedWSP,i));

		return Buffer.toString();
	}

	public static byte[] decodeBase64(String RawMessage){
		String CharSet="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		ByteArrayOutputStream Buffer=new ByteArrayOutputStream();
		int i,j,Data,Size=RawMessage.length();

		if (Base64LookupTable==null){
			Base64LookupTable=new int[256];
			for (i=0;i<256;i++) Base64LookupTable[i]=-1;
			for (i=0;i<CharSet.length();i++) Base64LookupTable[(int)CharSet.charAt(i)]=i;
		}
		
		for (Data=j=0,i=0;i<Size;i++){
			if (Base64LookupTable[(int)RawMessage.charAt(i)]!=-1){
				Data=(Data<<6)+Base64LookupTable[(int)RawMessage.charAt(i)];
				if ((j+=6)>=8){
					Buffer.write((Data>>(j-8))&0xff);
					j-=8;
				}
			}
		}

		return Buffer.toByteArray();
	}
}
