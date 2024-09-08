# Coupons Management


Prerequisite :
It's Spring-Boot maven project Implemented using java-17 so please insure before running the application it is properly installed in your machine .
I have Implemented this on Windows Machine so please do changes accordingly.

I am using Mysql database and Spring JPA for database operation so please installed it as well and create database named "Coupons" or if you want to different name create with that name and mention in application.yml file

Initally in am runnig on port :8081

so these are basic requirment
	to run the application if you are using any Id than go with that otherwise , i have built it as well and and jar is present in you will direclty run that jar using command

	java -jar coupons-0.0.1-SNAPSHOT.jar 

	and it will start the application 





Implemented Cases: 

	I have tried to Implement all the case mentioned in file 
	like :
			Cart_Wise :
					if product crossed the threshold limit than on that price will give 10 percent of discount irrespective of how many items of cart crossed threshold i will add all in discount 
					example :
						{"product_id": 1, "quantity": 6, "price": 50}, 
						{"product_id": 2, "quantity": 4, "price": 30},

						here if threshold is 100 and discount is 10 than it should be applied on both product
						than 300 of 10 percent is 30
						and 120 of 10 percent is 12
						than total discount is 42

			Product_Wise :
					if product in cart of discount 10 metion than i will provide 10 percent discount on every product 
					example :
						{"product_id": 1, "quantity": 3, "price": 50}, 
						{"product_id": 2, "quantity": 4, "price": 25},

						here if discount mention for product_id: 1 is 10  and for 2 is 5 
						than 150 of 10 percent is 15
						and 100 of 5 percent is 
						than total discount is 20

			BXGY :
				if coupon structure mention for BXGY is this 

				{
					"type": "BXGY",
						"details": {
							"buy_products": [
								{"product_id": 1, "quantity": 3},
								{"product_id": 2, "quantity": 3}
								],
								"get_products": [
								{"product_id": 3, "quantity": 1}
								],
							"repition_limit": 2
						},
						"expirationDate": "2024,8,10"
					}


					and product in cart is  this

					{"cart": {
						"items": [
							{"product_id": 1, "quantity": 6, "price": 50}, // Product X
							{"product_id": 2, "quantity": 3, "price": 30}, // Product Y
							{"product_id": 3, "quantity": 2, "price": 25} // Product Z
							]
						}
					}

					here repition_limit mention is 2 

					so we have two product in buy_products in Coupons so if both product is percent in cart than only BXGY will be applied otherwise it will be not applicable
					as well there is quantity 3 mentioned and in cart item for product_id i have 6 quantity than it is applied only 2 time on product_id 3 so total discount is :50

					{
						"coupon_id": 6,
						"type": "BXGY",
						"discount": 50
					}
					

UnImplemented Cases: 

					Only one feture is not Implement that is if you apply coupon id than it will not show you discount on particular product.
					means:
					 
					{
						"coupon_id": 6,
						"type": "BXGY",
						"details": {
							"buy_products": [
								{"product_id": 1,"quantity": 3},
								{"product_id": 2,"quantity": 3}
							],
							"get_products": [{"product_id": 3,"quantity": 1}
							],
							"repition_limit": 2
						},
						"expirationDate": "2024,9,10"
					}

					and cart item

					so if token is defined like this for BXGY  than the token is applicable for whole cart only on particular item an you will not see any updated product and discount on that instead on cart 

					than responnse is this
					{
						"items": [
							{
								"product_id": 1,
								"quantity": 6,
								"price": 50
							},
							{
								"product_id": 2,
								"quantity": 3,
								"price": 30
							},
							{
								"product_id": 3,
								"quantity": 2,
								"price": 25
							}
						],
						"total_price": 440,
						"total_discount": 50,
						"final_price": 390
				}

				not like :
				{
					"updated_cart": {
					"items": [
							{"product_id": 1, "quantity": 6, "price": 50,"total_discount": 0},
							{"product_id": 2, "quantity": 3, "price": 30, "total_discount": 0},
							{"product_id": 3, "quantity": 4, "price": 10, "total_discount": 40}
							],
							"total_price": 300,
							"total_discount": 50, 
							"final_price": 250
					}
				}





