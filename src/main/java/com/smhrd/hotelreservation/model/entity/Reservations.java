package com.smhrd.hotelreservation.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Reservations extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users users;

	// reservationDetails
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "reservation_id")
	private List<ReservationDetails> reservationDetails;
	
	 @Column(name = "imp_uid")
	 private String impUid;

	private String paymentStatus;
	
	private BigDecimal paymentAmount;
	
	private Long totalPrice;
	
	
	 // Getters and Setters
	
    public String getImpUid() {
        return impUid;
    }

    public void setImpUid(String impUid) {
        this.impUid = impUid;
    }

    public Users getUser() {
        return users;
    }

    public void setUser(Users user) {
        this.users = user;
    }
    



    @Builder
    public Reservations(Users users, List<ReservationDetails> reservationDetails, String impUid, BigDecimal paymentAmount, String paymentStatus) {
        this.users = users;
        this.reservationDetails = reservationDetails;
        this.impUid = impUid;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        calculateTotalPrice();
    }


	// calculateTotalPrice 메소드 수정
	private void calculateTotalPrice() {
		LocalDate checkInDate = reservationDetails.get(0).getDate();
		LocalDate checkOutDate = reservationDetails.size() == 1 ? reservationDetails.get(0).getDate()
				: reservationDetails.get(reservationDetails.size() - 1).getDate();
		long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
		long price = reservationDetails.get(0).getRooms().getRoomTypes().getPrice();
		this.totalPrice = price * numberOfNights;
	}

}
