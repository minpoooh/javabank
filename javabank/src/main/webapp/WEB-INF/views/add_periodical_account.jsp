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
    <form name="f" action="" method="post">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div>
            <p>비밀번호</p>
            <div class="passwd_box">
                <label>
                    <input type="password" name="depositPw" placeholder="비밀번호 4자리 입력" maxlength="4" oninput="checkPasswords(this)" required>
                </label>
                <label>
                    <input type="password" name="depositPw2" placeholder="비밀번호확인 4자리 입력" maxlength="4" oninput="checkPasswords(this)" required>
                </label>
            </div>
        </div>
		
		<div>
            <p>자동이체 계좌</p>
            <div class="info_box">
                <select name="">
                    <option value=""></option>
                </select>
            </div>
        </div>
		
        <div>
            <p>자동이체 금액</p>
            <div class="info_box">
                <label>
                    <input type="text" name="" value="">
                </label>
            </div>
        </div>

        <div>
            <p>가입 기간</p>
            <div class="info_box">
                <select name="">
                    <option value="6M">6개월 (기본 3.3%)</option>
                    <option value="12M">1년 (기본 3.5%)</option>
                </select>
            </div>
        </div>

        <div>
            <p>자동이체일 선택</p>
            <div class="info_box">
                <select name="">
                    <option value="5">매달 5일</option>
                    <option value="10">매달 10일</option>
                    <option value="25">매달25일</option>
                </select>
            </div>
        </div>

        <div class="btn_box">
            <button type="button">개설하기</button>
        </div>

    </form>
</section>
<!-- e: content -->
<%@ include file="bottom.jsp"%>

<script>
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
</script>