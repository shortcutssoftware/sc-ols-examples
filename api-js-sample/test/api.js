var assert = require('assert');
var config = require('../src/config.js');
var api = require('../src/api.js');
var url = require('url');

describe('API', function () {
    this.timeout(5000);
    
    describe('retrieveSiteDetails()', function () {
        it('should be able to retrieve site details', function (done) {

            api.retrieveSiteDetails(function (err, result) {
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