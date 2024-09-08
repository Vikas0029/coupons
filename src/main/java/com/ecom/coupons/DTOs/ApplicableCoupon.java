package com.ecom.coupons.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicableCoupon {
	
	private List<ApplicableCouponForCart> applicable_coupons;

}
