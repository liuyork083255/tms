package cn.edu.sspu.models;

public class Input {
	private String input_id;
	private String name;
	private String value;
	private String type;
	private String param;
	private String info;
	private String required;
	private String tableown;
	private String user_id;
	private String table_id;
	public String getInput_id() {
		return input_id;
	}
	public void setInput_id(String input_id) {
		this.input_id = input_id;
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
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
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
		return "Input [input_id=" + input_id + ", name=" + name + ", value="
				+ value + ", type=" + type + ", param=" + param + ", info="
				+ info + ", required=" + required + ", tableown=" + tableown
				+ ", user_id=" + user_id + ", table_id=" + table_id + "]";
	}
	
	
}
