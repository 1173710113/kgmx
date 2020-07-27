package com.example.demo.domain;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@ColumnWidth(12)
public class SubscribeView {

	@ExcelIgnore
	private String courseId;
	
	@ExcelProperty(value = "课程名称", index=2)
	private String courseName;
	
	@ExcelProperty(value = "学生姓名", index=0)
	private String studentNickName;
	
	@ExcelIgnore
	private String studentAvatar;
	
	@ExcelIgnore
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date signInTime;
	
	@ExcelIgnore
	private String departmentId;
	
	@ExcelProperty(value = "行部", index=1)
	private String departmentName;
	
	@ExcelProperty(value = "教师姓名", index = 3)
	private String teacherNickName;
	
	@ColumnWidth(20)
	@ExcelProperty(value = "开课时间", index = 4)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date startTime;
	
	@ColumnWidth(15)
	@ExcelProperty(value = "课程总时长", index = 5)
	private Integer courseTotalTime;
	
	@ExcelProperty(value = "上课时长", index = 6)
	private Integer studentTotalTime;
}
