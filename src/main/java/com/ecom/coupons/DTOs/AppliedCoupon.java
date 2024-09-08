package com.ecom.coupons.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppliedCoupon {
	
	private List<DiscountItem> items;
	
	private Integer total_price;
	
	private Integer total_discount;
	
	private Integer final_price;
}
