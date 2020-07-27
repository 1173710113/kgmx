package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Lecturer;

@Mapper
public interface LecturerMapper {

	public void updateById(Lecturer lecturer);
	
	public Lecturer selectById(String openId);
	
	public void deleteById(String openId);
}
