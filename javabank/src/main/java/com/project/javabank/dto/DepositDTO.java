package com.project.javabank.dto;

import java.math.BigDecimal;

public class DepositDTO {
	private String depositAccount;
	private int depositPw;
	private String userId;
	private String category;
	private String regDate;
	private double interestRate;
	private int transactionLimit;
	private String mainAccount;
	private String depositEnable;
	
	public String getDepositAccount() {
		return depositAccount;
	}
	public void setDepositAccount(String depositAccount) {
		this.depositAccount = depositAccount;
	}
	public int getDepositPw() {
		return depositPw;
	}
	public void setDepositPw(int depositPw) {
		this.depositPw = depositPw;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public int getTransactionLimit() {
		return transactionLimit;
	}
	public void setTransactionLimit(int transactionLimit) {
		this.transactionLimit = transactionLimit;
	}
	public String getMainAccount() {
		return mainAccount;
	}
	public void setMainAccount(String mainAccount) {
		this.mainAccount = mainAccount;
	}
	public String getDepositEnable() {
		return depositEnable;
	}
	public void setDepositEnable(String depositEnable) {
		this.depositEnable = depositEnable;
	}



	// 조인용 컬럼
	private BigDecimal balance;

	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	
	
	
}
