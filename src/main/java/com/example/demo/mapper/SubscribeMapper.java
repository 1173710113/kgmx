package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Subscribe;

@Mapper
public interface SubscribeMapper {

	public void insert(Subscribe subscribe);
	
	public void updateById(Subscribe subscribe);
	
	public void update(Subscribe subscribe, Subscribe condition);
	
	public List<Subscribe> select(Subscribe condition);
	
	public Subscribe selectById(String courseId, String studentOpenId);

}
