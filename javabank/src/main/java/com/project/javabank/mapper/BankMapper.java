package com.project.javabank.mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.javabank.dto.AlarmDTO;
import com.project.javabank.dto.DepositDTO;
import com.project.javabank.dto.DtransactionDTO;
import com.project.javabank.dto.ProductDTO;
import com.project.javabank.dto.PtransactionDTO;

@Service
public class BankMapper {
	
	@Autowired
	SqlSession sqlSession;
	
	// userId로 userName 조회
	public String getUserName(String userId) {
		return sqlSession.selectOne("getUserName", userId);
	}
	
	// userId가 보유한 입출금통장 조회  
	public List<DepositDTO> getAccountList(String userId) {
		return sqlSession.selectList("getAccountList", userId);
	}
	
	// userId가 보유한 정기예금 조회
	public List<ProductDTO> getFixedDepositList(String userId){
		return sqlSession.selectList("getFixedDepositList", userId);
	}
	
	// userId가 보유한 정기적금 조회
	public List<ProductDTO> getPeriodicalDepositList(String userId){
		return sqlSession.selectList("getPeriodicalDepositList", userId);
	}
	
	// 랜덤으로 생성한 계좌번호 중복체크
	public int getDepositAccountCheck(String depositNum){
		return sqlSession.selectOne("getDepositAccountCheck", depositNum);
	}
	
	// userId가 보유한 입출금통장 계좌 개수
	public int getDepositAccountCnt(String userId) {
		return sqlSession.selectOne("getDepositAccountCnt", userId);
	}
	
	// 입출금통장 개설
	@Transactional
	public void insertDeposit(Map<String, Object> params) {
		// deposit 테이블 인서트
		sqlSession.insert("insertDeposit", params);
		
		// deposit transaction 테이블 인서트
		sqlSession.insert("insertTransaction", params);
		
		params.put("alarmCate", "신규");
		params.put("alarmCont", "입출금통장(" + params.get("depositAccount") + ")이 개설되었습니다.");
		
		// alarm 테이블 인서트
		sqlSession.insert("insertAlarm", params);
	}
	
	// [메인] 입출금통장 조회
	public DepositDTO getDepositInfo(String depositAccount) {
		return sqlSession.selectOne("getDepositInfo", depositAccount);
	}
	
	// [메인, 정기예금 가입, 정기적금 자동이체 스케줄러] 입출금통장 잔액 확인
	public int getDepositBalance(String depositAccount) {
		return sqlSession.selectOne("getDepositBalance", depositAccount);
	}
	
	// [조회] 입출금통장 거래내역
	public List<DtransactionDTO> getDepositTransaction(Map<String, String> params){
		return sqlSession.selectList("getDepositTransaction", params);
	}
	
	// [이체] 입출금통장 비밀번호
	public String getDepositPw(String depositAccount) {
		return sqlSession.selectOne("getDepositPw", depositAccount);
	}
	
	// [이체] 내 계좌
	public List<DepositDTO> getMyAccountList(String userId){
		return sqlSession.selectList("getMyAccountList", userId);
	}
	
	// [이체] 최근 이체내역
	public List<DtransactionDTO> getMyTransactionList(Map<String, String> params){
		return sqlSession.selectList("getMyTransactionList", params);
	}
	
	// [이체] 계좌번호 존재여부 확인
	public int getCheckAccountExist(String transferAccount) {
		return sqlSession.selectOne("getCheckAccountExist", transferAccount);
	}
	
	// [이체] 계좌번호로 소유자 이름 확인
	public String getAccountName(String depositAccount) {
		return sqlSession.selectOne("getAccountName", depositAccount);
	}
	
	// [이체] 계좌번호로 소유자 아이디 확인
	public String getDepositUserId(String depositAccount) {
		return sqlSession.selectOne("getDepositUserId", depositAccount);
	}
	
