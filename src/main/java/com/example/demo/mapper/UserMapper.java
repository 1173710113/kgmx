package com.example.demo.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.User;

@Mapper
public interface UserMapper {
	
	public void insertUser(User user);
	
	public User selectUserByOpenId(String openId);

	public User selectUserBySessionKey(String sessionKey);
	
	public void updateUserSessionByOpenId(String openId, String sessionKey);
	
	public void updateUserEmployeeNumber(String openId, String employeeNumber);
	
	public void updateNickName(String openId, String nickName);
	
	public void updateAvatar(String openId, String avatarUrl);
	
	public void updateUserType(String openId, String type);
	
	public void updateTeacherLevel(String openId, String level);
	
}
