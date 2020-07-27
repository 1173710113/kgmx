package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.CourseView;

@Mapper
public interface CourseViewMapper {

	public CourseView selectById(String courseId);
	
	public List<CourseView> select(CourseView condition);
	
	public List<CourseView> selectByTime(CourseView condition);

}
