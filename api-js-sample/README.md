# Shortcuts JavaScript API Sample

Sample api implementation using JavaScript.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Running website using Node

1. Ensure that Node.JS and NPM are installed.

2. Install node packages

```
npm install
```

3. Run tests

```
npm test
```

The logging level used during the tests can be updated through the editing the [Test Hooks](src/_hooks.js) directives.

## Source Documentation

The following modules are available as part of this example.  The ones of particular interest are the [Site](#site), [Search](#search) and [Appointment](#appointment) modules.

* The API module ([source](src/api.js)) provides `GET`, `POST` and `DELETE` operations against the Shortcuts API

* The Appointment module([source](src/appointment.js)) provides the ability to manage appointments on behalf of customers, including booking, retrieving and cancelling appointments.

* The Config module ([source](src/config.js)) provides configuration of access information, including credentials and target salon identifier.

* The Log module ([source](src/log.js)) configures logging parameters.

* The OAuth module ([source](src/oauth.js)) provides for calculation of a signature for the purposes of authenticating requests against the API. This is implementated according to the OAuth 1.1 RFC.

* The Site module ([source](src/site.js)) provides for API operations to retrieve general information about the target salon.

* The Search module ([source](src/search.js)) provides for searching of available appointments at the target salon.

The [Site](#site), [Search](#search) and [Appointment](#appointment) modules are described in greater detail below.

### <a name=appointment></a>Appointment Module

The Appointment module provides for the following operations:

#### <a name=authenticate></a>authenticate

Authenticates a customer against the API, providing a customer session hyperlink reference that may be used for the other API endpoints in this module.  The function takes three arguments, the first being the customer username, the second being the customer plain-text password and the third being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
appointment.authenticate(username, password, function(err, result) {
    console.log(result.content);
    var customerSessionHref = result.content.href;
});
```
Result:
```json
{
    "expiry_utc_date_time":"2017-11-22T03:50:40.8076178Z",
    "customer":{
        "display_name":"Walters, Michael",
        "first_name":"Michael",
        "last_name":"Walters",
        "is_active":true,
        "phone_numbers":[
            {"type_code":"mobile","number":"12345678"}
        ],
        "email":"michael@walters.id.au",
        "preferred_contact_method_code":"email",
        "gender_code":"unknown",
        "display_image":"https://pos.shortcutssoftware.com/webapi/company/2200/images/customer/91557473.jpg?updated_utc_date_time=2016-08-11T07%3A13%3A38%2B00%3A00",
        "links":[{
            "rel":"resource/preferred_site",
            "display_name":"My Business 4",
            "href":"https://pos.shortcutssoftware.com/webapi/site/30179"
        }],
        "created_utc_date_time":"0001-01-01T00:00:00",
        "updated_utc_date_time":"2017-10-23T03:35:19",
        "version":0,
        "customer_relationships":[],
        "href":"https://pos.shortcutssoftware.com/webapi/company/2200/customer/91557473"
    },
    "href":"https://pos.shortcutssoftware.com/webapi/company/2200/customer_session/592a60LCurDNn1om"
}
```

#### <a name=bookAppointment></a>bookAppointment

Books an appointment for the customer against the API.  The function takes three arguments, the first being the customer session href from [authenticate](#authenticate), the second being an available appointment as per the response from the [Search](#search) module and the third being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
appointment.bookAppointment(
    'https://pos.shortcutssoftware.com/webapi/company/2200/customer_session/592a60LCurDNn1om', 
    {
        "services": [ {
            "start_time":"10:00:00",
            "sell_price":50,
            "duration":"PT15M",
            "links": [ {
                "rel":"resource/service",
                "display_name":"Wax Leg",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6/block/1"
            },{
                "rel":"resource/employee",
                "display_name":"John",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/1"
            } ]
        }],
        "scheduled_date":"2017-10-21",
        "duration":"PT15M",
        "sell_price":50,
        "links":[{
            "rel":"resource/site",
            "href":"https://pos.shortcutssoftware.com/webapi/site/30179"
        }]
    }, 
    function(err, result) {
        console.log(result.content);
    }
);
```
Successful Result:
```json
{
    "appointments":[ {
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/appointment/387"
    } ],
    "href":"https://pos.shortcutssoftware.com/webapi/site/30179/appointments"
}
```
Failed Result:
```json
{
    "error_type_code":"system",
    "message":"Proposed appointment is no longer available"
}
```

#### <a name=retrieveAppointments></a>retrieveAppointments

Retrieves appointments for the customer against the API.  The function takes two arguments, the first being the customer session href from [authenticate](#authenticate) and the second being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
appointment.retrieveAppointments(
    'https://pos.shortcutssoftware.com/webapi/company/2200/customer_session/592a60LCurDNn1om', 
    function(err, result) {
        console.log(result.content);
    }
);
```
Result:
```json
{
    "appointments": [ {
        "start_time":"20:00:00",
        "scheduled_date":"2017-10-32",
        "duration":"PT15M",
        "status_code":"awaiting_review",
        "appointment_type_code":"client_appointment",
        "links":[{
            "phone_numbers":[{"type_code":"mobile","number":"041775183"}],
            "first_name":"Michael",
            "last_name":"Walters",
            "rel":"resource/customer",
            "updated_utc_date_time":"2017-10-23T03:35:19",
            "display_name":"Walters, Michael",
            "display_image":"https://pos.shortcutssoftware.com/webapi/company/2200/images/customer/91557473.jpg?updated_utc_date_time=2016-08-11T07%3A13%3A38%2B00%3A00",
            "href":"https://pos.shortcutssoftware.com/webapi/site/30179/customer/91557473"
        },{
            "rel":"resource/employee",
            "display_name":"John",
            "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/1"
        },{
            "has_prepayment":false,
            "has_notes":false,
            "booking_status_code":"booked",
            "start_date":"2017-10-24",
            "version":6,
            "rel":"resource/booking",
            "created_utc_date_time":"2017-10-23T00:57:51+00:00",
            "updated_utc_date_time":"2017-10-23T01:55:09+00:00",
            "display_name":"Michael Walters",
            "href":"https://pos.shortcutssoftware.com/webapi/site/30179/booking/28"
        },{
            "rel":"resource/service",
            "display_name":"Wax Leg","href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6"
        },{
            "rel":"resource/site",
            "display_name":"My Business 4",
            "href":"https://pos.shortcutssoftware.com/webapi/site/30179"
        }],
        "is_active":true,
        "updated_utc_date_time":"2017-10-23T00:57:51Z",
        "version":2,
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/appointment/387"
    } ]
}
```

#### cancelAppointment
Cancels an appointment for the customer against the API.  The function takes three arguments, the first being the customer session href from [authenticate](#authenticate) and the second being an appointment href from either [bookAppointment](#bookAppointment) or [retrieveAppointments](#retrieveAppointments) and the third being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
appointment.cancelAppointment(
    'https://pos.shortcutssoftware.com/webapi/company/2200/customer_session/592a60LCurDNn1om', 
    'https://pos.shortcutssoftware.com/webapi/site/30179/appointment/387',
    function(err, result) {
        console.log(result.content);
    }
);
```
Result:
```
OK
```

### <a name=site></a>Site Module

The Site module provides the following operations:

#### retrieveSiteDetails
Retrieves the site information from the server.  The function takes one argument, being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
site.retrieveSiteDetails(function (err, result) {
    console.log(result.content);
});
```
Result:
```json
{  
    "display_name": "My Business 4",
    "updated_utc_date_time": "2017-06-19T04:06:11","created_utc_date_time":"2015-04-21T06:01:45",
    "version":"0",
    "links":[ { ... } ],
    "is_active":true,
    "is_subscribed":false,
    "is_connected":true,
    "href":"https://pos.shortcutssoftware.com/webapi/site/30179"
}
```

#### retrieveServiceCategories
Retrieves the service categories from server.  The function takes one argument, being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
site.retrieveServiceCategories(function (err, result) {
    console.log(result.content);
});
```
Result:
```json
{
    "service_categories": [ {
        "display_name":"Beauty",
        "version":3,
        "updated_utc_date_time":"2016-12-11T09:22:28",
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/se
rvice_category/2"
    },{
        "display_name":"Hair",
        "version":3,
        "updated_utc_date_time":"2016-12-11T09:22:28",
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service_category/1"
    } ],
    "paging": {
        "page":1,
        "number_of_pages":1
    },
    "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service_categories"
}
```

#### retrieveServicesByServiceCategoryName
Retrieves the services from the server belonging to a service category with a specific name.  The function takes two arguments, the first being the `serviceCategoryName`, the second being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
site.retrieveServicesByServiceCategoryName('Beauty', function (err, result) {
    console.log(result.content);
});
```
Result:
```json
{
    "services": [ { 
        "display_name":"Wax Back",
        "created_utc_date_time":"2015-04-22T10:13:33",
        "updated_utc_date_time":"2017-10-20T04:28:43",
        "version":0,
        "current_sell_price": {
            "default_sell_ex_tax_price":100,
            "default_sell_inc_tax_price":100,
            "effective_from_date":"1990-01-01",
            "effective_to_date":"9999-12-31"
        },
        "current_cost_price": {
            "default_cost_ex_tax_price":0,
            "default_cost_inc_tax_price":0,
            "effective_from_date":"1990-01-01",
            "effective_to_date":"9999-12-31"
        },
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/7"
    },{
        "display_name":"Wax Leg",
        "created_utc_date_time":"2015-04-22T10:13:33",
        "updated_utc_date_time":"2017-10-20T04:12:51",
        "version":0,
        "current_sell_price": {
            "default_sell_ex_tax_price":50,
            "default_sell_inc_tax_price":50,
            "effective_from_date":"1990-01-01",
            "effective_to_date":"9999-12-31"
        },
        "current_cost_price": {
            "default_cost_ex_tax_price":0,
            "default_cost_inc_tax_price":0,
            "effective_from_date":"1990-01-01",
            "effective_to_date":"9999-12-31"
        },
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6" 
    } ],
    "paging": {
        "page":1,
        "number_of_pages":1
    },
    "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service_category/2/services"
}
```

#### retrieveServicesByServiceName
Retrieves the services from the server with a specific name. The function takes two arguments, the first being the `serviceName`, the second being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
site.retrieveServicesByServiceName('Wax Leg', function (err, result) {
    console.log(result.content);
});
```
Result:
```json
{
    "services": [ {
        "display_name":"Wax Leg",
        "created_utc_date_time":"2015-04-22T10:13:33",
        "updated_utc_date_time":"2017-10-20T04:12:51",
        "version":0,
        "current_sell_price": {
            "default_sell_ex_tax_price":50,
            "default_sell_inc_tax_price":50,
            "effective_from_date":"1990-01-01",
            "effective_to_date":"9999-12-31"
        },
        "current_cost_price": {
            "default_cost_ex_tax_price":0,
            "default_cost_inc_tax_price":0,
            "effective_from_date":"1990-01-01",
            "effective_to_date":"9999-12-31"
        },
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6"
    } ],
    "paging": {
        "page":1,
        "number_of_pages":1
    },
    "href":"https://pos.shortcutssoftware.com/webapi/site/30179/services"
}
```

#### retrieveEmployeeByEmployeeAlias
Retrieves the employee from the server with a specific alias. The function takes two arguments, the first being the `employeeAlias`, the second being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
site.retrieveEmployeeServicePricing('Wendy', function (err, result) {
    console.log(result.content);
});
```
Result:
```json
{
    "employees":[{
        "created_utc_date_time":"2015-04-22T10:13:31",
        "updated_utc_date_time":"2016-12-11T09:22:28",
        "display_name":"Wendy",
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/2"
    }],
    "paging":{
        "page":1,
        "number_of_pages":1
    },
    "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employees"
}
```

#### retrieveServiceEmployeePricing
Retrieves the employee pricing from the server for a service with the specified name. The function takes two arguments, the first being the `serviceName`, the second being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
site.retrieveServiceEmployeePricing('Wax Leg', function (err, result) {
    console.log(result.content);
});
```
Result:
```json
{
    "on_premise_reference":1003,
    "employees":[{
        "on_premise_reference":2,
        "effective_date":"2017-10-23T00:00:00",
        "display_name":"John",
        "sell_ex_tax_price":50,
        "sell_inc_tax_price":50,
        "duration_minutes":15,
        "base_sell_ex_tax_price":50,
        "base_sell_inc_tax_price":50,
        "base_duration_minutes":15,
        "appointment_book_order":2,
        "is_available_for_walkin":true,
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/1"
    },{
        "on_premise_reference":3,
        "effective_date":"2017-10-23T00:00:00",
        "display_name":"Wendy",
        "sell_ex_tax_price":50,
        "sell_inc_tax_price":50,
        "duration_minutes":15,
        "base_sell_ex_tax_price":50,
        "base_sell_inc_tax_price":50,
        "base_duration_minutes":15,
        "appointment_book_order":3,
        "is_available_for_walkin":true,
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/2"
    }],
    "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6/employee_pricing?effective_date=2017-10-23T00%3A00%3A00"
}
```

#### retrieveEmployeeServicePricing
Retrieves the service pricing from the server for an employe with the specified alias. The function takes two arguments, the first being the `employeeAlias`, the second being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
site.retrieveEmployeeServicePricing('Wendy', function (err, result) {
    console.log(result.content);
});
```
Result:
```json
{
    "services":[{
        "effective_date":"2017-10-23T00:00:00",
        "display_name":"Cut for Child",
        "sell_ex_tax_price":5,
        "sell_inc_tax_price":5,
        "duration_minutes":30,
        "base_sell_ex_tax_price":5,
        "base_sell_inc_tax_price":5,
        "base_duration_minutes":30,
        "links":[{
            "rel":"resource/category",
            "display_name":"Hair",
            "href":"https://pos.shortcutssoftware.com/webapi/site/2017-10-23T00%3A00%3A00/service_category/1"
        }],
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/5"
    },{
        "effective_date":"2017-10-23T00:00:00",
        "display_name":"Cut for Men",
        "sell_ex_tax_price":15,
        "sell_inc_tax_price":15,
        "duration_minutes":30,
        "base_sell_ex_tax_price":15,
        "base_sell_inc_tax_price":15,
        "base_duration_minutes":30,
        "links":[{
            "rel":"resource/category",
            "display_name":"Hair",
            "href":"https://pos.shortcutssoftware.com/webapi/site/2017-10-23T00%3A00%3A00/service_category/1"
        }],
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/4"
    },{
        "effective_date":"2017-10-23T00:00:00",
        "display_name":"Wax Back",
        "sell_ex_tax_price":100,
        "sell_inc_tax_price":100,
        "duration_minutes":30,
        "base_sell_ex_tax_price":100,
        "base_sell_inc_tax_price":100,
        "base_duration_minutes":30,
        "links":[{
            "rel":"resource/category",
            "display_name":"Beauty",
            "href":"https://pos.shortcutssoftware.com/webapi/site/2017-10-23T00%3A00%3A00/service_category/2"
        }],
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/7"
    },{
        "effective_date":"2017-10-23T00:00:00",
        "display_name":"Wax Leg",
        "sell_ex_tax_price":50,
        "sell_inc_tax_price":50,
        "duration_minutes":15,
        "base_sell_ex_tax_price":50,
        "base_sell_inc_tax_price":50,
        "base_duration_minutes":15,
        "links":[{
            "rel":"resource/category",
            "display_name":"Beauty",
            "href":"https://pos.shortcutssoftware.com/webapi/site/2017-10-23T00%3A00%3A00/service_category/2"
        }],
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6"
    }],
    "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/2/service_pricing"
}
```
### <a name=search></a>Search Module

The Search module retrieves the available appointments from the server. All operations return the same result structure.

The `byServiceCategoryName` accepts the name of a service category and returns all available appointments for any service within the specified service category.

The `byServiceName` accepts the name of a service and returns all available appointments for the specified service.

The `byServiceAndEmployeeName` accepts the name of a service and an employee alias and returns all available appointments for the specified service with the specified employee.

The last argument of each function is a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  

An example `result.content` that may be retrieved with this function is:

Example:
```js
site.byServiceCategoryName('Beauty', function(err, result) {
    console.log(result.content);
});
site.byServiceName('Wax Leg', function (err, result) {
    console.log(result.content);
});
site.byServiceAndEmployeeNameName('Wax Leg', 'John', function (err, result) {
    console.log(result.content);
});
```
Result:
```json
{
    "available_appointments": [ {
        "services": [ {
            "start_time":"00:00:00",
            "sell_price":50,
            "duration":"PT15M",
            "links": [ {
                "rel":"resource/service",
                "display_name":"Wax Leg",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6/block/1"
            },{
                "rel":"resource/employee",
                "display_name":"John",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/1"
            } ]
        }],
        "scheduled_date":"2017-10-21",
        "duration":"PT15M",
        "sell_price":50,
        "links":[{
            "rel":"resource/site",
            "href":"https://pos.shortcutssoftware.com/webapi/site/30179"
        }]
    }, {
        "services":[{
            "start_time":"00:15:00",
            "sell_price":50,
            "duration":"PT15M",
            "links":[{
                "rel":"resource/service",
                "display_name":"Wax Leg",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6/block/1"
            }, {
                "rel":"resource/employee",
                "display_name":"John",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/1"
            }]
        }],
        "scheduled_date":"2017-10-21",
        "duration":"PT15M",
        "sell_price":50,
        "links":[{
            "rel":"resource/site",
            "href":"https://pos.shortcutssoftware.com/webapi/site/30179"
        }]
    }, {
        "services":[{
            "start_time":"00:30:00",
            "sell_price":50,
            "duration":"PT15M",
            "links":[{
                "rel":"resource/service",
                "display_name":"Wax Leg",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6/block/1"
            },
            {
                "rel":"resource/employee",
                "display_name":"John",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/1"
            }]
        }],
        "scheduled_date":"2017-10-21",
        "duration":"PT15M",
        "sell_price":50,
        "links":[{
        "rel":"resource/site",
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179"
        }]
    }, {
        "services":[{
            "start_time":"00:45:00",
            "sell_price":50,
            "duration":"PT15M",
            "links":[{
                "rel":"resource/service",
                "display_name":"Wax Leg",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6/block/1"
            },
            {
                "rel":"resource/employee",
                "display_name":"John",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/1"
            }]
        }],
        "scheduled_date":"2017-10-21",
        "duration":"PT15M",
        "sell_price":50,
        "links":[{
        "rel":"resource/site",
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179"
        }]
    }, {
        "services":[{
            "start_time":"01:00:00",
            "sell_price":50,
            "duration":"PT15M",
            "links":[{
                "rel":"resource/service",
                "display_name":"Wax Leg",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/service/6/block/1"
            },
            {
                "rel":"resource/employee",
                "display_name":"John",
                "href":"https://pos.shortcutssoftware.com/webapi/site/30179/employee/1"
            }]
        }],
        "scheduled_date":"2017-10-21",
        "duration":"PT15M",
        "sell_price":50,
        "links":[{
        "rel":"resource/site",
        "href":"https://pos.shortcutssoftware.com/webapi/site/30179"
        }]
    } ]
}
```
