package cn.edu.sspu.models;

public class User_Table {
	private String table_name;
	private String user_name;
	private String user_id;
	private String table_id;
	private String user_table_time;
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
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
	public String getUser_table_time() {
		return user_table_time;
	}
	public void setUser_table_time(String user_table_time) {
		this.user_table_time = user_table_time;
	}
	@Override
	public String toString() {
		return "User_Table [table_name=" + table_name + ", user_name="
				+ user_name + ", user_id=" + user_id + ", table_id=" + table_id
				+ ", user_table_time=" + user_table_time + "]";
	}
	
	
}
