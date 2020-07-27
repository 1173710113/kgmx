package com.example.demo.exception;

public class EmployeeNumberNotSetException extends MyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeNumberNotSetException() {
		super("工号未绑定，请先绑定工号");
		// TODO Auto-generated constructor stub
	}

}
