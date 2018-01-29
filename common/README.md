
# API questions


## Client knows they want a specific service in a specific date/time window but doesn’t know who with.

To get a list of services:

```
GET /webapi/site/40487/services?is_bookable=true HTTP/1.1
Host: pos.shortcutssoftware.com
Authorization: OAuth oauth_consumer_key="jv6YflwoCatKYxBNffV5",oauth_token="nJjQdJiPxDYfxfmqfM4S",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1517182270",oauth_nonce="5CDQ6OMNOcx",oauth_version="1.0",oauth_signature="Ow0gKyUO7NvrSPuR5N%2FLW3uvti8%3D"
Cache-Control: no-cache
Postman-Token: 01f7e490-5442-a59e-1473-d0cbc4dff980
```

Results:

``` json
{
    "services": [
        {
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service/14"
        },
        {
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service/37"
        },

...

        {
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service/26"
        }
    ],
    "paging": {
        "page": 1,
        "number_of_pages": 1
    },
    "href": "https://pos.shortcutssoftware.com/webapi/site/40487/services"
}
```

To get details of a service:

``` text
GET /webapi/site/40487/service/14 HTTP/1.1
Host: pos.shortcutssoftware.com
Authorization: OAuth oauth_consumer_key="jv6YflwoCatKYxBNffV5",oauth_token="nJjQdJiPxDYfxfmqfM4S",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1517182345",oauth_nonce="uOwuX2iGNZh",oauth_version="1.0",oauth_signature="zn%2FteVDmb8fZJceBuRvDc%2B2GLvs%3D"
Cache-Control: no-cache
Postman-Token: c6562309-94da-f615-8d52-765c4c2df58a

```

Results:

``` json
{
    "is_bookable": true,
    "is_customer_bookable": true,
    "is_class": false,
    "class_capacity": 0,
    "default_duration_minutes": 60,
    "break_duration_minutes": 30,
    "is_prompt_for_employee": false,
    "has_children": true,
    "is_child_item_only": false,
    "is_visit_note_required": true,
    "is_available_walkin": false,
    "is_customer_available_walkin": false,
    "on_premise_reference": 1008,
    "display_name": "1/2 Head Highlights",
    "is_active": true,
    "item_type": "service",
    "created_utc_date_time": "2018-01-25T05:14:35",
    "updated_utc_date_time": "2018-01-25T05:14:35",
    "version": 0,
    "links": [
        {
            "rel": "resource/category",
            "display_name": "Hair Colouring",
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service_category/1"
        },
        {
            "rel": "resource/schedule",
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service/14/schedule"
        },
        {
            "rel": "resource/price_books",
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service/14/price_books"
        },
        {
            "rel": "collection/sub_services",
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service/14/sub_services"
        },
        {
            "rel": "resource/tax_category",
            "display_name": "GST Service Tax",
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/tax_category/3"
        }
    ],
    "current_sell_price": {
        "default_sell_ex_tax_price": 72.72727272727273,
        "default_sell_inc_tax_price": 80,
        "effective_from_date": "2018-01-29",
        "effective_to_date": "9999-12-31"
    },
    "current_cost_price": {
        "default_cost_ex_tax_price": 0,
        "default_cost_inc_tax_price": 0,
        "effective_from_date": "2018-01-29",
        "effective_to_date": "9999-12-31"
    },
    "barcodes": [
        {
            "barcode": "0000F",
            "is_primary": true
        }
    ],
    "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service/14"
}
```

To get available appointments:

