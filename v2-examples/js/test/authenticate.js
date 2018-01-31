const expect = require('expect.js');
const config = require('../src/config.js');
const authenticate = require('../src/authenticate.js');
const _ = require('underscore');
const jwtDecode = require('jwt-decode');

describe('Appointment', function () {
    this.timeout(10000);

    var sharedState = {
        access_token: undefined
    };

    describe('Authentication operation', function () {
        it('should be able to authenticate using configured user', function (done) {
            authenticate.authenticate(function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                expect(result.status).to.eql(200);
                expect(result.content.access_token).to.not.be(undefined);
                expect(result.content.access_token).to.be.an('string');
                sharedState.access_token = result.content.access_token;

                var decoded = jwtDecode(sharedState.access_token);
                var roles = decoded.roles;
                var user = decoded.user;
                var exp = new Date();
                exp.setTime(decoded.exp * 1000); // jwt expiry is in seconds

                console.log('authenticated roles: ', roles);
                console.log('authenticated user: ', user);
                console.log('token expiration time: ', new Date(exp));

                done();
            })
        });
    });
});