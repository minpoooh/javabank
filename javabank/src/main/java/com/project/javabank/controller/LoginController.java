package com.project.javabank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.javabank.dto.UserDTO;
import com.project.javabank.mapper.JoinMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {
	
	@Autowired
	JoinMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	JavaMailSender mailSender;
	
	@GetMapping("/")
	public String home() {
		return "login";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(value="loginError", required=false) String loginError,
						@RequestParam(value="insertError", required=false) String insertError,
						@RequestParam(value="joinError", required=false) String joinError,
						@RequestParam(value="joinOK", required=false) String joinOK,
						@RequestParam(value="logout", required=false) String logout,
						Model model, HttpServletRequest req) {
		
		// CSRF 토큰 꺼내기
		CsrfToken csrfToken = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
        
		// CSRF 토큰 model 객체에 담아 뷰로 전달하기
		model.addAttribute("_csrf", csrfToken);        
        
        if (loginError != null) {
        	model.addAttribute("msg","입력하신 ID와 PW를 다시 확인해주세요.");
        }
        
        if (insertError != null) {
        	model.addAttribute("msg","회원가입 도중 에러가 발생했습니다. 관리자에게 문의해주세요.(에러코드:AA01)");
        }        
        
        if (joinError != null) {
        	model.addAttribute("msg","회원가입 도중 에러가 발생했습니다. 관리자에게 문의해주세요.(에러코드:AB01)");
        }    
        
        if (joinOK != null) {
        	model.addAttribute("msg","회원가입이 정상적으로 완료되었습니다. 가입하신 아이디와 비밀번호로 로그인해주세요.");
        }   
        
        if (logout != null) {
        	model.addAttribute("msg","로그아웃 되었습니다. 이용해주셔서 감사합니다.");
        }  
	    return "login"; 
	}
	
	@GetMapping("/join")
	public String join(Model model, HttpServletRequest req) {
		return "join";
	}
	
	@PostMapping("/join")
	public String joinProcess(@RequestParam Map<String, String> reqParams){
		
		// 비밀번호 암호화
		String userPw = reqParams.get("userPw");		
		String encodedUserPw = passwordEncoder.encode(userPw);
		
		// 이메일 합치기
		String userEmail = reqParams.get("userEmail1") + reqParams.get("userEmail2");
		
		Map<String, Object> params = new HashMap<>();
		params.put("userName", reqParams.get("userName"));
		params.put("userBirth", reqParams.get("userBirth"));
		params.put("userEmail", userEmail);
		params.put("userTel", reqParams.get("userTel"));
		params.put("userId", reqParams.get("userId"));
		params.put("userPw", encodedUserPw);
		
		try {
			int joinResult = mapper.joinUser(params);
			if(joinResult > 0) {
				System.out.println("회원가입 INSERT 성공");
				return "redirect:/login?joinOK";
			} else {
				System.out.println("회원가입 INSERT 실패");
				return "redirect:/login?insertError";
			}
		}catch(Exception e) {
			System.out.println("회원가입 에러 발생");
			e.printStackTrace();
			return "redirect:/login?joinError";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest req, HttpServletResponse resp) {
		new SecurityContextLogoutHandler().logout(req, resp, SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/login?logout";
	}
	
	@ResponseBody
	@PostMapping("/checkID.ajax")
	public String checkID(String userId) {
		try {
			int checkIDres = mapper.checkID(userId);
			if(checkIDres == 0) {
				return "OK";
			}else {
				return "ERROR";
			}
		} catch(Exception e) {
			System.out.println("아이디 중복확인 에러");
			e.printStackTrace();
			return "ERROR";
		}		
		
	}
	
	@ResponseBody
	@PostMapping("/sendEmail.ajax")
	public String sendEmail(HttpServletResponse resp, String mail1, String mail2) {
		try {
			String email = mail1 + mail2;
			
			// 인증코드
			Random random = new Random();
			String certiCode = String.valueOf(random.nextInt(900000) + 100000);
			
			// 쿠키
			Cookie cookie = new Cookie("certiCode", certiCode);
			cookie.setMaxAge(3*60);
			cookie.setPath("/");
			resp.addCookie(cookie);
			
			// 이메일 전송 로직
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom("admin@javabank.com");
			helper.setTo(email);
	        helper.setSubject("JavaBank 회원가입 인증번호입니다.");
	        helper.setText("안녕하세요!! JavaBank 입니다.\n\n 이메일 인증번호 : " + certiCode
	                + " \n\n 회원가입을 진행 하시려면 인증번호를 해당 칸에 입력해주세요.\n 이용해주셔서 감사합니다." + "\n\n --JavaBank--");
	        mailSender.send(msg);	              
			
			return "OK";
		}catch(Exception e){
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	@ResponseBody
	@PostMapping("/confirmCode.ajax")
	public String confirmCode(HttpServletRequest req, String inputCode) {
		Cookie [] cookies = req.getCookies();
		 if(cookies != null) {
			 for(Cookie cookie : cookies) {
				 if(cookie.getName().contentEquals("certiCode")) {
					 if(cookie.getValue().equals(inputCode)) {
						 return "OK";
					 }
				 }
			 }
		 }
		 return "ERROR";
	}
	
	@ResponseBody
	@PostMapping("/sendEmailFindId.ajax")
	public String sendEmailFindId(HttpServletResponse resp, String mail1, String mail2) {
		try {
			String email = mail1 + mail2;
			
			// 인증코드
			Random random = new Random();
			String certiCode = String.valueOf(random.nextInt(900000) + 100000);
			
			// 쿠키
			Cookie cookie = new Cookie("findIdCode", certiCode);
			cookie.setMaxAge(3*60);
			cookie.setPath("/");
			resp.addCookie(cookie);
			
			// 이메일 전송 로직
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom("admin@javabank.com");
			helper.setTo(email);
	        helper.setSubject("JavaBank 아이디 찾기 인증번호입니다.");
	        helper.setText("안녕하세요!! JavaBank 입니다.\n\n 이메일 인증번호 : " + certiCode
	                + " \n\n 아이디 찾기를 진행 하시려면 인증번호를 해당 칸에 입력해주세요.\n 이용해주셔서 감사합니다." + "\n\n --JavaBank--");
	        mailSender.send(msg);	              
			
			return "OK";
		}catch(Exception e){
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	
	@ResponseBody
	@PostMapping("/confirmCodeFindId.ajax")
	public String confirmCodeFindId(HttpServletRequest req, String inputCode) {
		Cookie [] cookies = req.getCookies();
		 if(cookies != null) {
			 for(Cookie cookie : cookies) {
				 if(cookie.getName().contentEquals("findIdCode")) {
					 if(cookie.getValue().equals(inputCode)) {
						 return "OK";
					 }
				 }
			 }
		 }
		 return "ERROR";
	}
	
	@PostMapping("/findId")
	public String findId(HttpServletRequest req, String userEmail1, String userEmail2) throws Exception {
		String userEmail = userEmail1 + userEmail2;
		// 해당 메일 주소로 가입된 유저가 있는지 확인
		List<UserDTO> user = mapper.getMailUser(userEmail);
		
		if(user == null || user.isEmpty()) {
			req.setAttribute("msg", "해당 이메일로 가입된 ID가 없습니다.");
		} else {
			// 이메일 전송 로직
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom("admin@javabank.com");
			helper.setTo(userEmail);
	        helper.setSubject("JavaBank 아이디 찾기 결과입니다.");
	        
	        // 사용자 ID를 수집하여 StringBuilder로 구성
	        StringBuilder userIds = new StringBuilder();
	        for (int i = 0; i < user.size(); i++) {
	            userIds.append(user.get(i).getUserId());
	            if (i < user.size() - 1) {
	                userIds.append(", ");
	            }
	        }
	        helper.setText("안녕하세요!! JavaBank 입니다.\n\n ID는 "+ userIds.toString() + "입니다. \n 이용해주셔서 감사합니다." + "\n\n --JavaBank--");
	        mailSender.send(msg);	
			req.setAttribute("msg", "인증 받은 이메일로 아이디를 발송했습니다. 로그인 후 이용해주세요.");
		}
		return "login";
	}
	
	
	@ResponseBody
	@PostMapping("/sendEmailFindPw.ajax")
	public String sendEmailFindPw(HttpServletResponse resp, String id, String mail1, String mail2) {
		try {
			String email = mail1 + mail2;
			Map<String, String> params = new HashMap<>();
			params.put("userEmail", email);
			params.put("userId", id);
			
			// 아이디와 이메일주소를 가진 유저가 있는지 확인
			List<UserDTO> user = mapper.getIdMailUser(params);
			
			if(user == null || user.isEmpty()) {
				return "NOTFOUND";
			} else {
				// 인증코드
				Random random = new Random();
				String certiCode = String.valueOf(random.nextInt(900000) + 100000);
				
				// 쿠키
				Cookie cookie = new Cookie("findPwCode", certiCode);
				cookie.setMaxAge(3*60);
				cookie.setPath("/");
				resp.addCookie(cookie);
				
				// 이메일 전송 로직
				MimeMessage msg = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(msg, true);
				helper.setFrom("admin@javabank.com");
				helper.setTo(email);
		        helper.setSubject("JavaBank 비밀번호 찾기 인증번호입니다.");
		        helper.setText("안녕하세요!! JavaBank 입니다.\n\n 이메일 인증번호 : " + certiCode
		                + " \n\n 비밀번호 찾기를 진행 하시려면 인증번호를 해당 칸에 입력해주세요.\n 이용해주셔서 감사합니다." + "\n\n --JavaBank--");
		        mailSender.send(msg);	              
				
				return "OK";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	
	@ResponseBody
	@PostMapping("/confirmCodeFindPw.ajax")
	public String confirmCodeFindPw(HttpServletRequest req, String inputCode) {
		Cookie [] cookies = req.getCookies();
		 if(cookies != null) {
			 for(Cookie cookie : cookies) {
				 if(cookie.getName().contentEquals("findPwCode")) {
					 if(cookie.getValue().equals(inputCode)) {
						 return "OK";
					 }
				 }
			 }
		 }
		 return "ERROR";
	}
	
	@PostMapping("/findPw")
	public String findPw(HttpServletRequest req, String findPwUserId, String userEmail1, String userEmail2) throws Exception {
		String userEmail = userEmail1 + userEmail2;
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", findPwUserId);
		params.put("userEmail", userEmail);
		
		// 임시 비밀번호 생성
		Random random = new Random();
		String imsiPw = String.valueOf(random.nextInt(900000) + 100000);
		String encodeImsiPw = passwordEncoder.encode(imsiPw);
		params.put("imsiPw", encodeImsiPw);
		
		// 임시 비밀번호로 패스워드 변경
		int changedPwResult = mapper.changedPwResult(params);
		if(changedPwResult > 0) {
			//System.out.println("임시비밀번호로 DB변경 성공");
		} else {
			System.out.println("임시비밀번호로 DB변경 실패");
		}
		
		// 이메일 전송 로직
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setFrom("admin@javabank.com");
		helper.setTo(userEmail);
        helper.setSubject("JavaBank 비밀번호 찾기 결과입니다.");

        helper.setText("안녕하세요!! JavaBank 입니다.\n\n 임시비밀번호는 "+ imsiPw + "입니다. \n 이용해주셔서 감사합니다." + "\n\n --JavaBank--");
        mailSender.send(msg);	
		req.setAttribute("msg", "인증 받은 이메일로 임시비밀번호를 발송했습니다. 로그인 후 이용해주세요.");
		
		return "login";
	}
}
