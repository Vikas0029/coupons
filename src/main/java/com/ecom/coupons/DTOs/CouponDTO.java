package com.ecom.coupons.DTOs;

import java.time.LocalDate;

import com.ecom.coupons.entity.CouponType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {
	
	Integer coupon_id;
	
	CouponType type;
	
	@JsonDeserialize(using = CouponDTODetailsDeserializer.class)
	Object details;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy,M,d")
	private LocalDate expirationDate;
}
