package cn.edu.sspu.pojo;

public class Json {
	private String success = null;
	private String msg = null;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Json [success=" + success + ", msg=" + msg + "]";
	}
	

}
