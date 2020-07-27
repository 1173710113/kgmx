package com.example.demo.domain;

import lombok.Data;

@Data
public class Remind {

	private String courseId;
	
	private String studentOpenId;
	
	private Boolean isSend;
	
	private Boolean isDelete;
}
