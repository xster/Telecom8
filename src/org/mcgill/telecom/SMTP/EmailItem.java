package org.mcgill.telecom.SMTP;

import org.mcgill.telecom.Main;

public class EmailItem {
	public EmailItem(){}
	public String FROM = Main.MAIL_ADDRESS;
	public String TO = Main.MAIL_ADDRESS;
	
	private String _subject = "Subj Test";
	private String _to = Main.MAIL_ADDRESS;
	private String _from = Main.MAIL_ADDRESS;
	private String _cc = Main.MAIL_ADDRESS; 
	private String _replyto = Main.MAIL_ADDRESS;
	private String _message = "message test";
	
	public void setMessage(String message) { _message = message; }
	
	public String getRawMessage()
	{
		String ret = "";
		
		if (_subject.length() > 0) ret += "Subject:" + _subject + "\r\n";
		if (_to.length() > 0) ret += "to:" + _to + "\r\n";
		if (_from.length() > 0) ret += "from:" + _from + "\r\n";
		if (_cc.length() > 0) ret += "cc:" + _cc + "\r\n";
		if (_replyto.length() > 0) ret += "reply-to:" + _replyto + "\r\n";
		ret += "\r\n";
		ret += _message;
		ret += "\r\n.\r\n";
		
		return ret;
	}
	
}
