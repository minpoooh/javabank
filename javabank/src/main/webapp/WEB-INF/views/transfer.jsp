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
    <title>javabank</title>
</head>
<body>
    <!-- s: content -->
    <section id="transfer" class="content">
        <form name="f" action="" method="post">
            <div class="bank_info">
                <p>송금할 계좌를 입력해주세요.</p>
                <select class="bank">
                    <option value="신한">신한</option>
                    <option value="국민">국민</option>
                    <option value="기업">기업</option>
                    <option value="농협">농협</option>
                    <option value="우리">우리</option>
                    <option value="하나">하나</option>
                    <option value="카카오뱅크">카카오뱅크</option>
                    <option value="케이뱅크">케이뱅크</option>
                    <option value="토스뱅크">토스뱅크</option>
                    <option value="새마을">새마을</option>
                </select>
                <label>
                    <input type="text" name="" value="" placeholder="계좌번호 직접입력" required>
                </label>
                <button class="bg_yellow" type="button">다음</button>
            </div>
        </form>

        <div class="bank_list">
            <p>내계좌</p>
            <ul class="my_list account_list">
                <li>
                    <a href="javascript:;">
                        <div class="img_box">
                            <img src="/@sources/images/icons/passbook.png">
                        </div>
                        <div class="txt_box">
                            <p class="account_name">계좌명</p>
                            <p class="deposit_account"><span>은행명</span>0000-0000-0000-0000</p>
                        </div>
                    </a>
                </li>
            </ul>
            <p>최근 이체</p>
            <ul class="recently_list account_list">
                <li>
                    <a href="javascript:;">
                        <div class="img_box">
                            <img src="/@sources/images/icons/passbook.png">
                        </div>
                        <div class="txt_box">
                            <p class="account_name">홍길동</p>
                            <p class="deposit_account"><span>은행명</span>0000-0000-0000-0000</p>
                        </div>
                    </a>
                </li>
            </ul>
        </div>
    </section>
    <!-- e: content -->
</body>
</html>