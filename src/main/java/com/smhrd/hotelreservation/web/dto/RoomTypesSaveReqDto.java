package com.smhrd.hotelreservation.web.dto;

import org.springframework.web.multipart.MultipartFile;

import com.smhrd.hotelreservation.model.entity.RoomTypes;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomTypesSaveReqDto {
	private Long id;
	private String roomName;
	private Long price;
	private MultipartFile file; 
    private String imageUrl; 
	
    @Builder
    public RoomTypesSaveReqDto(Long id, String roomName, Long price, MultipartFile file, String imageUrl) {
        this.id = id;
        this.roomName = roomName;
        this.price = price;
        this.file = file;
        this.imageUrl = imageUrl;
    }

    // RoomTypes 엔티티로 변환하는 메서드
    public RoomTypes toEntity() {
        return RoomTypes.builder()
            .roomName(this.roomName)
            .price(this.price)
            .imageUrl(this.imageUrl)  // 이미지 URL 저장
            .build();
    }
}
