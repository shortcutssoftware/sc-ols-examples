# Shortcuts Widgets THC Sample

Sample website implementation over the Shortcuts Widgets.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Running website using Node

1. Ensure that Node.JS and NPM are installed.

2. Install node packages

```
npm install
```

3. Run server

```
npm start
```

### Running website by static content delivery

This website has been designed to be able to be run independently of a dynamic server by copying the files to an accessible web location.  It is recommended that the website is not run from the file directory as this can cause issues with AJAX calls.

1. Copy all files other than the package.json and README.md files to a static web server.

2. Browse to the index.html file on your static web server.


## Stripe Upfront Payment Prerequisite 
1. Stripe Checkout javascript lib.
1. Stripe Account. This can be done at https://dashboard.stripe.com/register
1. The site is configured to accept Stripe as the payment provider. (Please contact Shortcuts Support to enable the site accepting the Stripe payment).


## How to add in Upfront payment support to webwidget

- The Stripe script tag will be loaded during runtime by the WebWidget script.

- In the webwidget config (where the OAuth secret key is defined), the following configuration must be added to the config, or the webwidget will not enable the upfront payment capability.

[example](js/config.js)

```
        authenticationUrl: 'https://pos-api.shortcutssoftware.com/authenticate',
        paymentGatewayUrl: 'https://7ah8al3ipi.execute-api.us-west-2.amazonaws.com/prod/stripe'
```

- On the [Tenant Console](https://console.shortcutssoftware.com), go to `BookMe Settings > Upfront Payment Settings`, and make sure Stripe is shown as the Upfront Payment provider. Click on `Edit General Settings > Connect with Stripe`. 
- Authorize Shortcuts to manage your Stripe payment. 
- Make sure Upfront Payment active is ticked. Save all the changes.
- Your client is now able to make upfront payment subject to the upfront payment rules in the Bookme settings.   
