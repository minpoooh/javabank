package com.project.javabank.dto;

public class ProductDTO {
	private String productAccount;
	private int productPw;
	private String userId;
	private String category;
	private String autoTransferDate;
	private int monthlyPayment;
	private int payment;
	private String regDate;
	private String expiryDate;
	private double interestRate;
	private String depositAccount;
	private String productEnable;
	
	public String getProductAccount() {
		return productAccount;
	}
	public void setProductAccount(String productAccount) {
		this.productAccount = productAccount;
	}
	public int getProductPw() {
		return productPw;
	}
	public void setProductPw(int productPw) {
		this.productPw = productPw;
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
	public String getAutoTransferDate() {
		return autoTransferDate;
	}
	public void setAutoTransferDate(String autoTransferDate) {
		this.autoTransferDate = autoTransferDate;
	}
	public int getMonthlyPayment() {
		return monthlyPayment;
	}
	public void setMonthlyPayment(int monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}
	public int getPayment() {
		return payment;
	}
	public void setPayment(int payment) {
		this.payment = payment;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public String getDepositAccount() {
		return depositAccount;
	}
	public void setDepositAccount(String depositAccount) {
		this.depositAccount = depositAccount;
	}
	public String getProductEnable() {
		return productEnable;
	}
	public void setProductEnable(String productEnable) {
		this.productEnable = productEnable;
	}



	// 조인용 컬럼
	private int balance;

	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	
}
