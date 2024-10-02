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
    <section id="transfer_money" class="content">
        <form name="f" action="" method="post">
            <div class="bank_info">
                <p>송금할 금액를 입력해주세요.</p>
                <label>
                    <input type="text" name="" value="" placeholder="금액입력" required>
                </label>
                <button class="bg_yellow" type="button">다음</button>
            </div>

            <div class="select_box">
                <button type="button">1만원</button>
                <button type="button">5만원</button>
                <button type="button">10만원</button>
                <button type="button">전액</button>
            </div>
        </form>
    </section>
    <div class="popup_box" id="pwbox" style="display: none;">
        <p class="popup_title">비밀번호룰 입력해주세요.</p>
        <label>
            <input type="password" placeholder="비밀번호 4자리 입력" maxlength="4" required>
        </label>
        <div class="pbtn_box">
            <button type="button">확인</button>
            <button type="button">취소</button>
        </div>
    </div>
    <div class="dimm"></div>
    <!-- e: content -->
</body>
</html>