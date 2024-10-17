<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="top.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- s: content -->
<section id="add_deposit" class="content add_bank">
    <p>정기예금 가입</p>
    <div class="txt_box bg_yellow">
        <p>java<em>bank</em></p>
        <p>${userName}님의 정기예금</p>
    </div>
    <form name="f" action="/createFixedProcess" method="post" onsubmit="return finalCheck()">
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
            <p>출금 계좌</p>
            <div class="info_box">
                <select name="selectAccount">
                	<option value="notSelected">계좌를 선택해주세요.</option>
                	<c:forEach var="account" items="${accountList}">
                    	<option value="${account.depositAccount}">${account.depositAccount}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div>
            <p>가입 금액</p>
            <div class="info_box">
                <label>
                    <input type="text" name="payment" placeholder="최소 백만원 ~ 최대 천만원" value="" maxlength="10" oninput="checkPayment(this)" required> <span>원</span>
                </label>
                <span class="info_noti"></span>
            </div>
        </div>

        <div>
            <p>가입 기간</p>
            <div class="info_box">
                <select name="registerMonth">
                	<option value="notSelected">가입 기간을 선택해주세요.</option>
                    <option value="6M">6개월 (금리 2.8%)</option>
                    <option value="12M">1년 (금리 3.0%)</option>
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
	

	function checkPayment(input){
		let value = input.value.replace(/[^0-9]/g, ''); // 숫자만 허용
		input.value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ','); // 천단위 구분기호
		let noti = document.querySelector('.info_noti');
		
		if (value > 10000000) {
			noti.textContent = '* 10,000,000원 이내로 가입 금액을 입력해주세요.';
			noti.style.color = 'red';
			payCheck = false;
		} 
		
		if (value < 1000000) {
			noti.textContent = '* 1,000,000원 이상으로 가입 금액을 입력해주세요.';
			noti.style.color = 'red';
			payCheck = false;
		}
		
		if (value >= 1000000 && value <= 10000000){
			noti.textContent = '';
			payCheck = true;
		}
		
		if (value === ''){
			noti.textContent = '';
			payCheck = false;
		}
	}
	
	function finalCheck(){
		let csrfToken = '${_csrf.token}';
		console.log("pwCheck:"+pwCheck);
		
		// 입출금계좌 선택 체크
		let selectAccount = document.getElementsByName('selectAccount')[0].value;
		if (selectAccount != 'notSelected'){
			acCheck = true;
		} else {
			acCheck = false;
		}
		
		console.log("acCheck:"+acCheck);
		console.log("payCheck:"+payCheck);
		
		// 가입기간 체크
		let registerMonth = document.getElementsByName('registerMonth')[0].value;
		if (registerMonth != 'notSelected'){
			periCheck = true;
		} else {
			periCheck = false;
		}
		console.log("periCheck:"+periCheck);

		// 가입금액에서 천단위 구분기호 제거
		let payment = document.getElementsByName('payment')[0];		
		changedValue = payment.value.replace(/,/g, '');
		payment.value = changedValue;
		
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
				console.log("payment:"+payment.value);
				
				if(balance === -1){
					console.log("잔액 확인 중 에러");
				}
				
				if(balance >= payment.value){
					balanceCheck = true;
				}else {
					alert("선택하신 출금계좌의 잔액이 부족합니다.");
					balanceCheck = false;
				}
			},
			error : function(err){
				console.log(err);
			}
		});
		console.log("balanceCheck: "+balanceCheck);
		
		if(pwCheck && acCheck && payCheck){
			return true;
		} else {
			return false;
		}

	}
</script>