package com.example.demo.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Appointment;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lecturer;
import com.example.demo.domain.LecturerView;
import com.example.demo.domain.Student;
import com.example.demo.domain.StudentView;
import com.example.demo.domain.Subscribe;
import com.example.demo.domain.User;
import com.example.demo.exception.EmployeeNumberNotSetException;
import com.example.demo.exception.MyException;
import com.example.demo.exception.SessionException;
import com.example.demo.exception.UserRoleNotSetException;
import com.example.demo.mapper.AppointmentMapper;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.mapper.LecturerMapper;
import com.example.demo.mapper.LecturerViewMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.mapper.StudentViewMapper;
import com.example.demo.mapper.SubscribeMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private StudentViewMapper studentViewMapper;

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	private LecturerMapper lecturerMapper;

	@Autowired
	private LecturerViewMapper lecturerViewMapper;

	@Autowired
	private AppointmentMapper appointmentMapper;

	@Autowired
	private SubscribeMapper subscribeMapper;

	@Autowired
	private CourseMapper courseMapper;

	@Value("${admincode}")
	private String adminCode;

	@Override
	public JSONObject wxLogin(JSONObject SessionKeyOpenId, String rawData, String signature) throws MyException {
		// 3.接收微信接口服务 获取返回的参数
		JSONObject rawDataJson = JSON.parseObject(rawData);
		String openid = SessionKeyOpenId.getString("openid");
		String sessionKey = SessionKeyOpenId.getString("session_key");

		// 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
		String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
		if (!signature.equals(signature2)) {
			throw new MyException("签名校验失败");
		}
		// 5.根据返回的User实体类，判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间
		User user = userMapper.selectById(openid, null);
		// uuid生成唯一key，用于维护微信小程序用户与服务端的会话
		String skey = UUID.randomUUID().toString();
		JSONObject result = new JSONObject();
		result.put("sessionKey", skey);
		if (user == null) {
			// 用户信息入库
			String nickName = rawDataJson.getString("nickName");
			String avatarUrl = rawDataJson.getString("avatarUrl");
			String gender = rawDataJson.getString("gender");
			user = new User();
			user.setOpenId(openid);
			user.setSessionKey(skey);
			user.setAvatarUrl(avatarUrl);
			user.setGender(Integer.parseInt(gender));
			user.setNickName(nickName);
			userMapper.insert(user);
		} else {
			// 已存在，更新用户登录时间
			// 重新设置会话skey
			user.setSessionKey(skey);
			userMapper.updateById(user);
		}
		result.put("type", user.getType());
		result.put("employeeNumber", user.getEmployeeNumber());
		result.put("departmentId", null);
		result.put("departmentName", null);
		result.put("brief", null);
		result.put("level", null);
		getUserExtraInfo(user.getOpenId(), user.getType(), result);
		user.setOpenId(null);
		result.put("userInfo", user);
		return result;
	}

	private void getUserExtraInfo(String openId, String type, JSONObject result) {
		if (type == null)
			return;
		switch (type) {
		case "student":
			StudentView studentView = studentViewMapper.selectById(openId);
			log.info(studentView.toString());
			result.put("departmentId", studentView.getDepartmentId());
			result.put("departmentName", studentView.getDepartmentName());
			break;
		case "lecturer":
			Lecturer lecturer = lecturerMapper.selectById(openId);
			result.put("brief", lecturer.getBrief());
			result.put("level", lecturer.getLevel());
			break;
		default:
			break;
		}
	}

	@Override
	public void setUserType(User user, String type, String code) {
		String employeeNuber = user.getEmployeeNumber();
		if (employeeNuber == null) {
			throw new EmployeeNumberNotSetException();
		} else {
			if (user.getType() == null) {
				if (type.equals("admin")) {
					if (code == null || !code.equals(adminCode)) {
						throw new MyException("管理员密码错误");
					}
				}
				user.setType(type);
				userMapper.updateById(user);
			} else {
				throw new MyException("角色以绑定，需要修改请联系管理员");
			}

		}

	}

	@Override
	public void setEmployeeNumber(User user, String employeeNumber) {
		if (user.getEmployeeNumber() == null) {
			user.setEmployeeNumber(employeeNumber);
			userMapper.updateById(user);
		} else {
			throw new MyException("工号已绑定，需要修改请联系管理员");
		}

	}

	@Override
	public User validateSession(String sessionKey) {
		User user = userMapper.selectById(null, sessionKey);
		if (user == null) {
			throw new SessionException();
		}
		return user;
	}

	@Override
	public void validateUserEmployeeNumber(User user) {
		if (user.getEmployeeNumber() == null) {
			throw new EmployeeNumberNotSetException();
		}

	}

	@Override
	public void validateUserType(User user) {
		String type = user.getType();
		if (type == null || !(type.equals("admin") || type.equals("student") || type.equals("lecturer"))) {
			throw new UserRoleNotSetException();
		}

	}

	@Override
	public void setDepartment(User user, String departmentId) {
		String type = user.getType();
		if (type != null && type.equals("student")) {
			Student student = studentMapper.selectById(user.getOpenId());
			if (student.getDepartment() != null) {
				throw new MyException("行部已绑定，需要修改请联系管理员");
			}
			student.setDepartment(departmentId);
			studentMapper.updateById(student);
		} else {
			throw new MyException("用户角色错误");
		}
	}

	@Override
	public void setLecturerDescription(User user, String description) {
		String type = user.getType();
		if (type == null || !type.equals("lecturer")) {
			throw new MyException("用户类型错误");
		}
		Lecturer lecturer = lecturerMapper.selectById(user.getOpenId());
		lecturer.setBrief(description);
		lecturerMapper.updateById(lecturer);
	}

	@Override
	public List<LecturerView> getAllTeacher() {
		return lecturerViewMapper.select(new LecturerView());
	}

	@Override
	public LecturerView getTeacher(String id) {
		return lecturerViewMapper.selectById(id);
	}

	@Override
	public List<Map<String, Object>> getAllStudent(User user) {
		String type = user.getType();
		if (!type.equals("admin")) {
			throw new MyException("用户类型错误");
		}
		List<StudentView> studentViews = studentViewMapper.select(new StudentView());
		log.info(studentViews.toString());
		Map<String, List<StudentView>> map = new LinkedHashMap<String, List<StudentView>>();
		for (StudentView studentView : studentViews) {
			String departmentName = studentView.getDepartmentName();
			if (departmentName == null) {
				if (!map.containsKey("#")) {
					map.put("#", new ArrayList<StudentView>());
				}
				map.get("#").add(studentView);
			} else if (map.containsKey(departmentName)) {
				map.get(departmentName).add(studentView);
			} else {
				map.put(departmentName, new ArrayList<StudentView>());
				map.get(departmentName).add(studentView);
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String key : map.keySet()) {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("departmentName", key);
			newMap.put("students", map.get(key));
			list.add(newMap);
		}
		return list;
	}

	@Override
	public StudentView getStudent(User user, String studentId) {
		String type = user.getType();
		if (!type.equals("admin")) {
			throw new MyException("用户类型错误");
		}
		StudentView studentView = studentViewMapper.selectById(studentId);
		if (studentView == null) {
			throw new MyException("不存在学生");
		}
		return studentView;
	}

	@Override
	public LecturerView updateTeacherLevel(String teacherId, String level) {
		Lecturer lecturer =lecturerMapper.selectById(teacherId);
		lecturer.setLevel(level);
		lecturerMapper.updateById(lecturer);
		return lecturerViewMapper.selectById(teacherId);
	}

	@Override
	public void updateEmployeeNumber(String openId, String employeeNumber) {
		User user = userMapper.selectById(openId, null);
		if(user == null || user.getIsDelete()) {
			throw new MyException("找不到用户");
		}
		user.setEmployeeNumber(employeeNumber);
		userMapper.updateById(user);

	}

	@Override
	public void updateDepartment(String studentId, String departmentId) {
		Student student = studentMapper.selectById(studentId);
		if (student == null) {
			throw new MyException("用户不存在");
		}
		student.setDepartment(departmentId);
		studentMapper.updateById(student);
	}

	@Override
	public String updateAvatarUrl(User user, String avatarUrl) {
		user.setAvatarUrl(avatarUrl);
		userMapper.updateById(user);
		return avatarUrl;
	}

	@Override
	public void updateUserName(String openId, String nickName) {
		User user = userMapper.selectById(openId, null);
		if(user == null || user.getIsDelete()) {
			throw new MyException("找不到用户");
		}
		user.setNickName(nickName);
		userMapper.updateById(user);
	}

	@Override
	public void changeUserType(String userId, String newType) {
		User user = userMapper.selectById(userId, null);
		if (user == null || user.getIsDelete()) {
			throw new MyException("用户不存在");
		}
		String type = user.getType();
		if (type == null) {
			user.setType(newType);
			userMapper.updateById(user);
		} else {
			if (type.equals(newType)) {
				return;
			}
			Appointment appointment = new Appointment();
			Appointment appointmentCon = new Appointment();
			appointment.setIsDelete(true);
			
			Subscribe subscribe = new Subscribe();
			subscribe.setIsDelete(true);
			Subscribe subscribeCon = new Subscribe();
			switch (type) {
			case "admin":
				user.setType(newType);
				userMapper.updateById(user);
				break;
			case "student":
				studentMapper.deleteById(userId);
				appointmentCon.setStudentId(userId);
				appointmentMapper.update(appointment, appointmentCon);
				subscribeCon.setStudentOpenId(userId);
				subscribeMapper.update(subscribe, subscribeCon);
				user.setType(newType);
				userMapper.updateById(user);
				break;
			case "lecturer":
				lecturerMapper.deleteById(userId);
				appointmentCon.setTeacherId(userId);
				appointmentMapper.update(appointment, appointmentCon);
				Course course = new Course();
				course.setIsDelete(true);
				Course courseCon = new Course();
				courseCon.setLecturerId(userId);
				courseMapper.update(course, courseCon);
				List<Course> courses = courseMapper.select(courseCon);
				for(Course item:courses) {
					subscribeCon.setCourseId(item.getId());
					subscribeMapper.update(subscribe, subscribeCon);
				}
				user.setType(newType);
				userMapper.updateById(user);
				break;
			default:
				throw new MyException("用户类型错误");
			}
			switch (newType) {
			case User.STUDENT:
				Student student = new Student();
				student.setOpenId(userId);
				studentMapper.insert(student);
				break;
			case User.TEACHER:
				Lecturer lecturer = new Lecturer();
				lecturer.setOpenId(userId);
				lecturerMapper.insert(lecturer);
				break;
			default:
				break;
			}
		}

	}

}
