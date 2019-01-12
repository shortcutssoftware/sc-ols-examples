define(function () {
    return {
        widgetBaseUri: 'd19ujuohqco9tx.cloudfront.net/webwidgets/7-17-30-50052',
        apiUri: 'pos.shortcutssoftware.com/webapi',
        apiVersion: '7.17.30',
        culture: 'en-us',
        // IMPORTANT
        // In a production system, these should be provided by dynamic AJAX call, session cookie
        // or other dynamic process rather than in static content.
        // the values below are for a demonstration salon on Shortcuts Online Services.
        consumerKey: 'dYQ3ZMrZOBDG9mMCpUby',
        consumerSecret: '5JPeuazfjc9qJjNXktNV',
        accessTokenKey: 'F096VkVTh3KpLj6PXMtM',
        accessTokenSecret: 'RQyEvU3bINqvTK3G4vXE',
        siteId: '30179',
        authenticationUrl: 'https://pos-api.shortcutssoftware.com/authenticate',
        paymentGatewayUrl: 'https://7ah8al3ipi.execute-api.us-west-2.amazonaws.com/prod/stripe'
    };
});