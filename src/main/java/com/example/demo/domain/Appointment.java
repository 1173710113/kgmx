package com.example.demo.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Appointment {

	public static final String UNCHECKED="unchecked", REFUSE="refuse", AGREE="agree";
	
	private String id;
	private String studentId;
	private String teacherId;
	private String studentName;
	private String studentJob;
	private String studentBranch;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date plannedStartTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date signInTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date studentSignInTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date startTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date endTime;
	private String form;
	private String state;
	private Boolean isDelete;
	private Boolean isSend;
}
