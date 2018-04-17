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
drive the _Stylist single sign-on_ process from their end. This means that:

1. the organization will collect the stylist's credentials and supply them
to Shortcuts for validation.
    1. Shortcuts will call back to the organization to validate
    the supplied credentials  
    1. If this validation is successful then the organization will 
    receive a cookie that allows the stylist access to their 
    site in the Shortcuts Live environment.
1. The organization will open up a browser for the Shortcuts Live environment,
and supply the cookie obtained above. This will be enough to allow access
to a site.

## This example provides two implementations

Please contact Shortcuts if you need to implement this functionality 
in a technology not covered here and require further assistance.

### Javascript implementation


### Java implementation

