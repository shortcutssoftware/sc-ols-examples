const expect = require('expect.js');
const authenticate = require('../src/authenticate.js');
const config = require('../src/config.js');
const api = require('../src/api.js');
const url = require('url');

describe('API', function () {
    this.timeout(5000);

    describe('request()', function () {

        var sharedState = {
            access_token: undefined
        };

        it('must authenticate', function (done) {
            authenticate.authenticate(function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                expect(result.status).to.eql(200);
                expect(result.content.access_token).to.not.be(undefined);
                sharedState.access_token = result.content.access_token;
                done();
            })
        });

        it('authenticated request must return 200', function (done) {
            api.get(
                url.resolve(config.apiUri, 'site/' + config.siteId),
                { Authorization: 'JWT ' + sharedState.access_token },
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.href).to.eql(url.resolve(config.apiUri, 'site/' + config.siteId));
                    done();
                })
        });

        it('unauthenticated request must return 401', function (done) {
            api.get(
                url.resolve(config.apiUri, 'site/' + config.siteId),
                null,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(401);
                    done();
                })
        });

    })
});