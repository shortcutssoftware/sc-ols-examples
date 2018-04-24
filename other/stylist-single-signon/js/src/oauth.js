const randomInt = require('random-int');
const CryptoJS = require("crypto-js");
const _ = require('underscore');
const querystring = require('querystring');

var oauth = (function () {

    function randomString(length, chars) {
        var result = '';
        for (var i = length; i > 0; --i) {
            result += chars[randomInt(0, chars.length)];
        }
        return result;
    }

    function generateNonce() {
        return randomString(8, '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');
    }

    function generateTimestamp() {
        return Math.floor((new Date()).getTime() / 1000);
    }

    function encode(value) {
        if (value == null) {
            return '';
        }
        if (value instanceof Array) {
            var e = '';
            for (var i = 0; i < value.length; ++value) {
                if (e != '') e += '&';
                e += encode(value[i]);
            }
            return e;
        }
        value = encodeURIComponent(value);
        value = value.replace(/\!/g, "%21");
        value = value.replace(/\*/g, "%2A");
        value = value.replace(/\'/g, "%27");
        value = value.replace(/\(/g, "%28");
        value = value.replace(/\)/g, "%29");
        return value;
    }

    function signedHeader(method, url, options, oAuthData) {
        var options = _.extend({}, options);
        // Parse the URL into parts
        // Note that we are currently assuming that the url is already in canonical format, i.e. standardized scheme, port and domain.
        var urlParts = url.split('?');
        var url = urlParts[0];
        var query = urlParts[1];
        var queryParams = [];
        if (query) {
            queryParams = _.map(query.split('&'), function (param) {
                var paramParts = param.split('=');
                return {
                    key: encode(decodeURIComponent(paramParts[0])),
                    value: encode(decodeURIComponent(paramParts[1]))
                };
            });
        }

        // Generate anti-replay values
        var nonce = (options.generateNonce || generateNonce)();
        var timestamp = (options.generateTimestamp || generateTimestamp)();

        // Generate the oauth parameters
        var oauthParams = [
            { key: 'oauth_consumer_key', value: encode(oAuthData.OAuthConsumerKey) },
            { key: 'oauth_nonce', value: nonce },
            { key: 'oauth_signature_method', value: 'HMAC-SHA1' },
            { key: 'oauth_timestamp', value: timestamp },
            { key: 'oauth_token', value: encode(oAuthData.OAuthAccessKey) },
            { key: 'oauth_version', value: '1.0' }
        ];

        // Combine in the query paramaters and sort them.
        var sortedParams = _.union(queryParams, oauthParams).sort(function (left, right) {
            if (left.key < right.key) {
                return -1;
            } else if (left.key > right.key) {
                return 1;
            } else if (left.value < right.value) {
                return -1;
            } else if (left.value > right.value) {
                return 1;
            } else {
                return 0;
            }
        });

        // Format the parameters back into a single string.
        var formattedParams = _.map(sortedParams, function (param) {
            return param.key + '=' + param.value;
        }).join('&');

        // Calculate the OAuth Signature
        var base = method + '&' + encode(url) + '&' + encode(formattedParams);
        var key = encode(oAuthData.OAuthConsumerSecret) + '&' + encode(oAuthData.OAuthAccessSecret);
        var signatureBytes = CryptoJS.HmacSHA1(base, key);
        var signature = signatureBytes.toString(CryptoJS.enc.Base64);

        // Compile the OAuth Header
        var authHeader =
            'OAuth realm="' + url + '", ' +
            'oauth_consumer_key="' + encode(oAuthData.OAuthConsumerKey) + '", ' +
            'oauth_token="' + encode(oAuthData.OAuthAccessKey) + '", ' +
            'oauth_nonce="' + nonce + '", ' +
            'oauth_timestamp="' + timestamp + '", ' +
            'oauth_signature_method="' + 'HMAC-SHA1' + '", ' +
            'oauth_version="' + '1.0' + '", ' +
            'oauth_signature="' + encode(signature) + '"';

        return authHeader;
    }

    function signedQueryString(method, url, options, oAuthData) {
        var options = _.extend({}, options);
        // Parse the URL into parts
        // Note that we are currently assuming that the url is already in canonical format, i.e. standardized scheme, port and domain.
        var urlParts = url.split('?');
        var url = urlParts[0];
        var query = urlParts[1];
        var queryParams = [];
        if (query) {
            queryParams = _.map(query.split('&'), function (param) {
                var paramParts = param.split('=');
                return {
                    key: encode(decodeURIComponent(paramParts[0])),
                    value: encode(decodeURIComponent(paramParts[1]))
                };
            });
        }

        // Generate anti-replay values
        var nonce = (options.generateNonce || generateNonce)();
        var timestamp = (options.generateTimestamp || generateTimestamp)();

        // Generate the oauth parameters
        var oauthParams = [
            { key: 'oauth_consumer_key', value: encode(oAuthData.OAuthConsumerKey) },
            { key: 'oauth_nonce', value: nonce },
            { key: 'oauth_signature_method', value: 'HMAC-SHA1' },
            { key: 'oauth_timestamp', value: timestamp },
            { key: 'oauth_token', value: encode(oAuthData.OAuthAccessKey) },
            { key: 'oauth_version', value: '1.0' }
        ];

        // Combine in the query paramaters and sort them.
        var sortedParams = _.union(queryParams, oauthParams).sort(function (left, right) {
            if (left.key < right.key) {
                return -1;
            } else if (left.key > right.key) {
                return 1;
            } else if (left.value < right.value) {
                return -1;
            } else if (left.value > right.value) {
                return 1;
            } else {
                return 0;
            }
        });

        // Format the parameters back into a single string.
        var formattedParams = _.map(sortedParams, function (param) {
            return param.key + '=' + param.value;
        }).join('&');

        // Calculate the OAuth Signature
        var base = method + '&' + encode(url) + '&' + encode(formattedParams);
        var key = encode(oAuthData.OAuthConsumerSecret) + '&' + encode(oAuthData.OAuthAccessSecret);
        var signatureBytes = CryptoJS.HmacSHA1(base, key);
        var signature = signatureBytes.toString(CryptoJS.enc.Base64);

        // Compile the query string containing OAuth parameters
        var resultingQueryStringParameters = {};
        for (var i = 0; i < sortedParams.length; i++) {
            var parameter = sortedParams[i];
            resultingQueryStringParameters[parameter.key] = parameter.value;
        }
        resultingQueryStringParameters['oauth_signature'] = signature;
        return querystring.stringify(resultingQueryStringParameters);
    };
    return {
        signedHeader: signedHeader,
        signedQueryString: signedQueryString
    }
})();

module.exports = oauth
