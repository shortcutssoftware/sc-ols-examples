const request = require('request-promise');

var stylistSingleSignon = (function () {

    function getOAuthData(stylistCredentials, done) {

        // 1. validate the stylist credentials object
        if (!stylistCredentials
            || !stylistCredentials.stylist_username
            || !stylistCredentials.stylist_password
            || !stylistCredentials.oauth_consumer_key
            || !stylistCredentials.grant_type) {
            done(new Error('invalid stylist credentials object'))
            return;
        }

        // 2. create an Authorization header by encoding the stylist credentials
        var credentialPair = stylistCredentials.stylist_username + ':' + stylistCredentials.stylist_password;
        var encoded = btoa(credentialPair);
        var authorizationHeaderValue = 'Basic ' + encoded;

        // 3. make a request to the signon server. note that the stylist
        // credentials are in the Authorization header, and there are two
        // values supplied in the form that is submitted. these values
        // help the signon server determine how to handle your stylist's
        // credentials
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

        }).then(function (response) {

            if (response.statusCode != 200) {
                // stylist was not authenticated
                throw new Error('authentication failed');
            }

            // stylist was authenticated. we are interested in the OAuth
            // tokens that were delivered by the signon server.
            console.log('successful authentication for stylist: %s', stylistCredentials.stylist_username)
            done(null, getOAuthDataFromResponse(response));

        }).catch(function (err) {

            // stylist was not authenticated
            console.log('error: %s', err);
            done(err)

        })
    }

    function getOAuthDataFromResponse(response) {
        var oAuthData = {};
        var setCookieHeaders = response.headers['set-cookie'];
        for (var i = 0; i < setCookieHeaders.length; i++) {
            var setCookieHeader = setCookieHeaders[i];
            console.log('header:', setCookieHeader);
            var headerChunks = setCookieHeader.split(/=|&|;/);
            if (!headerChunks
                || headerChunks.length < 5
                || headerChunks[0] != 'OAuth') {
                continue;
            }
            headerChunks.shift(); // not interested in the header name
            while (headerChunks.length > 1) {
                var name = headerChunks.shift();
                var value = headerChunks.shift();
                oAuthData[name] = value;
            }
        }
        if (Object.keys(oAuthData).length == 0) {
            throw new Error('unable to find OAuth data in response');
        }
        return oAuthData;
    }

    return {
        getOAuthData: getOAuthData
    }
})();

module.exports = stylistSingleSignon
