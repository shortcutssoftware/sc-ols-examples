const request = require('request-promise');

var stylistSingleSignon = (function () {

    function getAuthCookies(stylistCredentials, done) {

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
            done(null, getAuthCookiesSetByResponse(response));

        }).catch(function (err) {

            // stylist was not authenticated
            console.log('error: %s', err);
            done(err)

        })
    }

    function getAuthCookiesSetByResponse(response) {
        var authCookies = {};
        var setCookieHeaders = response.headers['set-cookie'];
        for (var i = 0; i < setCookieHeaders.length; i++) {
            var setCookieHeader = setCookieHeaders[i];
            let cookieName = setCookieHeader.split('=')[0]
            if (cookieName === 'OAuth' || cookieName === '.ASPAUTH') {
                console.log('header:', setCookieHeader);
                authCookies[cookieName] = setCookieHeader;
            }
        }
        if (Object.keys(authCookies).length != 2) {
            throw new Error('unable to find auth cookies in response');
        }
        return authCookies;
    }

    return {
        getAuthCookies: getAuthCookies
    }
})();

module.exports = stylistSingleSignon
