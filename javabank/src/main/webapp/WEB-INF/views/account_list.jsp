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
    <section id="account_list" class="content">
        <div class="info_box bg_yellow">
            <div class="txt_box">
                <p class="account_name">계좌명(입출금/예금/적금)</p>
                <p class="account_number">0000-0000-0000-0000</p>
                <p class="account_amount">0원</p>
            </div>
            <div class="btn_box">
                <button type="button">이체</button>
            </div>
        </div>

        <div class="account_details">
            <div class="toolbar">
                <select name="period">
                    <option value="1M">1개월</option>
                    <option value="3M">3개월</option>
                    <option value="1Y">1년</option>
                </select>
                <select name="details">
                    <option value="all">전체</option>
                    <option value="deposit">입금</option>
                    <option value="payment">출금</option>
                </select>
            </div>
            <ul class="account_list">
                <li class="account_items">
                    <div class="txt_box">
                        <p class="account_date font_gray">2024.10.10 00:00:00</p>
                        <p class="account_name">홍길동</p>
                        <p class="account_meno font_darkgray">입출금 메모</p>
                        <p></p>
                    </div>
                    <div class="account_info">
                        <p class="account_type font_blue">입금</p>
                        <p class="delta_amount font_blue">100,000원</p>
                        <p class="account_balance font_darkgray">100,000원</p>
                    </div>
                </li>
            </ul>
        </div>
    </section>
    <!-- e: content -->
</body>
</html>