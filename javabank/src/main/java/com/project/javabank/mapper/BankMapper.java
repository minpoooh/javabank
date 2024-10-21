package com.project.javabank.mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.javabank.dto.DepositDTO;
import com.project.javabank.dto.DtransactionDTO;
import com.project.javabank.dto.ProductDTO;
import com.project.javabank.dto.PtransactionDTO;

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
	
	public List<DtransactionDTO> getDepositTransaction(Map<String, String> params){
		return sqlSession.selectList("getDepositTransaction", params);
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
	
	public int getCheckAccountExist(String transferAccount) {
		return sqlSession.selectOne("getCheckAccountExist", transferAccount);
	}
	
	public String getAccountName(String depositAccount) {
		return sqlSession.selectOne("getAccountName", depositAccount);
	}
	
	public String getReceiveUserId(String depositAccount) {
		return sqlSession.selectOne("getReceiveUserId", depositAccount);
	}
	
	@Transactional
	public void transferProcess(Map<String, Object> params) {
		// 출금계좌 잔액 조회
		int balance = sqlSession.selectOne("getDepositBalanceOnly", params.get("depositAccount"));
		int sendMoney = (int) params.get("sendMoneyAmount");
		int depositBalance = balance - sendMoney;
		params.put("depositBalance", depositBalance);
		
		// 수신계좌 잔액 조회
		int rBalance = sqlSession.selectOne("getDepositBalanceOnly", params.get("inputAccount"));
		int receiveBalance = rBalance + sendMoney;
		params.put("receiveBalance", receiveBalance);
		
		//System.out.println("최종 params :" + params);
		// 출금계좌에서 출금 인서트
		sqlSession.insert("withdrawProcess", params);
		// 수신계좌에서 입금 인서트
		sqlSession.insert("depositProcess", params);
	}
	
	@Transactional
	public void processMonthlyInterest() {
		// 이자 계산 처리
		
		List<DepositDTO> allDepositAccountList = sqlSession.selectList("allDepositAccountList");
		//System.out.println(allDepositAccountList.size());
		if(allDepositAccountList.size() != 0) {
			for(DepositDTO account : allDepositAccountList) {
				BigDecimal balance = account.getBalance();
				BigDecimal interest = balance.multiply(BigDecimal.valueOf(account.getInterestRate()));			
				BigDecimal sumInterestBalance = balance.add(interest);
				
				account.setBalance(sumInterestBalance);
	
				Map<String, Object> params = new HashMap<>();
				params.put("depositAccount", account.getDepositAccount());
				params.put("userId", account.getUserId());
				params.put("deltaAmount", interest);
				params.put("balance", sumInterestBalance);
				
				sqlSession.insert("insertSumInterest", params);
			}
		}
	}
	
	public int getFixedAccountCheck(String depositNum){
		return sqlSession.selectOne("getFixedAccountCheck", depositNum);
	}
	
	@Transactional
	public void insertProduct(Map<String, Object> params) {
		// Product 테이블 인서트
		sqlSession.insert("insertProduct", params);
		
		// Product transaction 테이블 인서트
		sqlSession.insert("insertPtransaction", params);
		
		// Deposit 테이블 잔액 계산
		int balance = sqlSession.selectOne("getBalancebyFixed", params);
		int payment = (int) params.get("payment");
		int updatedBalance = balance - payment;
		
		params.put("balance", updatedBalance);
		
		// Deposit 테이블 인서트
		sqlSession.insert("insertDtransactionbyFixed", params);
	}
	
	public ProductDTO getProductInfo(String productAccount) {
		return sqlSession.selectOne("getProductInfo", productAccount);
	}
	
	public int getProductBalance(String productAccount) {
		return sqlSession.selectOne("getProductBalance", productAccount);
	}
	
	public List<PtransactionDTO> getProductTransaction(Map<String, String> params){
		return sqlSession.selectList("getProductTransaction", params);
	}
	
	@Transactional
	public void cancelProduct(Map<String, Object> params) {
		// Product 테이블 업데이트
		sqlSession.update("updateProductEnableN", params);
		
		// Product transaction 테이블 인서트
		sqlSession.insert("insertPtransactionCancel", params);
		
		// Deposit 테이블 잔액 계산
		int balance = sqlSession.selectOne("getBalancebyFixed", params);
		int payment = (int) params.get("payment");
		int cancelInterest = (int) params.get("interest");
		int deltaAmount = payment + cancelInterest;
		int updatedBalance = balance + payment + cancelInterest;
		params.put("deltaAmount", deltaAmount);
		params.put("balance", updatedBalance);
		
		System.out.println("params :"+params);
		
		// Deposit Transaction 테이블 인서트
		sqlSession.insert("insertDtransactionbyFixedCancel", params);
	}
	
	public List<ProductDTO> getFixedDepositMaturity(String today){
		return sqlSession.selectList("getFixedDepositMaturity", today);
	}
	
	@Transactional
	public void ExpiryFixedAccount(Map<String, Object> params) {
		// Product 테이블 업데이트
		sqlSession.update("updateProductEnableN", params);
		
		// Product transaction 테이블 인서트
		sqlSession.insert("updateExpiryFixedTransaction", params);
		
		// Deposit 테이블 잔액계산
		int balance = sqlSession.selectOne("getBalancebyFixed", params);
		int deltaAmount = (int)params.get("payment") + (int)params.get("interest");
		int updatedBalance = balance + deltaAmount;
		params.put("deltaAmount", deltaAmount);
		params.put("balance", updatedBalance);
		
		// Deposit Transaction 테이블 인서트
		sqlSession.insert("insertDtransactionbyFixedExpiry", params);
		
	}
}

