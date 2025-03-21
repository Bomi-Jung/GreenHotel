package com.smhrd.hotelreservation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smhrd.hotelreservation.model.entity.ReservationDetails;
import com.smhrd.hotelreservation.model.entity.Reservations;
import com.smhrd.hotelreservation.model.entity.Rooms;
import com.smhrd.hotelreservation.model.entity.Users;
import com.smhrd.hotelreservation.model.repository.ReservationDetailsJpaRepository;
import com.smhrd.hotelreservation.model.repository.ReservationsJpaRepository;
import com.smhrd.hotelreservation.model.repository.RoomsJpaRepository;
import com.smhrd.hotelreservation.model.repository.UsersJpaRepository;
import com.smhrd.hotelreservation.web.dto.ReservationDeleteReqDto;
import com.smhrd.hotelreservation.web.dto.ReservationDetailListResDto;
import com.smhrd.hotelreservation.web.dto.ReservationDetailSaveReqDto;
import com.smhrd.hotelreservation.web.dto.ReservationListResDto;
import com.smhrd.hotelreservation.web.dto.ReservationSaveReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReservationsService {
	private static final Logger log = LoggerFactory.getLogger(ReservationsService.class);
	public final RoomsService roomsService;
	public final RoomTypesService rooomTypesService;
	public final ReservationsJpaRepository reservationsJpaRepository;
	public final ReservationDetailsJpaRepository reservationDetailsJpaRepository;
	public final RoomsJpaRepository roomsJpaRepository;
	public final UsersJpaRepository usersJpaRepository;

	/**
	 * method 예약 후 정보 등록
	 */
	// 결제 요청 시 impUid 저장
	@Transactional
	public void saveImpUidForReservation(String username, String impUid, BigDecimal paymentAmount,
			String paymentStatus) {
		// 사용자 조회
		Users user = usersJpaRepository.findByUsername(username);

		if (user != null) {
			// 가장 최근 예약 조회
			Reservations latestReservation = reservationsJpaRepository.findLatestByUsers(user);

			if (latestReservation != null) {
				// 가장 최근 예약에 impUid 추가
				latestReservation.setImpUid(impUid);
				latestReservation.setPaymentAmount(paymentAmount);
				latestReservation.setPaymentStatus(paymentStatus);

				// 기존 예약 업데이트
				reservationsJpaRepository.save(latestReservation);
				reservationsJpaRepository.flush();
			} else {
				throw new RuntimeException("기존 예약을 찾을 수 없습니다.");
			}
		} else {
			throw new RuntimeException("사용자를 찾을 수 없습니다.");
		}
	}

	@Transactional
	public Long saveReservation(ReservationSaveReqDto requestDto, String username) {

		Users users = usersJpaRepository.findByUsername(username);
		if (users == null) {
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username);
		}

		// 빈 객실 찾기
		List<ReservationDetails> usedReservationDetails = reservationDetailsJpaRepository
				.findAllByDateBetween(requestDto.getCheckInDate(), requestDto.getCheckOutDate());

		List<Rooms> allRooms = roomsJpaRepository.findAll();

		// 예약된 날짜에 사용되지 않은 빈 객실을 찾는 필터링
		List<Rooms> emptyRooms = allRooms.stream().filter(
				room -> usedReservationDetails.stream().noneMatch(elem -> elem.getRooms().getId().equals(room.getId())))
				.filter(room -> room.getRoomTypes().getId().equals(requestDto.getRoomtype_id())) // 원하는 방 타입으로 필터링
				.collect(Collectors.toList());

		// 빈방이 없는 경우 -1L 반환
		if (emptyRooms.size() <= 0)
			return -1L;

		// 랜덤한 빈방 배정
		Rooms emtRoom = emptyRooms.get(new Random().nextInt(emptyRooms.size()));
		List<ReservationDetails> reservationList = new ArrayList<>();

		int days = (int) ChronoUnit.DAYS.between(requestDto.getCheckInDate(), requestDto.getCheckOutDate()) + 1;
		for (int i = 0; i < days; i++) {
			reservationList.add(
					ReservationDetails.builder().rooms(emtRoom).date(requestDto.getCheckInDate().plusDays(i)).build());
		}

		return reservationsJpaRepository
				.save(Reservations.builder().users(users).reservationDetails(reservationList).build()).getId();

	}

	/**
	 * method 전체 예약 정보 조회
	 */
	@Transactional
	public List<ReservationListResDto> findAll() {
		List<ReservationListResDto> rl = reservationsJpaRepository.findAll().stream().map(rv -> {
			Users user = rv.getUsers(); // 예약 정보에 연결된 사용자 정보를 가져옵니다.
			List<ReservationDetails> details = reservationDetailsJpaRepository.findAllByReservationsId(rv.getId());

			String imageUrl = details.isEmpty() ? null : details.get(0).getRooms().getRoomTypes().getImageUrl();

			return ReservationListResDto.builder().reservation(rv).user(user).l(details).imageUrl(imageUrl).build();
		}).collect(Collectors.toList());
		return rl;
	}

	/**
	 * method 회원별 예약 정보 조회
	 */
	@Transactional
	public List<ReservationListResDto> findAllByUser(String username) {
		log.info("Finding reservations for username: {}", username);

		Users users = usersJpaRepository.findByUsername(username);
		if (users == null) {
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username);
		}

		List<ReservationListResDto> rl = reservationsJpaRepository.findByUsers(users).stream().map(rv -> {
			List<ReservationDetails> details = reservationDetailsJpaRepository.findAllByReservationsId(rv.getId());

			// 예약 리스트에서 체크인 날짜와 체크아웃 날짜를 가져옵니다.
			LocalDate checkInDate = rv.getReservationDetails().get(0).getDate();
			LocalDate checkOutDate = rv.getReservationDetails().size() == 1
					? rv.getReservationDetails().get(0).getDate()
					: rv.getReservationDetails().get(rv.getReservationDetails().size() - 1).getDate();

			// 체크인 날짜와 체크아웃 날짜를 합쳐서 문자열로 만들기
			String dateRange = checkInDate.toString() + " ~ " + checkOutDate.toString();

			// 예약 내역 DTO를 생성하여 반환합니다.
			return ReservationListResDto.builder().reservation(rv).user(users).dateRange(dateRange) // 새로 만든 dateRange를
																									// 추가
					.l(reservationDetailsJpaRepository.findAllByReservationsId(rv.getId()).stream()
							.collect(Collectors.toList()))
					.build();
		}).collect(Collectors.toList());

		log.info("Found {} reservations for username: {}", rl.size(), username);
		return rl;
	}

	public Reservations findByReservationIdAndUsername(Long reservationId, String username) {
		return reservationsJpaRepository.findByReservationIdAndUsername(reservationId, username)
				.orElseThrow(() -> new RuntimeException("No reservation found for this id and username"));
	}

	/**
	 * method 단일 예약 정보 조회
	 */
	@Transactional
	public List<ReservationDetailListResDto> findOne(Long id) {
		return reservationsJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 예약 정보가 없습니다."))
				.getReservationDetails().stream().map(ReservationDetailListResDto::new).collect(Collectors.toList());
	}

	/**
	 * method 단일 예약 정보 삭제
	 */
	@Transactional
	public Long deleteReservation(ReservationDeleteReqDto requestDto) {
		Reservations reservation = reservationsJpaRepository.findById(requestDto.getId())
				.orElseThrow(() -> new IllegalArgumentException("해당 예약 정보가 없습니다"));
		reservationsJpaRepository.delete(reservation);
		return reservation.getId();
	}

	/**
	 * method 단일 예약상세 정보 삭제
	 */
	@Transactional
	public Long deleteReservationDetails(ReservationDetailSaveReqDto requestDto) {
		ReservationDetails detail = ReservationDetails.builder().id(requestDto.getId()).build();
		reservationDetailsJpaRepository.delete(detail);
		return detail.getId();
	}

}
