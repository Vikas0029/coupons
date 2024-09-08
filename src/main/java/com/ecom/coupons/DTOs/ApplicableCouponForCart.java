package com.ecom.coupons.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicableCouponForCart {
	private Integer coupon_id;
	private String type;
	private Integer discount;
}
