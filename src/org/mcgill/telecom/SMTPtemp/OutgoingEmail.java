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

public interface OutgoingEmail{
	public static final int RECIPIENT_TO=0;
	public static final int RECIPIENT_CC=1;
	public static final int RECIPIENT_BCC=2;
	public EmailComposer getEmailComposer();
	public String getRawMessage();
	public String getHeader(String Name);
	public AddressList getRecipients(int Type);
	public void loadFromStream(InputStream Input) throws IOException;
	public void saveToStream(OutputStream Output) throws IOException;
}
