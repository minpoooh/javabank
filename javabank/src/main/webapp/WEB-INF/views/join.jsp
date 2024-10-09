<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="css/reset.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="js/script.js"></script>
    <title>javabank_Join</title>
</head>
<body>
    <section id="join">
        <form name="f" action="/join" method="post" onsubmit="return finalCheck()">
            <div class="logo_box">
                <p>java<em>bank</em></p>
            </div>            
            <div class="input_box">
                <div class="info_box">
                	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <label>
                        <input type="text" name="userName" placeholder="이름" oninput="validateName(this)" maxlength="10" required>
                    </label>
                    <label>
                        <input type="text" name="userBirth" placeholder="생년월일 ex)2024-10-01" oninput="validateBirth(this)" maxlength="10" required>
                    </label>
                    <div class="tel_box">
                        <label>
                            <input type="text" name="userTel" placeholder="핸드폰번호 ex)010-0000-0000" oninput="validateTel(this)" maxlength="13" required>
                        </label>
                    </div>
                    <div class="email_box">
                        <label>
                            <input type="text" name="userEmail1" placeholder="이메일" maxlength="16" required>
                        </label>
                        <select name="userEmail2">
                        	<option value="notSelected">==선택==</option>
                            <option value="@naver.com">@naver.com</option>
                            <option value="@nate.com">@nate.com</option>
                            <option value="@gmail.com">@gmail.com</option>
                        </select>
                        <button class="confirm_btn confirm_btn--01" type="button" name="sendBtn" onclick="sendEmail()">인증받기</button>
                    </div>
                    <!-- s: 인증번호 박스 -->
                    <div class="confirm_box" style="display: none;">
                        <label>
                            <input type="text" name="confirmNum" placeholder="인증번호 입력">
                            <div class="count_box">
                                <p><span id="timerMin">3</span>:<span id="timerSec">00</span></p>
                            </div>
                        </label>
                        <button class="confirm_btn confirm_btn--02" type="button" name="confirmBtn" onclick="confirm()">인증확인</button>
                    </div>
                    <!-- e: 인증번호 박스 -->
                </div>

                <div class="my_box">
                    <div class="id_box">
                        <label>
                            <input type="text" name="userid" placeholder="아이디" oninput="validateId(this)" maxlength="10" required>
                            <input type="hidden" name="userId" >
                        </label>
                        <button class="repeat_btn" type="button" name="checkIdBtn" onclick="checkID()">중복확인</button>
                    </div>
                    <label>
                        <input type="password" name="userPw" placeholder="비밀번호 ex)영문,숫자 포함된 6글자 이상 10자 미만" maxlength="11" oninput="checkPasswords()" required>
                    </label>
                    <label>
                        <input type="password" name="userPw2" placeholder="비밀번호확인" maxlength="11" oninput="checkPasswords()" required>
                    </label>

                    <span class="passwd_noti"></span>
                </div>
                <button class="join_btn" type="submit">회원가입</button>
            </div>
        </form>
    </section>
</body>

