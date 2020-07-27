package com.example.demo.exception;

public class CourseNotFoundException extends MyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CourseNotFoundException(String id) {
		super("找不到课程:"+id);
		// TODO Auto-generated constructor stub
	}

}
