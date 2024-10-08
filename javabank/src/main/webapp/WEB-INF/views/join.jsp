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
        <form name="f" action="/join" method="post">
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
                        <input type="text" name="userBirth" placeholder="생년월일 ex)2024-10-01" oninput="validateBirth(this)" maxlength="8" required>
                    </label>
                    <div class="tel_box">
                        <label>
                            <input type="text" name="userTel" placeholder="핸드폰번호 ex)010-0000-0000" oninput="validateTel(this)" maxlength="11" required>
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
                        <button class="confirm_btn confirm_btn--01" type="button">인증받기</button>
                    </div>
                    <!-- s: 인증번호 박스 -->
                    <div class="comfirm_box" style="display: none;">
                        <label>
                            <input type="text" name="confirmNum" placeholder="인증번호 입력">
                            <div class="count_box">
                                <p>3:00</p>
                            </div>
                        </label>
                        <button class="confirm_btn confirm_btn--02" type="button">인증확인</button>
                    </div>
                    <!-- e: 인증번호 박스 -->
                </div>

                <div class="my_box">
                    <div class="id_box">
                        <label>
                            <input type="text" name="userId" placeholder="아이디" oninput="validateId(this)" required>
                        </label>
                        <button class="repeat_btn" type="button">중복확인</button>
                    </div>
                    <label>
                        <input type="password" name="userPw" placeholder="비밀번호 ex)영문,숫자가 포함된 6글자 이상의 조합" required>
                    </label>
                    <label>
                        <input type="password" name="userPw2" placeholder="비밀번호확인" required>
                    </label>
                    <span class="passwd_noti"></span>
                </div>
                <button class="join_btn" type="submit">회원가입</button>
            </div>
        </form>
    </section>
</body>

<script>
	function validateName(input) {
	    input.value = input.value.replace(/[^0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]/g, ''); // 영문, 한글, 숫자 허용
	}
	
	function validateBirth(input) {
	    input.value = input.value.replace(/[^0-9]/g, ''); // 숫자 허용
	}
	
	function validateTel(input) {
	    input.value = input.value.replace(/[^0-9]/g, ''); // 숫자 허용
	}
	
	function validateId(input) {
	    input.value = input.value.replace(/[^0-9a-zA-Z]/g, ''); // 영문, 숫자 허용
	}
</script>

</html>