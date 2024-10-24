package com.project.javabank.controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.javabank.dto.AlarmDTO;
import com.project.javabank.dto.DepositDTO;
import com.project.javabank.dto.DtransactionDTO;
import com.project.javabank.dto.ProductDTO;
import com.project.javabank.dto.PtransactionDTO;
import com.project.javabank.dto.autoTransferLogger;
import com.project.javabank.mapper.BankMapper;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BankController {
	
	@Autowired
	BankMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// 메인페이지
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
	
	// 입출금통장 개설 페이지 이동
	@GetMapping("/createDeposit")
	public String createDeposit(@AuthenticationPrincipal User user, HttpServletRequest req) {
		// 로그인 정보 꺼내기
		String userName = mapper.getUserName(user.getUsername());
		req.setAttribute("userName", userName);
		return "add_account";
	}
	
	// 입출금통장 개설 처리
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
		//System.out.println((Math.random() * 9000000) + 1000000); // 1000000 ~ 9999999
		int randomNum = 0;
		int depositAccountCheck = 9999;
		while(depositAccountCheck > 0) {
			randomNum = (int) (Math.random() * 9000000) + 1000000;
			String depositNum = "3333-01-" + String.valueOf(randomNum);
			// 생성된 계좌번호 중복 체크
			depositAccountCheck = mapper.getDepositAccountCheck(depositNum);
			if(depositAccountCheck == 0) {
				params.put("depositAccount", depositNum);
				break;
			}
		}
		
		// 3. 해당 유저의 입출금통장 첫 개설인지 확인
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
		
		// 4. Deposit, Dtransaction, Alarm 테이블 INSERT
		try {
			mapper.insertDeposit(params);
			red.addFlashAttribute("msg", "입출금 통장이 개설되었습니다.");
		} catch(Exception e) {
			System.out.println("Deposit, Dtransaction 테이블 INSERT 에러");
			e.printStackTrace();
		}
		return "redirect:/index";
	}
	
	// 입출금통장 처리
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
			
		} else if(submitType.equals("transfer")) {
			// 이체 버튼 클릭한 경우
			req.setAttribute("depositAccount", depositAccount);
			req.setAttribute("depositBalance", depositBalance);
			req.setAttribute("transactionLimit", depositInfo.getTransactionLimit());
			return "transfer_money";
			
		} else if(submitType.equals("detail")) {
			// 계좌상세버튼 클릭한 경우
			// 이자율
			double interestRate = (double)depositInfo.getInterestRate();
			double percent = interestRate * 100;
			DecimalFormat df = new DecimalFormat("0.00");
	        String formattedPercent = df.format(percent);
			String interestRateStr = formattedPercent + "%";
			req.setAttribute("interestRate", interestRateStr);
			return "deposit_detail";
			
		} else {
			// 해지버튼 클릭한 경우
			LocalDate date = LocalDate.now();
			String todayDate = String.valueOf(date);
			req.setAttribute("todayDate", todayDate);
			
			// 패스워드
			req.setAttribute("depositPw", depositInfo.getDepositPw());
			
			// 잔액
			req.setAttribute("depositBalance", depositBalance);
			
			return "deposit_exit";
		}
	}
	
	// 입출금통장 거래내역 정렬옵션 변경
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
		//System.out.println(params);
		List<DtransactionDTO> transactionList = mapper.getDepositTransaction(params);
		
		Map<String, Object> response = new HashMap<>();
		response.put("transactionList", transactionList);
		response.put("period", period);
		response.put("details", details);
		
		return ResponseEntity.ok(response);
	}

	// 이체 시 비밀번호 확인
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
	
	// 계좌번호 입력 페이지 이동
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
	
	// 이체 시 계좌번호가 존재하는지 확인
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
	
	// 입출금통장 소유자 이름 조회
	@ResponseBody
	@PostMapping("/getAccountName.ajax")
	public String getAccountName(String depositAccount) {
		String name = mapper.getAccountName(depositAccount);
		return name;
	}
	
	// 이체 처리
	@PostMapping("/transferProcess")
	public String transferProcess(@AuthenticationPrincipal User user, HttpServletRequest req, String depositAccount, int sendMoneyAmount, String inputAccount, String inputMemo) {
		
		// 상대방 계좌번호로 아이디 가져오기
		String receiveUserId = mapper.getDepositUserId(inputAccount);
		
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
			System.err.println("이체 처리 중 에러 발생");
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
	
	// 입출금통장 해지 처리
	@PostMapping("/depositCancel")
	public String cancelDeposit(@AuthenticationPrincipal User user, RedirectAttributes red, String depositAccount) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("userId", user.getUsername());
		params.put("depositAccount", depositAccount);
		
		try {
			mapper.cancelAccount(params);
		} catch(Exception e) {
			System.err.println("입출금 통장 해지 에러");
			e.printStackTrace();
		}
		
		red.addFlashAttribute("msg", "입출금통장 해지가 완료되었습니다. 이용해주셔서 감사합니다.");
		return "redirect:/index";
	}
	
	// 입출금통장 매달 이자입금 처리
	public void monthlyInterest() {
		try {
			mapper.processMonthlyInterest();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("입출금통장 한달에 한번 이자 입금 처리 에러");
		}
		LocalDate date = LocalDate.now();
		System.out.println(date + " 입출금통장 이자입금 완료");
	}
	
	// 예적금 가입 페이지 이동
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
	
	// 예금가입 처리
	@PostMapping("/createFixedProcess")
	public String createFixedProcess(@AuthenticationPrincipal User user, HttpServletRequest req, RedirectAttributes red, String productPw, String selectAccount, int payment, String registerMonth) {
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
			params.put("expiryDate", expiryDate);
			params.put("interestRate", 0.0028);
		} else if (registerMonth.equals("12M")) {
			LocalDate expiry = date.plusYears(1);
			String expiryDate = String.valueOf(expiry);
			params.put("expiryDate", expiryDate);
			params.put("interestRate", 0.003);
		}
		
		// 4. Product, Ptransaction 테이블 INSERT
		try {
			mapper.insertFixedProduct(params);
			red.addFlashAttribute("msg", "정기예금 상품에 가입되었습니다.");
		} catch(Exception e) {
			System.err.println("Product, Ptransaction 테이블 INSERT 에러");
			e.printStackTrace();
		}
		return "redirect:/index";
	}
	
	// 입출금통장 잔액 확인
	@ResponseBody
	@PostMapping("/balanceCheck.ajax")
	public int balanceCheck(String selectAccount) {
		try {
			int balance = mapper.getDepositBalance(selectAccount);
			return balance;
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("정기예금 가입 잔액확인 중 에러");
			return -1;
		}	
	}	
	
	// 메인 - 예금 페이지 이동 처리
	@PostMapping("/productFixedList")
	public String productFixedList(HttpServletRequest req, String submitType, String productAccount) {
		
		// 상품정보
		ProductDTO productInfo = mapper.getProductInfo(productAccount);
		req.setAttribute("productInfo", productInfo);
		
		// 상품잔액
		int productBalance = mapper.getProductBalance(productAccount);
		req.setAttribute("productBalance", productBalance);
		
		// 조회 버튼 클릭한 경우
		if(submitType.equals("list")) {
			Map<String, String> params = new HashMap<>();
			params.put("productAccount", productAccount);
			
			LocalDate date = LocalDate.now();
			LocalDate today = date.plusDays(1);
			String todayDate = String.valueOf(today);
			
			LocalDate oneMonthAgo = today.minusMonths(1);
			String oneMonthAgoDate = String.valueOf(oneMonthAgo);
			
			params.put("today", todayDate);
			params.put("period", oneMonthAgoDate);			
			
			// 거래내역
			List<PtransactionDTO> transactionList = mapper.getProductTransaction(params);
			req.setAttribute("transactionList", transactionList);
			req.setAttribute("category", "fixed");
			return "product_list";
		} else if(submitType.equals("exit")){
			// 해지 버튼 클릭한 경우
			LocalDate date = LocalDate.now();
			String todayDate = String.valueOf(date);
			req.setAttribute("todayDate", todayDate);
			
			// 가입금액
			int payment = productInfo.getPayment();
			
			// 예치일 구하기
			String regDateStr = productInfo.getRegDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDate regDate = LocalDate.parse(regDateStr, formatter);
			double daysBetween = ChronoUnit.DAYS.between(regDate, date);
			
			// 가입기간
			double interest = productInfo.getInterestRate();
			int productRegPeriod;
			if(interest == 0.0028) {
				productRegPeriod = 180;
			} else {
				productRegPeriod = 365;
			}
			
			// 이자 계산
			double expiryInterest = payment * interest * daysBetween / productRegPeriod ;
			int expiryInterestInt = (int)expiryInterest;
			req.setAttribute("expiryInterest", expiryInterestInt);
			
			// 패스워드
			req.setAttribute("productPw", productInfo.getProductPw());
			
			return "product_exit";
		} else {
			// 상품 계좌상세 클릭한 경우
			// 이자율
			double interestRate = (double)productInfo.getInterestRate();
			double percent = interestRate * 100;
			DecimalFormat df = new DecimalFormat("0.00");
	        String formattedPercent = df.format(percent);
			String interestRateStr = formattedPercent + "%";
			req.setAttribute("interestRate", interestRateStr);
			
	        // 가입기간
 			String productPeriod; 			
			if(interestRate == 0.0028) {
 				productPeriod = "6개월";
 			} else {
 				productPeriod = "1년";
 			}
	        req.setAttribute("productPeriod", productPeriod);
			return "product_detail";
		}
	}
	
	// 상품 해지 처리
	@PostMapping("/productCancel")
	public String productCancel(@AuthenticationPrincipal User user, RedirectAttributes red, String category, String productAccount, 
								String depositAccount, String payment, String interest, String productBalance) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("category", category);
		params.put("productAccount", productAccount);
		params.put("depositAccount", depositAccount);
		params.put("payment", Integer.parseInt(payment));
		params.put("interest", Integer.parseInt(interest));
		params.put("userId", user.getUsername());
		params.put("productBalance", Integer.parseInt(productBalance));
		
		try {
			mapper.cancelProduct(params);
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("상품해지 중 에러 발생");
		}
		
		red.addFlashAttribute("msg", "상품 해지가 완료되었습니다. 이용해주셔서 감사합니다.");
		return "redirect:/index";
	}
	
	// 적금가입 처리
	@PostMapping("/createPeriodicalProcess")
	public String createPeriodicalProcess(@AuthenticationPrincipal User user, HttpServletRequest req, RedirectAttributes red, 
			String productPw, String selectAccount, String monthlyPayment, String registerMonth, String selectTransferDate) {
	
		// 계좌 생성 파라미터
		Map<String, Object> params = new HashMap<>();
		params.put("productPw", productPw);
		params.put("depositAccount", selectAccount);
		params.put("monthlyPayment", Integer.parseInt(monthlyPayment));
		params.put("autoTransferDate", selectTransferDate);

		// 1. 유저 ID 뽑기
		String userId = user.getUsername();
		params.put("userId", userId);
		
		// 2. 계좌번호 랜덤으로 생성하기
		int randomNum = 0;
		int depositAccountCheck = 9999;
		
		while(depositAccountCheck > 0) {
			randomNum = (int) (Math.random() * 9000000) + 1000000;
			String depositNum = "3333-03-" + String.valueOf(randomNum);
			
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
			params.put("expiryDate", expiryDate);
			params.put("interestRate", 0.0033);
		} else if (registerMonth.equals("12M")) {
			LocalDate expiry = date.plusYears(1);
			String expiryDate = String.valueOf(expiry);
			params.put("expiryDate", expiryDate);
			params.put("interestRate", 0.0035);
		}
		
		// 4. Product, Ptransaction 테이블 INSERT
		try {
			//System.out.println(params);
			mapper.insertPeriodicalProduct(params);
			red.addFlashAttribute("msg", "정기적금 상품에 가입되었습니다.");
		} catch(Exception e) {
			System.err.println("Product, Ptransaction 테이블 INSERT 에러");
			e.printStackTrace();
		}	
		
		return "redirect:/index";
	}
	
	// 메인 - 적금 페이지 이동 처리
	@PostMapping("/productPeriodicalList")
	public String productPeriodicalList(HttpServletRequest req, String submitType, String productAccount) {
		
		// 상품정보
		ProductDTO productInfo = mapper.getProductInfo(productAccount);
		req.setAttribute("productInfo", productInfo);
		
		// 상품잔액
		int productBalance = mapper.getProductBalance(productAccount);
		req.setAttribute("productBalance", productBalance);
		
		// 유저명
		String userName = mapper.getUserName(productInfo.getUserId());
		
		// 조회 버튼 클릭한 경우
		if(submitType.equals("list")) {
			Map<String, String> params = new HashMap<>();
			params.put("productAccount", productAccount);
			
			LocalDate date = LocalDate.now();
			LocalDate today = date.plusDays(1);
			String todayDate = String.valueOf(today);
			
			LocalDate oneMonthAgo = today.minusMonths(1);
			String oneMonthAgoDate = String.valueOf(oneMonthAgo);
			
			params.put("today", todayDate);
			params.put("period", oneMonthAgoDate);			
			
			// 거래내역
			List<PtransactionDTO> transactionList = mapper.getProductTransaction(params);
			req.setAttribute("transactionList", transactionList);
			req.setAttribute("category", "periodical");
			req.setAttribute("userName", userName);
			return "product_list";
			
		} else if(submitType.equals("exit")){
			// 해지 버튼 클릭한 경우
			LocalDate date = LocalDate.now();
			String todayDate = String.valueOf(date);
			req.setAttribute("todayDate", todayDate);
			
			// 예치일 구하기
			String regDateStr = productInfo.getRegDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDate regDate = LocalDate.parse(regDateStr, formatter);
			double daysBetween = ChronoUnit.DAYS.between(regDate, date);
			
			// 가입기간
			double interest = productInfo.getInterestRate();
			int productRegPeriod;
			if(interest == 0.0033) {
				productRegPeriod = 180;
			} else {
				productRegPeriod = 365;
			}
			
			// 이자 계산
			double expiryInterest = productBalance * interest * daysBetween / productRegPeriod ;
			int expiryInterestInt = (int)expiryInterest;
			req.setAttribute("expiryInterest", expiryInterestInt);
			
			// 패스워드
			req.setAttribute("productPw", productInfo.getProductPw());
			
			return "product_exit";
		} else {
			// 상품 계좌상세 클릭한 경우
			// 이자율
			double interestRate = (double)productInfo.getInterestRate();
			double percent = interestRate * 100;
			DecimalFormat df = new DecimalFormat("0.00");
	        String formattedPercent = df.format(percent);
			String interestRateStr = formattedPercent + "%";
			req.setAttribute("interestRate", interestRateStr);
			
	        // 가입기간
 			String productPeriod; 			
			if(interestRate == 0.0033) {
 				productPeriod = "6개월";
 			} else {
 				productPeriod = "1년";
 			}
	        req.setAttribute("productPeriod", productPeriod);
 			
			return "product_detail";
		}
		
	}
	
	// 적금 자동이체 처리
	public void autoTransfer() {
		LocalDate todayDate = LocalDate.now();
		int date = todayDate.getDayOfMonth();
		System.out.println("자동이체 스케줄러 시작");
		try {
			// 오늘이 자동이체 날인 적금 상품 리스트 찾기
			List<ProductDTO> list = mapper.getTransferPeriodicalAccount(date);
			
			Map<String, Object> params = new HashMap<>();
			if(list.size() > 0) {
				for(ProductDTO product : list) {
					params.put("depositAccount",product.getDepositAccount());
					params.put("productAccount",product.getProductAccount());
					params.put("monthlyPayment",product.getMonthlyPayment());
					params.put("userId", product.getUserId());
					
					// deposit 테이블 잔액 계산
					int balance = mapper.getDepositBalance(product.getDepositAccount());
					
					if(balance >= product.getMonthlyPayment()) {
						int updatedDbalance = balance - product.getMonthlyPayment();
						params.put("balance", updatedDbalance);
											
						// product 테이블 잔액 계산
						int pBalance = mapper.getProductBalance(product.getProductAccount());
						int updatedPbalance = pBalance + product.getMonthlyPayment();
						params.put("productBalance", updatedPbalance);
						
						//System.out.println(params);
						mapper.autoTransfer(params);
						
						System.out.println("자동이체 처리 성공");
					} else {
						// 자동이체 실패 시 에러 로그 파일 저장
						autoTransferLogger.logFailedTransfer(product.getProductAccount(), balance, product.getMonthlyPayment());
					}
					
				}
			} else {
				System.out.println("자동이체 대상 없음");
			}
		} catch(Exception e) {
			System.err.println("자동이체 스케줄러 에러 발생");
			e.printStackTrace();
		}
	}
	
	// 상품 만기해지 처리
	public void productMaturity(){
		LocalDate todayDate = LocalDate.now();
		String today = String.valueOf(todayDate);		
		System.out.println("상품 만기해지 스케줄러 시작");
		try {
			// 오늘이 만기인 상품리스트 찾기
			List<ProductDTO> list = mapper.getDepositMaturity(today);
			if(list.size() > 0) {
				for(ProductDTO product : list) {
					if(product.getCategory().equals("정기예금")) {
						System.out.println("정기예금 만기처리 시작");
						// 1. 가입금액
						int payment = product.getPayment();
						// 2. 이자계산
						double interest = product.getInterestRate();						
						double expiryInterest = payment * interest;
						int expiryInterestInt = (int)expiryInterest;
						System.out.println("이자 계산완료");
						System.out.println("payment :" + payment);
						System.out.println("interest :"+ interest);
						System.out.println("expiryInterestInt :"+ expiryInterestInt);
						// 3. 정기예금 통장 해지 + 출금						
						Map<String, Object> params = new HashMap<>();
						params.put("productAccount", product.getProductAccount());
						params.put("payment", payment);
						// 4. 입출금통장 원금 + 이자 입금
						params.put("depositAccount", product.getDepositAccount());
						params.put("interest", expiryInterestInt);
						params.put("userId", product.getUserId());
						mapper.ExpiryFixedAccount(params);
						System.out.println("정기예금 해지 완료");
					} else if(product.getCategory().equals("정기적금")) {
						System.out.println("정기적금 만기처리 시작");
						// 1. 납입금액
						int productBalance = mapper.getProductBalance(product.getProductAccount());
						// 2. 이자계산
						double interest = product.getInterestRate();						
						double expiryInterest = productBalance * interest;
						int expiryInterestInt = (int)expiryInterest;
						System.out.println("이자 계산완료");
						System.out.println("productBalance :" + productBalance);
						System.out.println("interest :"+ interest);
						System.out.println("expiryInterestInt :"+ expiryInterestInt);
						// 3. 정기적금 통장 해지 + 출금						
						Map<String, Object> params = new HashMap<>();
						params.put("productAccount", product.getProductAccount());
						params.put("productBalance", productBalance);
						// 4. 입출금통장 원금 + 이자 입금
						params.put("depositAccount", product.getDepositAccount());
						params.put("interest", expiryInterestInt);
						params.put("userId", product.getUserId());
						mapper.ExpiryPeriodicalAccount(params);
						System.out.println("정기적금 해지 완료");
					} else {
						System.err.println("만기 스케줄러 작동 중 상품 구분 에러");
					}
				}
			} else {
				System.out.println("만기 상품 없음");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("만기예정 예금리스트 찾기 에러");
		}
	}

	
	// 알람페이지 이동
	@GetMapping("/alarms")
	public String alarms(@AuthenticationPrincipal User user, HttpServletRequest req) {		
		String userId = user.getUsername();
		// 읽지 않은 알람 리스트
		List<AlarmDTO> newAlarmList = mapper.getNewAlarmList(userId);
		req.setAttribute("newAlarmList", newAlarmList);
		
		// 읽은 알람 리스트
		List<AlarmDTO> alarmList = mapper.getAlarmList(userId);
		req.setAttribute("alarmList", alarmList);
		
		return "alarms";
	}
	
	// 알람페이지 이동 후 알림 읽음 처리
	@ResponseBody
	@PostMapping("/updateAlarmRead.ajax")
	public String updateAlarmRead(@AuthenticationPrincipal User user) {
		String userId = user.getUsername();
		try {
			// 알림 읽음 처리
			mapper.updateReadY(userId);
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("알림 읽음 처리 중 에러");
		}
		return "OK";
	}
	
	// 알람 정렬
	@ResponseBody
	@PostMapping("/updateAlarmList.ajax")
	public ResponseEntity<Map<String, Object>> updateAlarmList(@AuthenticationPrincipal User user, String alarmCate) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", user.getUsername());
		
		// 정렬 기준에 맞는 리스트 조회
		List<AlarmDTO> sortedAlarmList = null;
		
		if(alarmCate.equals("all")) {
			sortedAlarmList = mapper.getAlarmList((String)params.get("userId"));
		} else if (alarmCate.equals("new")) {
			params.put("alarmCate", "신규");
			sortedAlarmList = mapper.getSortedAlarmList(params);
		} else if (alarmCate.equals("transfer")) {
			params.put("alarmCate", "이체");
			sortedAlarmList = mapper.getSortedAlarmList(params);
		} else if (alarmCate.equals("maturity")) {
			params.put("alarmCate", "상품만기");
			sortedAlarmList = mapper.getSortedAlarmList(params);
		} else if (alarmCate.equals("interest")) {
			params.put("alarmCate", "이자입금");
			sortedAlarmList = mapper.getSortedAlarmList(params);
		} else if (alarmCate.equals("close")) {
			params.put("alarmCate", "중도해지");
			sortedAlarmList = mapper.getSortedAlarmList(params);
		} 
		
		if(sortedAlarmList.size() > 0) {		
			for(AlarmDTO alarm : sortedAlarmList) { 
				String alarmDate = alarm.getAlarmRegDate();
				String alarmDateF = alarmDate.substring(0, 16);
				alarm.setFormattedAlarmRegDate(alarmDateF);
			}			
		}
		Map<String, Object> response = new HashMap<>();
		response.put("alarmList", sortedAlarmList);
		return ResponseEntity.ok(response);
	}
	
	
	// 읽지않은 알림 개수 표시
	@ResponseBody
	@PostMapping("/getNotReadAlarm.ajax")
	public int getNotReadAlarm(@AuthenticationPrincipal User user) {
		String userId = user.getUsername();
		int notReadAlarmCnt = mapper.checkNotReadAlarm(userId);
		return notReadAlarmCnt;
	}
	
	//내계좌 모아보기
	@GetMapping("/myAccount")
	public String myAccount(@AuthenticationPrincipal User user, HttpServletRequest req) {
		String userId = user.getUsername();
		
		// 입출금통장
		List<DepositDTO> depositList = mapper.getAccountList(userId);
		
		// 예금
		List<ProductDTO> fixedList = mapper.getFixedDepositList(userId);
		
		// 적금
		List<ProductDTO> periodList = mapper.getPeriodicalDepositList(userId);
		
		// 해지
		List<DepositDTO> expiryDepositList = mapper.getExpiryDepositList(userId);
		List<ProductDTO> expiryProductList = mapper.getExpiryProductList(userId);
		
		req.setAttribute("depositList", depositList);
		req.setAttribute("fixedList", fixedList);
		req.setAttribute("periodList", periodList);
		req.setAttribute("expiryDepositList", expiryDepositList);
		req.setAttribute("expiryProductList", expiryProductList);
		
		return "my_account";
	}
	
	// 메인 통장 변경
	@ResponseBody
	@PostMapping("/changeMainAccount.ajax")
	public String changeMainAccount(@AuthenticationPrincipal User user, String depositAccount) {
		
		String userId = user.getUsername();
		String msg;
		
		// 입출금통장 개수 확인
		int accountCnt = mapper.getAccountCnt(userId);
		
		if(accountCnt <= 1) {
			msg = "하나 이상의 주거래통장이 있어야 서비스가 가능하여 주거래통장 해제가 불가합니다.";
		} else {
			// 해당 계좌번호가 메인 통장인지 확인
			String mainAccount = mapper.getMainAccount(depositAccount);			
			
			// 주거래 통장 개수 확인
			int mainAccountCnt = mapper.getMainAccountCnt(userId);
			
			if(mainAccountCnt == 1) {
				if (mainAccount.equals("Y")) {
					mapper.updateMainAccountN(depositAccount);
					msg = "주거래통장 해제되었습니다. 다른 계좌를 주거래통장으로 설정해주세요.";
				} else {
					mapper.updateMainAccountY(depositAccount);
					msg = "주거래통장 설정되었습니다.";
				}
			} else {
				if (mainAccount.equals("Y")) {
					mapper.updateMainAccountN(depositAccount);
					msg = "주거래통장 해제되었습니다.";
				} else {
					mapper.updateMainAccountY(depositAccount);
					msg = "주거래통장 설정되었습니다.";
				}
			}
		}		
		return msg;
	}
	
	
	//===============================================================================================
	
	
	// 정기적금 자동이체 스케줄러
	//@Scheduled(cron = "0 0 0 * * *") // 매일 자정(00:00:00)에 작업을 실행
	@Scheduled(cron = "0 20 * * * *") // 테스트용
	public void autoTransferSchedule() {
		autoTransfer();
	}
	
	// 입출금통장 이자 지급 스케줄러
	//@Scheduled(cron = "0 0 0 L * *") // 매월 마지막 날 자정(00:00:00)에 작업을 실행
	@Scheduled(cron = "0 21 * * * *") // 테스트용
	public void monthlyInterestSchedule() {		
		monthlyInterest();
	}
	
	//상품 만기해지 스케줄러
	//@Scheduled(cron = "0 0 0 * * *") // 매일 자정(00:00:00)에 작업을 실행
	@Scheduled(cron = "0 22 * * * *") // 테스트용
	public void productMaturitySchedule() {
		productMaturity();
	}
	
}
