package cn.edu.sspu.pojo;

public class Json {
	private Object obj = null;
	private boolean success = false;
	private String msg = null;
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
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
		return "Json [obj=" + obj + ", success=" + success + ", msg=" + msg
				+ "]";
	}
	
	

}
