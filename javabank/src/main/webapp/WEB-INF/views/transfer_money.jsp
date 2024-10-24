<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="top.jsp"%>
<!-- s: content -->
<section id="transfer_money" class="content">
	<div class="bank_info">
	    <p>송금할 금액를 입력해주세요.</p>
	    <label>
	        <input type="text" name="amount" value="" placeholder="금액 입력" oninput="checkAmount(this)" required>
	    </label>
	    <button class="popup_btn" type="button" data-popup="pwbox">다음</button>
	</div>
	
	<div class="select_box">
	    <button type="button" onclick="changedAmount(10000)">1만원</button>
	    <button type="button" onclick="changedAmount(50000)">5만원</button>
	    <button type="button" onclick="changedAmount(100000)">10만원</button>
	    <button type="button" onclick="changedAmount('all')">전액</button>
	</div>
</section>
 
<!-- 비밀번호 입력 팝업 -->
<div class="popup_box" id="pwbox" style="display: none;">
    <p class="popup_title">비밀번호를 입력해주세요.</p>
    <label>
        <input type="password" name="inputPw" placeholder="비밀번호 4자리 입력" maxlength="4" required>
    </label>
    <div class="pbtn_box">
        <button class="confirm_btn" type="button" onclick="checkPw()">확인</button>
        <button class="close_btn" type="button">취소</button>
    </div>
</div>
<div class="dimm" id="dimm"></div>
<!-- e: content -->
<%@ include file="bottom.jsp"%>

