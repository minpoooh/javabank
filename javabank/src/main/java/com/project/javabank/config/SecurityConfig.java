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

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http	
			.authorizeHttpRequests(request -> request
					.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
					.requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
					.requestMatchers("/", "/login", "/join", "/logout").permitAll()
					.anyRequest().authenticated()
			)
			.formLogin(form -> form					
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.usernameParameter("userid")
						.passwordParameter("pw")
						.defaultSuccessUrl("/index", true)
						.failureUrl("/login?error")
						.permitAll()
			)
			.logout(Customizer.withDefaults());
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if ("userid".equals(username)) { // 고정된 사용자 아이디
                return User.builder()
                        .username("userid")
                        .password(passwordEncoder().encode("pw")) // 고정된 패스워드
                        .roles("USER")
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        };
    }
	
}
