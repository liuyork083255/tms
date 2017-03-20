package cn.edu.sspu.models;

public class TableIdAndName {
	private String table_id;
	private String name;
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
	@Override
	public String toString() {
		return "TableIdAndName [table_id=" + table_id + ", name=" + name + "]";
	}
	
}
