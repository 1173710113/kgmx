package com.example.demo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Appointment;
import com.example.demo.domain.Course;
import com.example.demo.domain.Remind;
import com.example.demo.domain.TemplateData;
import com.example.demo.mapper.AppointmentMapper;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.mapper.RemindMapper;
import com.example.demo.vo.WxMssVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Schedule {

	@Autowired
	private WechatUtil wechatUtil;

	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private RemindMapper remindMapper;

	@Autowired
	private AppointmentMapper appointmentMapper;


	private static final String templateId = "xfBl-8MX4UdX38zMZ_8IvGxIN22oVsowzPTh0uF1-B0";

	private static int expire; // 过期时间，秒

	private static Date latestTime;

	private static String accessToken = null;

	@Scheduled(cron = "0 */1 * * * ?")
	public void sendCourse() {
		Date date = new Date();
		List<Course> courses = courseMapper.selectCourseToSend(date, new Date(date.getTime() + (10 * 60 * 1000)));
		for (Course course : courses) {
			if (!course.getIsDelete()) {
				log.info("send course:" + course.toString());
				Remind remindCon = new Remind();
				remindCon.setCourseId(course.getId());
				remindCon.setIsSend(false);
				remindCon.setIsDelete(false);
				List<Remind> reminds = remindMapper.select(remindCon);
				Map<String, TemplateData> map = new HashMap<String, TemplateData>();
				map.put("thing1", new TemplateData(course.getName()));
				DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm");
				String dateStr = df.format(course.getSignInTime());
				map.put("time5", new TemplateData(dateStr));
				for (Remind remind : reminds) {
					send(remind.getStudentOpenId(), map, course.getId());
					remind.setIsSend(true);
					remindMapper.updateById(remind);
				}
			}
		}
	}

	@Scheduled(cron = "0 */1 * * * ?")
	public void sendAppoitment() {
		Date date = new Date();
		List<Appointment> appointments = appointmentMapper.selectAppointmentToSend(date,
				new Date(date.getTime() + (10 * 60 * 1000)));
		for (Appointment appointment : appointments) {
			appointment.setIsSend(true);
			appointmentMapper.updateById(appointment);
			Map<String, TemplateData> map = new HashMap<String, TemplateData>();
			map.put("thing1", new TemplateData("一对一"));
			DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm");
			String dateStr = df.format(appointment.getSignInTime());
			map.put("time5", new TemplateData(dateStr));
			WxMssVo wxMssVo = new WxMssVo();
			wxMssVo.setTouser(appointment.getStudentId());
			wxMssVo.setTemplate_id(templateId);
			wxMssVo.setPage("pages/appointmentInfo/appointmentInfo?id=" + appointment.getId());
			wxMssVo.setData(map);
			if (!isAccessTokenValid()) {
				getAccessToken();
			}
			log.info(wechatUtil.sendMessage(accessToken, wxMssVo));
		}
	}

	@Scheduled(cron = "0 0 */2 * * ?")
	private void getAccessToken() {
		JSONObject object = wechatUtil.getAccessToken();
		log.info("access_token:" + object.toJSONString());
		latestTime = new Date();
		expire = object.getIntValue("expire_in");
		accessToken = object.getString("access_token");
	}

	private boolean isAccessTokenValid() {
		if (accessToken == null) {
			return false;
		}
		Date newDate = new Date(latestTime.getTime() + expire * 1000);
		if (newDate.before(new Date())) {
			return false;
		}
		return true;
	}

	private void send(String openId, Map<String, TemplateData> data, String courseId) {
		if (!isAccessTokenValid()) {
			getAccessToken();
		}
		WxMssVo wxMssVo = new WxMssVo();
		wxMssVo.setTouser(openId);
		wxMssVo.setTemplate_id(templateId);
		wxMssVo.setData(data);
		wxMssVo.setPage("pages/studentCourseInfo/studentCourseInfo?courseId=" + courseId);
		log.info(wechatUtil.sendMessage(accessToken, wxMssVo));
	}

}
