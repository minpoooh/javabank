package com.project.javabank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.javabank.dto.DepositDTO;
import com.project.javabank.mapper.BankMapper;

import ch.qos.logback.core.subst.Token.Type;
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
			
			// 등록된 적금계좌 있는지 확인
		
		
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
	public String createDepositProcess() {
		
		/*
		 	챙겨야할것
		 	1. 계좌번호 랜덤생성
		 	2. 아이디 꺼내오기
		 	3. 1일 이체한도, 비밀번호 입력받기
		 	4. 첫 개설이면 주거래계좌 Y 처리 아니면 N 처리
		 */
		
		
		return "";
	}
}
