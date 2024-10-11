package com.project.javabank.mapper;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JoinMapper {
	
	@Autowired
	SqlSession sqlSession;
	
	public int joinUser(Map<String, Object> params) {
		return sqlSession.insert("joinUser", params);
	}
	
	public int checkID(String userId) {
		return sqlSession.selectOne("checkID", userId);
	}
}
