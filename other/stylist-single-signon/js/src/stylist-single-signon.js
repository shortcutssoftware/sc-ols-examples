const request = require('request-promise');

var stylistSingleSignon = (function () {

    function getAuthenticationCookie(stylistCredentials, done) {
        if (!stylistCredentials
            || !stylistCredentials.stylist_username
            || !stylistCredentials.stylist_password
            || !stylistCredentials.oauth_consumer_key
            || !stylistCredentials.grant_type) {
            done(new Error('invalid stylist credentials object'))
            return;
        }
        var credentialPair = stylistCredentials.stylist_username + ':' + stylistCredentials.stylist_password;
        var encoded = btoa(credentialPair);
        var authorizationHeaderValue = 'Basic ' + encoded;
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
                done(new Error('authentication failed'));
                return;
            }
            console.log('successful authentication for stylist: %s', stylistCredentials.stylist_username)
            done(null, 'foo');
        }).catch(function (err) {
            console.log('error: %s', err);
            done(err)
        })
    }

    return {
        getAuthenticationCookie: getAuthenticationCookie
    }
})();

module.exports = stylistSingleSignon
