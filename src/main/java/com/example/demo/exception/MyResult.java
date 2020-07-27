package com.example.demo.exception;

import java.io.Serializable;

/**
 * 返回消息体
 * @author msi-user
 *
 */
public class MyResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MyResult() {
		
	}
	/**
	 * 单例模式获取对象
	 * @return
	 */
	public static MyResult newInstance() {
		return new MyResult();
	}
	
	private String msg; //消息
	private boolean flag; //成功的标志
	private Object result; //返回的对象
	private long time; //时间
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}
	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
	/**
	 * @param meg the meg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}
	
	
}
