package com.example.demo.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Appointment;
import com.example.demo.domain.LecturerView;
import com.example.demo.domain.StudentView;
import com.example.demo.domain.User;
import com.example.demo.exception.MyException;
import com.example.demo.mapper.AppointmentMapper;
import com.example.demo.mapper.LecturerViewMapper;
import com.example.demo.mapper.StudentViewMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.AppointmentService;

@Service
public class AppointmentServiceImp implements AppointmentService {

	@Autowired
	private AppointmentMapper appointmentMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private StudentViewMapper studentViewMapper;

	@Autowired
	private LecturerViewMapper lecturerViewMapper;

	@Override
	public void addAppointment(User user, String name, String job, String branch, String form, Date plannedStartTime,
			String teacherId) {
		String type = user.getType();
		if (type == null || !type.equals("student")) {
			throw new MyException("用户类型错误");
		}
		User teacher = userMapper.selectById(teacherId, null);
		if (teacher == null || teacher.getType() == null || !teacher.getType().equals("lecturer")) {
			throw new MyException("找不到教师");
		}
		Appointment appointment = new Appointment();
		appointment.setStudentName(name);
		appointment.setStudentJob(job);
		appointment.setStudentBranch(branch);
		appointment.setForm(form);
		appointment.setPlannedStartTime(plannedStartTime);
		appointment.setStudentId(user.getOpenId());
		appointment.setTeacherId(teacherId);
		appointmentMapper.insert(appointment);

	}

