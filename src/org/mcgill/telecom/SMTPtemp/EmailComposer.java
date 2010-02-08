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

import org.mcgill.telecom.base.ComponentType;
import org.mcgill.telecom.base.EditingComponent;

public interface EmailComposer extends ComponentType{
	//public void init(Preferences Config); // TODO: TEMP
	public EditingComponent getEditingComponent(OutgoingEmail Target);
	public OutgoingEmail newOutgoingEmail();
	public OutgoingEmail reply(EmailID Target,boolean All) throws IOException,FolderException;
	public OutgoingEmail forward(EmailID Target) throws IOException,FolderException;
}