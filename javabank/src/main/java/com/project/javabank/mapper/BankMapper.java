package com.project.javabank.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.javabank.dto.DepositDTO;
import com.project.javabank.dto.DtransactionDTO;
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
	
	public DepositDTO getDepositInfo(String depositAccount) {
		return sqlSession.selectOne("getDepositInfo", depositAccount);
	}
	
	public int getDepositBalance(String depositAccount) {
		return sqlSession.selectOne("getDepositBalance", depositAccount);
	}
	
	public List<DtransactionDTO> getDepositTransaction(String depositAccount){
		return sqlSession.selectList("getDepositTransaction", depositAccount);
	}
	
	public String getDepositPw(String depositAccount) {
		return sqlSession.selectOne("getDepositPw", depositAccount);
	}
	
	public List<DepositDTO> getMyAccountList(String userId){
		return sqlSession.selectList("getMyAccountList", userId);
	}
	
	public List<DtransactionDTO> getMyTransactionList(Map<String, String> params){
		return sqlSession.selectList("getMyTransactionList", params);
	}
	
	@Transactional
	public void transferProcess(Map<String, Object> params) {
		//{sendMoneyAmount=10000, depositAccount=3333-01-1878191, inputAccount=3333014376949}
		
		// 출금계좌에서 출금 인서트
		
		// 수신계좌에서 입금 인서트
	}
	

}

