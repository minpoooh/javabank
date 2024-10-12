<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="top.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<!-- s: content -->
    <section id="add_account" class="content add_bank">
        <p>입출금통장 개설</p>
        <div class="txt_box bg_yellow">
            <p>java<em>bank</em></p>
            <p>${userName}님의 통장</p>
        </div>
        <form name="f" action="/createDeposit" method="post" onsubmit="return finalCheck()">
        	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div>
                <p>통장 비밀번호 설정</p>
                <div class="passwd_box">
                    <label>
                        <input type="password" name="depositPw" placeholder="비밀번호 4자리 입력" maxlength="4" oninput="checkPasswords(this)" required>
                    </label>
                    <label>
                        <input type="password" name="depositPw2" placeholder="비밀번호확인 4자리 입력" maxlength="4" oninput="checkPasswords(this)" required>
                    </label>
                    <span class="passwd_noti"></span>
                </div>
            </div>

            <div>
                <p>금융거래 1일 이체한도</p>
                <div class="limit_box">
                    <label>
                        <input type="text" name="transactionLimit" placeholder="최대 10,000,000원" value="" maxlength="10" oninput="checkTransactionLimit(this)" required> <span>원</span>
                    </label>
                    <span class="limit_noti"></span>
                </div>
            </div>

            <div class="btn_box">
                <button type="submit">개설하기</button>
            </div>
            <input type="hidden" name="userId" value="">
        </form>
    </section>
    <!-- e: content -->
    
<script>
	let pwCheck = false; // 패스워드 일치
	let limitCheck = false; // 이체한도 체크
	
	function checkPasswords(input) {
		input.value = input.value.replace(/[^0-9]/g, ''); // 숫자만 허용
	    let pw = document.getElementsByName('depositPw')[0].value;
	    let pw2 = document.getElementsByName('depositPw2')[0].value;
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
	}
	
	function checkTransactionLimit(input){
		let value = input.value.replace(/[^0-9\-]/g, ''); // 숫자, - 허용
		input.value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ','); // 천단위 구분기호
		let noti = document.querySelector('.limit_noti');
		
		if (value > 10000000) {
			noti.textContent = '* 10,000,000원 이내로 이체 한도를 조정해주세요.';
			noti.style.color = 'red';
			limitCheck = false;
		} 
		
		if (value <= 10000000){
			noti.textContent = '';
			limitCheck = true;
		}
	}
	
	function finalCheck(){
		console.log(pwCheck);
		console.log(limitCheck);
		
		// 이체한도에서 천단위 구분기호 제거
		let transactionLimit = document.getElementsByName('transactionLimit')[0];		
		changedValue = transactionLimit.value.replace(/,/g, '');
		transactionLimit.value = changedValue;
		
		if(pwCheck && limitCheck){
			return true;
		} else {
			return false;
		}
	}
	
	
</script>    
<%@ include file="bottom.jsp"%>