package cn.edu.sspu.model;

public class Date {
	private String id;
	private String name;
	private java.util.Date value;

	
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


	public java.util.Date getValue() {
		return value;
	}


	public void setValue(java.util.Date value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return "Date [id=" + id + ", name=" + name + ", value=" + value + "]";
	}
}
