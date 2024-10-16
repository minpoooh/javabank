<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="top.jsp" %>

<section id="transfer_complete" class="content">
    <div class="info_box">
    	<div class="info">
	        <h1>이체가 완료되었습니다!</h1>
		</div>
		<div class="cont">
	        	<label>이체일시 <input type="text" value="${transferTime}" disabled></label>
	        	<label>출금계좌 <input type="text" value="${depositAccount}" disabled></label>
	        	<label>출금금액 <input type="text" value="<fmt:formatNumber value="${sendMoneyAmount}" pattern="###,###"/>원" disabled></label>
	        	<label>수신계좌 <input type="text" value="${inputAccount}" disabled></label>
		</div>
		<div class="confirm_box">
	        <button class="confirm_btn" type="button" onclick="location.href='/index'">확인</button>
	    </div>
    </div>
</section>

<%@ include file="bottom.jsp" %>
