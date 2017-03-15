package cn.edu.sspu.models.searchfilter;

public class Userid_TableidModel {
	private String table_id;
	private String user_id;
	public String getTable_id() {
		return table_id;
	}
	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "Userid_TableidModel [table_id=" + table_id + ", user_id="
				+ user_id + "]";
	}
	
}
