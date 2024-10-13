package com.project.javabank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import jakarta.servlet.DispatcherType;

@Configuration // 설정파일임
@EnableWebSecurity // 설정파일을 필터에 등록
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		//return new SimplePasswordEncoder(); // 테스트용
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http	
			.authorizeHttpRequests(request -> request
					.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE).permitAll()
					.requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
					.requestMatchers("/", "/login", "/logout", "/join", "/joinProcess", "/findId", "/findPw", "/favicon.ico",
									"/checkID.ajax", "/sendEmail.ajax", "/confirmCode.ajax", "/error",
									"/createDeposit", "/sendEmailFindId.ajax", "/confirmCodeFindId.ajax", "/findId",
									"/sendEmailFindPw.ajax", "/confirmCodeFindPw.ajax", "/findPw"
									).permitAll()
					.anyRequest().authenticated()
			)
			.formLogin(form -> form					
						.loginPage("/login") // GET
						.loginProcessingUrl("/login") // POST
						.usernameParameter("userId")
						.passwordParameter("userPw")
						//.defaultSuccessUrl("/index?javabank", true) // 핸들러없을 때 사용
						.successHandler(new CustomAuthenticationSuccessHandler()) // 커스토마이징 핸들러 사용
						.failureUrl("/login?loginError")
						.permitAll()
			)
			.logout(Customizer.withDefaults());
		
		return http.build();
	}
	
	
//	@Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            if ("userid".equals(username)) { // 고정된 사용자 아이디
//                return User.builder()
//                        .username("userid")
//                        .password(passwordEncoder().encode("pw")) // 고정된 패스워드
//                        .roles("USER")
//                        .build();
//            } else {
//                throw new UsernameNotFoundException("User not found");
//            }
//        };
//    }
	
}
