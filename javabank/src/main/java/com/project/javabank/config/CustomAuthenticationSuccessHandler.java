package com.project.javabank.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp,
			Authentication authentication) throws IOException, ServletException {
		String saveId = req.getParameter("saveId");
		String loginId = authentication.getName();
		
		if("on".equals(saveId)) {
			Cookie cookie = new Cookie("saveId", loginId);
			cookie.setMaxAge(7 * 24 * 60 * 60); // 일주일 유지
			cookie.setPath("/");
			resp.addCookie(cookie);
		} else {
			Cookie cookie = new Cookie("saveID", "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			resp.addCookie(cookie);
		}
		
		resp.sendRedirect("/index?javabank"); // 로그인 성공 후 리다이렉트
	}

}
