package com.project.javabank.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.javabank.dto.DepositDTO;
import com.project.javabank.dto.ProductDTO;
import com.project.javabank.dto.UserDTO;

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
	
	public int getDepositAccountCheck(String depositNum){
		return sqlSession.selectOne("getDepositAccountCheck", depositNum);
	}
	
	public int getDepositAccountCnt(String userId) {
		return sqlSession.selectOne("getDepositAccountCnt", userId);
	}
	
	@Transactional
	public void insertDeposit(Map<String, Object> params) {
		// deposit 테이블 인서트
		sqlSession.insert("insertDeposit", params);
		
		// deposit transaction 테이블 인서트
		sqlSession.insert("insertTransaction", params);
	}

}

