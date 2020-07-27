package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.StudentView;

@Mapper
public interface StudentViewMapper {

	public StudentView selectById(String openId);
	
	public List<StudentView> selectAll();
}
