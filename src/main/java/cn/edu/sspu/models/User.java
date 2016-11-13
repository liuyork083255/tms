package cn.edu.sspu.models;

public class User {
	private String user_id;
	private String name;
	private String password;
	private String email;
	private String type;//这个type是权限的字段
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", name=" + name + ", password="
				+ password + ", email=" + email + ", type=" + type + "]";
	}
	
}
