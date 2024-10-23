<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="top.jsp" %>

<section id="transfer_complete" class="content">
    <div class="info_box">
    	<div class="info">
	        <h1>계좌 상세정보</h1>
		</div>
			<div class="cont">
				<label>가입상품 <input type="text" value="${productInfo.category}" disabled/></label>
				<fmt:parseDate var="parsedRegDate" value="${productInfo.regDate}" pattern="yyyy-MM-dd" />				
	        	<label>가입일자 <input type="text" value=<fmt:formatDate value="${parsedRegDate}" pattern="yyyy-MM-dd"/> disabled></label>
	        	<fmt:parseDate var="parsedExpiryDate" value="${productInfo.expiryDate}" pattern="yyyy-MM-dd" />
	        	<label>만기일자 <input type="text" value=<fmt:formatDate value="${parsedExpiryDate}" pattern="yyyy-MM-dd"/> disabled></label>
	        	<label>가입기간 <input type="text" value="${productPeriod}" disabled/></label>
	        	<c:if test="${productInfo.category eq '정기예금'}">	        		
	        		<label>가입금액 <input type="text" value="<fmt:formatNumber value="${productInfo.payment}" pattern="###,###"/>원" disabled></label>
	        	</c:if>
	        	<c:if test="${productInfo.category eq '정기적금'}">
	        		<label>납입금액 <input type="text" value="<fmt:formatNumber value="${productBalance}" pattern="###,###"/>원" disabled></label>
	        	</c:if>
	        	<label>이자율 <input type="text" value="${interestRate}" disabled></label>
			</div>
			<div class="confirm_box">
				<button class="confirm_btn" type="button" onclick="location.href='/index'">메인페이지</button>
		        <button class="confirm_btn" type="button" onclick="location.href='/myAccount'">계좌관리 목록</button>
		    </div>
    </div>
</section>

<%@ include file="bottom.jsp" %>
