package cn.edu.sspu.models;

public class User_Table {
	private String user_id;
	private String table_id;
	private String user_table_time;
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
		return "User_Table [user_id=" + user_id + ", table_id=" + table_id + ", user_table_time=" + user_table_time
				+ "]";
	}
	
}
