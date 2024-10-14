package com.project.javabank.dto;

public class DtransactionDTO {
	private int accountSeq;
	private String depositAccount;
	private String userId;
	private String updateDate;
	private String type;
	private String memo;
	private int deltaAmount;
	private int balance;
	private String transferAccount;
	private String transferName;
	
	public int getAccountSeq() {
		return accountSeq;
	}
	public void setAccountSeq(int accountSeq) {
		this.accountSeq = accountSeq;
	}
	public String getDepositAccount() {
		return depositAccount;
	}
	public void setDepositAccount(String depositAccount) {
		this.depositAccount = depositAccount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getTransferAccount() {
		return transferAccount;
	}
	public void setTransferAccount(String transferAccount) {
		this.transferAccount = transferAccount;
	}
	public String getTransferName() {
		return transferName;
	}
	public void setTransferName(String transferName) {
		this.transferName = transferName;
	}


	// 조인용 컬림
	private String userName;

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	
}
