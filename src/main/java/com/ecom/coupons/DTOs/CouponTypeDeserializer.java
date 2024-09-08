package com.ecom.coupons.DTOs;

import java.io.IOException;

import com.ecom.coupons.entity.CouponType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CouponTypeDeserializer extends JsonDeserializer<CouponType>{
	
	 @Override
	    public CouponType deserialize(JsonParser p, DeserializationContext ctxt) 
	            throws IOException, JsonProcessingException {
	        String value = p.getText().toUpperCase();
	        try {
	            return CouponType.valueOf(value);
	        } catch (IllegalArgumentException e) {
	            throw new JsonProcessingException("Invalid value for CouponType: " + value) {};
	        }
	    }

	

}
