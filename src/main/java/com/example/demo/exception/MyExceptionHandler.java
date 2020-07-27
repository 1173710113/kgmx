package com.example.demo.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(annotations = Controller.class)
@ResponseBody
public class MyExceptionHandler {

	@ExceptionHandler
	@ResponseStatus
	public MyResult runtimeExceptionHandler(Exception e) {
		e.printStackTrace();
		return MyResultGenerator.errorResult(e.getMessage(), e);
	}
}
