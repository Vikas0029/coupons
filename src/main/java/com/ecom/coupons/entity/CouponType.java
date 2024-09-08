package com.ecom.coupons.entity;

import com.ecom.coupons.DTOs.CouponTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CouponTypeDeserializer.class)
public enum CouponType {
	 CART_WISE,
	 PRODUCT_WISE,
	 BXGY
}
