package com.project.javabank.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.javabank.dto.DepositDTO;
import com.project.javabank.dto.ProductDTO;
import com.project.javabank.mapper.BankMapper;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BankController {
	
	@Autowired
	BankMapper mapper;
	
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
	public String createDepositProcess(@AuthenticationPrincipal User user, String depositPw, String transactionLimit) {
		
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
				System.out.println("확정된 계좌번호:"+ depositNum);
				params.put("depositAccount", depositNum);
				break;
			}
		}
		
		// 3. 통장 비밀번호, 이체한도 확인
		System.out.println("통장 비밀번호: " + depositPw);
		System.out.println("이체한도: " + 	transactionLimit);
		
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
		} catch(Exception e) {
			System.out.println("Deposit, Dtransaction 테이블 INSERT 에러");
			e.printStackTrace();
		}
		
				
		return "redirect:/index";
	}
}
