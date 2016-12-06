package cn.edu.sspu.models;

import java.util.List;

public class Model {
	private String table_id;
	private String user_id;
	private String name;
	private List<Input> inputList;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Input> getInputList() {
		return inputList;
	}
	public void setInputList(List<Input> inputList) {
		this.inputList = inputList;
	}
	@Override
	public String toString() {
		return "Model [table_id=" + table_id + ", user_id=" + user_id
				+ ", name=" + name + ", inputList=" + inputList + "]";
	}
	
	
}