	@Override
	public Map<String, List<Map<String, Object>>> getAppointments(User user) {
		String type = user.getType();
		List<Map<String, Object>> uncheckedList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> agreeList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> refuseList = new ArrayList<Map<String, Object>>();
		List<Appointment> unckeckedAppointments;
		List<Appointment> agreeAppointments;
		List<Appointment> refuseAppointments;
		String openId = user.getOpenId();
		Appointment appointmentCon = new Appointment();
		appointmentCon.setIsDelete(false);
		switch (type) {
		case "student":
			appointmentCon.setStudentId(openId);
			break;
		case "lecturer":
			appointmentCon.setTeacherId(openId);
			break;
		default:
			throw new MyException("用户类型错误");
		}
		appointmentCon.setState(Appointment.UNCHECKED);
		unckeckedAppointments = appointmentMapper.select(appointmentCon);
		appointmentCon.setState(Appointment.AGREE);
		agreeAppointments = appointmentMapper.select(appointmentCon);
		appointmentCon.setState(Appointment.REFUSE);
		refuseAppointments = appointmentMapper.select(appointmentCon);
		for (Appointment appointment : unckeckedAppointments) {
			StudentView student = studentViewMapper.selectById(appointment.getStudentId());
			LecturerView teacher = lecturerViewMapper.selectById(appointment.getTeacherId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appointment", appointment);
			map.put("student", student);
			map.put("teacher", teacher);
			uncheckedList.add(map);
		}
		for (Appointment appointment : agreeAppointments) {
			StudentView student = studentViewMapper.selectById(appointment.getStudentId());
			LecturerView teacher = lecturerViewMapper.selectById(appointment.getTeacherId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appointment", appointment);
			map.put("student", student);
			map.put("teacher", teacher);
			agreeList.add(map);
		}
		for (Appointment appointment : refuseAppointments) {
			StudentView student = studentViewMapper.selectById(appointment.getStudentId());
			LecturerView teacher = lecturerViewMapper.selectById(appointment.getTeacherId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appointment", appointment);
			map.put("student", student);
			map.put("teacher", teacher);
			refuseList.add(map);
		}
		Map<String, List<Map<String, Object>>> map = new HashMap<>();
		map.put("unchecked", uncheckedList);
		map.put("agree", agreeList);
		map.put("refuse", refuseList);
		return map;
	}

	@Override
	public Map<String, Object> getAppointment(User user, String appointmentId) {
		Appointment appointment = appointmentMapper.selectById(appointmentId);
		if (appointment == null) {
			throw new MyException("找不到预约");
		}
		String type = user.getType();
		String openId = user.getOpenId();
		switch (type) {
		case "student":
			if (!openId.equals(appointment.getStudentId())) {
				throw new MyException("无法查看预约");
			}
			break;
		case "lecturer":
			if (!openId.equals(appointment.getTeacherId())) {
				throw new MyException("无法查看预约");
			}
		default:
			break;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appointment", appointment);
		StudentView student = studentViewMapper.selectById(appointment.getStudentId());
		LecturerView teacher = lecturerViewMapper.selectById(appointment.getTeacherId());
		map.put("student", student);
		map.put("teacher", teacher);
		return map;
	}

	@Override
	public void signIn(User user, String appointmentId) {
		String type = user.getType();
		if (!type.equals("student")) {
			throw new MyException("用户类型错误");
		}
		Appointment appointment = appointmentMapper.selectById(appointmentId);
		if (appointment == null || appointment.getIsDelete()) {
			throw new MyException("找不到预约");
		}
		if (appointment.getStudentSignInTime() != null) {
			throw new MyException("不可以重复签到");
		}
		Date date = new Date();
		if (appointment.getSignInTime().after(date)) {
			throw new MyException("未到签到时间");
		}

		Date endTime = appointment.getEndTime();
		if (endTime != null) {
			throw new MyException("预约已结束");
		}
		appointment.setStudentSignInTime(date);
		appointmentMapper.updateById(appointment);
	}

	@Override
	public void refuse(User user, String appointmentId) {
		String type = user.getType();
		if (!type.equals("lecturer")) {
			throw new MyException("用户类型错误");
		}
		Appointment appointment = appointmentMapper.selectById(appointmentId);
		if (appointment == null || appointment.getIsDelete()) {
			throw new MyException("找不到预约");
		}
		if (!appointment.getTeacherId().equals(user.getOpenId())) {
			throw new MyException("没有权限");
		}

		appointment.setState(Appointment.REFUSE);
		appointmentMapper.updateById(appointment);
	}

	@Override
	public void agree(User user, String appointmentId, Date plannedStartTime, Date signInTime) {
		String type = user.getType();
		if (!type.equals("lecturer")) {
			throw new MyException("用户类型错误");
		}
		Appointment appointment = appointmentMapper.selectById(appointmentId);
		if (appointment == null || appointment.getIsDelete()) {
			throw new MyException("找不到预约");
		}
		if (!appointment.getTeacherId().equals(user.getOpenId())) {
			throw new MyException("没有权限");
		}
		appointment.setState(Appointment.AGREE);
		appointment.setPlannedStartTime(plannedStartTime);
		appointment.setSignInTime(signInTime);
		appointmentMapper.updateById(appointment);
	}

	@Override
	public void start(User user, String appointmentId) {
		String type = user.getType();
		if (!type.equals("lecturer")) {
			throw new MyException("用户类型错误");
		}
		Appointment appointment = appointmentMapper.selectById(appointmentId);
		if (appointment == null || appointment.getIsDelete()) {
			throw new MyException("找不到预约");
		}
		if (!appointment.getTeacherId().equals(user.getOpenId())) {
			throw new MyException("没有权限");
		}
		if (appointment.getStartTime() != null) {
			throw new MyException("预约已开始");
		}
		appointment.setStartTime(new Date());
		appointmentMapper.updateById(appointment);

	}

	@Override
	public void end(User user, String appointmentId) {
		String type = user.getType();
		if (!type.equals("lecturer")) {
			throw new MyException("用户类型错误");
		}
		Appointment appointment = appointmentMapper.selectById(appointmentId);
		if (appointment == null || appointment.getIsDelete()) {
			throw new MyException("找不到预约");
		}
		if (!appointment.getTeacherId().equals(user.getOpenId())) {
			throw new MyException("没有权限");
		}
		if (appointment.getEndTime() != null) {
			throw new MyException("预约已结束");
		}
		appointment.setEndTime(new Date());
		appointmentMapper.updateById(appointment);

	}

}
