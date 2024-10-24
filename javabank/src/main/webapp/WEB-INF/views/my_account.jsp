<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="top.jsp"%>
<!-- s: content -->
<section id="my_account" class="content">
    <p>계좌 관리</p>
    <div class="account_box">
        <p class="account_tit">입출금계좌</p>
        <ul>
        	<c:if test="${empty depositList}">
	            <li class="nolist">
	                <p>나의 입출금계좌가 없습니다.</p>
	                <div class="img_box">
	                    <img src="../../images/icons/account.png">
	                </div>
	            </li>
			</c:if>
			
			<c:if test="${not empty depositList}">
				<c:forEach var="depositList" items="${depositList}">
		            <li class="account_item bg_yellow">
		            	<form action="/depositList" method="post">
			                <!-- 주거래: star.png / 주거래X: star_line.png -->
			                <c:if test="${depositList.mainAccount eq 'Y'}">
				                <div class="img_box">
				                    <img src="../../images/icons/star.png" data-depositAccount="${depositList.depositAccount}" onclick="changeMainAccount(this)">
				                </div>
			                </c:if>
			                <c:if test="${depositList.mainAccount eq 'N'}">
				                <div class="img_box">
				                    <img src="../../images/icons/star_line.png" data-depositAccount="${depositList.depositAccount}" onclick="changeMainAccount(this)">
				                </div>
			                </c:if>
			                <div class="txt_box">
			                    <p class="account_name">${depositList.category}</p>
			                    <p class="account_number">${depositList.depositAccount}</p>
			                    <p class="account_amount"><fmt:formatNumber value="${depositList.balance}" pattern="###,###"/>원</p>
			                </div>
			                <div class="btn_box">
			                    <input type="hidden" name="depositAccount" value="${depositList.depositAccount}">
				        		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			                    <button type="submit" name="submitType" value="detail">계좌정보</button>
			                    <button type="submit" name="submitType" value="exit">해지</button>
			                </div>
			        	</form>
		            </li>
		    	</c:forEach>
            </c:if>
        </ul>
    </div>

    <div class="account_box">
        <p class="account_tit">예금계좌</p>
        <ul>
			<c:if test="${empty fixedList}">
	            <li class="nolist">
	                <p>나의 예금 계좌가 없습니다.</p>
	                <div class="img_box">
	                    <img src="../../images/icons/account.png">
	                </div>
	            </li>
			</c:if>
			
			<c:if test="${not empty fixedList}">
				<c:forEach var="fixedList" items="${fixedList}">
					<li class="account_item bg_green">
						<form action="/productPeriodicalList" method="post">
						    <div class="txt_box">
						        <p class="account_name">${fixedList.category}</p>
						        <p class="account_number">${fixedList.productAccount}</p>
						        <p class="account_amount"><fmt:formatNumber value="${fixedList.balance}" pattern="###,###"/>원</p>
						    </div>
						    <div class="btn_box">
								<input type="hidden" name="productAccount" value="${fixedList.productAccount}">
				        		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			                    <button type="submit" name="submitType" value="detail">계좌정보</button>
			                    <button type="submit" name="submitType" value="exit">해지</button>
						    </div>
						</form>
					</li>
				</c:forEach>
			</c:if>
        </ul>
    </div>


    <div class="account_box">
        <p class="account_tit">적금계좌</p>
        <ul>
   			<c:if test="${empty periodList}">
	            <li class="nolist">
	                <p>나의 적금 계좌가 없습니다.</p>
	                <div class="img_box">
	                    <img src="../../images/icons/account.png">
	                </div>
	            </li>
			</c:if>
			<c:if test="${not empty periodList}">
				<c:forEach var="periodList" items="${periodList}">
		            <li class="account_item bg_blue">
		            	<form action="/productPeriodicalList" method="post">
			                <div class="txt_box">
			                    <p class="account_name">${periodList.category}</p>
			                    <p class="account_number">${periodList.productAccount}</p>
			                    <p class="account_amount"><fmt:formatNumber value="${periodList.balance}" pattern="###,###"/>원</p>
			                </div>
			                <div class="btn_box">
			                	<input type="hidden" name="productAccount" value="${periodList.productAccount}">
				        		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			                    <button type="submit" name="submitType" value="detail">계좌정보</button>
			                    <button type="submit" name="submitType" value="exit">해지</button>
			                </div>
			        	</form>
		            </li>
	            </c:forEach>
 			</c:if>
        </ul>
    </div>
    
    <div class="account_box">
    	<c:if test="${not empty expiryDepositList || not empty expiryProductList}">
    		<p class="account_tit">해지계좌</p>
    	</c:if>
    	
    	<c:if test="${not empty expiryDepositList}">
	        <ul>
				<c:forEach var="expiryDeposit" items="${expiryDepositList}">
		            <li class="account_item bg_grey">
		            	<form action="/depositList" method="post">
			                <div class="txt_box">
			                    <p class="account_name">${expiryDeposit.category}</p>
			                    <p class="account_number">${expiryDeposit.depositAccount}</p>
			                </div>
			                <div class="btn_box">
			                	<input type="hidden" name="depositAccount" value="${expiryDeposit.depositAccount}">
				        		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			                    <button type="submit" name="submitType" value="detail">계좌정보</button>
			                    <button type="button" name="" value="">해지완료</button>
			                </div>
			        	</form>
		            </li>
	            </c:forEach>
	        </ul>
        </c:if>
    	<c:if test="${not empty expiryProductList}">
	        <ul>
				<c:forEach var="expiryProduct" items="${expiryProductList}">
		            <li class="account_item bg_grey">
		            	<form action="/productPeriodicalList" method="post">
			                <div class="txt_box">
			                    <p class="account_name">${expiryProduct.category}</p>
			                    <p class="account_number">${expiryProduct.productAccount}</p>
			                </div>
			                <div class="btn_box">
			                	<input type="hidden" name="productAccount" value="${expiryProduct.productAccount}">
				        		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			                    <button type="submit" name="submitType" value="detail">계좌정보</button>
			                    <button type="button" name="" value="">해지완료</button>
			                </div>
			        	</form>
		            </li>
	            </c:forEach>
	        </ul>
        </c:if>
    </div>

</section>
<!-- e: content -->
<%@ include file="bottom.jsp"%>

<script type="text/javascript">
	function changeMainAccount(element){
		let csrfToken = '${_csrf.token}';
		let depositAccount = element.getAttribute('data-depositAccount');
		$.ajax({
			url : "/changeMainAccount.ajax",
			type : "post",
			headers : {
				"X-CSRF-TOKEN": csrfToken
			},
			data : {
				depositAccount : depositAccount
			},
			success : function(res){
				alert(res);
				location.reload();
			},
			error : function(err){
				console.log(err);
			}
		});
	}

</script>