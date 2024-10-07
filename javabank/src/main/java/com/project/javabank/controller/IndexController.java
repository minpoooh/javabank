package com.project.javabank.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
	
	@GetMapping("/index")
	public String index(@AuthenticationPrincipal User user,
						@RequestParam(value="javabank", required=false) String javabank, Model model) {
		// 로그인 정보 꺼내기
		model.addAttribute("loginId", user.getUsername());
		model.addAttribute("loginRoles", user.getAuthorities());
		
		if (javabank != null) {
		    	model.addAttribute("msg", user.getUsername()+"님 환영합니다.");
		} 
		
		
		return "index";
	}
}
