const expect = require('expect.js');
const config = require('../src/config.js');
const api = require('../src/api.js');
const url = require('url');

describe('API', function () {
    this.timeout(5000);

    describe('request()', function () {
        it('should be able to make basic request', function (done) {
            api.get(url.resolve(config.apiUri, 'site/' + config.siteId), function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                expect(result.status).to.eql(200);
                expect(result.content.href).to.eql(url.resolve(config.apiUri, 'site/' + config.siteId));
                done();
            })
        })
    })
});