## Assumptions  And Limitation :
	1. while creating coupon you will use correct structure like ,we are using ENUM for type so coupon type should be always [CART_WISE,PRODUCT_WISE,BXGY] otherwise
	you will "Invalid value for CouponType: JSON parse error: Invalid value for CouponType: PRODUCTWISE".

	2.Details for each types are defined seperately and it shuold be in the format given below otherwise you will get exception "Please Provide Correct Json".

	3.if you want to add expirationDate you can in case of empty it will store null, rembemer that after creating token you can only update details not expirationDate and type

================================================================================
	Cart_Wise

	JSON Raw_Data : for cerating CART_WISE Coupons
	
	{
		"type": "CART_WISE",
		"details": {
		"threshold": 100,
		"discount": 10
		},
		"expirationDate":"2024,08,10"
	}

	reutrn value after creating

	{
		"coupon_id": 4,
		"type": "PRODUCT_WISE",
		"details": {
			"product_id": 1,
			"discount": 20
		},
		"expirationDate": "2024,8,10"
	}
====================================================================================

	PRODUCT_WISE

	JSON Raw_Data : for cerating PRODUCT_WISE Coupons

	{
		"type": "PRODUCT_WISE",
		"details": {
		"product_id": 1,
		"discount": 20
		},
		"expirationDate":"2024,08,10"
	}

	reutrn value after creating

	{
		"coupon_id": 5,
		"type": "PRODUCT_WISE",
		"details": {
			"product_id": 1,
			"discount": 20
		},
		"expirationDate": "2024,8,10"
	}

===========================================================================================
	BXGY

	JSON Raw_Data : for cerating BXGY Coupons
	
	
	{
	"type": "BXGY",
		"details": {
			"buy_products": [
				{"product_id": 1, "quantity": 3},
				{"product_id": 2, "quantity": 3}
				],
				"get_products": [
				{"product_id": 3, "quantity": 1}
				],
			"repition_limit": 2
		},
		"expirationDate": "2024,8,10"
	}

	reutrn value after creating

	{
		"coupon_id": 6,
		"type": "BXGY",
		"details": {
			"buy_products": [
				{
					"product_id": 1,
					"quantity": 3
				},
				{
					"product_id": 2,
					"quantity": 3
				}
			],
			"get_products": [
				{
					"product_id": 3,
					"quantity": 1
				}
			],
			"repition_limit": 2
		},
		"expirationDate": "2024,8,10"
	}

==========================================================================================


Url's and there responnse

http://localhost:8081/api/coupons    [POST]

	{
		"type": "CART_WISE",
		"details": {
		"threshold": 100,
		"discount": 10
		},
		"expirationDate":"2024,08,10"
	}


	responnse:

	{
    "coupon_id": 1,
    "type": "CART_WISE",
    "details": {
        "threshold": 100,
        "discount": 10
    },
    "expirationDate": "2024,8,10"
}


http://localhost:8081/api/coupons  	[POST]

	{
		"type": "PRODUCT_WISE",
		"details": {
		"product_id": 1,
		"discount": 20
		},
		"expirationDate":"2024,08,10"
	}

	responnse:

	
	{
    "coupon_id": 2,
    "type": "PRODUCT_WISE",
    "details": {
        "product_id": 1,
        "discount": 20
    },
    "expirationDate": "2024,8,10"
}

http://localhost:8081/api/coupons  	[POST]

	{
		"type": "BXGY",
			"details": {
				"buy_products": [
					{"product_id": 1, "quantity": 3},
					{"product_id": 2, "quantity": 3}
					],
					"get_products": [
					{"product_id": 3, "quantity": 1}
					],
				"repition_limit": 2
			},
			"expirationDate": "2024,8,10"
		}


