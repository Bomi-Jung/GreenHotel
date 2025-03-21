package com.smhrd.hotelreservation.web.controller;


import com.smhrd.hotelreservation.model.entity.AuthProvider;
import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.model.repository.UsersJpaRepository;
import com.smhrd.hotelreservation.service.RoomTypesService;
import com.smhrd.hotelreservation.service.RoomsService;
import com.smhrd.hotelreservation.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

	private final UsersService usersService;
	private final RoomsService roomsService;
	private final RoomTypesService roomTypesService;
	private final UsersJpaRepository usersJpaRepository;
	

	@GetMapping("/login")
	public String login() {
		return "login";
	}


	@GetMapping("/")
	public String main(ModelMap model) {
	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
	        String currentUsername = authentication.getName();
	        System.out.println("현재 SecurityContext 사용자: " + currentUsername);
	        
	        
	        Users user = usersJpaRepository.findByUsername(currentUsername);
	        String nickname = user != null ? user.getNickname() : null;
	        model.addAttribute("nickname", nickname);
	        
	        model.addAttribute("username", currentUsername);
	    }

	    return "main";
	}
	
	
	// 회원가입페이지
	@GetMapping("/register")
	public String register(ModelMap model) {
		return "register";
	}

	// 회원등록 (일반사용자)
	@PostMapping("/register")
	public String registerUser(@RequestParam("id") String id, @RequestParam("password") String password,
			@RequestParam("name") String name, RedirectAttributes redirectAttributes) {
		
		// 특수문자 및 경로 조작 문자 차단
	    String regex = "[^a-zA-Z0-9가-힣]";  // 영어, 숫자, 한글만 허용
	    
	    if (id.matches(".*" + regex + ".*") || password.matches(".*" + regex + ".*") || name.matches(".*" + regex + ".*")) {
	        redirectAttributes.addFlashAttribute("msg", "특수문자는 포함이 불가합니다.");
	        return "redirect:/register";
	    }


		boolean isSuccess = usersService.registerUser(id, name, password, AuthProvider.LOCAL);

		if (isSuccess) {
			return "redirect:/"; // 회원가입이 성공하면 메인 페이지로 리다이렉트
		} else {
			redirectAttributes.addFlashAttribute("msg", "아이디가 중복되었습니다");
			return "redirect:/register";
		}
	}

	// 인덱스 페이지
	@GetMapping("/index") // 보미_인덱스 html 페이지로 연결
	public String index(ModelMap model) {
	
		return "index";
	}

	// 사용자 페이지
	@GetMapping("/home")
	public String home(ModelMap model) {
	 
	    return "home";
	}
	// 관리자 페이지
	@GetMapping("/admin")
	public String admin(ModelMap model) {
		usersService.save();
		return "admin";
	}

	// 객실 타입 관리
	@GetMapping("/admin/roomtype")
	public String roomtype(Model model) {
		log.info("객실타입관리 이동...");
		model.addAttribute("roomTypes", roomTypesService.findAll());
		return "roomtype";
	}

	// room(객실 관리)
	@GetMapping("/admin/room")
	public String room(Model model) {
		log.info("객실관리 이동...");
		model.addAttribute("roomTypes", roomTypesService.findAll());
		model.addAttribute("rooms", roomsService.findAllRooms());
		return "room";
	}

	

}