``` text

POST /webapi/site/40487/calculate_available_appointments HTTP/1.1
Host: pos.shortcutssoftware.com
Content-Type: application/json
Authorization: OAuth oauth_consumer_key="jv6YflwoCatKYxBNffV5",oauth_token="nJjQdJiPxDYfxfmqfM4S",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1517185051",oauth_nonce="2DGpGJCPmw5",oauth_version="1.0",oauth_signature="km%2BHBKcpIwpOQuQwJlvY3wSKDEE%3D"
Cache-Control: no-cache
Postman-Token: 18a4be67-b046-4ea4-57b7-04783bd15fda

{
  "requested_services": [
    {
      "links": [
        {
          "rel": "site/service",
          "href": "http://pos.shortcutssoftware.com/webapi/site/601/service/14"
        }
      ]
    }
  ],
  "date_time_filter": [
    {
      "days_of_week": [
        "monday",
        "tuesday",
        "wednesday",
        "thursday",
        "friday",
        "saturday",
        "sunday"
      ],
      "from_date": "2018-01-28",
      "to_date": "2018-02-02",
      "start_time": "00:00:00",
      "finish_time": "23:59:59"
    }
  ],
  "maximum_availabilities_count": 1,
  "maximum_search_days_count": 7
}
```

Results:

``` json
{
    "available_appointments": [
        {
            "services": [
                {
                    "start_time": "10:15:00",
                    "sell_price": 80,
                    "duration": "PT45M",
                    "links": [
                        {
                            "rel": "resource/service",
                            "display_name": "1/2 Head Highlights",
                            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service/14/block/1"
                        },
                        {
                            "rel": "resource/employee",
                            "display_name": "Anna",
                            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/employee/2"
                        }
                    ]
                },
                {
                    "start_time": "11:30:00",
                    "duration": "PT15M",
                    "links": [
                        {
                            "rel": "resource/service",
                            "display_name": "1/2 Head Highlights",
                            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service/14/block/2"
                        },
                        {
                            "rel": "resource/employee",
                            "display_name": "Anna",
                            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/employee/2"
                        }
                    ]
                }
            ],
            "scheduled_date": "2018-01-29",
            "duration": "PT1H30M",
            "sell_price": 80,
            "links": [
                {
                    "rel": "resource/site",
                    "href": "https://pos.shortcutssoftware.com/webapi/site/40487"
                }
            ]
        }
    ]
}
```

Note that in the above example we only asked for 1 appointment, and the 
employee is referenced in the available appointment. You can request a 
number of appointments and iterate through these appointments to select 
the desired employee.



## Client wants category of service (e.g. massage) in a specific date/time window but doesn’t know which kind of massage or who with.

Get the list of service categories:

``` text
GET /webapi/site/40487/service_categories?is_bookable=true HTTP/1.1
Host: pos.shortcutssoftware.com
Authorization: OAuth oauth_consumer_key="jv6YflwoCatKYxBNffV5",oauth_token="nJjQdJiPxDYfxfmqfM4S",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1517185641",oauth_nonce="mCTDZ7oVvlr",oauth_version="1.0",oauth_signature="f9NU1GhOQsLNMKdxcTiEChruoaI%3D"
Cache-Control: no-cache
Postman-Token: 8f73cd30-6015-10df-8b2b-59a51ded4eca

```

Results:

``` json
{
    "service_categories": [
        {
            "display_name": "Body Treatments",
            "is_active": true,
            "is_customer_bookable": true,
            "links": [
                {
                    "rel": "collection/services",
                    "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service_category/11/services"
                }
            ],
            "version": 0,
            "updated_utc_date_time": "2018-01-25T05:14:35",
            "created_utc_date_time": "2018-01-25T05:14:35",
            "is_system_service_category": false,
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service_category/11"
        },
        {
            "display_name": "Facials",
            "is_active": true,
            "is_customer_bookable": true,
            "links": [
                {
                    "rel": "collection/services",
                    "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service_category/10/services"
                }
            ],
            "version": 0,
            "updated_utc_date_time": "2018-01-25T05:14:35",
            "created_utc_date_time": "2018-01-25T05:14:35",
            "is_system_service_category": false,
            "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service_category/10"
        }
...
    ],
    "paging": {
        "page": 1,
        "number_of_pages": 1
    },
    "href": "https://pos.shortcutssoftware.com/webapi/site/40487/service_categories"
}
```

To get available appointments for a service category:




## Client wants specific service with specific stylist/therapist but doesn’t know available times/dates or gives range of date/time.



## Client wants specific service within time/date window, wants to choose price band, doesn’t know who with.



## Client to be able to cancel booking before cancellation period expiration/cut off.


