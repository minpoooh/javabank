<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- s: content -->
    <section id="my_account" class="content">
        <p>내 계좌</p>
        <div class="account_box">
            <p class="account_tit">입출금</p>
            <ul>
                <li class="nolist">
                    <p>나의 입출금 계좌가 없습니다.</p>
                    <div class="img_box">
                        <img src="../../images/icons/account.png">
                    </div>
                </li>

                <li class="account_item bg_yellow">
                    <!-- 주거래: star.png / 주거래X: star_line.png -->
                    <div class="img_box">
                        <img src="../../images/icons/star_line.png">
                    </div>
                    <div class="txt_box">
                        <p class="account_name">계좌명(입출금/예금/적금)</p>
                        <p class="account_number">0000-0000-0000-0000</p>
                        <p class="account_amount">0원</p>
                    </div>
                    <div class="btn_box">
                        <button type="button">조회</button>
                        <button type="button">이체</button>
                        <button type="button">계좌삭제</button>
                    </div>
                </li>
            </ul>
        </div>

        <div class="account_box">
            <p class="account_tit">예금</p>
            <ul>

	            <li class="account_item bg_green">
	                <div class="txt_box">
	                    <p class="account_name">${deposit.category}</p>
	                    <p class="account_number">${deposit.productAccount}</p>
	                    <p class="account_amount">${deposit.accountBalance}원</p>
	                </div>
	                <div class="btn_box">
	                    <button type="button">조회</button>
	                    <button type="button">이체</button>
	                    <button type="button">계좌삭제</button>
	                </div>
	            </li>

            </ul>
        </div>
	

        <div class="account_box">
            <p class="account_tit">적금</p>
            <ul>
			    
                <li class="account_item bg_green">
                    <div class="txt_box">
                        <p class="account_name">${savingAccount.category}</p>
                        <p class="account_number">${savingAccount.productAccount}</p>
                        <p class="account_amount">${savingAccount.accountBalance}원</p>
                    </div>
                    <div class="btn_box">
                        <button type="button">조회</button>
                        <button type="button">이체</button>
                        <button type="button">계좌삭제</button>
                    </div>
                </li>
			  
            </ul>
        </div>
		
    </section>
    <!-- e: content -->