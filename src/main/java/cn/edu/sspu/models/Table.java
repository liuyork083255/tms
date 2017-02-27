package cn.edu.sspu.models;

import java.util.Date;

public class Table {
	private String table_id;
	private String name;
	private String createtime;
	private String info;
	public String getTable_id() {
		return table_id;
	}
	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "Table [table_id=" + table_id + ", name=" + name
				+ ", createtime=" + createtime + ", info=" + info + "]";
	}
	
	
	
}
