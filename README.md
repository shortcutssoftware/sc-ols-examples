# Shortcuts Code Examples

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

### API versions

Shortcuts Online Services APIs are primarily supported on the
`api.shortcutssoftware.io` domain. 

We will continue to support API solutions developed against the 
legacy  `pos.shortcutssoftware.com/webapi` domain, however, the 
emphasis of our research and development efforts is targeted at 
the  `api.shortcutssoftware.io` domain, because on it we are 
able to offer a far superior API experience.

You will see a mix of newer and legacy API examples in this 
repository. Examples will be marked as _(new)_ or _(legacy)_ 
to allow you to differentiate between them.

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

The biggest difference between the _(new)_ and the _(legacy)_ APIs 
is the way that requests are authenticated.

### JWT Authentication _(new)_

At this stage we support two modes of authentication:

#### Using on-premise details

Authenticate using a site installation id and a serial number,
issued when your site was set up and your on-premise software was
installed. This is what you do when you want to call the APIs as 
if they were being driven by your business, not by an individual. 
The capabilities that you will have if you authenticate this way 
are powerful, but they are not as configurable as credential-based 
capabilities.

#### Using OAuth credentials

Authenticate using [OAuth 1.0](https://en.wikipedia.org/wiki/OAuth)
credentials, issued when you request API user access from Shortcuts
Software Ltd, or when an individual user is created. These are what 
you use  when you want to call the APIs as if they were being called 
by an individual user (or community), where you might want to tailor 
the permissions granted to the API caller. The capabilities that 
you will have if you authenticate this way can be individually 
configured without affecting the operation of your on-premise 
software.

When you authenticate, you will be given a [JWT token](http://jwt.io)
which will give you access to the API for 30 minutes. The token expiry 
time is stored in the token itself, so you can proactively acquire
a new token when it approaches expiry if you wish. Tokens are cheap
and can be discarded and/or reacquired at will.

_Important:_ Each time you call the _(new)_ Shortcuts API you must 
supply a JWT token in the `Authorization` header.

Please take a look at the following classes to see examples of
the two types of authentication:

- [Using on-premise details](./v2-examples/java/src/main/java/com/shortcuts/example/java/authentication/JWTSerialNumberAuthenticationService.java)
- [Using oauth credentials](./v2-examples/java/src/main/java/com/shortcuts/example/java/authentication/JWTOAuthAuthenticationService.java)

### OAuth request signing _(legacy)_

When you use the _(legacy)_ Shortcuts Online Services API, you must sign
each request according to the [OAuth 1.0](https://en.wikipedia.org/wiki/OAuth)
specification.

OAuth credentials are issued when you request API user access from Shortcuts
Software Ltd, or when an individual user is created. These are what you use 
when you want to call the APIs as if they were being called by an individual 
user (or community), where you might want to tailor the permissions granted 
to the API caller.

Please take a look at the following example:

- [Using oauth credentials](./v1-examples/js/src/oauth.js)

_Important:_ Each time you call the _(legacy)_ Shortcuts API you must
sign the request.

### Comparison

The big differences between authentication for the _(legacy)_ and 
the _(new)_ APIs are:

- Using the _(legacy)_ API
  - You need to sign every request individually.
  - You can authenticate as a community, or as an individual user.
- Using the _(new)_ API with a JWT token
  - You only need to authenticate once (until the token expires).
  - You can authenticate as a Point Of Sales machine/as the business, 
  or as a community, or as an individual user. 

## API breakdown

API usage generally falls into one or more of the following categories.
For example, the most common business process is to create a booking. 
This can be done as follows:

1. Authenticate,
1. Search for services at a site,
1. Select a service,
1. Search for available appointments for that service,
1. Authenticate the client,
1. Create a booking for the client.

Following these steps will take you through some of the below categories.

*It is important to remember that you are not limited to just the
business processes in these examples.* It is entirely valid for
you to make calls to various APIs below, and then make bookings 
based on your own combinations of the data returned for your own
reasons. For example: you could search for all employees who are 
struggling to meet their `visit_count` kpi target, and prefer 
them when making appointments.  

### Company

The Company APIs are used to retrieve information about the business 
(the brand) from the Shortcuts API.

### Site

The Site APIs are used to retrieve information about the real physical
locations of the business through the Shortcuts API.

The Sites APIs will give you information about:

- Service categories and services,
- Employees,
- Pricing,
- Available appointments.

### Customer

The Customer APIs will allow you to manage information related to your customers. 
This includes information about the customers themselves, like contact details, 
as well as information about customer bookings. And for apps that are put in 
the hands of a customer, there are also APIs to manage customer sessions.

### Appointment

The Appointment APIs allow you to manage appointments for customers. The 
appointment is where services, employees, rosters and customers come together,
so the Appointment APIs are never used in isolation.

## Examples

### Shortcuts API Version 2

#### [Java API Example](./v2-examples/java)

An end-to-end example of using the Shortcuts API from the Java language.

### Shortcuts API Version 1

#### [Common tasks](./v1-examples/common)

APIs called when performing some common booking scenarios.

#### [Default Worklow](./widgets-examples/default-workflow)

Shows a basic workflow using the Shortcuts Widgets and Online 
Services with minimal changes from the widgets natural workflow order.

#### [Javascript API Example](./v1-examples/js)

Shows sample implementation against the API using JavaScript.

#### [Single Signon](./other/single-signon)

Shows how the single sign-on process works with Shortcuts 
Online Services. Intended for implementors of single sign-on.


---
## Notes:

- When you develop request patterns that allow you to follow complex
business processes that are not covered in this example, we would love 
to know about them. Please consider forking this repository, and creating 
a [pull request](https://help.github.com/articles/about-pull-requests/)
if you would like to share your creations. 
- Please [email us](mailto:rob.barrett@shortcuts.com.au) 
if you have any questions about this example.



