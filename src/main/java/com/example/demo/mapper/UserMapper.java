package com.example.demo.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.User;

@Mapper
public interface UserMapper {
	
	public void insert(User user);
	
	public User selectById(String openId, String sessionKey);
	
	public void updateById(User user);
	
}
