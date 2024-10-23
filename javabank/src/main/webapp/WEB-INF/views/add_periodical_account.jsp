<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="top.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- s: content -->
<section id="add installment_saving" class="content add_bank">
    <p>정기적금 가입</p>
    <div class="txt_box bg_yellow">
        <p>java<em>bank</em></p>
        <p>${userName}님의 정기적금</p>
    </div>
    <form name="f" action="/createPeriodicalProcess" method="post" onsubmit="return finalCheck()">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div>
            <p>비밀번호</p>
            <div class="passwd_box">
                <label>
                    <input type="password" name="productPw" placeholder="비밀번호 4자리 입력" maxlength="4" oninput="checkPasswords(this)" required>
                </label>
                <label>
                    <input type="password" name="productPw2" placeholder="비밀번호확인 4자리 입력" maxlength="4" oninput="checkPasswords(this)" required>
                </label>
                <span class="passwd_noti"></span>
            </div>
        </div>
		
		<div>
            <p>자동이체 계좌</p>
            <div class="info_box">
                <select name="selectAccount" onchange="checkAccount()">
                    <option value="notSelected">계좌를 선택해주세요.</option>
                	<c:forEach var="account" items="${accountList}">
                    	<option value="${account.depositAccount}">${account.depositAccount}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
		
        <div>
            <p>자동이체 금액</p>
            <div class="info_box">
                <label>
                    <input type="text" name="monthlyPayment" value="" maxlength="10" oninput="checkPayment(this)" required> <span>원</span>
                </label>
                <span class="info_noti"></span>
            </div>
        </div>

        <div>
            <p>가입 기간</p>
            <div class="info_box">
                <select name="registerMonth" onchange="checkRegisterMonth()">
                	<option value="notSelected">가입 기간을 선택해주세요.</option>
                    <option value="6M">6개월 (기본 3.3%)</option>
                    <option value="12M">1년 (기본 3.5%)</option>
                </select>
            </div>
        </div>

        <div>
            <p>자동이체일 선택</p>
            <div class="info_box">
                <select name="selectTransferDate" onchange="checkTransferDate()">
                	<option value="notSelected">자동이체일을 선택해주세요.</option>
                    <option value="5">5일</option>
                    <option value="10">10일</option>
                    <option value="25">25일</option>
                </select>
            </div>
        </div>

        <div class="btn_box">
            <button type="submit">개설하기</button>
        </div>

    </form>
</section>
<!-- e: content -->
<%@ include file="bottom.jsp"%>

