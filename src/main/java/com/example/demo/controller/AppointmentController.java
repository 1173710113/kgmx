package com.example.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.User;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/appoint")
public class AppointmentController {

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	
	@ResponseBody
	@RequestMapping("/add")
	public MyResult addAppointment(String sessionKey, String name, String job, String branch, String form, Date plannedStartTime, String teacherId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		appointmentService.addAppointment(user, name, job, branch, form, plannedStartTime, teacherId);
		return MyResultGenerator.successResult(null);
	}
	
	@ResponseBody
	@RequestMapping("/query/all")
	public MyResult getAppointments(String sessionKey) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(appointmentService.getAppointments(user));
	}
	
	@ResponseBody
	@RequestMapping("/query")
	public MyResult getAppointment(String sessionKey, String appointmentId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(appointmentService.getAppointment(user, appointmentId));
	}
	
	@ResponseBody
	@RequestMapping("/signin")
	public MyResult signInAppointment(String sessionKey, String appointmentId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		appointmentService.signIn(user, appointmentId);
		return MyResultGenerator.successResult(appointmentService.getAppointment(user, appointmentId));
	}
	
	@ResponseBody
	@RequestMapping("/refuse")
	public MyResult refuseAppointment(String sessionKey, String appointmentId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		appointmentService.refuse(user, appointmentId);
		return MyResultGenerator.successResult(null);
	}
	
	@ResponseBody
	@RequestMapping("/agree")
	public MyResult agreeAppointment(String sessionKey, String appointmentId, Date plannedStartTime, Date signInTime) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		appointmentService.agree(user, appointmentId, plannedStartTime, signInTime);
		return MyResultGenerator.successResult(null);
	}
	
	@ResponseBody
	@RequestMapping("/start")
	public MyResult startAppointment(String sessionKey, String appointmentId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		appointmentService.start(user, appointmentId);
		return MyResultGenerator.successResult(appointmentService.getAppointment(user, appointmentId));
	}
	
	@ResponseBody
	@RequestMapping("/end")
	public MyResult endAppointment(String sessionKey, String appointmentId) {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		appointmentService.end(user, appointmentId);
		return MyResultGenerator.successResult(appointmentService.getAppointment(user, appointmentId));
	}
}
