# Stylist single sign-on

This folder contains example code for the _Stylist single sign-on_ 
feature.

## Overview

_Stylist single sign-on_ refers to the capability where an organization
provides a mechanism for Shortcuts to call back to their own systems in
order to validate a stylist's credentials, rather than using the Shortcuts
signon servers to validate the stylist's credentials.

This means that Shortcuts does not have to store or manage the stylist's 
password, and also allows the organization to provide a seamless path
for a stylist to access the Shortcuts Live environment without requiring
them to log on twice.

## How does it work?

The first step to making this feature work is to contact Shortcuts and 
arrange for the _Stylist single sign-on_ feature to be enabled for your
organization. Because each organization differs in the way that it manages
passwords and credentials, we will have to provide a pathway unique to
you that allows us to call back to you with stylist credentials in order
for you to validate them.

The next step is for the organization to put in place some code that will
drive the _Stylist single sign-on_ process from your end. 

The (simple) process is:

1. You will collect the stylist's credentials and supply them
to the Shortcuts signon server for validation.
    1. Shortcuts will call back to you to validate the supplied 
    credentials  
    1. If this validation is successful then you will 
    receive 2 cookies that allow the stylist access to their 
    site in the Shortcuts Live environment.
1. You will then make a request to the Shortcuts Live environment,
and supply the cookies obtained above. This will be enough to allow access
to a site.

It is possible to do this programmatically, as the following tests show: 
[stylist-single-signon.test.js](./js/test/stylist-single-signon.test.js)

An example test:
~~~ javascript
        it('must return auth cookies when called with valid credentials', function (done) {
            stylistSingleSignon.getAuthCookies(stylistCredentials, function (err, authCookies) {
                expect(err).toEqual(null);
                expect(authCookies).toBeDefined();
                expect(authCookies['OAuth']).toBeDefined();
                expect(authCookies['.ASPAUTH']).toBeDefined();
                sharedAuthCookies = authCookies;
                done();
            });
        });
~~~

The code that actually invokes the Shortcuts signon server in the
correct way is in [stylist-single-signon.js](./js/src/stylist-single-signon.js)
and can be freely altered or reused. The relevant call is:
~~~ javascript
     request({
     
                 method: 'POST',
                 uri: 'https://signon.shortcutssoftware.com/authenticate',
                 headers: {
                     'Authorization': authorizationHeaderValue
                 },
                 form: {
                     grant_type: stylistCredentials.grant_type,
                     oauth_consumer_key: stylistCredentials.oauth_consumer_key
                 },
                 resolveWithFullResponse: true
     
             })
~~~

## However

Due to security safeguards in different browsers, it is not possible to
reliably get and set cookies for another domain (`shortcutssoftware.com`), 
while you are on a web page hosted in your domain.

If you are using a web page to perform the _Stylist single sign-on_ 
process, then you will have to do a little bit of extra legwork, so the
process described above becomes:

- Collect the stylist's credentials and supply them via a _self-hosted
  server_ to Shortcuts for validation. 
  - We have provided an example implementation of such a server in 
    NodeJS for you at [server.js](./js/src/server.js), but you can 
    implement a this server in almost any technology. The 
    authentication is done by issuing a request as shown in 
    [stylist-single-signon.js](./js/src/stylist-single-signon.js).
  - We have also used this server to deliver the html page that is 
    used to gather the stylist's credentials. See [index.html](./js/src/index.html).
    You do not need to host these two things on the same server. 
    It is done this way in the example purely for convenience.

- Take the object that is returned by this call to the server. It 
  contains the two cookies that will enable you to access the 
  Shortcuts Live environment.

*Note: because you are not on a webpage hosted by the Shortcuts Live 
domain, your browser will not allow you to reliably get and 
set cookies for that domain. These cookies were issued to the 
`shortcutssoftware.com` domain, and so may only be accepted by your 
browser if it is on a page hosted on the `shortcutssoftware.com` domain*

- To enable this issuing of cookies to work, we use a mechanism whereby
the browser page in domain A (your domain) opens a new iframe on 
the `shortcutssoftware.com` domain, which makes a GET request to domain 
`shortcutssoftware.com` using these cookie values. Because this request 
is on the `shortcutssoftware.com` domain, the browser accepts 
the cookie that is issued. You may not have visibility of the contents
of the iframe and the cookies issued to that iframe from your web page,
but that is ok. The browser will remember those cookies when they are 
later required to be sent to the Shortcuts Live pages.

- You then open up a browser window for the Shortcuts Live 
environment. Since the cookies set in the iframe were stored
for the `shortcutssoftware.com` domain, the stylist will be able
to access to their site in the Shortcuts Live environment.

## How to run this example

1. Clone this repository, then change directory to the 
   `other/stylist-single-signon/js` folder.
1. Run the following command to install dependencies: `npm install`
1. Run the following command to execute all the tests: `npm test`.
   1. You can see the results of the tests printed out on the console
      as they run. 
   1. Alternatively, you can use the IDE of your choice
      to run the tests and debug them to get a better understanding
      of what individual steps are happening.
1. Run the following command to start the http server: `npm start`.
   1. You will see a message saying `Stylist single signon server listening on port 8080!`.
   1. You can then open up the following page in a browser to test
      the _Stylist single signon_ feature through the browser of your choice:
      [http://localhost:8080/index.html](http://localhost:8080/index.html).


## Notes:

- The example server implementation does not use HTTPS. Please ensure 
  that when you deliver a real implementation you only support HTTPS
  for any traffic where passwords are transmitted.
- The tests expect a file of the name `example-stylist-credentials.js` 
  that declares stylist credentials. Since the information in this file 
  is sensitive, please copy and use 
  [example-stylist-credentials-template.js](./js/test/example-stylist-credentials-template.js)
  as a guide to creating this file using your own stylist credentials.
- We have not applied any styling to the example web page, but in the
  case of the iframes, it is not necessary to display these, their only
  function is to cause the correct cookies to be issued on the correct 
  domain. You can safely hide or dispose of them.
- Please [email us](mailto:api-questions@shortcuts.com.au) 
  if you have any questions about this example.
