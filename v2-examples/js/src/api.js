const https = require('https');
const http = require('http');
const url = require('url');
const log = require('./log.js');

var api = (function () {

    function request(method, href, headers, data, done) {

        var endpoint, options, request, requestContent;

        endpoint = url.parse(href);

        if (!headers) {
            headers = {};
        }

        options = {
            method: method,
            hostname: endpoint.hostname,
            path: endpoint.path,
            port: endpoint.port,
            headers: headers
        };

        if(method === 'POST') {
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
                var statusCode = response.statusCode;
                if (statusCode / 100 != 2) {
                    console.log('response is in error:', statusCode, response.req.method, response.req.path)
                }
                done(null, { status: statusCode, content: jsonContent || content });
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

        if(method === 'POST') {
            request.write(requestContent);
        }

        request.end();
    }

    function get(href, headers, done) {
        request('GET', href, headers, null, done);
    }

    function post(href, headers, data, done) {
        request('POST', href, headers, data, done);
    }

    function del(href, headers, done) {
        request('DELETE', href, headers, null, done);
    }

    return {
        get: get,
        post: post,
        del: del
    };
})();

module.exports = api;