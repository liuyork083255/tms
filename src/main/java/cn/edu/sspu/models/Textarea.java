package cn.edu.sspu.models;

public class Textarea {
	private String textarea_id;
	private String name;
	private String value;
	private String type;
	private String info;
	private String user_id;
	private String table_id;
	public String getTextarea_id() {
		return textarea_id;
	}
	public void setTextarea_id(String textarea_id) {
		this.textarea_id = textarea_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTable_id() {
		return table_id;
	}
	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}
	@Override
	public String toString() {
		return "Textarea [textarea_id=" + textarea_id + ", name=" + name
				+ ", value=" + value + ", type=" + type + ", info=" + info
				+ ", user_id=" + user_id + ", table_id=" + table_id + "]";
	}
	
}
