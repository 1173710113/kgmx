package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.LecturerView;

@Mapper
public interface LecturerViewMapper {

	public List<LecturerView> selectAll();
	
	public LecturerView selectById(String id);
}