<script>
	let idCheck = false; // 아이디 중복확인
	let pwCheck = false; // 패스워드 일치
	let mailCheck = false; // 메일 발송
	let timeout = false; // 인증 타임아웃
	let confirmCheck = false; // 인증확인

	function validateName(input) {
	    input.value = input.value.replace(/[^a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]/g, ''); // 영문, 한글 허용
	}
	
	function validateBirth(input) {
		let value = input.value.replace(/[^0-9\-]/g, ''); // 숫자, - 허용
	    
	    if (value.length > 4 && value[4] !== '-'){
	    	value = value.slice(0, 4) + '-' + value.slice(4);
	    }
	    
	    if (value.length > 7 && value[7] !== '-'){
	    	value = value.slice(0, 7) + '-' + value.slice(7);
	    }
	    
	    input.value = value;
	}
	
	function validateTel(input) {
		let value = input.value.replace(/[^0-9\-]/g, ''); // 숫자, - 허용
	    
	    if (value.length > 3 && value[3] !== '-'){
	    	value = value.slice(0, 3) + '-' + value.slice(3);
	    }
	    
	    if (value.length > 8 && value[8] !== '-'){
	    	value = value.slice(0, 8) + '-' + value.slice(8);
	    }
	    
	    input.value = value;
	}
	
	function validateId(input) {
	    input.value = input.value.replace(/[^0-9a-zA-Z]/g, ''); // 영문, 숫자 허용
	}
	
	function checkPasswords() {
	    let pw = document.getElementsByName('userPw')[0].value;
	    let pw2 = document.getElementsByName('userPw2')[0].value;
	    let noti = document.querySelector('.passwd_noti');

	    if (pw === pw2 && pw.length > 0) {
	    	noti.textContent = '일치';
	    	noti.style.color = 'green'; // 일치할 경우 초록색으로 표시
	    	pwCheck = true;
	    } else if (pw2.length > 0) {
	    	noti.textContent = '불일치';
	    	noti.style.color = 'red'; // 불일치할 경우 빨간색으로 표시
	    	pwCheck = false;
	    } else {
	    	noti.textContent = ''; // 비밀번호가 입력되지 않은 경우
	    }
	}
	
	let timer;
	let timeRemaining = 180;
	
	function startTimer() {
	    let timerMin = document.getElementById('timerMin');
	    let timerSec = document.getElementById('timerSec');
	    
	    timer = setInterval(() => {
	        if (timeRemaining <= 0) {
	            clearInterval(timer);
	            alert("인증 시간이 초과되었습니다.");
	            timeout = true;
	            return;
	        }
	        timeRemaining--;
	        let minutes = Math.floor(timeRemaining / 60);
	        let seconds = timeRemaining % 60;

	        timerMin.textContent = minutes;
	        timerSec.textContent = seconds < 10 ? '0' + seconds : seconds;
	    }, 1000);
	}
	
	function stopTimer() {
	    if (timer) {
	        clearInterval(timer);
	        timer = null;
	    }
	}

	function sendEmail(){
		let csrfToken = '${_csrf.token}';
		let mail1 = document.getElementsByName('userEmail1')[0].value;
		let mail2 = document.getElementsByName('userEmail2')[0].value;
		let sendBtn = document.getElementsByName('sendBtn')[0];
		let confirmBox = document.querySelector('.confirm_box');
		
		if (mail1 === ''){
			alert("이메일 주소를 입력해주세요.");
			document.getElementsByName('userEmail1')[0].focus();
		} else if (mail2 === 'notSelected'){
			alert("이메일 주소를 선택해주세요.");
			document.getElementsByName('userEmail2')[0].focus();
		}		

		confirmBox.style.display = 'inline-block';
		let timeRemaining = 180;
		startTimer();
		
		$.ajax({
			url : "sendEmail.ajax",
			type : "POST",
			headers : {
				"X-CSRF-TOKEN" : csrfToken
			},
			data : {
				"mail1" : mail1,
				"mail2" : mail2
			},
			success : function(res){
				console.log(res);
				if(res === 'OK'){
					alert("인증번호가 메일로 발송되었습니다.");
					sendBtn.textContent = '재전송';
					mailCheck = true;
				} else {
					alert("메일 발송 중 에러가 발생했습니다. 관리자에게 문의해주세요. (에러코드:AB03)");
					mailCheck = false;
				}
			},
			error : function(err){
				console.log(err);
				alert("에러가 발생했습니다. 관리자에게 문의해주세요. (에러코드:AB04)");
			}
		});
		
	}
	
	function confirm(){
		let csrfToken = '${_csrf.token}';
		let inputCode = document.getElementsByName('confirmNum')[0].value;
		let confirmBtn = document.getElementsByName('confirmBtn')[0];
		$.ajax({
			url : "confirmCode.ajax",
			type : "POST",
			headers : {
                "X-CSRF-TOKEN": csrfToken
            },
			data : {
				"inputCode" : inputCode
			},
			success : function(res){
				if(res === 'OK'){
					alert("인증 완료되었습니다.")
					stopTimer();
					confirmBtn.disabled = true;
					confirmBtn.textContent = "인증완료";
					confirmBtn.style.backgroundColor = "grey";
					document.getElementsByName('confirmNum')[0].disabled = true;
					confirmCheck = true;
				}else if(res === 'ERROR'){
					alert("인증에 실패하였습니다. 인증코드를 다시 입력해주세요.")
                	document.getElementsByName('confirmNum')[0].focus();
					confirmBtn.disabled = false;
					confirmBtn.textContent = "인증확인";
					confirmBtn.style.backgroundColor = "";
					confirmCheck = false;
				}
			},
			error : function(err){
				console.log(err);
			}
		});
	}
	
	function checkID(){
		let csrfToken = '${_csrf.token}';
		let id = document.getElementsByName('userid')[0].value;
		let Id = document.getElementsByName('userId')[0];
		Id.value = id;
		let userId = Id.value;
		let checkIdBtn = document.getElementsByName('checkIdBtn')[0];

		$.ajax({
			url : "checkID.ajax",
			type : "POST",
			headers : {
                "X-CSRF-TOKEN": csrfToken
            },
			data : {
				"userId" : userId
			},
			success : function(res){
				if(res === 'OK'){
					alert("사용 가능한 아이디입니다.")
					checkIdBtn.disabled = true;
					checkIdBtn.textContent = "확인완료";
					checkIdBtn.style.backgroundColor = "grey";
					document.getElementsByName('userid')[0].disabled = true;
					idCheck = true;
				}else{
					alert("이미 사용중인 아이디입니다.")
					document.getElementsByName('userid')[0].value = '';
                	document.getElementsByName('userid')[0].focus();
                	checkIdBtn.disabled = false;
					checkIdBtn.textContent = "중복확인";
					checkIdBtn.style.backgroundColor = "";
					idCheck = false;
				}
			},
			error : function(err){
				console.log(err);
			}
		});
	}
	
	function finalCheck(){

		console.log(idCheck); //t
		console.log(pwCheck); //t
		console.log(mailCheck); //t
		console.log(timeout);
		console.log(confirmCheck);//t
		
		if (idCheck && pwCheck && mailCheck && !timeout && confirmCheck) {
		    return true;
		} else {
		    return false;
		}

	}

	
	
</script>

</html>