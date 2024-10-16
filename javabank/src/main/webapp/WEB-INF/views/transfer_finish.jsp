<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="top.jsp" %>

<section id="transfer_complete" class="content">
    <div class="complete_box">
        <h2>이체가 완료되었습니다!</h2>
        <p>아래 내용을 확인하세요.</p>

        <table class="transfer_info">
            <tr>
                <th>이체 일시</th>
                <td></td>
            </tr>
            <tr>
                <th>출금 계좌</th>
                <td></td>
            </tr>
            <tr>
                <th>보낸 금액</th>
                <td><c:out value="$" /> 원</td>
            </tr>
            <tr>
                <th>받는 사람</th>
                <td></td>
            </tr>
        </table>

        <button class="bg_yellow" onclick="">확인</button>
    </div>
</section>

<%@ include file="bottom.jsp" %>
