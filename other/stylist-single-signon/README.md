# Stylist single sign-on

This folder contains example code for the _stylist single sign-on_ 
capability.

## Overview

_Stylist single sign-on_ refers to the capability where an organization
provides a mechanism for Shortcuts to call back to their own system in
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
passwords and credentials, we will have to provide a mechanism unique to
you that allows us to call back to you with stylist credentials in order
for you to validate them.

The next step is for the organization to put in place some code that will
drive the _Stylist single sign-on_ process from their end. 

The (simple) process is:

1. The organization will collect the stylist's credentials and supply them
to Shortcuts for validation.
    1. Shortcuts will call back to the organization to validate
    the supplied credentials  
    1. If this validation is successful then the organization will 
    receive 2 cookies that allow the stylist access to their 
    site in the Shortcuts Live environment.
1. The organization will make a request to the Shortcuts Live environment,
and supply the cookies obtained above. This will be enough to allow access
to a site.

It is possible to do this programmatically, as the following tests show: 
[stylist-single-signon.test.js](./js/test/stylist-single-signon.test.js)

## However

Due to security safeguards in different browsers, it is not possible to
set cookies for another domain (let's say domain B), while you are a web
page running in domain A.

If you are using a web page to perform the _Stylist single sign-on_ 
process, then you will have to do a little bit of extra legwork, so the
process described above becomes:

- Collect the stylist's credentials and supply them to Shortcuts for 
validation. This can be done by issuing a request as shown in 
[stylist-single-signon.js](./js/src/stylist-single-signon.js), the 
relevant call is:
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

- Take the object that is returned by this call. It contains the two
cookies that will enable you to access the Shortcuts Live environment.

*Note: because you are not on a webpage hosted by the Shortcuts Live 
environment, your browser will not allow you to set these cookies for
that environment. These cookies are issued to the `shortcutssoftware.com`
domain, and so will only be accepted by your browser if it is on a page
hosted on the `shortcutssoftware.com` domain*

- To enable this issuing of cookies to work, we use a mechanism whereby
the browser page in domain A (your domain) opens a new window, which
makes a GET request to domain B (`shortcutssoftware.com`). The response
to this request sets the cookie you need, and then the new window closes.

- The organization will open up a browser window for the Shortcuts Live 
environment. Since the cookies set in the temporary window were stored
on the `shortcutssoftware.com` domain, the stylist stylist will be able
to access to their site in the Shortcuts Live environment.

