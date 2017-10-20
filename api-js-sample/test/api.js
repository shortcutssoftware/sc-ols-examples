var assert = require('assert');
var config = require('../src/config.js');
var api = require('../src/api.js');
var url = require('url');

describe('API', function () {
    this.timeout(5000);

    describe('request()', function () {
        it('should be able to make basic request', function (done) {
            api.get(url.resolve(config.apiUri, 'site/' + config.siteId), function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                assert.equal(200, result.status );
                assert.equal(url.resolve(config.apiUri, 'site/' + config.siteId), result.content.href);

                done();
            })
        })
    })
});