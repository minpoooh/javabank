<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="top.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- s: content -->
<section id="main" class="content">
    <div class="account_box">
        <p class="account_tit">내 계좌</p>
        <ul>
        	<c:if test="${empty accountList}">
	            <li class="nolist">
	                <a href="/createDeposit">
	                    <p>등록된 계좌가 없습니다. 계좌를 등록해주세요.</p>
	                    <div class="img_box">
	                        <img src="images/icons/account.png">
	                    </div>
	                </a>
	            </li>
            </c:if>
            
            <c:if test="${not empty accountList}">
	            <li class="list">
	                <a href="/createDeposit">
	                    <p>입출금통장 추가 개설하기</p>
	                    <div class="img_box">
	                        <img src="images/icons/account.png">
	                    </div>
	                </a>
	            </li>
            </c:if>
            
            <c:if test="${not empty accountList}">
            	<c:forEach var="accountList" items="${accountList}">
		            <li class="account_item bg_yellow">
		            	<form action="/depositList" method="post">
			                <div class="txt_box">
			                    <p class="account_name">${accountList.category}</p>
			                    <p class="account_number">${accountList.depositAccount}</p>
			                    <p class="account_amount"><fmt:formatNumber value="${accountList.balance}" pattern="###,###"/>원</p>
			                </div>
			                <div class="btn_box">
			                	<input type="hidden" name="depositAccount" value="${accountList.depositAccount}">
			                	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			                    <button type="submit" name="submitType" value="list">조회</button>
			                    <button type="submit" name="submitType" value="transfer">이체</button>
			                </div>
		                </form>
		            </li>
	            </c:forEach>
            </c:if>
        </ul>
    </div>

	<c:if test="${not empty fixedDepositList}">
    <div class="account_box">
        <p class="account_tit">예금</p>
        <c:forEach var="fixedDeposit" items="${fixedDepositList}">
        <ul>
            <li class="account_item bg_green">
                <div class="txt_box">
                    <p class="account_name">${fixedDeposit.category}</p>
                    <p class="account_number">${fixedDeposit.productAccount}</p>
                    <p class="account_amount"><fmt:formatNumber value="${fixedDeposit.balance}" pattern="###,###"/>원</p>
                </div>
                <div class="btn_box">
                    <button type="button">조회</button>
                    <button type="button">이체</button>
                </div>
            </li>
        </ul>
        </c:forEach>
    </div>
    </c:if>
	
	
	<c:if test="${not empty periodicalDepositList}">
    <div class="account_box">
        <p class="account_tit">적금</p>
        <c:forEach var="periodicalDeposit" items="${periodicalDepositList}">
        <ul>
            <li class="account_item bg_blue">
                <div class="txt_box">
                    <p class="account_name" style="color: #dbdbdb;">${periodicalDeposit.category}</p>
                    <p class="account_number" style="color: #c3c1c1;">${periodicalDeposit.productAccount}</p>
                    <p class="account_amount" style="color: #dbdbdb;">${periodicalDeposit.balance}원</p>
                </div>
                <div class="btn_box">
                    <button type="button">조회</button>
                    <button type="button">이체</button>
                </div>
            </li>
        </ul>
        </c:forEach>
    </div>
    </c:if>
	
    <div class="account_box">
        <p class="account_tit">상품가입</p>
        <ul class="product_list">
            <li>
                <a href="/createProduct?fixed">
                    <div class="img_box">
                        <img src="/images/icons/pig.png">
                    </div>
                    <span>정기예금 가입하기</span>
                    <p class="txt_noti">6개월 기준 기본 3.0%</p>
                </a>
            </li>

            <li>
                <a href="/createProduct?periodical">
                    <div class="img_box">
                        <img src="/images/icons/timer_money.png">
                    </div>
                    <span>정기적금 가입하기</span>
                    <p class="txt_noti">6개월 기준 기본 3.5%</p>
                </a>
            </li>
        </ul>
    </div>

</section>
<!-- e: content -->
<%@ include file="bottom.jsp"%>


