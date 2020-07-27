package com.example.demo.domain;

import lombok.Data;

@Data
public class User {

	private String openId;

	private String nickName;

	private Integer gender;

	private String avatarUrl;

	private String sessionKey;

	private String employeeNumber;

	private String type;

	private Boolean isDelete;
}
