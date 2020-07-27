package com.example.demo.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Appointment;

@Mapper
public interface AppointmentMapper {

	public void insert(Appointment appointment);
	
	public List<Appointment> select(Appointment condition);
	
	public Appointment selectById(String id);
	
	public List<Appointment> selectAppointmentToSend(Date startTime, Date endTime);
	
	public void updateById(Appointment appointment);
	
	public void update(Appointment appointment, Appointment condition);
	
}
