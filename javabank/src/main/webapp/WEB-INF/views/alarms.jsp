<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="top.jsp"%>
<!-- s: content -->
<section id="alarms" class="content">
    <ul class="alarm_list">
    	<div class="toolbar">
        	<form method="post">
				<label>
				     <select id="cate" name="alarmCate" onchange="updateList()">
				         <option value="all">전체</option>
				         <option value="new">신규</option>
				         <option value="transfer">이체</option>
				         <option value="maturity">상품만기</option>
				         <option value="interest">이자입금</option>
				         <option value="close">중도해지</option>
				     </select>
				 </label>
            </form>
        </div>
        <div class="alarm_cont">
	        <c:if test="${empty alarmList && empty newAlarmList}">
	        	<li class="alarm_items">
	    			<p>알람 이력이 없습니다.</p>
	    		</li>
	    	</c:if>
	    	<c:if test="${not empty alarmList || not empty newAlarmList}">
		    	<c:forEach var="alarm" items="${newAlarmList}">
			        <li class="alarm_items">
			            <div class="img_box">
			                <img src="images/icons/message.png">
			            </div>
			            <div class="new_box">
			                <em>${alarm.alarmCate}</em>
			                <p>${alarm.alarmCont}</p>	 
			                <fmt:parseDate var="parsedRegDate" value="${alarm.alarmRegDate}" pattern="yyyy-MM-dd HH:mm" />
							<p style="color: #959595;"><fmt:formatDate value="${parsedRegDate}" pattern="yyyy-MM-dd HH:mm"/> </p>              
			            </div>
			        </li>
		        </c:forEach>
		        <c:forEach var="alarm" items="${alarmList}">
			        <li class="alarm_items">
			            <div class="img_box">
			                <img src="images/icons/messageRead.png">
			            </div>
			            <div class="txt_box">
			                <em>${alarm.alarmCate}</em>
			                <p>${alarm.alarmCont}</p>	 
			                <fmt:parseDate var="parsedRegDate" value="${alarm.alarmRegDate}" pattern="yyyy-MM-dd HH:mm" />
							<p style="color: #959595;"><fmt:formatDate value="${parsedRegDate}" pattern="yyyy-MM-dd HH:mm"/> </p>              
			            </div>
			        </li>
		        </c:forEach>
	        </c:if>
        </div>
    </ul>
</section>
<!-- e: content -->
<%@ include file="bottom.jsp"%>

<script type="text/javascript">
	let csrfToken = '${_csrf.token}';

	window.onload = function(){
		$.ajax ({
			url : "/updateAlarmRead.ajax",
			type : "post",
			headers : {
				"X-CSRF-TOKEN": csrfToken
			},
			data : {
			},
			success : function(res){
				if(res === 'OK'){
					console.log("읽음처리 완료");
				}
				
			},
			error : function(err){
				console.log(err);
			}				
		});
	}

	function updateList(){
		let alarmCate = document.getElementsByName('alarmCate')[0].value;
		
		$.ajax ({
			url : "/updateAlarmList.ajax",
			type : "post",
			headers : {
				"X-CSRF-TOKEN": csrfToken
			},
			data : {
				alarmCate : alarmCate
			},
			success : function(res){
				let alarmList = res.alarmList;
				let newContent = '';
				
				if(alarmList.length === 0){
					newContent = '<li class="alarm_items"><p>알람 이력이 없습니다.</p></li>';
				} else {
					for (var i=0; i<alarmList.length; i++){
						let alarm = alarmList[i];
						newContent += 						
									'<li class="alarm_items">'+
					            		'<div class="img_box">'+
					                		'<img src="images/icons/messageRead.png">'+
					            		'</div>'+
					            		'<div class="txt_box">'+
							                '<em>'+ alarm.alarmCate +'</em>'+
							                '<p>'+ alarm.alarmCont +'</p>'+
							                '<p style="color: #959595;">'+ alarm.formattedAlarmRegDate +'</p>'+
							            '</div>'+
							          '</li>';
					}
				}
				document.querySelector('.alarm_cont').innerHTML = newContent;
			},
			error : function(err){
				console.log(err);
			}				
		}); 
		
		
	}

</script>