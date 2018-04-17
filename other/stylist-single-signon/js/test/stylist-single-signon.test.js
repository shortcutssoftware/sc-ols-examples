const stylistCredentials = require('./example-stylist-credentials');
const stylistSingleSignon = require('../src/stylist-single-signon.js');

describe('Stylist Single Signon', function () {

    describe('get stylist credentials', function () {
        it('stylist credentials must be present for test', function (done) {
            expect(stylistCredentials.stylist_username).toBeDefined();
            expect(stylistCredentials.stylist_password).toBeDefined();
            expect(stylistCredentials.grant_type).toBeDefined();
            expect(stylistCredentials.oauth_consumer_key).toBeDefined();
            done();
        })
    });

    describe('getAuthenticationCookie()', function () {
        it('should return an error when called with empty incredentials', function (done) {
            stylistSingleSignon.getAuthenticationCookie(null, function (err, authenticationCookieValue) {
                expect(err).toBeDefined();
                expect(authenticationCookieValue).toBeUndefined();
                done();
            });
        });
        it('should return an oauth cookie when called with invalid credentials', function (done) {
            var badStylistCredentials = JSON.parse(JSON.stringify(stylistCredentials));
            badStylistCredentials.stylist_password = 'bogus';
            stylistSingleSignon.getAuthenticationCookie(badStylistCredentials, function (err, authenticationCookieValue) {
                expect(err).toBeDefined();
                expect(authenticationCookieValue).toBeUndefined();
                done();
            });
        })
        it('should return an oauth cookie when called with valid credentials', function (done) {
            stylistSingleSignon.getAuthenticationCookie(stylistCredentials, function (err, authenticationCookieValue) {
                expect(err).toEqual(null);
                expect(authenticationCookieValue).toEqual('foo');
                done();
            });
        })
    });
});