package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Remind;

@Mapper
public interface RemindMapper {

	public void insert(Remind remind);
		
	public Remind selectById(String courseId, String studentOpenId);
	
	public void updateById(Remind remind);
	
	public List<Remind> select(Remind remind);
}
