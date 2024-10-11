package com.project.javabank.dto;

public class PtransactionDTO {
	private int productSeq;
	private String productAccount;
	private String updateDate;
	private String type;
	private String memo;
	private int deltaAmount;
	private int balance;
	
	public int getProductSeq() {
		return productSeq;
	}
	public void setProductSeq(int productSeq) {
		this.productSeq = productSeq;
	}
	public String getProductAccount() {
		return productAccount;
	}
	public void setProductAccount(String productAccount) {
		this.productAccount = productAccount;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getDeltaAmount() {
		return deltaAmount;
	}
	public void setDeltaAmount(int deltaAmount) {
		this.deltaAmount = deltaAmount;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	
}
