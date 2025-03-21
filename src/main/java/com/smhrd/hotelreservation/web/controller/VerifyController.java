package com.smhrd.hotelreservation.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siot.IamportRestClient.IamportClient;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import com.smhrd.hotelreservation.web.dto.SessionUser;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VerifyController {

	private final IamportClient iamportClient;
	private final HttpSession httpSession;

	@PostMapping("/verifyIamport/{imp_uid}")
	public ResponseEntity<?> verifyIamport(@PathVariable String imp_uid) {
		try {
			System.out.println("결제 검증 요청, imp_uid: " + imp_uid);
			// 아임포트 API 호출로 결제 정보 확인
			IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(imp_uid);
			if (paymentResponse.getResponse() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 정보 조회 실패");
			}
			Payment payment = paymentResponse.getResponse();
			System.out.println("결제 정보: " + payment);

			// 결제 금액 및 상태 검증
			if (payment.getStatus().equals("paid")) {
				System.out.println("결제 상태: " + payment.getStatus());

				// 소셜 로그인과 일반 로그인 분기 처리
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				String username = null;

				if (authentication instanceof OAuth2AuthenticationToken) {
					// 소셜 로그인 사용자: SessionUser에서 username을 가져옴
					SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
					if (sessionUser != null) {
						username = sessionUser.getUsername();
					}
				} else {
					// 일반 로그인 사용자: authentication.getName()에서 username을 가져옴
					username = authentication.getName();
				}
				if (username == null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자를 찾을 수 없습니다.");
				}

				return ResponseEntity.ok("결제 검증 성공. 결제 상태: " + payment.getStatus());
			} else {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 상태가 올바르지 않습니다.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 검증 중 오류 발생: " + e.getMessage());
		}
	}
}
