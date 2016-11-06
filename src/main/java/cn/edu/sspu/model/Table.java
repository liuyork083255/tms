package cn.edu.sspu.model;

import java.util.List;

public class Table {
	
	private String id;
	private String name;
	
	private List<Text> textList;
	private List<Select> selectList;
	private List<Date> datetList;
	private List<Upload> uploatList;
	
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
	
	public List<Text> getTextList() {
		return textList;
	}
	public void setTextList(List<Text> textList) {
		this.textList = textList;
	}
	public List<Select> getSelectList() {
		return selectList;
	}
	public void setSelectList(List<Select> selectList) {
		this.selectList = selectList;
	}
	public List<Date> getDatetList() {
		return datetList;
	}
	public void setDatetList(List<Date> datetList) {
		this.datetList = datetList;
	}
	public List<Upload> getUploatList() {
		return uploatList;
	}
	public void setUploatList(List<Upload> uploatList) {
		this.uploatList = uploatList;
	}
	
}
