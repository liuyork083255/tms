package cn.edu.sspu.models.websocket;

public class WebSocketModel {
	private String type;
	private String targetUserId;
	private String targetUserName;
	private String sourceUserId;
	private String sourceUserName;
	private String message;
	private int onlineNumber;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTargetUserId() {
		return targetUserId;
	}
	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}
	public String getTargetUserName() {
		return targetUserName;
	}
	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}
	public String getSourceUserId() {
		return sourceUserId;
	}
	public void setSourceUserId(String sourceUserId) {
		this.sourceUserId = sourceUserId;
	}
	public String getSourceUserName() {
		return sourceUserName;
	}
	public void setSourceUserName(String sourceUserName) {
		this.sourceUserName = sourceUserName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getOnlineNumber() {
		return onlineNumber;
	}
	public void setOnlineNumber(int onlineNumber) {
		this.onlineNumber = onlineNumber;
	}
	
	
}
