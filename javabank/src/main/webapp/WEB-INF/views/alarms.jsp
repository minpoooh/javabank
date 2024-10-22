<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="top.jsp"%>
<!-- s: content -->
<section id="alarms" class="content">
    <ul class="alarm_list">
    	<c:forEach var="alarm" items="${alarmList}">
	        <li class="alarm_items">
	            <div class="img_box">
	                <img src="images/icons/message.png">
	            </div>
	            <div class="txt_box">
	                <em>${alarm.alarmCate}</em>
	                <p>${alarm.alarmCont}</p>	 
	                <fmt:parseDate var="parsedRegDate" value="${alarm.alarmRegDate}" pattern="yyyy-MM-dd HH:mm" />
					<p style="color: #959595;"><fmt:formatDate value="${parsedRegDate}" pattern="yyyy-MM-dd HH:mm"/> </p>              
	            </div>
	        </li>
        </c:forEach>
    </ul>
</section>
<!-- e: content -->
<%@ include file="bottom.jsp"%>