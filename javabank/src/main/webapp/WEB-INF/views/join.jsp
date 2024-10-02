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
        <form name="f" action="" method="post">
            <div class="logo_box">
                <p>java<em>bank</em></p>
            </div>
            
            <div class="input_box">
                <div class="info_box">
                    <label>
                        <input type="text" placeholder="이름" required>
                    </label>
                    <label>
                        <input type="text" placeholder="생년월일 ex&#41;2024-10-01" required>
                    </label>
                    <div class="email_box">
                        <label>
                            <input type="text" placeholder="이메일" required>
                        </label>
                        <select>
                            <option value="@naver.com">@naver.com</option>
                            <option value="@nate.com">@nate.com</option>
                            <option value="@gamil.com">@gamil.com</option>
                        </select>
                    </div>
                    <div class="tel_box">
                        <label>
                            <input type="text" placeholder="핸드폰번호 ex&#41;010-0000-0000" required>
                        </label>
                        <button class="confirm_btn confirm_btn--01" type="button">인증받기</button>
                    </div>
                    <!-- s: 인증번호 박스 -->
                    <div class="comfirm_box" style="display: none;">
                        <label>
                            <input type="text" placeholder="인증번호 입력" required>
                            <div class="count_box">
                                <span>3:00</span>
                            </div>
                        </label>
                        <button class="confirm_btn confirm_btn--02" type="button">인증확인</button>
                    </div>
                    <!-- e: 인증번호 박스 -->
                </div>

                <div class="my_box">
                    <div class="id_box">
                        <label>
                            <input type="text" placeholder="아이디" required>
                        </label>
                        <button class="repeat_btn" type="button">중복확인</button>
                    </div>
                    <label>
                        <input type="password" placeholder="비밀번호 ex&#41;영문,숫자가 포함된 8글자 이상의 조합" required>
                    </label>
                    <!-- <p class="passwd_noti"></p> -->
                </div>
                <button class="join_btn" type="button">회원가입</button>
            </div>
        </form>
    </section>
</body>
</html>