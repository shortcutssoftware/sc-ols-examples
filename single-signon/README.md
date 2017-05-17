
# Shortcuts Example: Single Sign On

A simple example of how to implement a single signon solution that works with Shortcuts Online Services.

## How it works

This is the Shortcuts single signon component diagram:

![Shortcuts single signon component diagram](images/single-signon-components.png)

It's describing the components required to make single signon work with Shortcuts Online Services.
The diagram is generic because it comes from a design of the single signon solution based on open
architectural standards. This example will show you how the various parts of this diagram need to 
behave and interact with each other, and how to implement them.

There are 4 parts _of which you will have to implement only 2_. You can use this example as a starting 
point for your implementation, by changing it to carry out real authentication using your existing systems.

The component parts are:

### 1. The Salon Authorization Server
 
_You will need to implement this component._

The sample implementation of this component is here:
[SalonAuthorizationServer.java](src/main/java/com/shortcuts/example/single_signon/salon/SalonAuthorizationServer.java) 

Essentially this component provides an endpoint where a customer (using a web browser or an app) supplies some
credentials for authentication. You will need to implement the actual authentication. The important step is that
after you have successfully authenticated the customer, you issue a token and store this token for retrieval later.

Along with this token, you will need to store the following information (for later transmission to Shortcuts):

~~~ json
{
  "first_name" : "Orville",
  "last_name" : "Wright",
  "email" : "orville@example.com"
}
~~~

### 2. The Salon Resource Server

_You will need to implement this component._

The sample implementation of this component is here:
[SalonResourceServer.java](src/main/java/com/shortcuts/example/single_signon/salon/SalonResourceServer.java) 

This component provides an endpoint that takes a token (obtained in 1 above) and checks to see if it valid.
The token is supplied in the `Authorization` header of the request.

If the token is valid, then a response containing the information above is returned.

That's all you need to implement. However, there are a few additional things that you will need to do:

1. Get valid OAuth credentials to interact with Shortcuts Online Services
2. Let Shortcuts know where your Salon Resource Server (2) is, so that we can use it to validate tokens.


## This is the sequence of events

| Step | Who       | What |
|------|-----------|------|
| 1    | You       | Authenticate your customer against the Salon Authorization Server, storing the response (customer token) |
| 2    | You       | Call the `authenticate_customer` endpoint in Shortcuts Online Services, supplying the customer token |
| 3    | Shortcuts | Calls your Salon Resource Server using the customer token |
| 4    | You       | Return customer information if the token is valid |
| 5    | Shortcuts | Create a session for the returned customer details |

That's essentially all there is to it. There are a few more details that need to be worked through:

When you call the `authenticate_customer` endpoint you will still need to authenticate using the OAuth 
credentials that were issued to your organization. We will only attempt the callback to your Salon Resource
Server if these credentials are valid. You supply your OAuth credentials in the `Authorization` header as usual, 
and you supply the customer token in the request body, formatted as a JSON payload that looks like this:

~~~ json
{
  "credential_type_code": "access_token",
  "token_type": "<token_type>",
  "access_token": "<token>"
}
~~~

The `<token_type>` is a string value that is agreed between you and Shortcuts when you notify us of the location
of your Salon Resource Server. This is often a string like `"dev"` or `"prod"`. Registering multiple token types 
allows you to specify multiple Salon Resource Servers for different uses.

When we call your Salon Resource Server, we will use the HTTP `GET` verb. You can see in the example that the
important information in this request is in the `Authorization` header. Don't confuse the authorization header 
that you send us (which contains OAuth credentials) with the authorization header that we send to your Salon 
Resource Server (which contains a string that looks like this: `"Bearer <token>"`)


## How to run the example

The sample consists of two HTTP servers. The first server listens on port 8080, and has implementations of 
the Salon Authorization Server endpoint, and the Salon Resource Server endpoint as described above. 

The second server listens on port 9090, and contains an implementation of the Shortcuts 
`authenticate_customer` endpoint. The sample implementation is here:
[AuthenticateCustomerServer.java](src/main/java/com/shortcuts/example/single_signon/shortcuts/AuthenticateCustomerServer.java) 

You can regard the servers as a 'Salon' server (on 8080), and 'Shortcuts' server (on 9090).

The example is coded in Java, using Spring Boot and gradle. This is not important though - you can implement
your own versions of these servers in any language as long as you match the message format and the HTTP verbs
that are used here.

To run the example:

1. install [Java 8](https://www.oracle.com/java/index.html) and [Gradle 3.5](https://gradle.org/) or above
1. clone the repository
1. change directory into the cloned repository: `cd <repository folder>`
1. build the example: `gradle clean build`
1. start the servers: `java -jar build/libs/single_signon-1.0-SNAPSHOT.jar`

At this stage you should see that the servers are listening on port 8080 and 9090. You can then use an API
testing tool (like [Postman](https://www.getpostman.com/)) to execute the following requests:


#### 1. Authorize a customer using the Salon Authorization Server

~~~
POST /authenticate HTTP/1.1
Host: localhost:8080

{
    "firstName": "Orville",
    "lastName": "Wright",
    "email": "orville@example.com",
    "someOtherProperty": "qwertyuiop"
}
~~~

which will return a token:

~~~
77245d1a-49f7-40e6-aa93-6313c43b1ade
~~~

#### 2. Call the Shortcuts `authenticate_customer` endpoint with the customer token

~~~
POST /authenticate_customer HTTP/1.1
Host: localhost:9090
Authorization: OAuth foo

{
  "credential_type_code": "access_token",
  "token_type": "any",
  "access_token": "77245d1a-49f7-40e6-aa93-6313c43b1ade"
}
~~~

_NB: don't forget to send some OAuth credentials in the `Authorization` header._

The Shortcuts endpoint will return the following response:

~~~
in real life this body would contain a Shortcuts customer session response
~~~

#### 3. If you would like to see what is happening in the Salon Resource Server, you can manually execute the following 

~~~
GET /validate HTTP/1.1
Host: localhost:8080
Authorization: Bearer  77245d1a-49f7-40e6-aa93-6313c43b1ade
~~~

which will return the following response:

~~~
{
  "first_name" : "Orville",
  "last_name" : "Wright",
  "email" : "orville@example.com"
}
~~~

---

Please contact us if you have any questions about this example.



