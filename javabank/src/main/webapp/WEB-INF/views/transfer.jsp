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
            <label>
                <input type="text" name="inputMemo" value="" placeholder="메모 입력(선택)">
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
		                        <p class="deposit_account"><span>${accountList.depositAccount}</span></p>
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
		                        <p class="deposit_account"><span>${accountList.depositAccount} [현재계좌]</span></p>
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
		                        <p class="account_name">${transactionList.userName}</p>
		                        <p class="deposit_account"><span>${transactionList.transferAccount}</span></p>
		                        <p class="update_date">${transactionList.updateDate} </p>
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
	let sendMoney = document.getElementsByName('sendMoneyAmount')[0].value;
	let inputBox = document.getElementsByName('inputAccount')[0];
	function selectAccount(selected){
		inputBox.value = '';
		
		let text = selected.querySelector('.deposit_account').innerText.trim();
		let account = text.replace('JAVABANK', '');
		
		inputBox.value = account;
	}
	
	function accountCheck(input){
		let value = input.value.replace(/[^0-9]/g, ''); // 숫자만 허용
		
	    if (value.length > 4 && value[4] !== '-'){
	    	value = value.slice(0, 4) + '-' + value.slice(4);
	    }
		
	    if (value.length > 7 && value[7] !== '-'){
	    	value = value.slice(0, 7) + '-' + value.slice(7);
	    }
		
		if (value.length > 15){
			value = value.slice(0, 15);
		}
		input.value = value;
	}
	
	function finalCheck(){
		let csrfToken = '${_csrf.token}';
		
		if(inputBox.value.length < 13){
			alert("계좌번호를 다시 확인해주세요.");
			inputBox.focus();
			return false;
			
		} else {
			let account = inputBox.value;
			// DB에 있는 계좌번호인지 확인
			
			$.ajax({
	            url: "/checkAccountExist.ajax", // 서버의 계좌 체크 URL
	            type: "POST",
	            headers: {
	                "X-CSRF-TOKEN": csrfToken
	            },
	            data: {
	                "transferAccount": account
	            },
	            success: function(res) {
	                console.log(res);
	                if (res === 'OK') {
	                	// 계좌번호가 존재하면 해당 계좌 소유자 이름 꺼내오기
	                	$.ajax({
	                		url: "/getAccountName.ajax", 
	        	            type: "POST",
	        	            headers: {
	        	                "X-CSRF-TOKEN": csrfToken
	        	            },
	        	            data: {
	        	                "depositAccount": account
	        	            },
	        	            success: function(res) {
	        	            	let name = res;
	    	                	if (confirm(account +"("+ name +")님에게 " + new Intl.NumberFormat().format(sendMoney) + "원 " +"이체하시겠습니까?")) {
	    	                		document.forms['f'].submit();
	    	                	} 
	        	            },
	        	            error: function(err){
	        	            	console.log(err);
	        	            }
	                	});
	                	
	                } else {
	                    alert("없는 계좌번호 입니다. 다시 확인해주세요.");
	                    inputBox.focus();	                    
	                }
	            },
	            error: function(err) {
	                console.log(err);
	                alert("계좌 확인 중 오류가 발생했습니다.");
	            }
	        });
	        return false;
	    }
	}

</script>