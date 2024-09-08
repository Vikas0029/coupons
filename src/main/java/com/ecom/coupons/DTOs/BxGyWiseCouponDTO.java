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
public class BxGyWiseCouponDTO {

	
	private List<Product> buy_products;
	
    private List<Product> get_products;
	
	private Integer repition_limit;
	
}
