<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="top.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <jsp:include page="index_top.jsp"/> --%>
	<!-- s: content -->
    <section id="add_account" class="content add_bank">
        <p>입출금통장 개설</p>
        <div class="txt_box bg_yellow">
            <p>java<em>bank</em></p>
            <p>${userName}님의 통장</p>
        </div>
        <form name="f" action="" method="post">
            <div>
                <p>통장 비밀번호 설정</p>
                <div class="passwd_box">
                    <label>
                        <input type="password" name="depositPw" placeholder="비밀번호 4자리 입력" required>
                    </label>
                    <label>
                        <input type="password" placeholder="비밀번호확인 4자리 입력" required>
                    </label>
                </div>
            </div>

            <div>
                <p>금융거래 1일 이체한도</p>
                <div class="">
                    <label>
                        <input type="text" name="accountLimit" value="">
                    </label>
                </div>
            </div>

            <div class="btn_box">
                <button type="button">개설하기</button>
            </div>
            <input type="hidden" name="userId" value="">
        </form>
    </section>
    <!-- e: content -->
<%@ include file="bottom.jsp"%>