package com.example.demo.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Course;

@Mapper
public interface CourseMapper {

	public void insert(Course course);
	
	public Course selectById(String courseId);
	
	public void updateById(Course course);
	
	public void update(Course course, Course condition);
	
	public List<Course> selectCourseToSend(Date startTime, Date endTime); 

	
	
}
