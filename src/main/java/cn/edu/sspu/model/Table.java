package cn.edu.sspu.model;

import java.util.List;

public class Table {
	private List<Text> textList;
	private List<Select> selectList;
	private List<Date> datetList;
	private List<Upload> uploatList;
	
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
