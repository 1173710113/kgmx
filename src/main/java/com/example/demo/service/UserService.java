package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.LecturerView;
import com.example.demo.domain.StudentView;
import com.example.demo.domain.User;

public interface UserService {

	/**
	 * 处理微信登入
	 * @param SessionKeyOpenId
	 * @return
	 */
	public JSONObject wxLogin(JSONObject SessionKeyOpenId, String rawData, String signature);
	
	public void setEmployeeNumber(User user, String employeeNumber);
	
	public void updateEmployeeNumber(String openId, String employeeNumber);
	
	public void updateUserName(String openId, String nickName);
	
	public void setUserType(User user, String type, String code);
	
	public void setLecturerDescription(User user, String description);
	
	public User validateSession(String sessionKey);
	
	public void validateUserEmployeeNumber(User user);
	
	public void validateUserType(User user);
	
	public void setDepartment(User user, String departmentId);
	
	public void updateDepartment(String studentId, String departmentId);
	
	public List<LecturerView> getAllTeacher();
	
	public LecturerView getTeacher(String id);
	
	public List<Map<String, Object>> getAllStudent(User user);
	
	public StudentView getStudent(User user, String studentId);
	
	public LecturerView updateTeacherLevel(String teacherId, String level);
	
	public String updateAvatarUrl(User user, String avatarUrl);
	
	public void changeUserType(String userId, String newType);
}
