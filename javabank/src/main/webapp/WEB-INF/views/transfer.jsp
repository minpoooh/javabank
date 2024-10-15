<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="top.jsp"%>
<!-- s: content -->
<section id="transfer" class="content">
    <form name="f" action="/transferProcess" method="post" onsubmit="return finalCheck()">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    	<input type="hidden" name="sendMoneyAmount" value="${sendMoneyAmount}">
    	<input type="hidden" name="depositAccount" value="${depositAccount}">
    	
        <div class="bank_info">
            <p>송금할 계좌번호를 입력해주세요.</p>
            <select class="bank">
                <option value="JAVABANK">자바뱅크</option>
            </select>
            <label>
                <input type="text" name="inputAccount" value="" placeholder="계좌번호 입력" oninput="accountCheck(this)" required>
            </label>
            <button class="bg_yellow" type="submit">다음</button>
        </div>
    </form>

    <div class="bank_list">
        <p>내 통장</p>
        <ul class="my_list account_list">
        	<c:if test="${not empty myAccountList}">
	        	<c:forEach var="accountList" items="${myAccountList}">
	        		<c:if test="${accountList.depositAccount ne param.depositAccount}">
			        	<li onclick="selectAccount(this)">
			        		<a href="javascript:;">
				        	<div class="img_box">
			                    <img src="/images/icons/passbook.png">
			                </div>
			                <div class="txt_box">
		                        <p class="account_name">${accountList.category}</p>
		                        <p class="deposit_account"><span>JAVABANK</span>${accountList.depositAccount}</p>
			                </div>
			                </a>
			        	</li>
		        	</c:if>
		            <c:if test="${accountList.depositAccount eq param.depositAccount}">
			        	<li>
			        		<a href="javascript:;">
				        	<div class="img_box">
			                    <img src="/images/icons/passbook.png">
			                </div>
			                <div class="txt_box">
		                        <p class="account_name">${accountList.category}</p>
		                        <p class="deposit_account"><span>JAVABANK</span>${accountList.depositAccount} [현재계좌]</p>
			                </div>
			                </a>
			        	</li>
		        	</c:if>
	            </c:forEach>
            </c:if>
            
        </ul>
        <p>최근 이체 내역</p>
        <ul class="recently_list account_list">
        	<c:if test="${not empty myTransactionList}">
	        	<c:forEach var="transactionList" items="${myTransactionList}">
		            <li onclick="selectAccount(this)">
		                <a href="javascript:;">
		                    <div class="img_box">
		                        <img src="/images/icons/passbook.png">
		                    </div>
		                    <div class="txt_box">
		                        <p class="account_name">${transactionList.transferName}</p>
		                        <p class="deposit_account"><span>JAVABANK</span>${transactionList.transferAccount}</p>
		                    </div>
		                </a>
		            </li>
	            </c:forEach>
            </c:if>
            <c:if test="${empty myTransactionList}">
            	<li>
	                <a href="javascript:;">
	                    <div class="img_box">
	                        <img src="/images/icons/passbook.png">
	                    </div>
	                    <div class="txt_box">
	                        <p class="account_name">최근 이체내역이 없습니다.</p>
	                    </div>
	                </a>
	            </li>
            </c:if>
        </ul>
    </div>
</section>
<!-- e: content -->
<%@ include file="bottom.jsp"%>
<script>
	let inputBox = document.getElementsByName('inputAccount')[0];
	function selectAccount(selected){
		inputBox.value = '';
		
		let text = selected.querySelector('.deposit_account').innerText.trim();
		let account = text.replace('JAVABANK', '');
		let number = account.replaceAll('-', '');
		
		inputBox.value = number;
	}
	
	function accountCheck(input){
		let value = input.value.replace(/[^0-9]/g, ''); // 숫자만 허용
		
		if (value.length > 13){
			value = value.slice(0, 13);
		}
		input.value = value;
	}
	
	function finalCheck(){
		if(inputBox.value.length < 13){
			alert("계좌번호를 다시 확인해주세요.");
			inputBox.focus();
			return false;
		} else {
			return true;
		}
	}

</script>