<script>
	//팝업 열기
	document.querySelectorAll('.popup_btn').forEach(button => {
	    button.addEventListener('click', function (e) {
	        e.preventDefault();
	        let popupId = button.getAttribute('data-popup'); // 열 팝업 ID 가져오기
	        let popup = document.getElementById(popupId);
	        let dimm = document.getElementById('dimm');
	
	        if (popup) popup.style.display = 'block'; // 팝업 열기
	        if (dimm) dimm.style.display = 'block'; // dimm 표시
	    });
	});
	
	// 팝업 닫기
	document.querySelectorAll('.close_btn').forEach(button => {
	    button.addEventListener('click', function (e) {
	        e.preventDefault();
	        let popup = document.getElementById('pwbox'); // 닫을 팝업 ID 직접 참조
	        let dimm = document.getElementById('dimm');
	
	        if (popup) popup.style.display = 'none'; // 팝업 닫기
	        if (dimm) dimm.style.display = 'none'; // dimm 숨기기
	    });
	});
	
	// dimm 클릭 시 모든 팝업 닫기
	document.getElementById('dimm').addEventListener('click', function () {
	    document.querySelectorAll('.popup_box').forEach(popup => {
	        popup.style.display = 'none'; // 모든 팝업 닫기
	    });
	    this.style.display = 'none'; // dimm 숨기기
	});
	
	// 비밀번호 입력 팝업 확인 버튼 이벤트
	document.querySelector('.confirm_btn').addEventListener('click', function () {
	    document.getElementById('pwbox').style.display = 'none'; // 팝업 닫기
	    document.getElementById('dimm').style.display = 'none'; // dimm 닫기
	});
	
	function checkAmount(input){
		let value = input.value.replace(/[^0-9]/g, ''); // 숫자만 허용
		input.value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ','); // 천단위 구분기호
	}
		
	function changedAmount(amount){
		let amountBox = document.getElementsByName('amount')[0];
		
	    if (amount === 'all') {
	        amount = '${depositBalance}'; 
	    }
		 
		let number = String(amount).replace(/[^0-9]/g, ''); // 숫자만 허용
		let format = number.replace(/\B(?=(\d{3})+(?!\d))/g, ','); // 천단위 구분기호
		amountBox.value = format;
	}
	
	
	let pwCheck = false;
	let balanceCheck = false;
	let limitCheck = false;
	let depositAccount = '${depositAccount}';
	
	
	function checkLimit(){
		let csrfToken = '${_csrf.token}';
		
		// 이체한도 체크용 변수
		let transferMoneySum = '${transferMoneySum}';
		transferMoneySum = Number(transferMoneySum);

		let transactionLimit = '${transactionLimit}';
		let depositBalance = '${depositBalance}';
		let sendAmount = document.getElementsByName('amount')[0].value;
		
		// 송금 금액 천단위 구분기호 제거
		sendAmount = sendAmount.replace(/,/g, '');
		sendAmount = Number(sendAmount);
		
		let sum = transferMoneySum + sendAmount;
		
		if (sum <= transactionLimit){
			limitCheck = true;
		} else {
			limitCheck = false;
		}
		
	}
	
	function checkBalance(){

		// 잔액 체크용 변수
		let depositBalance = '${depositBalance}';
		let sendAmount = document.getElementsByName('amount')[0].value;
		sendAmount = parseInt(sendAmount.replace(/,/g, ''), 10);

		// 잔액 체크
		if (depositBalance >= sendAmount){
			balanceCheck = true; 
		} else {
			balanceCheck = false;
		}
	}
	
	function checkPw(){		
		let csrfToken = '${_csrf.token}';
		
		// 패스워드 체크용 변수
		let inputPw = document.getElementsByName('inputPw')[0].value;
		
		if(inputPw.length < 4){
			alert("비밀번호 4자리를 입력해주세요.")
			document.getElementsByName('inputPw')[0].value = '';
			pwCheck = false
		} else {
			$.ajax({
				url : "checkPwForTransfer.ajax",
				type : "POST",
				headers : {
					"X-CSRF-TOKEN" : csrfToken
				},
				data : {
					"depositAccount" : depositAccount,
					"inputPw" : inputPw
				},
				success : function(res){
					console.log(res);
					if(res === 'OK'){
						pwCheck = true;
						
						
						checkLimit();
						checkBalance();
						
						console.log("pwCheck :" + pwCheck);
						console.log("balanceCheck :" +balanceCheck);
						console.log("limitCheck :" +limitCheck);

						if(!balanceCheck){
							alert("입출금통장 잔액이 부족합니다.");
						}
						
						if(!limitCheck){
							alert("금일 이체한도가 초과되었습니다.");
						}
						
						if(pwCheck && balanceCheck && limitCheck){							
							// 동적으로 폼 생성
						    let form = document.createElement('form');
						    form.method = 'POST';
						    form.action = '/inputSendAccount'; 
						    
						 	// hidden input으로 금액 추가
						    let amount = document.createElement('input');
						    amount.type = 'hidden';
						    amount.name = 'sendMoneyAmount';
						    let value = document.getElementsByName('amount')[0].value;
						    amount.value = parseInt(value.replace(/,/g, ''), 10);
						    form.appendChild(amount);

						    // hidden input으로 출금계좌번호 추가
						    let account = document.createElement('input');
						    account.type = 'hidden';
						    account.name = 'depositAccount';
						    account.value = '${depositAccount}';
						    form.appendChild(account);
						    
							 // hidden input으로 토큰 추가
						    let token = document.createElement('input');
						    token.type = 'hidden';
						    token.name = '_csrf';
						    token.value = csrfToken;
						    form.appendChild(token);

						    // 폼을 body에 추가한 후 제출
						    document.body.appendChild(form);
						    form.submit();
						}
						
					} else if(res === 'FAIL') {
						alert("비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
						document.getElementsByName('inputPw')[0].value = '';
						pwCheck = false;
					} else {
						alert("에러 발생! 관리자에게 문의해주세요. (에러코드:AB10)")
						pwCheck = false;
					}
				},
				error : function(err){
					console.log(err);
					alert("에러가 발생했습니다. 관리자에게 문의해주세요. (에러코드:AB11)");
				}
			});
		}
	}


</script>