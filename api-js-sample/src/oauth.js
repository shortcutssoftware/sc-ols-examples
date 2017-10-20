const randomInt = require('random-int');
const hmacSha1 = require('crypto-js/hmac-sha1')
const base64 = require('crypto-js/enc-base64')
const _ = require('underscore');

const config = require('./config.js')

var oauth = (function() {

    function randomString(length, chars) {
        var result = '';
        for (var i = length; i > 0; --i) result += chars[randomInt(0, chars.length)];
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
    
    function sign(method, url) {
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
        var nonce = (this.generateNonce || generateNonce)();
        var timestamp = (this.generateTimestamp || generateTimestamp)();
    
        // Generate the oauth parameters
        var oauthParams = [
            { key: 'oauth_consumer_key', value: encode(config.consumerKey) },
            { key: 'oauth_nonce', value: nonce },
            { key: 'oauth_signature_method', value: 'HMAC-SHA1' },
            { key: 'oauth_timestamp', value: timestamp },
            { key: 'oauth_token', value: encode(config.accessTokenKey) },
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
        var key = encode(config.consumerSecret) + '&' + encode(config.accessTokenSecret);
        var signature = hmacSha1(base, key).toString(base64);
    
        // Compile the OAuth Header
        var authHeader =
            'OAuth realm="' + url + '", ' +
            'oauth_consumer_key="' + encode(config.consumerKey) + '", ' +
            'oauth_token="' + encode(config.accessTokenKey) + '", ' +
            'oauth_nonce="' + nonce + '", ' +
            'oauth_timestamp="' + timestamp + '", ' +
            'oauth_signature_method="' + 'HMAC-SHA1' + '", ' +
            'oauth_version="' + '1.0' + '", ' +
            'oauth_signature="' + encode(signature) + '"';
    
        return authHeader;
    };
    return {
        sign: sign
    }
})();


module.exports = oauth
