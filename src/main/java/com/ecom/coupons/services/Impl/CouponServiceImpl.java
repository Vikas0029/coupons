package com.ecom.coupons.services.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.coupons.DTOs.CartItems;
import com.ecom.coupons.DTOs.ApplicableCoupon;
import com.ecom.coupons.DTOs.ApplicableCouponForCart;
import com.ecom.coupons.DTOs.AppliedCoupon;
import com.ecom.coupons.DTOs.BxGyWiseCouponDTO;
import com.ecom.coupons.DTOs.Cart;
import com.ecom.coupons.DTOs.CartWiseCouponDTO;
import com.ecom.coupons.DTOs.CouponDTO;
import com.ecom.coupons.DTOs.DiscountItem;
import com.ecom.coupons.DTOs.Items;
import com.ecom.coupons.DTOs.ProductWiseCouponDTO;
import com.ecom.coupons.entity.Coupon;
import com.ecom.coupons.entity.CouponType;
import com.ecom.coupons.exceptions.InvalidTypeException;
import com.ecom.coupons.exceptions.ResourceNotFoundException;
import com.ecom.coupons.repository.CouponRepository;
import com.ecom.coupons.services.CouponService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Override
	public Object createCoupon(CouponDTO coupon) throws JsonProcessingException {

		if (coupon.getType().toString().equalsIgnoreCase(CouponType.CART_WISE.toString())) {
			return this.saveCartWiseCoupon(coupon, false);
		}

		else if (coupon.getType().toString().equalsIgnoreCase(CouponType.PRODUCT_WISE.toString())) {
			return this.saveProductWiseCoupon(coupon, false);
		}

		else {
			return this.bxGyWiseCoupon(coupon, false);
		}

	}

	@Transactional
	public Object saveProductWiseCoupon(CouponDTO coupon, boolean isUpdate)
			throws JsonMappingException, JsonProcessingException {
		Coupon temp_coupon = new Coupon();
		ObjectMapper mapper = new ObjectMapper();
		Object obj = coupon.getDetails();
		String jsonString = mapper.writeValueAsString(obj);
		mapper.readValue(jsonString, ProductWiseCouponDTO.class);
		temp_coupon.setType(coupon.getType());
		temp_coupon.setExpirationDate(coupon.getExpirationDate());
		temp_coupon.setDetails(jsonString);
		if (!isUpdate) {
			temp_coupon.setCoupon_id(couponRepository.findMaxId() + 1);
			coupon.setCoupon_id(temp_coupon.getCoupon_id());
			couponRepository.save(temp_coupon);
		} else {
			couponRepository.updateCoupon(coupon.getCoupon_id(), temp_coupon.getDetails());
		}

		return coupon;

	}

	public Object bxGyWiseCoupon(CouponDTO coupon, boolean isUpdate)
			throws JsonMappingException, JsonProcessingException {
		Coupon temp_coupon = new Coupon();
		ObjectMapper mapper = new ObjectMapper();
		Object obj = coupon.getDetails();
		String jsonString = mapper.writeValueAsString(obj);
		mapper.readValue(jsonString, BxGyWiseCouponDTO.class);
		temp_coupon.setType(coupon.getType());
		temp_coupon.setDetails(jsonString);
		temp_coupon.setExpirationDate(coupon.getExpirationDate());
		if (!isUpdate) {
			temp_coupon.setCoupon_id(couponRepository.findMaxId() + 1);
			coupon.setCoupon_id(temp_coupon.getCoupon_id());
			couponRepository.save(temp_coupon);
		} else {
			couponRepository.updateCoupon(coupon.getCoupon_id(), temp_coupon.getDetails());
		}

		return coupon;

	}

	@Transactional
	public CouponDTO saveCartWiseCoupon(CouponDTO coupon, boolean isUpdate) throws JsonProcessingException {
		Coupon temp_coupon = new Coupon();
		ObjectMapper mapper = new ObjectMapper();
		Object obj = coupon.getDetails();
		String jsonString = mapper.writeValueAsString(obj);
		mapper.readValue(jsonString, CartWiseCouponDTO.class);
		temp_coupon.setType(coupon.getType());
		temp_coupon.setDetails(jsonString);
		temp_coupon.setExpirationDate(coupon.getExpirationDate());
		if (!isUpdate) {
			temp_coupon.setCoupon_id(couponRepository.findMaxId() + 1);
			coupon.setCoupon_id(temp_coupon.getCoupon_id());
			couponRepository.save(temp_coupon);
		} else {
			couponRepository.updateCoupon(coupon.getCoupon_id(), temp_coupon.getDetails());
		}

		return coupon;
	}

	@Override
	public CouponDTO getCouponById(Integer id) {
		return daotoDto(couponRepository.findByCouponId(id));
	}

	@Override
	public List<CouponDTO> getAllCoupons() {
		List<Coupon> coupons = couponRepository.findAll();
		List<CouponDTO> couponDTOs = coupons.stream().map(coupon -> daotoDto(coupon)).collect(Collectors.toList());

		return couponDTOs;
	}

	private CouponDTO daotoDto(Coupon coupon) {
		CouponDTO couponDTO = new CouponDTO();
		ObjectMapper mapper = new ObjectMapper();
		if (coupon.getType().toString().equalsIgnoreCase(CouponType.CART_WISE.toString())) {
			couponDTO.setCoupon_id(coupon.getCoupon_id());
			couponDTO.setType(coupon.getType());
			couponDTO.setExpirationDate(coupon.getExpirationDate());
			CartWiseCouponDTO cartWiseCouponDTO = null;
			try {
				cartWiseCouponDTO = mapper.readValue(coupon.getDetails(), CartWiseCouponDTO.class);
			} catch (JsonProcessingException e) {

				e.printStackTrace();
			}
			couponDTO.setDetails(cartWiseCouponDTO);
		}

		if (coupon.getType().toString().equalsIgnoreCase(CouponType.PRODUCT_WISE.toString())) {
			couponDTO.setCoupon_id(coupon.getCoupon_id());
			couponDTO.setType(coupon.getType());
			ProductWiseCouponDTO productWiseCouponDTO = null;
			couponDTO.setExpirationDate(coupon.getExpirationDate());
			try {
				productWiseCouponDTO = mapper.readValue(coupon.getDetails(), ProductWiseCouponDTO.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			couponDTO.setDetails(productWiseCouponDTO);
		}

		if (coupon.getType().toString().equalsIgnoreCase(CouponType.BXGY.toString())) {
			couponDTO.setCoupon_id(coupon.getCoupon_id());
			couponDTO.setType(coupon.getType());
			BxGyWiseCouponDTO bxGyWiseCouponDTO = null;
			couponDTO.setExpirationDate(coupon.getExpirationDate());
			try {
				bxGyWiseCouponDTO = mapper.readValue(coupon.getDetails(), BxGyWiseCouponDTO.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			couponDTO.setDetails(bxGyWiseCouponDTO);
		}

		return couponDTO;
	}

	@Override
	public void deleteCoupon(Integer id) {
		couponRepository.deleteById(id);
	}

	@Override
	public Object updateCoupon(CouponDTO coupon) throws JsonProcessingException {
		Coupon temp_coupon = couponRepository.findByCouponId(coupon.getCoupon_id());

		if (temp_coupon == null) {
			throw new ResourceNotFoundException("Coupon is not present with this Id");
		} else {
			if (temp_coupon.getType().equals(coupon.getType())) {
				if (coupon.getType().toString().equalsIgnoreCase(CouponType.CART_WISE.toString())) {
					return this.saveCartWiseCoupon(coupon, true);
				}

				else if (coupon.getType().toString().equalsIgnoreCase(CouponType.PRODUCT_WISE.toString())) {
					return this.saveProductWiseCoupon(coupon, true);
				} else {
					return this.bxGyWiseCoupon(coupon, true);
				}
			} else {
				throw new InvalidTypeException("Type MissMatch of token for this Id");
			}

		}
	}

	@Override
	public ApplicableCoupon applicableCoupons(CartItems applicableCoupon) {
		List<CouponDTO> couponDto = this.getAllCoupons();
		ApplicableCoupon temp_applicableCoupon = new ApplicableCoupon();
		List<ApplicableCouponForCart> listApplicableCouponForCart = new ArrayList<>();
		temp_applicableCoupon.setApplicable_coupons(listApplicableCouponForCart);

		for (int i = 0; i < couponDto.size(); i++) {

			if (couponDto.get(i).getType().toString().equalsIgnoreCase(CouponType.CART_WISE.toString())) {
				ApplicableCouponForCart applicableCouponForCart = new ApplicableCouponForCart();
				applicableCouponForCart.setDiscount(0);
				for (int j = 0; j < applicableCoupon.getCart().getItems().size(); j++) {
					Items items = applicableCoupon.getCart().getItems().get(j);
					ObjectMapper mapper = new ObjectMapper();
					Object obj = couponDto.get(i).getDetails();
					CartWiseCouponDTO cartWiseCouponDTO = null;
					String jsonString;
					try {
						jsonString = mapper.writeValueAsString(obj);
						cartWiseCouponDTO = mapper.readValue(jsonString, CartWiseCouponDTO.class);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					Double threshold = (double) (items.getQuantity() * items.getPrice());

					if (threshold > cartWiseCouponDTO.getThreshold()) {
						
						applicableCouponForCart.setCoupon_id(couponDto.get(i).getCoupon_id());
						Integer dis = (int) ((threshold * cartWiseCouponDTO.getDiscount()) / 100);
						applicableCouponForCart.setDiscount(applicableCouponForCart.getDiscount()+dis);
						applicableCouponForCart.setType(couponDto.get(i).getType().toString());
						
					}

				}
				temp_applicableCoupon.getApplicable_coupons().add(applicableCouponForCart);

			}

			if (couponDto.get(i).getType().toString().equalsIgnoreCase(CouponType.PRODUCT_WISE.toString())) {
				ApplicableCouponForCart applicableCouponForCart = new ApplicableCouponForCart();
				applicableCouponForCart.setDiscount(0);
				for (int j = 0; j < applicableCoupon.getCart().getItems().size(); j++) {
					Items items = applicableCoupon.getCart().getItems().get(j);
					ObjectMapper mapper = new ObjectMapper();
					Object obj = couponDto.get(i).getDetails();
					ProductWiseCouponDTO productWiseCouponDTO = null;
					String jsonString;
					try {
						jsonString = mapper.writeValueAsString(obj);
						productWiseCouponDTO = mapper.readValue(jsonString, ProductWiseCouponDTO.class);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					if (items.getProduct_id() == productWiseCouponDTO.getProduct_id()) {
						
						applicableCouponForCart.setCoupon_id(couponDto.get(i).getCoupon_id());
						Integer dis = (int) ((items.getPrice() * items.getQuantity()
								* productWiseCouponDTO.getDiscount()) / 100);
						applicableCouponForCart.setDiscount(applicableCouponForCart.getDiscount()+dis);
						applicableCouponForCart.setType(couponDto.get(i).getType().toString());
						
					}

				}
				temp_applicableCoupon.getApplicable_coupons().add(applicableCouponForCart);

			}

			if (couponDto.get(i).getType().toString().equalsIgnoreCase(CouponType.BXGY.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				Object obj = couponDto.get(i).getDetails();
				BxGyWiseCouponDTO bxGyWiseCouponDTO = null;
				String jsonString;
				try {
					jsonString = mapper.writeValueAsString(obj);
					bxGyWiseCouponDTO = mapper.readValue(jsonString, BxGyWiseCouponDTO.class);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				List<Integer> productIdsformcart = applicableCoupon.getCart().getItems().stream()
						.map(itm -> itm.getProduct_id()).collect(Collectors.toList());
				List<Integer> productIdsformbxgy = bxGyWiseCouponDTO.getBuy_products().stream()
						.map(prod -> prod.getProduct_id()).collect(Collectors.toList());

				Set<Integer> bxgyset = new HashSet<>();
				bxgyset.addAll(productIdsformbxgy);

				Set<Integer> cartset = new HashSet<>();
				cartset.addAll(productIdsformcart);

				Set<Integer> difference = new HashSet<>(cartset);
				difference.removeAll(bxgyset);
				Integer[] id = difference.toArray(new Integer[0]);
				List<Float> productPrice = applicableCoupon.getCart().getItems().stream()
						.filter(itm -> itm.getProduct_id() == id[0]).map(itm -> itm.getPrice())
						.collect(Collectors.toList());

				ApplicableCouponForCart applicableCouponForCart = new ApplicableCouponForCart();
				applicableCouponForCart.setCoupon_id(couponDto.get(i).getCoupon_id());
				applicableCouponForCart.setType(couponDto.get(i).getType().toString());
				applicableCouponForCart.setDiscount(0);
				if (cartset.containsAll(cartset)) {
					Map<Integer, Integer> mapcoup = new HashMap<>();
					bxGyWiseCouponDTO.getBuy_products().forEach(bxgy -> {
						mapcoup.put(bxgy.getProduct_id(), bxgy.getQuantity());
					});
					for (int k = 0; k < applicableCoupon.getCart().getItems().size(); k++) {

						Items itm = applicableCoupon.getCart().getItems().get(k);
						if (bxGyWiseCouponDTO.getRepition_limit() > 0) {
							int discount = 0;
							if (mapcoup.containsKey(itm.getProduct_id())) {
								int limit = itm.getQuantity() / mapcoup.get(itm.getProduct_id());
								if (bxGyWiseCouponDTO.getRepition_limit() > limit) {
									discount = applicableCouponForCart.getDiscount()
											+ (int) (productPrice.get(0) * limit);
								} else {
									discount = applicableCouponForCart.getDiscount()
											+ (int) (productPrice.get(0) * bxGyWiseCouponDTO.getRepition_limit());
								}
								applicableCouponForCart.setDiscount(discount);
								bxGyWiseCouponDTO.setRepition_limit(bxGyWiseCouponDTO.getRepition_limit() - limit);
							}
						}
					}

				}

				temp_applicableCoupon.getApplicable_coupons().add(applicableCouponForCart);

			}

		}

		return temp_applicableCoupon;
	}

	@Override
	public Object applyCouponById(CartItems cartItem, Integer id) throws Exception {
		ApplicableCoupon ac=this.applicableCoupons(cartItem);
		
		List<ApplicableCouponForCart> lacfc=ac.getApplicable_coupons().stream().filter(itm -> itm.getCoupon_id() == id).map(itm -> itm).collect(Collectors.toList());
		CouponDTO cd=daotoDto(couponRepository.findByCouponId(id));
		
		if(isExpired(cd.getExpirationDate()))
			throw new Exception("Token Expeired : "+cd.getExpirationDate().toString());
		AppliedCoupon appliedc=new AppliedCoupon();
		List<DiscountItem> items=new ArrayList();
		appliedc.setItems(items);
			Integer pricetotal=0;
			
				for(int i=0;i<cartItem.getCart().getItems().size();i++) {
				Items itm =cartItem.getCart().getItems().get(i);
				
				DiscountItem discountItem=new DiscountItem();
				discountItem.setPrice((int)itm.getPrice());
				discountItem.setProduct_id(itm.getProduct_id());
				discountItem.setQuantity(itm.getQuantity());
				appliedc.getItems().add(discountItem);
				pricetotal +=(int)itm.getPrice()*itm.getQuantity();
				
			}
			
			appliedc.setFinal_price(pricetotal-lacfc.get(0).getDiscount());
			appliedc.setTotal_price(pricetotal);
			appliedc.setTotal_discount(lacfc.get(0).getDiscount());
			
			
		return appliedc;
	}
	
	
	
	 public boolean isExpired(LocalDate expirationDate) {
		 if(expirationDate==null) {
	        return LocalDate.now().isAfter(expirationDate);
		 }
		 return false;
	    }

}