<script>
	let pwCheck = false;  // 비밀번호 일치 체크
	let acCheck = false;  // 계좌선택 체크
	let payCheck = false; // 가입금액 체크
	let periCheck = false; // 가입기간 체크
	let transferDateCheck = false; // 자동이체일 체크
	let balanceCheck = false; // 입출금계좌 잔액 체크
	
	

	function checkPasswords(input) {
		input.value = input.value.replace(/[^0-9]/g, ''); // 숫자만 허용
	    let pw = document.getElementsByName('productPw')[0].value;
	    let pw2 = document.getElementsByName('productPw2')[0].value;
	    let noti = document.querySelector('.passwd_noti');
	    
	    if((pw.length < 4 && pw.length > 0)|| (pw2.length < 4 && pw2.length >0)) {
	    	noti.textContent = '* 비밀번호 4자리를 입력해주세요.';
	    	noti.style.color = 'red';
		}
	    
	    if(pw === pw2 && pw.length === 4) {
	    	noti.textContent = '비밀번호 일치';
	    	noti.style.color = 'green'; // 일치할 경우 초록색으로 표시
	    	pwCheck = true;
	    }
	    
	    if(pw != pw2 && pw2.length === 4) {
	    	noti.textContent = '* 비밀번호 불일치';
	    	noti.style.color = 'red'; // 불일치할 경우 빨간색으로 표시
	    	pwCheck = false;
	    }
	    
	    if(pw === '' && pw2 === ''){
	    	noti.textContent = '';
	    	pwCheck = false;
	    }
	}
	
	function checkRegisterMonth(){
		// 가입기간 체크
		let registerMonth = document.getElementsByName('registerMonth')[0].value;
		if (registerMonth != 'notSelected'){
			periCheck = true;
		} else {
			periCheck = false;
		}
		console.log("periCheck:"+periCheck);
	}
	
	function checkTransferDate(){
		// 자동이체일 선택 체크
		let selectTransferDate = document.getElementsByName('selectTransferDate')[0].value;
		if (selectTransferDate != 'notSelected'){
			transferDateCheck = true;
		} else {
			transferDateCheck = false;
		}
		console.log("transferDateCheck:"+transferDateCheck);
	}
	
	function checkPayment(input){
		let value = input.value.replace(/[^0-9]/g, ''); // 숫자만 허용
		input.value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ','); // 천단위 구분기호
		let noti = document.querySelector('.info_noti');
		
		if (value > 10000000) {
			noti.textContent = '* 10,000,000원 이내로 가입 금액을 입력해주세요.';
			noti.style.color = 'red';
			payCheck = false;
		} 
		
		if (value < 10000) {
			noti.textContent = '* 10,000원 이상으로 가입 금액을 입력해주세요.';
			noti.style.color = 'red';
			payCheck = false;
		}
		
		if (value >= 10000 && value <= 10000000){
			noti.textContent = '';
			payCheck = true;
		}
		
		if (value === ''){
			noti.textContent = '';
			payCheck = false;
		}
	}
	
	function checkAccount(){
		let csrfToken = '${_csrf.token}';
		// 입출금계좌 선택 체크
		let selectAccount = document.getElementsByName('selectAccount')[0].value;
		if (selectAccount != 'notSelected'){
			acCheck = true;
			
			// 가입금액에서 천단위 구분기호 제거
			let monthlyPayment = document.getElementsByName('monthlyPayment')[0];		
			changedValue = monthlyPayment.value.replace(/,/g, '');
			monthlyPayment.value = changedValue;			
			
			// 입출금 계좌 잔액 체크
			$.ajax({
				url : "balanceCheck.ajax",
				type : "POST",
				headers : {
	                "X-CSRF-TOKEN": csrfToken
	            },
				data : {
					"selectAccount" : selectAccount				
				},
				success : function(balance){				
					console.log("balance:"+balance);
					console.log("monthlyPayment:"+monthlyPayment.value);
					
					if(balance === -1){
						console.log("잔액 확인 중 에러");
					}
					if(balance >= monthlyPayment.value){
						balanceCheck = true;
						
					}else {
						balanceCheck = false;
					}
				},
				error : function(err){
					console.log(err);
				}
			});
			
		} else {
			acCheck = false;
		}	
	}

	
	function finalCheck(){
		console.log("pwCheck:"+pwCheck);
		console.log("payCheck:"+payCheck);
		console.log("periCheck:"+periCheck);
		console.log("transferDateCheck:"+transferDateCheck);
		console.log("balanceCheck:"+balanceCheck);
		console.log("acCheck:"+acCheck);
		
		if(!pwCheck){
			alert("비밀번호를 확인해주세요.");
		}
		
		if(!payCheck){
			alert("자동이체 금액을 확인해주세요.");
		}
		
		if(!periCheck){
			alert("가입기간을 확인해주세요.");
		}
		
		if(!transferDateCheck){
			alert("자동이체일을 확인해주세요.");
		}
		
		if(!balanceCheck){
			alert("선택하신 자동이체 계좌 잔액이 부족합니다.");
		}
		
		if(!acCheck){
			alert("자동이체 계좌를 확인해주세요.");
		}

	 	// 최종 점검
		if(pwCheck && acCheck && payCheck && periCheck && transferDateCheck && balanceCheck){
			// 가입금액에서 천단위 구분기호 제거
			let monthlyPayment = document.getElementsByName('monthlyPayment')[0];		
			changedValue = monthlyPayment.value.replace(/,/g, '');
			monthlyPayment.value = changedValue;
			return true;
		} else {
			return false;
		} 
	}
</script>