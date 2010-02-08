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
package org.mcgill.telecom.SMTP;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MonitoredOutputStream extends FilterOutputStream{
	int _temp,_bytesWritten;
	int _granularity;
	
	public MonitoredOutputStream(OutputStream Out,int Granulity){
		super(Out);
		this._granularity=Granulity;
		_bytesWritten=0;
	}

	public void resetCounter(){_bytesWritten=0;}
	public int getBytesDone(){return _bytesWritten;}
	public void setGranulity(int New){_granularity=New;}

	public void write(byte b[]) throws IOException{
		write(b,0,b.length);
	}

	public void write(byte b[],int off,int len) throws IOException{
		if (len>=(_temp=_granularity-(_bytesWritten%_granularity))){
			out.write(b,off,_temp);
			off+=_temp;len-=_temp;_bytesWritten+=_temp;
			while(len>=_granularity){
				out.write(b,off,_granularity);
				off+=_granularity;len-=_granularity;_bytesWritten+=_granularity;
			}
		}
		out.write(b,off,len);
		_bytesWritten+=len;
	}

	public void write(int b) throws IOException{
		out.write(b);
		_bytesWritten++;
	}
}