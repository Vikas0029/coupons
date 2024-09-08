package com.ecom.coupons.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.coupons.DTOs.CartItems;
import com.ecom.coupons.DTOs.Cart;
import com.ecom.coupons.DTOs.CouponDTO;
import com.ecom.coupons.entity.Coupon;
import com.ecom.coupons.entity.CouponType;
import com.ecom.coupons.exceptions.InvalidTypeException;
import com.ecom.coupons.exceptions.ResourceNotFoundException;
import com.ecom.coupons.services.CouponService;
import com.ecom.coupons.services.Impl.CouponServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

	@Autowired
	private CouponService couponService;

	@PostMapping
	public ResponseEntity<Object> createCoupon(@RequestBody CouponDTO coupon) {
		try {
			Object createdCoupon = couponService.createCoupon(coupon);
			return ResponseEntity.ok(createdCoupon);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().body("Please Provide Correct Json For Coupon" + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<CouponDTO> getCouponById(@PathVariable Integer id) {
		return new ResponseEntity<>(couponService.getCouponById(id), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<CouponDTO>> getAllCoupons() {
		List<CouponDTO> coupons = couponService.getAllCoupons();
		return ResponseEntity.ok(coupons);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCoupon(@PathVariable Integer id) {
		couponService.deleteCoupon(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateCoupon(@RequestBody CouponDTO coupon,@PathVariable Integer id) {
		try {
			Object createdCoupon = couponService.updateCoupon(coupon);
			return ResponseEntity.ok(createdCoupon);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().body("Please Provide Correct Json For Coupon" + e.getMessage());
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (InvalidTypeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PostMapping("/applicable-coupons")
	public ResponseEntity<Object> applicableCoupons(@RequestBody CartItems applicableCoupon) {
		Object createdCoupon = couponService.applicableCoupons(applicableCoupon);
		return ResponseEntity.ok(createdCoupon);

	}
	
	

	@PostMapping("/apply-coupon/{id}")
	public ResponseEntity<Object> applicableCoupons(@RequestBody CartItems applicableCoupon,@PathVariable Integer id) {
		Object createdCoupon;
		try {
			createdCoupon = couponService.applyCouponById(applicableCoupon,id);
			return ResponseEntity.ok(createdCoupon);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		

	}

}