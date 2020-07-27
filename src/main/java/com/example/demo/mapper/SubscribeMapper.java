package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Subscribe;

@Mapper
public interface SubscribeMapper {

	public void insert(Subscribe subscribe);
	
	public void updateById(Subscribe subscribe);
	
	public Subscribe selectById(String courseId, String studentOpenId);
	
	public List<Subscribe> selectByCourse(String courseId);
	
	public void softDeleteStudentSubscribe(String studentId);
	
	public void softDeleteSubscribeByLecturerId(String lecturerId);
}
