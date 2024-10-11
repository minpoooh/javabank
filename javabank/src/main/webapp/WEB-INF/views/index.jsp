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
	                    <p>등록된 계좌가 없습니다. 계좌를 추가해주세요.</p>
	                    <div class="img_box">
	                        <img src="images/icons/account.png">
	                    </div>
	                </a>
	            </li>
            </c:if>
            
            <c:if test="${not empty accountList}">
            	<c:forEach var="accountList" items="${accountList}">
		            <li class="account_item bg_yellow">
		                <div class="txt_box">
		                    <p class="account_name">${accountList.accountCategory}</p>
		                    <p class="account_number">${accountList.depositAccount}</p>
		                    <p class="account_amount">${accountList.accountBalance}원</p>
		                </div>
		                <div class="btn_box">
		                    <button type="button">조회</button>
		                    <button type="button">이체</button>
		                </div>
		            </li>
	            </c:forEach>
            </c:if>
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
<%@ include file="bottom.jsp"%>
