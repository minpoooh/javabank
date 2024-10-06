package com.project.javabank.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class LoginController {
	
	@GetMapping("/")
	public String home() {
		return "login";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(value="error", required=false) String error,	Model model, HttpServletRequest req) {
		CsrfToken csrfToken = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);        
        if (error != null) {
        	model.addAttribute("msg","입력하신 ID와 PW를 다시 확인해주세요.");
        }        
	    return "login"; 
	}
	
	@GetMapping("/join")
	public String join() {
		return "join";
	}
	
	@PostMapping("/joinProcess")
	public String joinProcess(){
		
		return "";
	}
	
}
