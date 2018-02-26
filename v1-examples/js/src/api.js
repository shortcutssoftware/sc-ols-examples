const https = require('https');
const http = require('http');
const url = require('url');
const oauth = require('./oauth.js');
const log = require('./log.js');

var api = (function () {

    function request(method, href, data, done) {

        method = method.toUpperCase();

        var endpoint, options, request, requestContent;
        endpoint = url.parse(href);
        options = {
            method: method,
            hostname: endpoint.hostname,
            path: endpoint.path,
            port: endpoint.port,
            headers: { 'Authorization': oauth.sign(method, url.format(endpoint)) }
        };

        if(method === 'POST' || method === 'PUT') {
            requestContent = JSON.stringify(data || {});
            options.headers['Content-Type'] = 'application/json';
            options.headers['Content-Length'] = Buffer.byteLength(requestContent);
        }

        function apiRequestCallback(response) {
            var content, jsonContent;

            content = '';
            response.on('data', function (chunk) {
                return content += chunk;
            });
            response.on('end', function () {
                try {
                    jsonContent = JSON.parse(content);
                    log.info('Json content: %s', JSON.stringify(jsonContent));
                } catch (e) {
                    log.info('Non-json content: %s', content);
                }
                done(null, { status: response.statusCode, content: jsonContent || content });
            });

            request.on('error', function (err) {
                log.error('Error: %s', err);
                done(err);
            });
        }

        log.info('Requesting: %s', url.format(endpoint));
        if (endpoint.protocol === 'http:') {
            request = http.request(options, apiRequestCallback);
        } else if (endpoint.protocol === 'https:') {
            request = https.request(options, apiRequestCallback);
        }
        else {
            done('Unknown protocol:' + endpoint.protocol)
        }

        if(method === 'POST' || method === 'PUT') {
            request.write(requestContent);
        }

        request.end();
    }

    function get(href, done) {
        request('GET', href, null, done);
    }

    function post(href, data, done) {
        request('POST', href, data, done);
    }

    function put(href, data, done) {
        request('PUT', href, data, done);
    }

    function del(href, done) {
        request('DELETE', href, null, done);
    }

    return {
        get: get,
        post: post,
        put: put,
        del: del
    };
})();

module.exports = api;