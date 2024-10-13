<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="css/reset.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="js/script.js"></script>
    <title>javabank_Login</title>
    <script>
    	let msg = "${msg}";
		if(msg){
			alert(msg);
		}
	</script>
</head>
<body>
    <section id="login">
        <form name="f" action="/login" method="post">
            <div class="logo_box">
                <p>java<em>bank</em></p>
            </div>
            
            <div class="input_box">
            	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            	
            	<c:if test="${empty cookie['saveId']}">
	                <label>
	                    <input type="text" name="userId" placeholder="ID" required>
	                </label>
                </c:if>
                <c:if test="${not empty cookie['saveId']}">
	                <label>
	                    <input type="text" name="userId" placeholder="ID" value="${cookie['saveId'].value}" required>
	                </label>
                </c:if>
                
                <label>
                    <input type="password" name="userPw" placeholder="PASSWORD" required>
                </label>
                <button class="login_btn" type="submit">로그인</button>
            </div>

            <div class="save_box">
            	<c:if test="${empty cookie['saveId']}">
	                <label>
	                	아이디저장	<input type="checkbox" name="saveId">
	                </label>
	            </c:if>
	            
	            <c:if test="${not empty cookie['saveId']}">
	                <label>
	                	아이디저장	<input type="checkbox" name="saveId" value="on" checked>
	                </label>
	            </c:if>
	            
            </div>

            <div class="join_box">
                <a href="/join">회원가입</a>
                <ul class="find_box">
                    <li><a class="popup_btn" href="javascript:;" data-popup="findbyid">아이디찾기</a></li>
                    <li><a class="popup_btn" href="javascript:;" data-popup="findbypw">비밀번호찾기</a></li>
                </ul>
            </div>
        </form> 
            
        <!-- 아이디 찾기 팝업 -->            
        <div id="findbyid" class="popup_box" style="display: none;">
	        <p class="popup_title">아이디 찾기</p>	        
	        <form name="f" action="/findId" method="post" onsubmit="return finalCheck()">
	        	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	            <div class="input_box">
	                <div class="email_box">
	                    <label>
	                        <input type="text" class="userEmail1" name="userEmail1" value="" placeholder="이메일입력" required>
	                    </label>
	                    <label>
	                        <select name="userEmail2" class="userEmail2">
	                        	<option value="notSelected">==선택==</option>
	                            <option value="@naver.com">@naver.com</option>
	                            <option value="@nate.com">@nate.com</option>
	                            <option value="@gmail.com">@gmail.com</option>
	                        </select>
	                    </label>
	                    <button type="button" class="confirm_btn" name=confirm_btn onclick="sendEmail()">인증번호 발송</button>
	                </div>
	            
		            <div class="confirm_box" style="display: none">
		                <label>
		                    <input type="text" class="code" name="confirmNum" value="" placeholder="인증번호 입력">		                    
		                </label>
		                <p class="timer"><span id="timerMin">3</span>:<span id="timerSec">00</span></p>
		                <button class="confirm_btn" name="confirmBtn" type="button" onclick="confirm()">인증번호 확인</button>
		            </div>
		            
		            <div class="btn_box">
			            <button class="submit_btn" type="submit">아이디 찾기</button>
			            <button class="close_btn" data-popup="findbyid">취소</button>
		        	</div>
		        </div>
		        
	        </form>       
    	</div>
        
        <!-- dimm 배경 -->
		<div class="dimm" id="dimm"></div>
        
    </section>
