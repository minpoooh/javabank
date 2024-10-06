package com.project.javabank.config;

import org.springframework.security.crypto.password.PasswordEncoder;

public class SimplePasswordEncoder implements PasswordEncoder {
	
	// 테스트용 클래스
	
	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();  // 입력된 비밀번호를 암호화해서 리턴
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {		
		return encodedPassword.equals(encode(rawPassword)); // DB에 있는 암호화된 비밀번호와의 일치여부 리턴
	}

}
