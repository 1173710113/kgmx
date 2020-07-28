package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Student;

@Mapper
public interface StudentMapper {
	
	public void insert(Student student);

	public Student selectById(String id);
	
	public void updateById(Student student);
	
	public void deleteById(String openId);
}