responnse:

	{
    "coupon_id": 3,
    "type": "BXGY",
    "details": {
        "buy_products": [
            {
                "product_id": 1,
                "quantity": 3
            },
            {
                "product_id": 2,
                "quantity": 3
            }
        ],
        "get_products": [
            {
                "product_id": 3,
                "quantity": 1
            }
        ],
        "repition_limit": 2
    },
    "expirationDate": "2024,8,10"
}


http://localhost:8081/api/coupons	[GET]

	responnse:
		[
			{
				"coupon_id": 1,
				"type": "CART_WISE",
				"details": {
					"threshold": 100,
					"discount": 10
				},
				"expirationDate": "2024,8,10"
			},
			{
				"coupon_id": 2,
				"type": "PRODUCT_WISE",
				"details": {
					"product_id": 1,
					"discount": 20
				},
				"expirationDate": "2024,8,10"
			},
			{
				"coupon_id": 3,
				"type": "BXGY",
				"details": {
					"buy_products": [
						{
							"product_id": 1,
							"quantity": 3
						},
						{
							"product_id": 2,
							"quantity": 3
						}
					],
					"get_products": [
						{
							"product_id": 3,
							"quantity": 1
						}
					],
					"repition_limit": 2
				},
				"expirationDate": "2024,8,10"
			}
		]


http://localhost:8081/api/coupons/1		[GET]

{
    "coupon_id": 1,
    "type": "CART_WISE",
    "details": {
        "threshold": 100,
        "discount": 10
    },
    "expirationDate": "2024,8,10"
}



http://localhost:8081/api/coupons/1 [PUT]  Note in url id is mentioned to only match url mention in doc , but internally i am using json url use carefully

{
        "coupon_id": 3,
        "type": "BXGY",
        "details": {
            "buy_products": [
                {
                    "product_id": 1,
                    "quantity": 3
                },
                {
                    "product_id": 2,
                    "quantity": 3
                }
            ],
            "get_products": [
                {
                    "product_id": 3,
                    "quantity": 1
                }
            ],
            "repition_limit": 2
        },
        "expirationDate": "2024,8,10"
    }


responnse;

	{
    "coupon_id": 3,
    "type": "BXGY",
    "details": {
        "buy_products": [
            {
                "product_id": 1,
                "quantity": 3
            },
            {
                "product_id": 2,
                "quantity": 3
            }
        ],
        "get_products": [
            {
                "product_id": 3,
                "quantity": 1
            }
        ],
        "repition_limit": 2
    },
    "expirationDate": "2024,8,10"
}


http://localhost:8081/api/coupons/1 		[Delete]


http://localhost:8081/api/coupons/applicable-coupons  [Post]

	{"cart": {
			"items": [
				{"product_id": 1, "quantity": 6, "price": 50}, // Product X
				{"product_id": 2, "quantity": 3, "price": 30}, // Product Y
				{"product_id": 3, "quantity": 2, "price": 25} // Product Z
			]
	}}

	responnse:

	{
    "applicable_coupons": [
        {
            "coupon_id": 1,
            "type": "CART_WISE",
            "discount": 30
        },
        {
            "coupon_id": 2,
            "type": "PRODUCT_WISE",
            "discount": 60
        },
        {
            "coupon_id": 3,
            "type": "BXGY",
            "discount": 50
        }
    ]
}


http://localhost:8081/api/coupons/apply-coupon/3

		{"cart": {
			"items": [
			{"product_id": 1, "quantity": 6, "price": 50}, // Product X
			{"product_id": 2, "quantity": 3, "price": 30}, // Product Y
			{"product_id": 3, "quantity": 2, "price": 25} // Product Z
			]
		}}


		responnse:

		{
    "items": [
        {
            "product_id": 1,
            "quantity": 6,
            "price": 50
        },
        {
            "product_id": 2,
            "quantity": 3,
            "price": 30
        },
        {
            "product_id": 3,
            "quantity": 2,
            "price": 25
        }
    ],
    "total_price": 440,
    "total_discount": 50,
    "final_price": 390
}



Note: I have Implemented two testcase for expirationDate and second to verify cart-waise token ,other we Implement later.


thanks for reading
Vikas Mishra
Contact Number:9918388488
email: vikasmi0029@gmail.com