</body>
<script>
	//팝업 열기
	document.querySelectorAll('.popup_btn').forEach(button => {
	    button.addEventListener('click', function (e) {
	        e.preventDefault();
	        const popupId = button.getAttribute('data-popup'); // 어떤 팝업을 열지 확인
	        document.getElementById(popupId).style.display = 'block'; // 팝업 보이기
	        document.getElementById('dimm').style.display = 'block'; // dimm 보이기
	    });
	});
	
	// 팝업 닫기
	document.querySelectorAll('.close_btn').forEach(button => {
	    button.addEventListener('click', function (e) {
	        e.preventDefault();
	        const popupId = button.getAttribute('data-popup'); // 닫을 팝업 ID 가져오기
	        document.getElementById(popupId).style.display = 'none'; // 팝업 숨기기
	        document.getElementById('dimm').style.display = 'none'; // dimm 숨기기
	    });
	});
	
	// dimm 클릭 시 모든 팝업 닫기
	document.getElementById('dimm').addEventListener('click', function () {
	    document.querySelectorAll('.popup_box').forEach(popup => {
	        popup.style.display = 'none'; // 모든 팝업 숨기기
	    });
	    this.style.display = 'none'; // dimm 숨기기
	});
	
	// 최종 체크 항목
	let mailCheck = false; // 메일 발송
	let timeout = false; // 인증 타임아웃
	let confirmCheck = false; // 인증확인
	
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
	
	
	function sendEmail() {	
		
		let csrfToken = '${_csrf.token}';
		
		let mail1 = document.getElementsByName('userEmail1')[0].value;
		let mail2 = document.getElementsByName('userEmail2')[0].value;
		let sendBtn = document.getElementsByName('confirm_btn')[0];
		let confirmBox = document.querySelector('.confirm_box');
		
		if (mail1 === ''){
			alert("이메일 주소를 입력해주세요.");
			document.getElementsByName('userEmail1')[0].focus();
		} else if (mail2 === 'notSelected'){
			alert("이메일 주소를 선택해주세요.");
			document.getElementsByName('userEmail2')[0].focus();
		} else {
			// 이메일 인증 버튼 클릭 시 인증번호 입력칸 보이기
		    document.querySelector('.confirm_box').style.display = 'block';
			
			// 타이머 시작
			let timeRemaining = 180;
			startTimer();
			
			// 이메일 발송
			$.ajax({
				url : "sendEmailFindId.ajax",
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
						alert("메일 발송 중 에러가 발생했습니다. 관리자에게 문의해주세요. (에러코드:AB05)");
						mailCheck = false;
					}
				},
				error : function(err){
					console.log(err);
					alert("에러가 발생했습니다. 관리자에게 문의해주세요. (에러코드:AB06)");
				}
			});
		}
		
	}
	
	
	function confirm(){
		let csrfToken = '${_csrf.token}';
		let inputCode = document.getElementsByName('confirmNum')[0].value;
		let confirmBtn = document.getElementsByName('confirmBtn')[0];
		let sendBtn = document.getElementsByName('confirm_btn')[0];
		
		$.ajax({
			url : "confirmCodeFindId.ajax",
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
					sendBtn.style.backgroundColor = "grey";
					confirmBtn.disabled = true;
					confirmBtn.textContent = "인증완료";
					confirmBtn.style.backgroundColor = "grey";
					document.getElementsByName('confirmNum')[0].disabled = true;
					confirmCheck = true;
				}else if(res === 'ERROR'){
					alert("인증에 실패하였습니다. 인증코드를 다시 입력해주세요.")
                	document.getElementsByName('confirmNum')[0].focus();
					confirmBtn.disabled = false;
					confirmBtn.textContent = "인증코드 확인";
					confirmBtn.style.backgroundColor = "";
					confirmCheck = false;
				}
			},
			error : function(err){
				console.log(err);
			}
		});
	}
	
	function finalCheck(){

		console.log(mailCheck);
		console.log(timeout);
		console.log(confirmCheck);
		
		if(!mailCheck){
			alert("이메일을 입력하여 인증번호를 발송받아주세요.");
		}
		
		if(timeout){
			alert("인증 시간이 초과되었습니다. 새로 고침 후 다시 진행해주세요.");
		}
		
		if(!confirmCheck){
			alert("메일로 발송된 인증번호를 확인 후 입력해주세요.");
		}
		
		
		if (mailCheck && !timeout && confirmCheck) {
		   return true;
		} else {
		   return false;
		}

	}

</script>
</html>

