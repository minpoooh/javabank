<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="top.jsp" %>

<section id="transfer_complete" class="content">
    <div class="info_box">
    	<div class="info">
	        <h1>상품 해지에 따른 내용을 확인해주세요.</h1>
		</div>
		<form name="cf" action="/productCancel" method="post" onsubmit="return finalCheck()">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<input type="hidden" name="productAccount" value="${productInfo.productAccount}"/>
			<input type="hidden" name="depositAccount" value="${productInfo.depositAccount}"/>
			<input type="hidden" name="payment" value="${productInfo.payment}"/>
			<input type="hidden" name="interest" value="${expiryInterest}"/>
			<div class="cont">
				<label>가입상품 <input type="text" value="${productInfo.category}" disabled/></label>
				<fmt:parseDate var="parsedRegDate" value="${productInfo.regDate}" pattern="yyyy-MM-dd" />				
	        	<label>가입일자 <input type="text" value=<fmt:formatDate value="${parsedRegDate}" pattern="yyyy-MM-dd"/> disabled></label>
	        	<fmt:parseDate var="parsedTodayDate" value="${todayDate}" pattern="yyyy-MM-dd" />	
	        	<label>해지요청일자<input type="text" value=<fmt:formatDate value="${parsedTodayDate}" pattern="yyyy-MM-dd"/> disabled></label>
	        	<label>가입금액 <input type="text" value="<fmt:formatNumber value="${productInfo.payment}" pattern="###,###"/>원" disabled></label>
	        	<label>중도해지이자 <input type="text" value="<fmt:formatNumber value="${expiryInterest}" pattern="###,###"/>원" disabled></label>
			</div>
			<div class="confirm_box">
		        <button class="popup_btn" type="button" data-popup="pwbox">상품해지</button>
		        <button class="confirm_btn" type="button" onclick="location.href='/index'">취소</button>
		    </div>
	    </form>
    </div>
    
    <!-- 비밀번호 입력 팝업 -->
	<div class="popup_box" id="pwbox" style="display: none;">
	   <p class="popup_title">비밀번호를 입력해주세요.</p>
	   <label>
	       <input type="password" name="inputPw" placeholder="비밀번호 4자리 입력" maxlength="4" required>
	   </label>
	   <div class="pbtn_box">
	       <button class="ok_btn" type="button" onclick="checkPw()">확인</button>
	        <button class="close_btn" type="button">취소</button>
	    </div>
	</div>
	<div class="dimm" id="dimm"></div>
</section>

<%@ include file="bottom.jsp" %>
<script>
	//팝업 열기
	document.querySelectorAll('.popup_btn').forEach(button => {
	    button.addEventListener('click', function (e) {
	        e.preventDefault();
	        let popupId = button.getAttribute('data-popup'); // 열 팝업 ID 가져오기
	        let popup = document.getElementById(popupId);
	        let dimm = document.getElementById('dimm');
	
	        if (popup) popup.style.display = 'block'; // 팝업 열기
	        if (dimm) dimm.style.display = 'block'; // dimm 표시
	    });
	});
	
	// 팝업 닫기
	document.querySelectorAll('.close_btn').forEach(button => {
	    button.addEventListener('click', function (e) {
	        e.preventDefault();
	        let popup = document.getElementById('pwbox'); // 닫을 팝업 ID 직접 참조
	        let dimm = document.getElementById('dimm');
	
	        if (popup) popup.style.display = 'none'; // 팝업 닫기
	        if (dimm) dimm.style.display = 'none'; // dimm 숨기기
	    });
	});
	
	// dimm 클릭 시 모든 팝업 닫기
	document.getElementById('dimm').addEventListener('click', function () {
	    document.querySelectorAll('.popup_box').forEach(popup => {
	        popup.style.display = 'none'; // 모든 팝업 닫기
	    });
	    this.style.display = 'none'; // dimm 숨기기
	});
	
	// 비밀번호 입력 팝업 확인 버튼 이벤트
	document.querySelector('.confirm_btn').addEventListener('click', function () {
	    document.getElementById('pwbox').style.display = 'none'; // 팝업 닫기
	    document.getElementById('dimm').style.display = 'none'; // dimm 닫기
	});
	
	let pwCheck = false;
	function checkPw(){
		let productPw = '${productPw}';
		let inputPw = document.getElementsByName('inputPw')[0].value;
		
		if(inputPw.length < 4){
			alert("비밀번호 4자리를 입력해주세요.")
			document.getElementsByName('inputPw')[0].value = '';
			pwCheck = false
		} else {
			if(productPw === inputPw){
				pwCheck = true;
				let form = document.getElementsByName('cf')[0];
				form.submit();
			} else {
				alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
				document.getElementsByName('inputPw')[0].value = '';
				pwCheck = false;
			}
		}
	}

	function finalCheck(){		
		if(pwCheck){
			if(confirm("상품 해지 후 취소는 불가합니다. 해지하시겠습니까?")){
				return true;
			}
		}
		return false;
	}

</script>
