package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.demo.domain.CourseView;
import com.example.demo.domain.CourseViewToStudent;
import com.example.demo.domain.SubscribeView;
import com.example.demo.domain.User;

public interface CourseService {

	public void addCourse(User user, String courseName, Date plannedStartTime, Date signInTime);

	/**
	 * precondition:用户已登录，会话有效，工号以绑定，角色以绑定
	 * 
	 * @param user
	 * @return
	 */
	public Map<String, List<CourseView>> getUserCourses(User user);
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public Map<String, List<CourseViewToStudent>> getStudentCourses(User user);
	
	public List<CourseView> getCourses();
	
	public List<CourseViewToStudent> getCoursesToStudent(User user);
	
	public CourseViewToStudent getCourseToStudent(User user, String courseId);

	public CourseView getCourse(String courseId);

	public List<SubscribeView> getCourseSubscribe(String courseId);

	public CourseView startCourse(User user, String courseId);
	
	public CourseView endCourse(User user, String courseId);
	
	public CourseViewToStudent signInCourse(User user, String courseId);
	
	public List<CourseViewToStudent> getStudentCourses(User user, String studentId);
	
	public List<CourseView> getTeacherCourses(String teacherId);
	
	public void addRemind(User user, String courseId);
}
