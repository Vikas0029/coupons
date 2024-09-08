package com.ecom.coupons.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecom.coupons.entity.Coupon;

import jakarta.transaction.Transactional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	
	
	@Query("SELECT COALESCE(MAX(c.coupon_id),0) FROM Coupon c")
    int findMaxId();

	 @Query("SELECT c FROM Coupon c WHERE c.coupon_id = :id")
	 Coupon findByCouponId(@Param("id") Integer id);
	 
	 @Modifying
	 @Transactional
	 @Query("UPDATE Coupon c SET c.details = :details WHERE c.coupon_id = :id")
	 int updateCoupon(@Param("id") Integer id, @Param("details") String details);
}