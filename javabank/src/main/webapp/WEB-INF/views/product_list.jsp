<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="top.jsp"%>
<!-- s: content -->
<section id="account_list" class="content">
    <div class="info_box bg_yellow">
        <div class="txt_box">
            <p class="account_name">${productInfo.category}</p>
            <p class="account_number">${productInfo.productAccount}</p>
            <p class="account_amount"><fmt:formatNumber value="${productBalance}" pattern="###,###"/>원</p>
        </div>
        <div class="btn_box">
        	<form action="/productList" method="post">
        		<input type="hidden" name="productAccount" value="${productInfo.productAccount}">
			    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            	<fmt:parseDate var="parsedExpiryDate" value="${productInfo.expiryDate}" pattern="yyyy-MM-dd" />
				만기일 : <fmt:formatDate value="${parsedExpiryDate}" pattern="yyyy-MM-dd" />
            	<button type="submit" name="submitType" value="exit">해지</button>
            </form>
        </div>
    </div>

    <div class="account_details">
        <div class="toolbar">
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
		                    <p class="account_name">신규</p>
		                    <p class="account_meno font_darkgray"># ${transactionList.memo}</p>
		                    <p></p>
		                </div>
		                <div class="account_info">
				                <p class="account_type font_red">${transactionList.type}</p>
				                <p class="delta_amount font_red">+<fmt:formatNumber value="${transactionList.deltaAmount}" pattern="###,###"/>원</p>
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

</script>


<%@ include file="bottom.jsp"%>