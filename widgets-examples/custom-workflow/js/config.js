define(function () {
    return {
        widgetBaseUri: 'd19ujuohqco9tx.cloudfront.net/webwidgets/7-20-6-37',
        apiUri: 'pos.shortcutssoftware.com/webapi',
        apiVersion: '7.20.6',
        culture: 'en-us',
        // IMPORTANT
        // In a production system, these should be provided by dynamic AJAX call, session cookie
        // or other dynamic process rather than in static content.
        // the values below are for a demonstration salon on Shortcuts Online Services.
        consumerKey: '',
        consumerSecret: '',
        accessTokenKey: '',
        accessTokenSecret: '',
        siteId: '',
        authenticationUrl: 'https://pos-api.shortcutssoftware.com/authenticate',
        paymentGatewayUrl: 'https://7ah8al3ipi.execute-api.us-west-2.amazonaws.com/prod/stripe'
    };
});