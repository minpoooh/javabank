package com.project.javabank.mapper;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.javabank.dto.DepositDTO;
import com.project.javabank.dto.ProductDTO;

@Service
public class BankMapper {
	
	@Autowired
	SqlSession sqlSession;
	
	public String getUserName(String userId) {
		return sqlSession.selectOne("getUserName", userId);
	}
	
	public List<DepositDTO> getAccountList(String userId) {
		return sqlSession.selectList("getAccountList", userId);
	}
	
	public List<ProductDTO> getFixedDepositList(String userId){
		return sqlSession.selectList("getFixedDepositList", userId);
	}
	
	public List<ProductDTO> getPeriodicalDepositList(String userId){
		return sqlSession.selectList("getPeriodicalDepositList", userId);
	}
}

