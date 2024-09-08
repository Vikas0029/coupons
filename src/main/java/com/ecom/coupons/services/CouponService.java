package com.ecom.coupons.services;

import java.util.List;
import java.util.Optional;

import com.ecom.coupons.DTOs.CartItems;
import com.ecom.coupons.DTOs.ApplicableCoupon;
import com.ecom.coupons.DTOs.Cart;
import com.ecom.coupons.DTOs.CouponDTO;
import com.ecom.coupons.entity.Coupon;
import com.fasterxml.jackson.core.JsonProcessingException;


public interface CouponService {

	 Object createCoupon(CouponDTO coupon)throws JsonProcessingException ;
	 
	 CouponDTO getCouponById(Integer id);
	 
	 List<CouponDTO> getAllCoupons();
	 
	 void deleteCoupon(Integer id);
	 
	 Object updateCoupon(CouponDTO coupon) throws JsonProcessingException;
	 
	 ApplicableCoupon applicableCoupons(CartItems applicableCoupon);
	 
	 Object applyCouponById(CartItems applicableCoupon,Integer id) throws Exception;
	 
	 
}
