package cn.edu.sspu.models;

import java.util.List;

public class EmailModel {
	private String myEmail;
	private String text;
	private List<String> toEmails;
	public String getMyEmail() {
		return myEmail;
	}
	public void setMyEmail(String myEmail) {
		this.myEmail = myEmail;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<String> getToEmails() {
		return toEmails;
	}
	public void setToEmails(List<String> toEmails) {
		this.toEmails = toEmails;
	}
	@Override
	public String toString() {
		return "EmailModel [myEmail=" + myEmail + ", text=" + text
				+ ", toEmails=" + toEmails + "]";
	}
	
}
