package com.smhrd.hotelreservation.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	@CreatedDate
	@Column(updatable = false)
	protected LocalDateTime createdAt;
	
	@CreatedBy
	@Column(updatable = false)
	private String createdBy;
	
	@LastModifiedDate
	private LocalDateTime modifiedAt;

	@LastModifiedBy
	private String modifiedBy;
	
	
}
