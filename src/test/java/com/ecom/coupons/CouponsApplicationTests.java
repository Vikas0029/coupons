package com.ecom.coupons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ecom.coupons.DTOs.CartWiseCouponDTO;
import com.ecom.coupons.DTOs.CouponDTO;
import com.ecom.coupons.entity.Coupon;
import com.ecom.coupons.entity.CouponType;
import com.ecom.coupons.repository.CouponRepository;
import com.ecom.coupons.services.Impl.CouponServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class CouponsApplicationTests {
	
	@Autowired 
	CouponServiceImpl couponServiceImpl;
	
	 @MockBean
	 private CouponRepository couponRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void testIsExpired() {
		Coupon coupon = new Coupon();
		LocalDate pastDate = LocalDate.of(2023, 2,1);
		LocalDate futureDate = LocalDate.of(2025, 1,1);
		assertFalse(couponServiceImpl.isExpired(futureDate));
	}
	
	@Test
	public void testCreateCoupon() {
		Coupon coupon = new Coupon();
		coupon.setExpirationDate(LocalDate.of(2024, 9, 30));
		CartWiseCouponDTO cartWiseCouponDTO =new CartWiseCouponDTO();
		 String json = "{ \"threshold\": 100, \"discount\": 10 }";
	     ObjectMapper objectMapper = new ObjectMapper();
	     try {
			cartWiseCouponDTO= objectMapper.readValue(json, CartWiseCouponDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	     coupon.setDetails(cartWiseCouponDTO.toString());
	     coupon.setType(CouponType.CART_WISE);
	     
	     CouponDTO dto =new CouponDTO();
	     dto.setDetails(cartWiseCouponDTO); 
	     dto.setExpirationDate(LocalDate.of(2024, 9, 30));
	     dto.setType(CouponType.CART_WISE);
	     CouponDTO savedcoupon=null;
	     Mockito.when(couponRepository.save(coupon)).thenReturn(coupon);
	     try {
			 savedcoupon = couponServiceImpl.saveCartWiseCoupon(dto,false);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}	 
	     assertNotNull(savedcoupon);
	     assertEquals(savedcoupon.getType(), dto.getType());
		
	}
}
