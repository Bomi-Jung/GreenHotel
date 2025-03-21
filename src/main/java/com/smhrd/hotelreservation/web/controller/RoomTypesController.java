package com.smhrd.hotelreservation.web.controller;

import com.smhrd.hotelreservation.service.RoomTypesService;
import com.smhrd.hotelreservation.web.dto.RoomTypesSaveReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/roomtypes")
public class RoomTypesController {

	private final RoomTypesService roomTypesService;

	@Value("${file.filepath}")
	private String uploadDir;

	@PostMapping("/add")
	public ResponseEntity<?> addRoomtype(@RequestParam("roomName") String roomName, @RequestParam("price") Long price,
			@RequestParam("imageFile") MultipartFile file) {
		log.info("view to controller with addRoomtype");

		try {
			// 파일 저장 후 접근 가능한 URL 생성
			String imageUrl = saveFileAndGetUrl(file);

			// DTO에 이미지 URL을 설정
			RoomTypesSaveReqDto requestDto = RoomTypesSaveReqDto.builder().roomName(roomName).price(price).file(file)
					.imageUrl(imageUrl) // 이미지 URL 추가
					.build();

			// 서비스 호출 후 ID 반환
			Long roomId = roomTypesService.addRoomtype(requestDto);

			return ResponseEntity.ok().body(Map.of("success", true, "id", roomId, "imageUrl", imageUrl));
		} catch (Exception e) {
			log.error("Error adding room type", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("success", false, "message", "객실 타입 추가 중 문제가 발생했습니다."));
		}
	}

	private String saveFileAndGetUrl(MultipartFile file) throws IOException {
		// 고유한 파일명 생성
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		String fullPath = uploadDir + fileName;

		// 디렉토리 생성
		File directory = new File("/home/ec2-user/uploadfile/");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		log.info("Saving file to path: {}", fullPath);
		log.info("Directory exists: {}", directory.exists());
		// 파일 저장
		file.transferTo(new File(fullPath));

		// 웹에서 접근할 수 있는 URL 생성
		return "/uploadfile/" + fileName;
	}

	@GetMapping("/uploadfile/{fileName}")
	public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
		Path file = Paths.get(uploadDir).resolve(fileName);
		Resource resource = new FileSystemResource(file);
		return ResponseEntity.ok().body(resource);
	}

	@DeleteMapping("/remove")
	public ResponseEntity<?> removeRoomtype(@RequestBody RoomTypesSaveReqDto requestDto) {
	    log.info("객실 타입 삭제 요청: " + requestDto.getId());

	    try {
	        // 서비스에서 해당 객체 삭제
	        roomTypesService.removeRoomtype(requestDto);

	        return ResponseEntity.ok().body(Map.of("success", true, "message", "객실 타입이 삭제되었습니다."));
	    } catch (Exception e) {
	        log.error("객실 타입 삭제 중 오류 발생", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("success", false, "message", "객실 타입 삭제 중 문제가 발생했습니다."));
	    }
	}
	

	@PutMapping("/update")
	public ResponseEntity<?> updateRoomtype(@RequestParam("id") Long id, @RequestParam("roomName") String roomName,
			@RequestParam("price") Long price,
			@RequestParam(value = "imageFile", required = false) MultipartFile file) {
		log.info("view to controller with updateRoomtype");

		try {
			// 이미지 파일이 존재하면 저장 후 URL을 가져옴
			String imageUrl = null;
			if (file != null && !file.isEmpty()) {
				imageUrl = saveFileAndGetUrl(file); // 새 파일이 있을 경우 이미지 URL 갱신
			}

			// DTO에 필요한 데이터를 설정 (파일이 있을 경우 이미지 URL도 포함)
			RoomTypesSaveReqDto requestDto = RoomTypesSaveReqDto.builder().id(id).roomName(roomName).price(price)
					.imageUrl(imageUrl) // 새로운 이미지 URL을 설정 (파일이 없으면 null)
					.build();

			// 서비스에서 update 처리
			Long roomId = roomTypesService.updateRoomtype(requestDto);

			return ResponseEntity.ok().body(Map.of("success", true, "id", roomId, "imageUrl", imageUrl));
		} catch (Exception e) {
			log.error("Error updating room type", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("success", false, "message", "객실 타입 수정 중 문제가 발생했습니다."));
		}
	}
}