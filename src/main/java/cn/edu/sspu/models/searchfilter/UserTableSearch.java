package cn.edu.sspu.models.searchfilter;

public class UserTableSearch {
	private String tablename;
	private String username;
	private String starttime;
	private String endtime;
	private String currenttime;
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getCurrenttime() {
		return currenttime;
	}
	public void setCurrenttime(String currenttime) {
		this.currenttime = currenttime;
	}
	@Override
	public String toString() {
		return "UserTableSearch [tablename=" + tablename + ", username="
				+ username + ", starttime=" + starttime + ", endtime="
				+ endtime + ", currenttime=" + currenttime + "]";
	}
}
