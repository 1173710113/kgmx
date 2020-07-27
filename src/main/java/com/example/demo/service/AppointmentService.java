package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.demo.domain.User;

public interface AppointmentService {

	public void addAppointment(User user,  String name, String job, String branch, String form, Date plannedStartTime, String teacherId);
	
	public Map<String, List<Map<String, Object>>> getAppointments(User user);
	
	public Map<String, Object> getAppointment(User user, String appointmentId);
	
	public void signIn(User user, String appointmentId);
	
	public void refuse(User user, String appointmentId);
	
	public void agree(User user, String appointmentId, Date plannedStartTime, Date signInTime);
	
	public void start(User user, String appointmentId);
	
	public void end(User user, String appointmentId);
}
