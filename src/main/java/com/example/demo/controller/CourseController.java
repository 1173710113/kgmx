package com.example.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.User;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.CourseService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/course")
public class CourseController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CourseService courseService;

	@RequestMapping("/add")
	@ResponseBody
	public MyResult addCourse(String sessionKey, String courseName, Date plannedStartTime, Date signInTime) {
		User user = userService.validateSession(sessionKey);
		courseService.addCourse(user, courseName, plannedStartTime, signInTime);
		return MyResultGenerator.successResult(null);
	}
	
	@RequestMapping("/query/user")
	@ResponseBody
	public MyResult queryUserCourses(String sessionKey) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.getUserCourses(user));
	}
	
	@RequestMapping("/query/teacher")
	@ResponseBody
	public MyResult queryTeacherCourses(String sessionKey, String teacherId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		if(!user.getType().equals("admin")) {
			throw new MyException("用户类型错误");
		}
		return MyResultGenerator.successResult(courseService.getTeacherCourses(teacherId));
	}
	
	@RequestMapping("/query/student/subscribe")
	@ResponseBody
	public MyResult queryStudentCourses(String sessionKey) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.getStudentCourses(user));
	}
	
	@RequestMapping("/query/all")
	@ResponseBody
	public MyResult queryCourses(String sessionKey) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.getCourses());
	}
	
	@RequestMapping("/query/all/student")
	@ResponseBody
	public MyResult queryCoursesToStudent(String sessionKey) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.getCoursesToStudent(user));
	}
	
	@RequestMapping("/query/enroll/all/student")
	@ResponseBody
	public MyResult queryEnrollCoursesToStudent(String sessionKey, String studentId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.getStudentCourses(user, studentId));
	}
	
	@RequestMapping("/query/course/student")
	@ResponseBody
	public MyResult queryCourseToStudent(String sessionKey, String courseId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.getCourseToStudent(user, courseId));
	}
	
	@RequestMapping("/query")
	@ResponseBody
	public MyResult queryCourse(String sessionKey, String courseId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.getCourse(courseId));
	}
	
	@RequestMapping("/query/student")
	@ResponseBody
	public MyResult queryCourseSubscribe(String sessionKey, String courseId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.getCourseSubscribe(courseId));
	}
	
	@RequestMapping("/update/start")
	@ResponseBody
	public MyResult startCourse(String sessionKey, String courseId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.startCourse(user, courseId));
	}
	
	@RequestMapping("/update/end")
	@ResponseBody
	public MyResult endCourse(String sessionKey, String courseId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.endCourse(user, courseId));
	}
	
	@RequestMapping("/enroll")
	@ResponseBody
	public MyResult enrollCourse(String sessionKey, String courseId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		courseService.enrollCourse(user, courseId);
		return MyResultGenerator.successResult(null);
	}
	
	@RequestMapping("/drop")
	@ResponseBody
	public MyResult dropCourse(String sessionKey, String courseId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		courseService.dropCourse(user, courseId);
		return MyResultGenerator.successResult(null);
	}
	
	@RequestMapping("/signin")
	@ResponseBody
	public MyResult signInCourse(String sessionKey, String courseId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(courseService.signInCourse(user, courseId));
	}
	
	@RequestMapping("/remind")
	@ResponseBody
	public MyResult remind(String sessionKey, String courseId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		courseService.addRemind(user, courseId);
		return MyResultGenerator.successResult(null);
		
	}
	
}
