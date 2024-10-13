package com.project.javabank.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.javabank.dto.UserDTO;

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
	
	public List<UserDTO> getMailUser(String userEmail) {
		return sqlSession.selectList("getMailUser", userEmail);
	}
	
	public List<UserDTO> getIdMailUser(Map<String, String> params) {
		return sqlSession.selectList("getIdMailUser", params);
	}
	
	public int changedPwResult(Map<String, String> params) {
		return sqlSession.update("changedPwResult", params);
	}
}
