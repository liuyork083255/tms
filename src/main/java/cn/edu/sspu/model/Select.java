package cn.edu.sspu.model;

import java.util.List;

public class Select {
	private String id;
	private String name;
	private List<String> valueList;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getValueList() {
		return valueList;
	}
	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}
	@Override
	public String toString() {
		return "Select [id=" + id + ", name=" + name + ", valueList="
				+ valueList + "]";
	}

}
