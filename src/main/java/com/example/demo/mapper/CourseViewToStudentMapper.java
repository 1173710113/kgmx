package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.CourseViewToStudent;

@Mapper
public interface CourseViewToStudentMapper {

	public List<CourseViewToStudent> selectStudentCourseNotStart(String studentOpenId);

	public List<CourseViewToStudent> selectStudentCourseIng(String studentOpenId);

	public List<CourseViewToStudent> selectStudentCourseEnd(String studentOpenId);
	
	public CourseViewToStudent selectCourseViewToStudentById(String studentOpenId, String courseId);
	
	public List<CourseViewToStudent> selectAllCoursesToStudent(String studentOpenId);
	
	public List<CourseViewToStudent> selectAllEnrollCoursesToStudent(String studentOpenId);
}
