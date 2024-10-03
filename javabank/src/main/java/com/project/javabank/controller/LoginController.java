package com.project.javabank.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class LoginController {
	
	@GetMapping("/")
	public String home() {
		return "login";
	}
	
	@GetMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);
	    return "login"; 
	}
	
	@GetMapping("/join")
	public String join() {
		return "join";
	}
	
}
