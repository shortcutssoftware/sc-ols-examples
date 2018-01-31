const url = require('url');
const _ = require('underscore');
const api = require('./api');
const config = require('./config.js');
const oauth = require('./oauth.js');

var authenticate = (function () {

    var authenticateHref = url.resolve(config.apiUri, 'authenticate');

    function authenticate(done) {
        var signedAuthorizationHeader = oauth.sign('POST', url.format(authenticateHref));
        var authenticateRequest = { credential_type_code: 'oauth' };
        api.post(
            authenticateHref,
            { 'Authorization': signedAuthorizationHeader },
            authenticateRequest,
            done);
    }

    return {
        authenticate: authenticate
    }
})();

module.exports = authenticate;
