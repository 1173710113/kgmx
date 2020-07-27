package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.User;
import com.example.demo.exception.MyException;
import com.example.demo.exception.MyResult;
import com.example.demo.exception.MyResultGenerator;
import com.example.demo.exception.SessionException;
import com.example.demo.service.UserService;
import com.example.demo.utils.WechatUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author msi-user
 *
 */
@Slf4j
@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	private WechatUtil wechatUtil;

	@Autowired
	private UserService userService;

	/**
	 * 微信用户登录详情
	 * 
	 * @throws MyException
	 */
	@PostMapping("/wx/login")
	@ResponseBody
	public MyResult userLogin(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "rawData", required = false) String rawData,
			@RequestParam(value = "signature", required = false) String signature,
			@RequestParam(value = "encrypteData", required = false) String encrypteData,
			@RequestParam(value = "iv", required = false) String iv) throws MyException {

		// 1.接收小程序发送的code
		// 2.开发者服务器 登录凭证校验接口 appi + appsecret + code
		JSONObject SessionKeyOpenId = wechatUtil.getSessionKeyOrOpenId(code);

		return MyResultGenerator.successResult(userService.wxLogin(SessionKeyOpenId, rawData, signature));
	}

	@RequestMapping("/update/employeenumber")
	@ResponseBody
	public MyResult updateEmployeeNumber(String sessionKey, String employeeNumber) throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.setEmployeeNumber(user, employeeNumber);
		return MyResultGenerator.successResult(null);
	}

	@RequestMapping("admin/update/employeenumber")
	@ResponseBody
	public MyResult adminUpdateEmployeeNumber(String sessionKey, String userId, String employeeNumber)
			throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		if (!user.getType().equals("admin")) {
			throw new MyException("用户类型错误");
		}
		log.info(userId);
		if (userId == null) {
			userService.updateEmployeeNumber(user.getOpenId(), employeeNumber);
		} else {
			userService.updateEmployeeNumber(userId, employeeNumber);
		}

		return MyResultGenerator.successResult(null);
	}

	@RequestMapping("admin/update/department")
	@ResponseBody
	public MyResult adminUpdateDepartment(String sessionKey, String userId, String departmentId)
			throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		if (!user.getType().equals("admin")) {
			throw new MyException("用户类型错误");
		}
		userService.updateDepartment(userId, departmentId);
		return MyResultGenerator.successResult(null);
	}
	
	@RequestMapping("admin/update/type")
	@ResponseBody
	public MyResult adminUpdateUserType(String sessionKey, String userId, String newType) 
	{
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		if (!user.getType().equals("admin")) {
			throw new MyException("用户类型错误");
		}
		userService.changeUserType(userId, newType);
		return MyResultGenerator.successResult(null);
	}

	@RequestMapping("/update/nickname")
	@ResponseBody
	public MyResult updateNickName(String sessionKey, String nickName) {
		User user = userService.validateSession(sessionKey);
		userService.updateUserName(user.getOpenId(), nickName);
		return MyResultGenerator.successResult(null);
	}

	@RequestMapping("/update/type")
	@ResponseBody
	public MyResult updateType(String sessionKey, String type, String code) throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.setUserType(user, type, code);
		return MyResultGenerator.successResult(null);
	}

	@RequestMapping("/update/department")
	@ResponseBody
	public MyResult updateStudentDepartment(String sessionKey, String departmentId) throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		userService.setDepartment(user, departmentId);
		return MyResultGenerator.successResult(null);
	}

	@RequestMapping("/update/description")
	@ResponseBody
	public MyResult updateTeacherDscription(String sessionKey, String description) throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		userService.setLecturerDescription(user, description);
		return MyResultGenerator.successResult(null);
	}

	@RequestMapping("/update/level")
	@ResponseBody
	public MyResult updateTeacherLevel(String sessionKey, String teacherId, String level) throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		if (!user.getType().equals("admin")) {
			throw new MyException("用户类型错误");
		}
		return MyResultGenerator.successResult(userService.updateTeacherLevel(teacherId, level));
	}

	@RequestMapping("/query/teacher/all")
	@ResponseBody
	public MyResult getAllTeacher(String sessionKey) throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(userService.getAllTeacher());
	}

	@RequestMapping("/query/student/all")
	@ResponseBody
	public MyResult getAllStudent(String sessionKey) throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(userService.getAllStudent(user));
	}

	@RequestMapping("/query/teacher")
	@ResponseBody
	public MyResult getTeacher(String sessionKey, String id) throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(userService.getTeacher(id));
	}

	@RequestMapping("/query/student")
	@ResponseBody
	public MyResult getStudent(String sessionKey, String id) throws SessionException {
		User user = userService.validateSession(sessionKey);
		userService.validateUserEmployeeNumber(user);
		userService.validateUserType(user);
		return MyResultGenerator.successResult(userService.getStudent(user, id));
	}

}
