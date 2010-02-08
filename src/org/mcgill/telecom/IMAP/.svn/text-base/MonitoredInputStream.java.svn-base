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
package org.mcgill.telecom.IMAP;

import java.io.*;

public class MonitoredInputStream extends FilterInputStream implements MonitoredStream{
	int _temp,_temp2,_bytesRead,_markedBytesRead;
	int _granularity;
	
	public MonitoredInputStream(InputStream In,int Granulity){
		super(In);
		this._granularity=Granulity;
		_bytesRead=0;
	}

	public void resetCounter(){_bytesRead=0;}
	public int getBytesDone(){return _bytesRead;}
	public void setGranulity(int New){_granularity=New;}

	public int read() throws IOException{
		if ((_temp=super.read())!=-1) _bytesRead++;
		return _temp;
	}

	public int read(byte b[],int off,int len) throws IOException{
		_temp=(_granularity-(_bytesRead%_granularity));
		if (len>=_temp){
			if ((_temp2=super.read(b,off,_temp))!=-1) _bytesRead+=_temp2;
			if (_temp2<_temp) return _temp2;
			len-=_temp2;off+=_temp2;
			while(len>=_granularity){
				if ((_temp=super.read(b,off,_granularity))!=-1){
					_bytesRead+=_temp;
					len-=_temp;
					off+=_temp;
					_temp2+=_temp;
				}else return _temp2;
				if (_temp<_granularity) return _temp2;
			}
			if ((_temp=super.read(b,off,len))!=-1){
				_bytesRead+=_temp;
				_temp2+=_temp;
			}
			return _temp2;
		}else{
			if ((_temp=super.read(b,off,len))!=-1) _bytesRead+=_temp;
			return _temp;
		}
	}

	public synchronized void mark(int readlimit){
		super.mark(readlimit);
		_markedBytesRead=_bytesRead;
	}

	public synchronized void reset() throws IOException{
		super.reset();
		_bytesRead=_markedBytesRead;
	}
}