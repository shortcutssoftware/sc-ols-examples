const assert = require('assert');
const https = require('https');
const http = require('http');
const url = require('url');

const config = require('./config.js');
const oauth = require('./oauth.js');

var api = (function () {

    function apiRequest(method, path, done) {
        var endpoint, options, request;

        endpoint = url.parse(url.resolve(config.apiUri, path));

        options = {
            method: method,
            hostname: endpoint.hostname,
            path: endpoint.pathname,
            port: endpoint.port,
            headers: { 'Authorization': oauth.sign(method, url.format(endpoint)) }
        };

        apiRequestCallback = function (response) {
            var content;
            content = '';
            response.on('data', function (chunk) {
                return content += chunk;
            });
            response.on('end', function () {
                done(null, { status: response.statusCode, content: JSON.parse(content) });
            });

            request.on('error', function (err) {
                done(err);
            });
        }

        if (endpoint.protocol === 'http:') {
            request = http.request(options, apiRequestCallback);
        } else if (endpoint.protocol === 'https:') {
            request = https.request(options, apiRequestCallback);
        }
        else {
            done('Unknown protocol:' + endpoint.protocol)
        }

        request.end();
    }

    function retrieveSiteDetails(done) {
        apiRequest('GET', 'site/' + config.siteId, done);
    }

    return {
        retrieveSiteDetails: retrieveSiteDetails
    }

})();

module.exports = api;