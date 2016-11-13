package cn.edu.sspu.models;

public class Select {
	private String select_id;
	private String name;
	private String value;
	private String values;
	private String type;
	private String info;
	private String tableown;
	private String user_id;
	private String table_id;
	public String getSelect_id() {
		return select_id;
	}
	public void setSelect_id(String select_id) {
		this.select_id = select_id;
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
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
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
	public String getTableown() {
		return tableown;
	}
	public void setTableown(String tableown) {
		this.tableown = tableown;
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
		return "Select [select_id=" + select_id + ", name=" + name + ", value="
				+ value + ", values=" + values + ", type=" + type + ", info="
				+ info + ", tableown=" + tableown + ", user_id=" + user_id
				+ ", table_id=" + table_id + "]";
	}
	
	
}
