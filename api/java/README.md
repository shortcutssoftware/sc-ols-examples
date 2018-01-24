
# Shortcuts Example: API usage with the [Java language](http://java.oracle.com).

An example of how to work with the Shortcuts API, and drive Shortcuts 
Online Services. Various common business processes are covered in this
example. More will be added over time, and we welcome contributions.

The interactions in this example make use of the individual APIs detailed 
at [Shortcuts API](http://www.shortcutssoftware.io/developer/). 
Each API used is covered in a unit test, so you can debug through 
server-side code without starting up the entire application server. 

If you browse through the API documentation above you will see 
detailed information about how to call each of the individual 
APIs that give access to Shortcuts Online Services. These examples,
however, start you off with working code that shows the way that 
individual APIs work together, and how they can be called in 
sequences that implement common business processes.

## Want to go straight to the implementation?

[Scenarios](#scenarios)

## How it works:

### Authentication

The first thing you need to do to use the Shortcuts API is to 
authenticate. At this stage we support two modes of authentication:

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
Software Ltd. This is what you do when you want to call the APIs 
as if they were being called by an individual user (or community),
where you might want to tailor the permissions granted to the
API caller. The capabilities that you will have if you authenticate
this way can be individually configured without affecting the
operation of your on-premise software.

When you authenticate, you will be given a [JWT token](http://jwt.io)
which will give you access to the API for 30 minutes. The token expiry 
time is stored in the token itself, so you can proactively acquire
a new token when it approaches expiry if you wish. Tokens are cheap
and can be discarded and/or reacquired at will.

_Important:_ Each time you call the Shortcuts API you must 
supply a JWT token in the `Authorization` header.

If you fail to authenticate, you will receive an http status 
code of `401 Unauthorized` when you call the Shortcuts APIs.

Please take a look at the following classes to see examples of
the two types of authentication:

- [Using on-premise details](./src/main/java/com/shortcuts/example/java/authentication/JWTSerialNumberAuthenticationService.java)
- [Using oauth credentials](./src/main/java/com/shortcuts/example/java/authentication/JWTOAuthAuthenticationService.java)

### Usage

After you authenticate, all that you have to do to invoke an API is
to make an HTTP request to the API endpoint. The HTTP request should
be built up with a few characteristics, all of which will be familiar 
to you as an API developer:

1. HTTP method must be specified to match the expected method in [the documentation](http://www.shortcutssoftware.io/developer/).
1. HTTP URI must be specified to match the documentation:
    1. path parameters are mandatory,
    1. query string parameters are optional.
1. the request body (where required) must match the json schema in the documentation.
1. the `Authorization` header must contain the JWT token (acquired above).

After that, it is a matter of submitting the correct requests in the correct
sequence. This is easy to do in most cases, as this example will show you. 
We anticipate that you will not need any in-depth help until you start to
implement business processes that are unique to the way you use the
Shortcuts Online Services.

Please take a look at the following classes to see the simplest example of
calling the Shortcuts API:

- [GetSitesService.java](./src/main/java/com/shortcuts/example/java/services/site/GetSitesService.java)
- [GetSitesServiceTest.java](./src/test/java/com/shortcuts/example/java/services/site/GetSitesServiceTest.java)
 
## Scenarios

Some common scenarios encountered when using the Shortcuts APIs.

### Client knows they want a specific service in a specific date/time window but doesn't know who with.

### Client wants category of service (e.g. massage) in a specific date/time window but doesn't know which kind of massage or who with.

### Client wants specific service with specific stylist/therapist but doesn't know available times/dates or gives range of date/time.

### Client wants specific service within time/date window, wants to choose price band, doesn't know who with.

### Client to be able to cancel booking before cancellation period expiration/cut off.


---

When you develop request patterns that allow you to follow complex
business processes that are not covered in this example, we would love 
to know about them. Please consider forking this repository, and creating 
a [pull request](https://help.github.com/articles/about-pull-requests/)
if you would like to share your creations. 

---

Please [email us](mailto:rob.barrett@shortcuts.com.au) 
if you have any questions about this example.



