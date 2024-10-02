<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="css/reset.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="js/script.js"></script>
    <title>javabank</title>
</head>
<body>
    <!-- s: content -->
    <section id="add_account" class="content add_bank">
        <p>입출금통장 개설</p>
        <div class="txt_box bg_yellow">
            <p>java<em>bank</em></p>
            <p>user님의 통장</p>
        </div>
        <form name="f" action="" method="post">
            <div>
                <p>통장 비밀번호 설정</p>
                <div class="passwd_box">
                    <label>
                        <input type="password" placeholder="비밀번호 4자리 입력" required>
                    </label>
                    <label>
                        <input type="password" placeholder="비밀번호확인 4자리 입력" required>
                    </label>
                </div>
            </div>

            <div>
                <p>통장 사용용도 선택</p>
                <div class="">
                    <select name="">
                        <option value="생활비관리">생활비관리</option>
                        <option value="급여관리">급여관리</option>
                    </select>
                </div>
            </div>

            <div>
                <p>금융거래 1일 이체한도</p>
                <div class="">
                    <label>
                        <input type="text" name="" value="">
                    </label>
                </div>
            </div>

            <div class="btn_box">
                <button type="button">개설하기</button>
            </div>

        </form>
    </section>
    <!-- e: content -->
</body>
</html>