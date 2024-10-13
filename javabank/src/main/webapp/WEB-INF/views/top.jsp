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
            <a href="/index"><p>java<em>bank</em></p></a>
        </div>

        <div class="icon_box">
            <div class="img_box">
                <a href="/alarms"><img src="images/icons/alarm.png"></a>
            </div>
            <p class="alarm_txt">0</p>
            <div class="setting_btn img_box">
                <img src="images/icons/setting.png">
            </div>
            <ul class="setting_box" style="display: none;">
                <li><a href="javascript:;">내계좌 모아보기</a></li>
                <li><a href="/logout">로그아웃</a></li>
            </ul>
        </div>
    </header>