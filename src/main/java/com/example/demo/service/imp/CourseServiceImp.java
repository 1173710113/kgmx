package com.example.demo.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.Course;
import com.example.demo.domain.CourseView;
import com.example.demo.domain.CourseViewToStudent;
import com.example.demo.domain.Remind;
import com.example.demo.domain.Subscribe;
import com.example.demo.domain.SubscribeView;
import com.example.demo.domain.User;
import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.exception.MyException;
import com.example.demo.exception.NoRightToCreateCourse;
import com.example.demo.exception.NoRightToEndCourse;
import com.example.demo.exception.NoRightToStartCourse;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.mapper.CourseViewMapper;
import com.example.demo.mapper.CourseViewToStudentMapper;
import com.example.demo.mapper.RemindMapper;
import com.example.demo.mapper.SubscribeMapper;
import com.example.demo.mapper.SubscribeViewMapper;
import com.example.demo.service.CourseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourseServiceImp implements CourseService {

	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private CourseViewMapper courseViewMapper;
	
	@Autowired
	private CourseViewToStudentMapper courseViewToStudentMapper;

	@Autowired
	private SubscribeViewMapper subscribeViewMapper;

	@Autowired
	private SubscribeMapper subscribeMapper;

	@Autowired
	private RemindMapper remindMapper;

	@Override
	public void addCourse(User user, String courseName, Date plannedStartTime, Date signInTime) {
		if (user.getType() != null && user.getType().equals("lecturer")) {
			Course course = new Course();
			course.setName(courseName);
			course.setCreatedTime(new Date());
			course.setLecturerId(user.getOpenId());
			course.setPlannedStartTime(plannedStartTime);
			course.setSignInTime(signInTime);
			courseMapper.insert(course);
		} else {
			throw new NoRightToCreateCourse();
		}

	}

	@Override
	public Map<String, List<CourseView>> getUserCourses(User user) {
		String type = user.getType();
		String openId = user.getOpenId();
		Map<String, List<CourseView>> map = new HashMap<String, List<CourseView>>();
		switch (type) {
		case "lecturer":
			CourseView courseView = new CourseView();
			courseView.setLecturerId(openId);
			map.put("notStart", courseViewMapper.selectByTime(courseView));
			courseView.setStartTime(new Date());
			map.put("ing", courseViewMapper.selectByTime(courseView));
			courseView.setEndTime(new Date());
			map.put("end", courseViewMapper.selectByTime(courseView));
			break;
		default:
			break;
		}
		return map;
	}

	@Override
	public CourseView getCourse(String courseId) {
		return courseViewMapper.selectById(courseId);
	}

	@Override
	public List<SubscribeView> getCourseSubscribe(String courseId) {
		Course course = courseMapper.selectById(courseId);
		if(course == null || course.getIsDelete()) {
			throw new MyException("找不到课程");
		}
		SubscribeView subscribeView = new SubscribeView();
		subscribeView.setCourseId(courseId);
		return subscribeViewMapper.select(subscribeView);
	}

	@Override
	public CourseView startCourse(User user, String courseId) {
		Course course = courseMapper.selectById(courseId);
		if (course == null || course.getIsDelete()) {
			throw new CourseNotFoundException(courseId);
		}
		if (!course.getLecturerId().equals(user.getOpenId())) {
			throw new NoRightToStartCourse();
		}
		Date date = new Date();
		if (date.before(course.getPlannedStartTime())) {
			throw new MyException("未到开课时间");
		}
		if (course.getStartTime() != null) {
			throw new MyException("课程已开课");
		}
		course.setStartTime(date);
		courseMapper.updateById(course);
		return courseViewMapper.selectById(courseId);
	}

	@Override
	public CourseView endCourse(User user, String courseId) {
		Course course = courseMapper.selectById(courseId);
		if (course == null || course.getIsDelete()) {
			throw new CourseNotFoundException(courseId);
		}
		if (!course.getLecturerId().equals(user.getOpenId())) {
			throw new NoRightToEndCourse();
		}
		if (course.getEndTime() != null) {
			throw new MyException("课程已结束");
		}
		course.setEndTime(new Date());
		courseMapper.updateById(course);
		return courseViewMapper.selectById(courseId);
	}

	@Override
	public List<CourseView> getCourses() {
		CourseView courseView = new CourseView();
		return courseViewMapper.select(courseView);
	}

	@Override
	public List<CourseViewToStudent> getCoursesToStudent(User user) {
		log.info(JSON.toJSONString(courseViewToStudentMapper.selectAllCoursesToStudent(user.getOpenId())));
		return courseViewToStudentMapper.selectAllCoursesToStudent(user.getOpenId());
	}

	@Override
	public Map<String, List<CourseViewToStudent>> getStudentCourses(User user) {
		String type = user.getType();
		if (type == null || !type.equals("student")) {
			throw new MyException("用户类型错误");
		}
		Map<String, List<CourseViewToStudent>> map = new HashMap<String, List<CourseViewToStudent>>();
		String openId = user.getOpenId();
		map.put("notStart", courseViewToStudentMapper.selectStudentCourseNotStart(openId));
		map.put("ing", courseViewToStudentMapper.selectStudentCourseIng(openId));
		map.put("end", courseViewToStudentMapper.selectStudentCourseEnd(openId));
		return map;
	}

	@Override
	public CourseViewToStudent signInCourse(User user, String courseId) {
		String type = user.getType();
		if (type == null || !type.equals("student")) {
			throw new MyException("用户类型错误");
		}
		Course course = courseMapper.selectById(courseId);
		if (course == null || course.getIsDelete()) {
			throw new MyException("找不到这门课");
		}
		Date date = new Date();
		if (course.getSignInTime().after(date)) {
			throw new MyException("未到签到时间");
		}
		Subscribe subscribe = subscribeMapper.selectById(courseId, user.getOpenId());
		if (subscribe == null) {
			subscribe = new Subscribe();
			subscribe.setCourseId(courseId);
			subscribe.setStudentOpenId(user.getOpenId());
			subscribe.setSignInTime(date);
			subscribeMapper.insert(subscribe);
		} else if (subscribe.getIsDelete()) {
			subscribe.setIsDelete(false);
			subscribe.setSignInTime(date);
			subscribeMapper.updateById(subscribe);
		} else if (subscribe.getSignInTime() != null) {
			throw new MyException("不要重复签到");
		}
		return getCourseToStudent(user, courseId);
	}

	@Override
	public CourseViewToStudent getCourseToStudent(User user, String courseId) {
		String type = user.getType();
		if (type == null || !type.equals("student")) {
			throw new MyException("用户类型错误");
		}
		Course course = courseMapper.selectById(courseId);
		if (course == null || course.getIsDelete()) {
			throw new MyException("找不到这门课");
		}
		return courseViewToStudentMapper.selectById(user.getOpenId(), courseId);
	}

	@Override
	public List<CourseViewToStudent> getStudentCourses(User user, String studentId) {
		String type = user.getType();
		if (!type.equals("admin")) {
			throw new MyException("用户类型错误");
		}
		List<CourseViewToStudent> courseViewToStudents = courseViewToStudentMapper.selectAllEnrollCoursesToStudent(studentId);
		return courseViewToStudents;
	}

	@Override
	public List<CourseView> getTeacherCourses(String teacherId) {
		CourseView courseView = new CourseView();
		courseView.setLecturerId(teacherId);
		return courseViewMapper.select(courseView);
	}

	@Override
	public void addRemind(User user, String courseId) {
		String type = user.getType();
		if (type == null || !type.equals("student")) {
			throw new MyException("用户类型错误");
		}
		Course course = courseMapper.selectById(courseId);
		if (course == null || course.getIsDelete()) {
			throw new MyException("找不到这门课");
		}
		Date startTime = course.getStartTime();
		if (startTime != null && startTime.before(new Date())) {
			throw new MyException("课程已开始");
		}
		Remind remind = remindMapper.selectById(courseId, user.getOpenId());
		if (remind == null) {
			remind = new Remind();
			remind.setCourseId(courseId);
			remind.setStudentOpenId(user.getOpenId());
			remindMapper.insert(remind);
		} else if (remind.getIsDelete()) {
			remind.setIsDelete(false);
			remindMapper.updateById(remind);
		} else {
			throw new MyException("已预约提醒");
		}

	}

}
