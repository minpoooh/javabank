package com.project.javabank.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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
			
		LocalDate date = LocalDate.now();
		System.out.println(date);

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
	
	@GetMapping("/createProduct")
	public String createProduct(@RequestParam(required=false) String fixed, @RequestParam(required=false) String periodical, @AuthenticationPrincipal User user, HttpServletRequest req) {
		
		String userName = mapper.getUserName(user.getUsername());
		req.setAttribute("userName", userName);
		
		List<DepositDTO> accountList = mapper.getAccountList(user.getUsername());
		req.setAttribute("accountList", accountList);
		
		
		if(fixed != null) {
			return "add_fixed_account";
		} else {
			return "add_periodical_account";
		}
		
	}
	
	@PostMapping("/createFixedProcess")
	public String createFixedProcess(@AuthenticationPrincipal User user, HttpServletRequest req, RedirectAttributes red, String productPw, String selectAccount, int payment, String registerMonth) {
	
		System.out.println(productPw);
		System.out.println(selectAccount);
		System.out.println(payment);
		System.out.println(registerMonth);
		
		// 계좌 생성 파라미터
		Map<String, Object> params = new HashMap<>();
		params.put("productPw", productPw);
		params.put("depositAccount", selectAccount);
		params.put("payment", payment);
		
		// 1. 유저 ID 뽑기
		String userId = user.getUsername();
		params.put("userId", userId);
		
		// 2. 계좌번호 랜덤으로 생성하기
		//System.out.println((Math.random() * 9)+1); // 1 ~ 9
		//System.out.println((Math.random() * 9000000) + 1000000); // 1000000 ~ 9999999
		
		int randomNum = 0;
		int depositAccountCheck = 9999;
		
		while(depositAccountCheck > 0) {
			randomNum = (int) (Math.random() * 9000000) + 1000000;
			String depositNum = "3333-02-" + String.valueOf(randomNum);
			
			// 생성된 계좌번호 중복 체크
			depositAccountCheck = mapper.getFixedAccountCheck(depositNum);
			
			if(depositAccountCheck == 0) {
				//System.out.println("확정된 계좌번호:"+ depositNum);
				params.put("productAccount", depositNum);
				break;
			}
		}
		
		// 3. 만기일자 계산
		LocalDate date = LocalDate.now();
		if(registerMonth.equals("6M")) {
			LocalDate expiry = date.plusMonths(6);
			String expiryDate = String.valueOf(expiry);
			System.out.println("expiryDate:"+ expiryDate);
			params.put("expiryDate", expiryDate);
			params.put("interestRate", 0.0028);
		} else if (registerMonth.equals("12M")) {
			LocalDate expiry = date.plusYears(1);
			String expiryDate = String.valueOf(expiry);
			System.out.println("expiryDate:"+ expiryDate);
			params.put("expiryDate", expiryDate);
			params.put("interestRate", 0.003);
		}
		
		// 4. Product, Ptransaction 테이블 INSERT
		try {
			mapper.insertProduct(params);
			red.addFlashAttribute("msg", "정기예금 상품에 가입되었습니다.");
		} catch(Exception e) {
			System.out.println("Product, Ptransaction 테이블 INSERT 에러");
			e.printStackTrace();
		}
		
		
		return "redirect:/index";
	}
	
	@ResponseBody
	@PostMapping("/balanceCheck.ajax")
	public int balanceCheck(String selectAccount) {
		try {
			int balance = mapper.getDepositBalance(selectAccount);
			return balance;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("정기예금 가입 잔액확인 중 에러");
			return -1;
		}	
	}	
	
	
	@Scheduled(cron = "0 0 0 L * *") // 매월 마지막 날 자정(00:00:00)에 작업을 실행
	//@Scheduled(cron = "0 0 * * * *") // 매시 정각, 1시간에 한번 작업 실행
	public void depositInterestCal() {
		
		// 입출금통장 이자 입금 처리
		try {
			mapper.processMonthlyInterest();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("입출금통장 한달에 한번 이자 입금 처리 에러");
		}
		
		LocalDate date = LocalDate.now();
		System.out.println(date + " 입출금통장 이자입금 완료");
	}
	
	
	
	
	
	
	
	@GetMapping("/alarms")
	public String alarms() {
		
		return "alarms";
	}
	
	
}
