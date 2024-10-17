<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="top.jsp"%>
<!-- s: content -->
<section id="account_list" class="content">
    <div class="info_box bg_yellow">
        <div class="txt_box">
            <p class="account_name">${depositInfo.category}</p>
            <p class="account_number">${depositInfo.depositAccount}</p>
            <p class="account_amount"><fmt:formatNumber value="${depositBalance}" pattern="###,###"/>원</p>
        </div>
        <div class="btn_box">
        	<form action="/depositList" method="post">
        		<input type="hidden" name="depositAccount" value="${depositInfo.depositAccount}">
			    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            	<button type="submit" name="submitType" value="transfer">이체</button>
            </form>
        </div>
    </div>

    <div class="account_details">
        <div class="toolbar">
            <select name="period" onchange="updateList()">
                <option value="1M" ${period == '1M' ? 'selected' : ''}>1개월</option>
                <option value="3M" ${period == '3M' ? 'selected' : ''}>3개월</option>
                <option value="1Y" ${period == '1Y' ? 'selected' : ''}>1년</option>
                <option value="3Y" ${period == '3Y' ? 'selected' : ''}>3년</option>
            </select>
            <select name="details" onchange="updateList()">
                <option value="all" ${details == 'all' ? 'selected' : ''}>전체</option>
                <option value="deposit" ${details == 'deposit' ? 'selected' : ''}>입금</option>
                <option value="withdraw" ${details == 'withdraw' ? 'selected' : ''}>출금</option>
            </select>
        </div>
        <ul class="account_list">
        	<c:if test="${empty transactionList}">
        		 <li class="account_items">
		                <div class="txt_box"> 거래 내역이 없습니다. </div>
		          </li>
        	</c:if>
        	<c:if test="${not empty transactionList}">
	        	<c:forEach var="transactionList" items="${transactionList}">  	
		            <li class="account_items">
		                <div class="txt_box">
		                    <p class="account_date font_gray">${transactionList.updateDate}</p>
		                    <p class="account_name">${transactionList.userName}</p>
		                    <c:if test="${not empty transactionList.memo}">
		                    <p class="account_meno font_darkgray"># ${transactionList.memo}</p>
		                    </c:if>
		                    <c:if test="${empty transactionList.memo}">
		                    <p class="account_meno font_darkgray">${transactionList.memo}</p>
		                    </c:if>
		                    <p></p>
		                </div>
		                <div class="account_info">
		                	<c:choose>
				                <c:when test="${transactionList.type eq '출금'}">
				                    <p class="account_type font_blue">${transactionList.type}</p>
				                    <p class="delta_amount font_blue">-<fmt:formatNumber value="${transactionList.deltaAmount}" pattern="###,###"/>원</p>
				                </c:when>
				                <c:when test="${transactionList.type eq '입금'}">
				                    <p class="account_type font_red">${transactionList.type}</p>
				                    <p class="delta_amount font_red">+<fmt:formatNumber value="${transactionList.deltaAmount}" pattern="###,###"/>원</p>
				                </c:when>
				                <c:when test="${transactionList.type eq '개설'}">
				                    <p class="account_type font_darkgray">${transactionList.type}</p>
				                    <p class="delta_amount font_darkgray"><fmt:formatNumber value="${transactionList.deltaAmount}" pattern="###,###"/>원</p>
				                </c:when>
				                <c:when test="${transactionList.type eq '이자 입금'}">
				                    <p class="account_type font_red">${transactionList.type}</p>
				                    <p class="delta_amount font_red">+<fmt:formatNumber value="${transactionList.deltaAmount}" pattern="###,###"/>원</p>
				                </c:when>
				                <c:when test="${transactionList.type eq '상품가입 출금'}">
				                    <p class="account_type font_blue">${transactionList.type}</p>
				                    <p class="delta_amount font_blue">-<fmt:formatNumber value="${transactionList.deltaAmount}" pattern="###,###"/>원</p>
				                </c:when>
				                <c:otherwise>
				                    <p class="account_type font_darkgray">${transactionList.type}</p>
				                </c:otherwise>
			                </c:choose>			                	
				            	<p class="account_balance font_darkgray"><fmt:formatNumber value="${transactionList.balance}" pattern="###,###"/>원</p>
			            </div>
		            </li>   
	            </c:forEach>    
	    	</c:if>     
        </ul>
        
    </div>
</section>
<!-- e: content -->
<script>
	function updateList(){
		let csrfToken = '${_csrf.token}';
		let period = document.getElementsByName('period')[0].value;
		let details = document.getElementsByName('details')[0].value;
		let depositAccount = document.getElementsByName('depositAccount')[0].value;
		
		
		$.ajax({
			url: "/selectChange.ajax",
			type: "POST",
			headers:{
				"X-CSRF-TOKEN": csrfToken
			},
			data:{
				"depositAccount": depositAccount,
				"period": period,
				"details": details
			},
			success:function(response){
				let transactionList = response.transactionList;
				
				let newContent = '';
				if(transactionList.length === 0){
					newContent = '<li class="account_items"><div class="txt_box"> 거래 내역이 없습니다. </div></li>';
				} else {
					for (var i=0; i<transactionList.length; i++){
						let transaction = transactionList[i];
						newContent += '<li class="account_items">'+
						                '<div class="txt_box">'+
						                    '<p class="account_date font_gray">' + transaction.updateDate +'</p>'+
						                    '<p class="account_name">' + transaction.userName +'</p>'+
						                    (transaction.memo ? '<p class="account_meno font_darkgray"># ' + transaction.memo +'</p>' : '') +
						                    '<p></p>'+
						                '</div>' +
						                '<div class="account_info">';
						                if(transaction.type === '출금'){
						                	newContent += '<p class="account_type font_blue">' + transaction.type +'</p>'+
						                				  '<p class="delta_amount font_blue">-' + new Intl.NumberFormat().format(transaction.deltaAmount) +'원</p>';
						                } else if(transaction.type === '입금'){
						                	newContent += '<p class="account_type font_red">' + transaction.type +'</p>'+
						                				  '<p class="delta_amount font_red">+' + new Intl.NumberFormat().format(transaction.deltaAmount) +'원</p>';
						                } else if(transaction.type === '개설'){	
						                	newContent += '<p class="account_type font_darkgray">' + transaction.type +'</p>'+
						                				  '<p class="delta_amount font_darkgray">' + new Intl.NumberFormat().format(transaction.deltaAmount) +'원</p>';
						                } else if(transaction.type === '이자 입금'){	
						                	newContent += '<p class="account_type font_darkgray">' + transaction.type +'</p>'+
						                				  '<p class="delta_amount font_darkgray">+' + new Intl.NumberFormat().format(transaction.deltaAmount) +'원</p>';
							            } else if(transaction.type === '상품가입 출금'){	
						                	newContent += '<p class="account_type font_blue">' + transaction.type +'</p>'+
			                				  			  '<p class="delta_amount font_blue">-' + new Intl.NumberFormat().format(transaction.deltaAmount) +'원</p>';
							            }
						 newContent += '<p class="account_balance font_darkgray">' + new Intl.NumberFormat().format(Math.floor(transaction.balance)) +'원</p>'+
							           '</div>';
						               '</li>';
					}
				}
				
				document.querySelector('.account_list').innerHTML = newContent;
			},
			error:function(err){
				console.log(err);
			}
		});
	}
</script>


<%@ include file="bottom.jsp"%>