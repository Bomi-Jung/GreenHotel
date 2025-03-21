package com.smhrd.hotelreservation.model.entity;

import java.util.List;

import com.smhrd.hotelreservation.web.dto.ReservationDetailListResDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersInfo {
    private Users users;
    private List<ReservationDetailListResDto> reservations;
    private Long totalPrice;

    public UsersInfo(Users users, List<ReservationDetailListResDto> reservations, Long totalPrice) {
        this.users = users;
        this.reservations = reservations;
        this.totalPrice = totalPrice; 
    }

   
    public Users getUser() {
        return users;
    }

    public List<ReservationDetailListResDto> getReservations() {
        return reservations;
    }

  
    public void setUser(Users users) {
        this.users = users;
    }

    public void setReservations(List<ReservationDetailListResDto> reservations) {
        this.reservations = reservations;
    }
}