	// [이체] 이체 처리
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
		// 이체유저 알람 인서트
		int money = (int)params.get("sendMoneyAmount");
		String formattedMoney = String.format("%,d", money);		
		params.put("alarmCate", "이체");
		params.put("alarmCont", params.get("inputAccount")+"계좌로 "+formattedMoney+"원 이체되었습니다.");
		sqlSession.insert("insertAlarm", params);
		// 수신유저 알람 인서트
		params.put("alarmCate", "이체");
		params.put("alarmCont", params.get("inputAccount")+"계좌로 "+formattedMoney+"원 입금되었습니다.");
		sqlSession.insert("insertAlarm", params);
	}
	
	// [입출금통장] 한달에 한번 이자입금 처리
	@Transactional
	public void processMonthlyInterest() {
		// 이자 계산	
		List<DepositDTO> allDepositAccountList = sqlSession.selectList("allDepositAccountList");
		if(allDepositAccountList.size() != 0) {
			for(DepositDTO account : allDepositAccountList) {
				BigDecimal balance = account.getBalance();
				BigDecimal interest = balance.multiply(BigDecimal.valueOf(account.getInterestRate()));			
				BigDecimal sumInterestBalance = balance.add(interest);
				
				account.setBalance(sumInterestBalance);
	
				Map<String, Object> params = new HashMap<>();
				params.put("depositAccount", account.getDepositAccount());				
				params.put("deltaAmount", interest);
				params.put("balance", sumInterestBalance);
				
				sqlSession.insert("insertSumInterest", params);
				
				// 알람 테이블 인서트
				params.put("userId", account.getUserId());
				params.put("alarmCate", "이자입금");
				params.put("alarmCont", "입출금통장("+params.get("depositAccount")+")의 이자가 입금되었습니다.");
				
				//System.out.println(params);
				sqlSession.insert("insertAlarm", params);
			}
		}
	}
	
	// [정기예금] 계좌번호 중복 체크
	public int getFixedAccountCheck(String depositNum){
		return sqlSession.selectOne("getFixedAccountCheck", depositNum);
	}
	
	// [정기예금] 정기예금 상품 가입
	@Transactional
	public void insertFixedProduct(Map<String, Object> params) {
		// Product 테이블 인서트
		sqlSession.insert("insertFixedProduct", params);
		
		// Product transaction 테이블 인서트
		sqlSession.insert("insertFixedPtransaction", params);
		
		// Deposit 테이블 잔액 계산
		int balance = sqlSession.selectOne("getBalancebyProduct", params);
		int payment = (int) params.get("payment");
		int updatedBalance = balance - payment;
		
		params.put("balance", updatedBalance);
		
		// Deposit 테이블 인서트
		sqlSession.insert("insertDtransactionbyFixed", params);
		
		// 알람 테이블 인서트
		params.put("alarmCate", "신규");
		params.put("alarmCont", "정기예금("+params.get("productAccount")+")이 개설되었습니다.");
		sqlSession.insert("insertAlarm", params);
	}
	
	// [정기예금] 상품 정보 조회
	public ProductDTO getProductInfo(String productAccount) {
		return sqlSession.selectOne("getProductInfo", productAccount);
	}
	
	// [정기예금] 상품 잔액 조회
	public int getProductBalance(String productAccount) {
		return sqlSession.selectOne("getProductBalance", productAccount);
	}
	
	// [정기예금] 상품 거래내역 조회
	public List<PtransactionDTO> getProductTransaction(Map<String, String> params){
		return sqlSession.selectList("getProductTransaction", params);
	}
	
	// [정기예금, 정기적금] 상품 해지
	@Transactional
	public void cancelProduct(Map<String, Object> params) {		
		// Product 테이블 업데이트
		sqlSession.update("updateProductEnableN", params);
		String category = (String) params.get("category");
		
		if(category.equals("정기예금")) {
			// Product transaction 테이블 인서트
			sqlSession.insert("insertPtransactionbyFixedCancel", params);
			
			// Deposit 테이블 잔액 계산
			int balance = sqlSession.selectOne("getBalancebyProduct", params);
			int payment = (int) params.get("payment");
			int cancelInterest = (int) params.get("interest");
			int deltaAmount = payment + cancelInterest;
			int updatedBalance = balance + deltaAmount;
			params.put("deltaAmount", deltaAmount);
			params.put("balance", updatedBalance);
			
			// Deposit Transaction 테이블 인서트
			sqlSession.insert("insertDtransactionbyFixedCancel", params);
			
			// 알람 테이블 인서트
			params.put("alarmCate", "중도해지");
			params.put("alarmCont", "정기예금("+params.get("productAccount")+")이 해지되었습니다.");
			sqlSession.insert("insertAlarm", params);
			
		} else {
			// Product transaction 테이블 인서트
			sqlSession.insert("insertPtransactionbyPeriodicalCancel", params);
			
			// Deposit 테이블 잔액 계산
			int balance = sqlSession.selectOne("getBalancebyProduct", params);
			int productBalance = (int) params.get("productBalance");
			int cancelInterest = (int) params.get("interest");
			int deltaAmount = productBalance + cancelInterest;
			int updatedBalance = balance + deltaAmount;
			params.put("deltaAmount", deltaAmount);
			params.put("balance", updatedBalance);
			
			// Deposit Transaction 테이블 인서트
			sqlSession.insert("insertDtransactionbyPeriodicalCancel", params);
			
			// 알람 테이블 인서트
			params.put("alarmCate", "중도해지");
			params.put("alarmCont", "정기적금("+params.get("productAccount")+")이 해지되었습니다.");
			sqlSession.insert("insertAlarm", params);
		}
	}
	
	// [정기예금, 정기적금] 만기 대상 상품 조회
	public List<ProductDTO> getDepositMaturity(String today){
		return sqlSession.selectList("getDepositMaturity", today);
	}
	
	// [정기예금] 만기 해지 처리
	@Transactional
	public void ExpiryFixedAccount(Map<String, Object> params) {
		// Product 테이블 업데이트
		sqlSession.update("updateProductEnableN", params);
		
		// Product transaction 테이블 인서트
		sqlSession.insert("updateExpiryFixedTransaction", params);
		
		// Deposit 테이블 잔액계산
		int balance = sqlSession.selectOne("getBalancebyProduct", params);
		int deltaAmount = (int)params.get("payment") + (int)params.get("interest");
		int updatedBalance = balance + deltaAmount;
		params.put("deltaAmount", deltaAmount);
		params.put("balance", updatedBalance);
		
		// Deposit Transaction 테이블 인서트
		sqlSession.insert("insertDtransactionbyFixedExpiry", params);
		
		// 알람 테이블 인서트
		params.put("alarmCate", "상품만기");
		params.put("alarmCont", "정기예금("+params.get("productAccount")+")이 만기가 되어 원금과 이자가 입금되었습니다.");
		sqlSession.insert("insertAlarm", params);
		
	}
	
	// [정기적금] 만기 해지 처리
	@Transactional
	public void ExpiryPeriodicalAccount(Map<String, Object> params) {
		// Product 테이블 업데이트
		sqlSession.update("updateProductEnableN", params);

		// Deposit 테이블 잔액계산
		int balance = sqlSession.selectOne("getBalancebyProduct", params);
		int deltaAmount = (int)params.get("productBalance") + (int)params.get("interest");
		int updatedBalance = balance + deltaAmount;
		params.put("deltaAmount", deltaAmount);
		params.put("balance", updatedBalance);
		
		// Product transaction 테이블 인서트
		sqlSession.insert("updateExpiryPeriodicalTransaction", params);		
		
		// Deposit Transaction 테이블 인서트
		sqlSession.insert("insertDtransactionbyPeriodicalExpiry", params);
		
		// 알람 테이블 인서트
		params.put("alarmCate", "상품만기");
		params.put("alarmCont", "정기적금("+params.get("productAccount")+")이 만기가 되어 원금과 이자가 입금되었습니다.");
		sqlSession.insert("insertAlarm", params);
	}
	
	// [정기적금] 정기적금 가입 처리
	@Transactional
	public void insertPeriodicalProduct(Map<String, Object> params) {
		// Product 테이블 인서트
		sqlSession.insert("insertPeriodicalProduct", params);
		
		// Product transaction 테이블 인서트
		sqlSession.insert("insertPeriodicalPtransaction", params);
		
		// Deposit 테이블 잔액 계산
		int balance = sqlSession.selectOne("getBalancebyProduct", params);
		int payment = (int) params.get("monthlyPayment");
		int updatedBalance = balance - payment;
		
		params.put("balance", updatedBalance);
		
		// Deposit 테이블 인서트
		sqlSession.insert("insertDtransactionbyPeriodical", params);
		
		// 알람 테이블 인서트
		params.put("alarmCate", "신규");
		params.put("alarmCont", "정기적금("+params.get("productAccount")+")이 개설되었습니다.");
		sqlSession.insert("insertAlarm", params);
	}
	
	// [정기적금] 자동이체 대상 정기적금 계좌 조회
	public List<ProductDTO> getTransferPeriodicalAccount(int date){
		return sqlSession.selectList("getTransferPeriodicalAccount", date);
	}
	
	// [정기적금] 자동이체 처리
	@Transactional
	public void autoTransfer(Map<String, Object> params) {
		// Deposit Transaction 테이블 인서트
		sqlSession.insert("insertAutoTransferDtransaction", params);
		
		// Product Transaction 테이블 인서트
		sqlSession.insert("insertAutoTransferPtransaction", params);
		
		// 알람 테이블 인서트
		params.put("alarmCate", "이체");
		params.put("alarmCont", "정기적금("+params.get("productAccount")+")으로 자동이체 금액이 입금되었습니다.");
		sqlSession.insert("insertAlarm", params);
	}
	
	// [알람] 알람 내역 조회
	public List<AlarmDTO> getAlarmList(String userId){
		return sqlSession.selectList("getAlarmList", userId);		
	}
	
	// [알람] 신규 알람 내역 조회
	public List<AlarmDTO> getNewAlarmList(String userId){
		return sqlSession.selectList("getNewAlarmList", userId);		
	}
	
	// [알람] 알림 읽음 처리
	public void updateReadY(String userId) {
		sqlSession.update("updateReadY", userId);
	}
	
	// [알람] 정렬별 알람리스트
	public List<AlarmDTO> getSortedAlarmList(Map<String, Object> params){
		return sqlSession.selectList("getSortedAlarmList", params);
	}
}

