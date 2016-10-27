package cn.edu.sspu.model;

public class Upload {
	private String id;
	private String name;
	private String fileName;
	private String localUrl;
	@Override
	public String toString() {
		return "Upload [id=" + id + ", name=" + name + ", fileName=" + fileName
				+ ", localUrl=" + localUrl + "]";
	}
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getLocalUrl() {
		return localUrl;
	}
	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

}
