# Shortcuts Code Examples

1. [Overview](#overview)
1. [Getting Started](#getting-started)
1. [Authentication](#authentication)
1. [Examples](#examples)

This repository contains code examples and explanations to assist 
developers with the use of Shortcuts APIs. 

This is a public repository licensed under the [ISC License](./LICENSE.txt).
You can clone or fork this entire repository. Most examples
will work out of the box with Shortcuts Demo sites. If you 
want to run the examples against your own data then you will
need to obtain API Access Keys for your site(s).

We usually find that this is a good starting point for API
developers to try out general code and processes. When you 
get to the point where you need to implement your own custom 
code and processes, that require your own data and business 
rule setup, then we recommend that you start using the APIs 
with your own Access Keys.

## Overview

The interactions in these examples make use of the individual APIs 
detailed at [Shortcuts API](http://www.shortcutssoftware.io/developer/). 
In most examples, each API used is covered in a unit test, so you 
can debug through the code and observe what is happening. 

If you browse through the API documentation above you will see 
detailed information about how to call each of the individual 
APIs that give access to Shortcuts Online Services. However, to
make things simple, these examples start you off with working 
code that shows the way that individual APIs work together, 
and how they can be called in sequences that implement 
common business processes.

### A RESTful API

The Shortcuts Online Services API is a 
[RESTful](https://en.wikipedia.org/wiki/Representational_state_transfer) 
API, so developers will have an immediate familiarity with it
if they have worked with other RESTful APIs. Any language or toolset
that supports API interactions of this style will be usable here.

In the examples below we frequently provide verbose code rather than
rely too heavily on toolsets and libraries. This is so that you get 
the clearest possible picture of what is happening with the API. In
your implementations you will make much more use of abstractions
over the API. 

### There are two API versions

Version 2 Shortcuts Online Services APIs are supported on the
`api.shortcutssoftware.io` domain. 

We will continue to support Version 1 API solutions developed 
against the `pos.shortcutssoftware.com/webapi` domain, however, 
the emphasis of our research and development efforts is targeted 
at the Version 2 API, because we are able to offer a superior API 
experience, with better performance and reliability this way.

You will see a mix of v1 and v2 API examples in this 
repository. Examples will be marked as **(version 2)** 
or **(version 1)** to help you differentiate between them.

The functionality available through version 1 of the Shortcuts API
will also be available through version 2. However, newer functionality
may be added to version 2 without being available in version 1.

**_If necessary, you can use the version 2 API from a solution 
that was initially built around version 1._**

## Getting Started

Start off by installing `git` and cloning or forking this repository.

### Javascript examples

Javascript samples run on top of [nodejs](https://nodejs.org/en/),
and use the [mocha](https://mochajs.org) framework for testing.

1. Ensure that nodejs and npm are installed.
1. Change directory to the directory containing the sample.
1. Install node packages:`npm install`
1. Run tests: `npm test`

You can also run the tests from your favourite IDE or debugging tool.

### Java examples

Java samples run on the [Spring Boot](https://projects.spring.io/spring-boot/)
framework, and use the [JUnit](http://junit.org/) framework for testing.

1. Ensure that Java 8 and [gradle](https://gradle.org) are installed.
1. Change directory to the directory containing the sample.
1. Build the example: `gradle build -x test`
1. Run tests: `gradle test`

You can also run the tests from your favourite IDE or debugging tool.

## Authentication

The biggest difference between the version 2 and the version 1 APIs 
is the way that requests are authenticated.

### JWT tokens **(version 2)**

#### Acquiring JWT tokens

When you use the version 2 Shortcuts Online Services API, you must
acquire a [JWT token](http://jwt.io) by supplying 
[OAuth 1.0](https://en.wikipedia.org/wiki/OAuth)
credentials, issued when you request community access from Shortcuts
Software Ltd, or when an individual user is created. These are what 
you use when you want to call the APIs as if they were being called 
by an individual user (or community), where you might want to tailor 
the permissions granted to the API caller. The capabilities that 
you will have if you authenticate this way can be individually 
configured without affecting the operation of your on-premise 
software.

When you correctly sign the request to the authenticate endpoint, 
you will be given a JWT token which will give you access to the 
API for 30 minutes. The token expiry time is stored in the token 
itself, so you can see it, and if you wish you can proactively 
acquire a new token when yours approaches expiry. Tokens are 
cheap and can be discarded and/or reacquired at will.

**_Important: Each time you call the version 2 Shortcuts API you must 
supply a valid JWT token in the `Authorization` header._**

Please take a look at the following class to see an example of
version 2 API authentication:

- [Version 2 API authentication](./v2-examples/java/src/main/java/com/shortcuts/example/java/authentication/JWTOAuthAuthenticationService.java)

### OAuth authentication **(version 1)**

#### Signing requests

When you use the version 1 Shortcuts Online Services API, you must sign every
request according to the [OAuth 1.0](https://en.wikipedia.org/wiki/OAuth)
specification. There is no actual _authentication_ operation, rather, each
request is processed when it is sent to the version 1 API. If the request's
OAuth signature is valid then the request will be processed.

OAuth credentials are issued when you request community access from Shortcuts
Software Ltd, or when an individual user is created. These are what you use 
when you want to call the APIs as if they were being called by an individual 
user (or community), where you might want to tailor the permissions granted 
to the API caller.

Please take a look at the following example:

- [Version 1 API authentication](./v1-examples/js/src/oauth.js)

**_Important: Each time you call the version 1 Shortcuts API you must
sign the request in this way._**

### Comparison

The big differences between authentication for the version 1 and 
the version 2 APIs are:

- Using the version 2 API
  - You only need to authenticate once to acquire a JWT token (until the token expires).
- Using the version 1 API
  - You need to sign every request individually.

## Examples

### Functional areas

API usage generally falls into one or more of the areas discussed below.
For example, the most common business process is to create a booking. 
One way that this could be done is as follows:

1. Search for services at a site,
1. Select a service,
1. Search for employees at a site,
1. Select an employee,
1. Search for available appointments for that service and employee,
1. Create a booking.

Following these steps will take you through some of the areas below.

##### You are not limited to just the business processes in the examples that follow
 
It is entirely valid for you to make calls to various APIs below, 
and then make bookings based on your own combinations of the data 
returned, for your own reasons. For example: you could search for 
all employees who are struggling to meet their `visit_count` kpi 
target, and prefer them when making appointments.  

### Company

The Company APIs are used to retrieve information about the business 
(the brand) from the Shortcuts API.

This is an example of a signed [Postman](https://www.getpostman.com) request
to the version 1 API to retrieve the sites that belong to a company:

```text
GET /webapi/company/2200/sites HTTP/1.1
Host: pos.shortcutssoftware.com
Authorization: OAuth oauth_consumer_key="dYQ3ZMrZOBDG9mMCpUby",oauth_token="F096VkVTh3KpLj6PXMtM",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1517377823",oauth_nonce="SsMPLr4K3Nb",oauth_version="1.0",oauth_signature="tmHi1kJOlx9lAsePozsqR31%2Bc0I%3D"
Cache-Control: no-cache
Postman-Token: a3527ebb-4780-18de-b67b-f81745f89e25

```

The response is:

```json
{
    "sites": [
        {
            "display_name": "My Business 1",
            "updated_utc_date_time": "2012-03-30T01:21:47",
            "created_utc_date_time": "2012-03-30T01:21:47",
            "version": "0",
            "description": "",
            "href": "https://pos.shortcutssoftware.com/webapi/site/17844"
        },
        {
            "display_name": "My Business 2",
            "updated_utc_date_time": "2017-06-11T21:16:13",
            "created_utc_date_time": "2012-03-30T01:21:47",
            "version": "0",
            "description": "",
            "href": "https://pos.shortcutssoftware.com/webapi/site/17843"
        },
        {
            "display_name": "My Business 3",
            "updated_utc_date_time": "2017-06-11T21:16:12",
            "created_utc_date_time": "2012-03-30T01:21:47",
            "version": "0",
            "description": "",
            "href": "https://pos.shortcutssoftware.com/webapi/site/17842"
        },
        {
            "display_name": "My Business 4",
            "updated_utc_date_time": "2017-06-19T04:06:11",
            "created_utc_date_time": "2015-04-21T06:01:45",
            "version": "0",
            "href": "https://pos.shortcutssoftware.com/webapi/site/30179"
        }
    ],
    "paging": {
        "page": 1,
        "number_of_pages": 1
    },
    "href": "https://pos.shortcutssoftware.com/webapi/company/2200/sites"
}
```

This is the same request made to the version 2 API:

```text
GET /company/2200/sites HTTP/1.1
Host: api.shortcutssoftware.io
Authorization: JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vcG9zLWFwaS5zaG9ydGN1dHNzb2Z0d2FyZS5jb20vYXV0aGVudGljYXRlIiwic2NvcGUiOiJzZWxmIiwiaWF0IjoxNTE3Mzc4MDU3LCJleHAiOjE1MTczNzk4NTcsInJvbGVzIjpbImNvbW11bml0eV9hcHBsaWNhdGlvbl9hY2Nlc3N8MTAwfDEiXSwidXNlciI6eyJpZCI6IjQ3MjIiLCJuYW1lIjoiQVUiLCJzdXJuYW1lIjoiRGVtbyIsImdpdmVuX25hbWUiOiJBVSIsImRpc3BsYXlfbmFtZSI6IkFVIERlbW8iLCJlbWFpbCI6ImJldi5kYXZpc0BzaG9ydGN1dHMuY29tLmF1In19.Z3LAJpnkZIBTZaUFs8oxn7yOBvwymlHhSiZq0aH9bSI
Cache-Control: no-cache
Postman-Token: 11493158-7918-829e-0926-fc643a218e75
```

The response is:

```json
{
    "sites": [
        {
            "display_name": "My Business 1",
            "updated_utc_date_time": "2012-03-30T01:21:47",
            "created_utc_date_time": "2012-03-30T01:21:47",
            "version": "0",
            "description": "",
            "href": "https://api.shortcutssoftware.io/site/17844"
        },
        {
            "display_name": "My Business 2",
            "updated_utc_date_time": "2017-06-11T21:16:13",
            "created_utc_date_time": "2012-03-30T01:21:47",
            "version": "0",
            "description": "",
            "href": "https://api.shortcutssoftware.io/site/17843"
        },
        {
            "display_name": "My Business 3",
            "updated_utc_date_time": "2017-06-11T21:16:12",
            "created_utc_date_time": "2012-03-30T01:21:47",
            "version": "0",
            "description": "",
            "href": "https://api.shortcutssoftware.io/site/17842"
        },
        {
            "display_name": "My Business 4",
            "updated_utc_date_time": "2017-06-19T04:06:11",
            "created_utc_date_time": "2015-04-21T06:01:45",
            "version": "0",
            "href": "https://api.shortcutssoftware.io/site/30179"
        }
    ],
    "paging": {
        "page": 1,
        "number_of_pages": 1
    },
    "href": "https://api.shortcutssoftware.io/company/2200/sites"
}
```

As you can see, the responses are identical except for the difference in `href` elements,
which indicate the version of the API that should be called to retrieve further details.

### Site

The Site APIs are used to retrieve information about the real physical
locations of the business through the Shortcuts API.

The Sites APIs will give you information about:

- Service categories and services,
- Employees,
- Pricing,
- Available appointments.

For instance, this code will search for service definitions (by name) in a site:

```js
    var siteHref = url.resolve(config.apiUri, 'site/' + config.siteId)
    
    function retrieveServicesByServiceName(jwtToken, serviceName, done) {
        api.get(
            url.resolve(siteHref + '/', 'services?search=' + serviceName + '&is_active=true&is_bookable=true&is_customer_bookable=true&fields=display,href'),
            {Authorization: 'JWT ' + jwtToken},
            done);
    }
```

Please refer to [site.js version 2 examples](./v2-examples/js/src/site.js), 
or [site.js version 1 examples](./v1-examples/js/src/site.js) for more ways
to interact with the Site APIs. 


### Customer

The Customer APIs will allow you to manage information related to your customers. 
This includes information about the customers themselves, like contact details, 
as well as information about customer bookings. And for apps that are put in 
the hands of a customer, there are also APIs to manage customer sessions.

For instance, this code will attempt to authenticate a customer:

```js
    var companyHref = url.resolve(config.apiUri, 'company/' + config.companyId);

    function authenticateCustomer(jwtToken, username, password, done) {
        api.post(
            url.resolve(companyHref + '/', 'authenticate_customer'),
            {Authorization: 'JWT ' + jwtToken},
            {
                credential_type_code: 'password',
                username: username,
                password: password
            },
            done);
    }
``` 

### Appointment

The Appointment APIs allow you to manage appointments for customers. The 
appointment is where services, employees, rosters and customers come together,
so the Appointment APIs are never used in isolation.

Please refer to [appointment.js version 2 examples](./v2-examples/js/src/appointment.js), 
or [appointment.js version 1 examples](./v1-examples/js/src/appointment.js) for more
ways to interact with the Appointment APIs.

### Common questions and solutions in the API

Please refer here for a curated list of common questions that API 
developers ask, and example solutions. [Common tasks](./v2-examples/common)

#### Calling the Shortcuts API from the Java language

Please refer here for an end-to-end example of using the Shortcuts API 
from the Java language. [Java API Example](./v2-examples/java)

### Calling the Shortcuts API through the Shortcuts Widgets

You do not necessarily have to write code to call the Shortcuts APIs.
For many common tasks and processes, we provide the Shortcuts Widgets, 
that can be embedded in your website, and provide seamless access to 
the Shortcuts APIs to your website users.

_The widgets can be styled vi CSS to match your brand._

This example shows a basic workflow using the Shortcuts Widgets and Online 
Services with minimal changes from the widgets natural workflow order.
[Default Worklow using Widgets](./widgets-examples/default-workflow)


### Single Sign-on

The single sign-on facility is required when you want to use your own 
systems to authenticate customers, rather than relying on Shortcuts 
Online Services.
 

This example shows how the single sign-on process works. It is only 
intended for implementors of single sign-on. You can still use all
the capabilities in the Shortcuts API without implementing single
sign-on.

[Single Signon](./other/single-signon)

---
## Notes:

- When you develop request patterns that allow you to follow complex
business processes that are not covered in this example, we would love 
to know about them. Please consider forking this repository, and creating 
a [pull request](https://help.github.com/articles/about-pull-requests/)
if you would like to share your creations. 
- Please [email us](mailto:rob.barrett@shortcuts.com.au) 
if you have any questions about this example.



