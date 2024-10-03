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
</head>
<body>
    <section id="login">
        <form name="f" action="/login" method="post">
            <div class="logo_box">
                <p>java<em>bank</em></p>
            </div>
            
            <div class="input_box">
            	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <label>
                    <input type="text" name="userid" placeholder="ID" required>
                </label>
                <label>
                    <input type="password" name="pw" placeholder="PASSWORD" required>
                </label>
                <button class="login_btn" type="submit">로그인</button>
            </div>

            <div class="save_box">
                <label> 아이디저장<input type="checkbox">
                </label>
            </div>

            <div class="join_box">
                <a href="javascript:;">회원가입</a>
                <ul class="find_box">
                    <li><a href="javascript:;">아이디찾기</a></li>
                    <li><a href="javascript:;">비밀번호찾기</a></li>
                </ul>
            </div>
        </form>
    </section>
</body>
</html>