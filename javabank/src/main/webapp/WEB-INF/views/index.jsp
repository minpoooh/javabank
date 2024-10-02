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
    <header>
        <div class="logo_box">
            <p>java<em>bank</em></p>
        </div>

        <div class="icon_box">
            <div class="img_box">
                <img src="images/icons/alarm.png">
            </div>
            <p class="alarm_txt">0</p>
            <div class="setting_btn img_box">
                <img src="images/icons/setting.png">
            </div>
            <ul class="setting_box" style="display: none;">
                <li><a href="javascript:;">내계좌 모아보기</a></li>
                <li><a href="javascript:;">로그아웃</a></li>
            </ul>
        </div>
    </header>

    <!-- s: content -->
    <section id="main" class="content">
        <div class="account_box">
            <p class="account_tit">내 계좌</p>
            <ul>
                <li class="nolist">
                    <a href="javascript:;">
                        <p>등록된 계좌가 없습니다. 계좌를 추가해주세요.</p>
                        <div class="img_box">
                            <img src="/@sources/images/icons/account.png">
                        </div>
                    </a>
                </li>
                <li class="account_item bg_yellow">
                    <div class="txt_box">
                        <p class="account_name">계좌명(입출금/예금/적금)</p>
                        <p class="account_number">0000-0000-0000-0000</p>
                        <p class="account_amount">0원</p>
                    </div>
                    <div class="btn_box">
                        <button type="button">조회</button>
                        <button type="button">이체</button>
                    </div>
                </li>
            </ul>
        </div>

        <div class="account_box">
            <p class="account_tit">예금</p>
            <ul>
                <li class="account_item bg_green">
                    <div class="txt_box">
                        <p class="account_name">계좌명(입출금/예금/적금)</p>
                        <p class="account_number">계좌번호</p>
                        <p class="account_amount">0원</p>
                    </div>
                    <div class="btn_box">
                        <button type="button">조회</button>
                        <button type="button">이체</button>
                    </div>
                </li>
            </ul>
        </div>

        <div class="account_box">
            <p class="account_tit">적금</p>
            <ul>
                <li class="account_item bg_blue">
                    <div class="txt_box">
                        <p class="account_name" style="color: #dbdbdb;">계좌명(입출금/예금/적금)</p>
                        <p class="account_number" style="color: #c3c1c1;">계좌번호</p>
                        <p class="account_amount" style="color: #dbdbdb;">0원</p>
                    </div>
                    <div class="btn_box">
                        <button type="button">조회</button>
                        <button type="button">이체</button>
                    </div>
                </li>
            </ul>
        </div>

        <div class="account_box">
            <p class="account_tit">상품</p>
            <ul class="product_list">
                <li>
                    <a href="javascript:;">
                        <div class="img_box">
                            <img src="/@sources/images/icons/account.png">
                        </div>
                        <span>정기예금</span>
                        <p class="txt_noti">6개월 기준 기본 2.8%</p>
                    </a>
                </li>
                <li>
                    <a href="javascript:;">
                        <div class="img_box">
                            <img src="/@sources/images/icons/account.png">
                        </div>
                        <span>정기적금</span>
                        <p class="txt_noti">6개월 기준 기본 3.3%</p>
                    </a>
                </li>
            </ul>
        </div>
    </section>
    <!-- e: content -->

    <footer>
        <ul>
            <li>Name. 김희선(HEESUN KIM)</li>
            <li><a href="mailto:khsun4624@naver.com">Mail. khsun4624@naver.com</a></li>
            <li><a href="tel:01075787465">Tel. 010-7578-7465</a></li>
            <li><a href="javascript:;">GitHub. HEESUNKIMM</a></li>
        </ul>
        <p>javabank made by hee.</p>
    </footer>
</body>
</html>