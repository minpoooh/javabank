package com.project.javabank.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.javabank.dto.DepositDTO;
import com.project.javabank.dto.DtransactionDTO;
import com.project.javabank.dto.ProductDTO;
import com.project.javabank.mapper.BankMapper;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BankController {
	
	@Autowired
	BankMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/index")
	public String index(@AuthenticationPrincipal User user,
						@RequestParam(value="javabank", required=false) String javabank, HttpServletRequest req, Model model) {
		
			// 로그인 정보 꺼내기
			String userId = user.getUsername();
			String userName = mapper.getUserName(user.getUsername());
			
			if (javabank != null) {
		    	model.addAttribute("msg", userName+"님 환영합니다.");
			}
			
			// 등록된 입출금계좌 있는지 확인
			List<DepositDTO> accountList = mapper.getAccountList(userId);

			if(accountList.size() > 0) {
				req.setAttribute("accountList", accountList);
			}
			
			// 등록된 예금계좌 있는지 확인
			List<ProductDTO> fixedDepositList = mapper.getFixedDepositList(userId);
			
			if(fixedDepositList.size() > 0) {
				req.setAttribute("fixedDepositList", fixedDepositList);
			}
			
			// 등록된 적금계좌 있는지 확인
			List<ProductDTO> periodicalDepositList = mapper.getPeriodicalDepositList(userId);
			
			if(periodicalDepositList.size() > 0) {
				req.setAttribute("periodicalDepositList", periodicalDepositList);
			}
		
		return "index";
	}
	
	@GetMapping("/createDeposit")
	public String createDeposit(@AuthenticationPrincipal User user, HttpServletRequest req) {
		
		// 로그인 정보 꺼내기
		String userName = mapper.getUserName(user.getUsername());
		
		req.setAttribute("userName", userName);
		
		return "add_account";
	}
	
	@PostMapping("/createDeposit")
	public String createDepositProcess(@AuthenticationPrincipal User user, RedirectAttributes red, String depositPw, String transactionLimit) {
		
		// 계좌 생성 파라미터
		Map<String, Object> params = new HashMap<>();
		params.put("depositPw", depositPw);
		params.put("transactionLimit", transactionLimit);
		
		// 1. 유저 ID 뽑기
		String userId = user.getUsername();
		params.put("userId", userId);
		
		// 2. 계좌번호 랜덤으로 생성하기
		//System.out.println((Math.random() * 9)+1); // 1 ~ 10
		//System.out.println((Math.random() * 9000000) + 1000000); // 1000000 ~ 10000000
		//System.out.println((Math.random() * 9000000) + 1000000); 	
		
		int randomNum = 0;
		int depositAccountCheck = 9999;
		
		while(depositAccountCheck > 0) {
			randomNum = (int) (Math.random() * 9000000) + 1000000;
			String depositNum = "3333-01-" + String.valueOf(randomNum);
			System.out.println("최초 번호:"+depositNum);
			
			// 생성된 계좌번호 중복 체크
			depositAccountCheck = mapper.getDepositAccountCheck(depositNum);
			
			if(depositAccountCheck == 0) {
				//System.out.println("확정된 계좌번호:"+ depositNum);
				params.put("depositAccount", depositNum);
				break;
			}
		}
		
		// 3. 통장 비밀번호, 이체한도 확인
		//System.out.println("통장 비밀번호: " + depositPw);
		//System.out.println("이체한도: " + 	transactionLimit);
		
		// 4. 해당 유저의 입출금통장 첫 개설인지 확인
		int depositAccountCnt = mapper.getDepositAccountCnt(userId);
		
		String mainAccount;
		if (depositAccountCnt == 0) {
			// 첫 개설이면
			mainAccount = "Y";
		} else {
			// 첫 개설이 아니면
			mainAccount = "N";
		}
		params.put("mainAccount", mainAccount);
		
		// 5. Deposit, Dtransaction 테이블 INSERT
		try {
			mapper.insertDeposit(params);
			red.addFlashAttribute("msg", "입출금 통장이 개설되었습니다.");
		} catch(Exception e) {
			System.out.println("Deposit, Dtransaction 테이블 INSERT 에러");
			e.printStackTrace();
		}
		return "redirect:/index";
	}
	
	@PostMapping("/depositList")
	public String depositList(HttpServletRequest req, String submitType, String depositAccount) {
		
		// 통장정보
		DepositDTO depositInfo = mapper.getDepositInfo(depositAccount);
		req.setAttribute("depositInfo", depositInfo);
					
		// 통장잔액
		int depositBalance = mapper.getDepositBalance(depositAccount);
		req.setAttribute("depositBalance", depositBalance);
		
		// 조회 버튼 클릭한 경우
		if(submitType.equals("list")) {
			Map<String, String> params = new HashMap<>();
			params.put("depositAccount", depositAccount);
			
			LocalDate date = LocalDate.now();
			LocalDate today = date.plusDays(1);
			String todayDate = String.valueOf(today);
			
			LocalDate oneMonthAgo = today.minusMonths(1);
			String oneMonthAgoDate = String.valueOf(oneMonthAgo);
			
			params.put("today", todayDate);
			params.put("period", oneMonthAgoDate);			
			params.put("details", "all");
			
			// 거래내역
			List<DtransactionDTO> transactionList = mapper.getDepositTransaction(params);
			System.out.println("리스트준비완료");
			req.setAttribute("transactionList", transactionList);
			
			return "deposit_list";
		} 
		// 이체 버튼 클릭한 경우
		else {
			req.setAttribute("depositAccount", depositAccount);
			req.setAttribute("depositBalance", depositBalance);
			req.setAttribute("transactionLimit", depositInfo.getTransactionLimit());
			return "transfer_money";
		}
		
	}
	
	@ResponseBody
	@PostMapping("/selectChange.ajax")
	public ResponseEntity<Map<String, Object>> selectChange (String depositAccount, String period, String details) {
	
		Map<String, String> params = new HashMap<>();
		params.put("depositAccount", depositAccount);		
		params.put("details", details);
		
		LocalDate date = LocalDate.now();
		LocalDate today = date.plusDays(1); 
		String todayDate = String.valueOf(today);
		params.put("today", todayDate);
		
		if (period.equals("1M")) {
			LocalDate oneMonthAgo = today.minusMonths(1);
			String oneMonthAgoDate = String.valueOf(oneMonthAgo);
			params.put("period", oneMonthAgoDate);
		} else if(period.equals("3M")) {
			LocalDate threeMonthAgo = today.minusMonths(3);
			String threeMonthAgoDate = String.valueOf(threeMonthAgo);
			params.put("period", threeMonthAgoDate);
		} else if(period.equals("1Y")) {
			LocalDate oneYearAgo = today.minusYears(1);
			String oneYearAgoDate = String.valueOf(oneYearAgo);
			params.put("period", oneYearAgoDate);
		} else if(period.equals("3Y")) {
			LocalDate threeYearAgo = today.minusYears(3);
			String threeYearAgoDate = String.valueOf(threeYearAgo);
			params.put("period", threeYearAgoDate);
		}		
		System.out.println(params);
		List<DtransactionDTO> transactionList = mapper.getDepositTransaction(params);
		
		Map<String, Object> response = new HashMap<>();
		response.put("transactionList", transactionList);
		response.put("period", period);
		response.put("details", details);
		
		return ResponseEntity.ok(response);
	}

	
	@ResponseBody
	@PostMapping("/checkPwForTransfer.ajax")
	public String checkPwForTransfer(String depositAccount, String inputPw) {
		try {		
			// 통장 비밀번호
			String depositPw = mapper.getDepositPw(depositAccount);
			
			if(depositPw.equals(inputPw)) {
				return "OK";
			} else {
				return "FAIL";
			}
		}catch(Exception e) {
			System.out.println("통장 비밀번호 확인 에러");
			e.printStackTrace();
			return "Error";
		}
	}
	
	
	@PostMapping("/inputSendAccount")
	public String inputSendAccount(@AuthenticationPrincipal User user, HttpServletRequest req, String depositAccount, int sendMoneyAmount) {
		// ID 꺼내기
		String userId = user.getUsername();
		List<DepositDTO> myAccountList = mapper.getMyAccountList(userId);
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", userId);
		params.put("depositAccount", depositAccount); // 출금 계좌번호
		List<DtransactionDTO> myTransactionList = mapper.getMyTransactionList(params);
		
		req.setAttribute("myAccountList", myAccountList);  // 내 계좌리스트
		req.setAttribute("myTransactionList", myTransactionList); // 최근 이체 내역
		req.setAttribute("sendMoneyAmount", sendMoneyAmount); // 이체 금액
		req.setAttribute("depositAccount", depositAccount); // 출금 계좌번호
		
		return "transfer";
	}
	
	@ResponseBody
	@PostMapping("/checkAccountExist.ajax")
	public String checkAccountExist(String transferAccount) {
		int exist = mapper.getCheckAccountExist(transferAccount);
		
		if(exist > 0) {
			return "OK";
		} else {
			return "NOTEXIST";
		}
		
	}
	
	@ResponseBody
	@PostMapping("/getAccountName.ajax")
	public String getAccountName(String depositAccount) {
		String name = mapper.getAccountName(depositAccount);
		return name;
	}
	
	@PostMapping("/transferProcess")
	public String transferProcess(@AuthenticationPrincipal User user, HttpServletRequest req, String depositAccount, int sendMoneyAmount, String inputAccount, String inputMemo) {
		
		// 상대방 계좌번호로 아이디 가져오기
		String receiveUserId = mapper.getReceiveUserId(inputAccount);
		
		Map<String, Object> params = new HashMap<>();
		params.put("depositAccount", depositAccount);	// 출금계좌
		params.put("sendMoneyAmount", sendMoneyAmount);
		params.put("inputAccount", inputAccount);		// 입금계좌
		params.put("userId", user.getUsername());
		params.put("receiveUserId", receiveUserId);
		params.put("inputMemo", inputMemo);
		
		// 이체 처리
		try {
			mapper.transferProcess(params);			
		} catch (Exception e) {
			System.out.println("이체 처리 중 에러 발생");
			e.printStackTrace();
		}
		
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
		String transferTime = dateTime.format(formatter);
		
		// 이체완료 페이지 이동
		req.setAttribute("msg", "이체가 완료되었습니다.");
		req.setAttribute("transferTime", transferTime);
		req.setAttribute("depositAccount", depositAccount);
		req.setAttribute("sendMoneyAmount", sendMoneyAmount);
		req.setAttribute("inputAccount", inputAccount);
		return "transfer_finish";
	}
	
	
	@GetMapping("/alarms")
	public String alarms() {
		
		return "alarms";
	}
}
