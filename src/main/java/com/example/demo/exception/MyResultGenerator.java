package com.example.demo.exception;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MyResultGenerator {
	public static MyResult result(boolean flag, String msg, Object result, Throwable throwable) {
		MyResult myResult = MyResult.newInstance();
		myResult.setFlag(flag);
		myResult.setMsg(msg == "" ? "执行成功":msg);
		myResult.setResult(result);
		myResult.setTime(System.currentTimeMillis());
		log.info(JSON.toJSONString(myResult));
		return myResult;
	}
	
	/**
	 * 返回成功的消息体
	 * @param result 附加到消息体中的返回对象
	 * @return 成功的消息体
	 */
	public static MyResult successResult(Object result) {
		return result(true, "", result, null);
	}
	
	/**
	 * 返回失败的消息体
	 * @param msg 失败的消息
	 * @param throwable 产生的异常
	 * @return 失败的消息体
	 */
	public static MyResult errorResult(String msg, Throwable throwable) {
		return result(false, msg, "", throwable);
	}
}
