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

The following modules are available as part of this example.  The ones of particular interest are the [Search](#search) and [Site](#site) modules.

* The API module ([source](src/api.js)) provides `GET` and `POST` operations against the Shortcuts API

* The Config module ([source](src/config.js)) provides configuration of access information, including credentials and target salon identifier.

* The Log module ([source](src/log.js)) configures logging parameters.

* The OAuth module ([source](src/oauth.js)) provides for calculation of a signature for the purposes of authenticating requests against the API. This is implementated according to the OAuth 1.1 RFC.

* The Site module ([source](src/site.js)) provides for API operations to retrieve general information about the target salon.

* The Search module ([source](src/search.js)) provides for searching of available appointments at the target salon.

the [Search](#search) and [Site](#site) modules are described in greater detail below.

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

### <a name=search></a>Search Module

The Search module provides the following operations:

#### byServiceName
Retrieves the available appointments from the server for a service with a specific name. The function takes two arguments, the first being the `serviceName`, the second being a callback of the form `function(err, result)`. If an error occured, the `err` property will contain the error information.  The `result` contains the `status` and the `content` of the response.  An example `result.content` that may be retrieved with this function is:

Example:
```js
site.byServiceName('Wax Leg', function (err, result) {
    console.log(result.content);
});
```

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
