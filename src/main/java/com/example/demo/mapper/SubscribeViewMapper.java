package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.SubscribeView;

@Mapper
public interface SubscribeViewMapper {
	
	public List<SubscribeView> select(SubscribeView condition);
}
