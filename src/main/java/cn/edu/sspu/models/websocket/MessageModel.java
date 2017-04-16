package cn.edu.sspu.models.websocket;

public class MessageModel {
	private String targetUserId;
	private String sourceUserId;
	private String message;
	public String getTargetUserId() {
		return targetUserId;
	}
	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}
	public String getSourceUserId() {
		return sourceUserId;
	}
	public void setSourceUserId(String sourceUserId) {
		this.sourceUserId = sourceUserId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
