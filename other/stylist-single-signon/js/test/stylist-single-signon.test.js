const stylistCredentials = require('./example-stylist-credentials');
const stylistSingleSignon = require('../src/stylist-single-signon.js');
const request = require('request-promise');

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

    var sharedAuthCookies = undefined;

    describe('getAuthCookies()', function () {

        it('must return an error when called with empty credentials', function (done) {
            stylistSingleSignon.getAuthCookies(null, function (err, authCookies) {
                expect(err).toBeDefined();
                expect(authCookies).toBeUndefined();
                done();
            });
        });

        it('must return an error when called with invalid credentials', function (done) {
            var badStylistCredentials = JSON.parse(JSON.stringify(stylistCredentials));
            badStylistCredentials.stylist_password = 'bogus';
            stylistSingleSignon.getAuthCookies(badStylistCredentials, function (err, authCookies) {
                expect(err).toBeDefined();
                expect(authCookies).toBeUndefined();
                done();
            });
        });

        it('must return auth cookies when called with valid credentials', function (done) {
            stylistSingleSignon.getAuthCookies(stylistCredentials, function (err, authCookies) {
                expect(err).toEqual(null);
                expect(authCookies).toBeDefined();
                expect(authCookies['OAuth']).toBeDefined();
                expect(authCookies['.ASPAUTH']).toBeDefined();
                sharedAuthCookies = authCookies;
                done();
            });
        });

        it('must return a valid response from the site_url when called with the correct auth cookies', function (done) {
            expect(sharedAuthCookies).toBeDefined();

            // sharedAuthCookies contains the 'Set-Cookie' headers
            // returned from the signon server when we performed the
            // stylist single signon.
            //
            // this test shows how to extract the cookie values, and
            // use them in a programmatic request to Shortcuts live.

            var cookieString = '';
            Object.keys(sharedAuthCookies).forEach(function (key) {
                console.log('adding the [%s] cookie', key);
                let setCookieString = sharedAuthCookies[key];
                let setCookieParts = setCookieString.split(';');
                let cookieNameAndValue = setCookieParts[0];
                let p = cookieNameAndValue.indexOf('=');
                let cookieValue = cookieNameAndValue.substr(p + 1);
                if (cookieString.length > 0) {
                    if (!cookieString.match(/^.*; +$/)) {
                        cookieString += '; ';
                    }
                }
                cookieString += (key + '=' + cookieValue + '; ');
            });
            request(
                {
                    method: 'GET',
                    uri: stylistCredentials.site_url,
                    headers: {
                        'Cookie': cookieString
                    },
                    resolveWithFullResponse: true,
                    followRedirect: false
                }
            ).then(function (response) {
                if (response.statusCode != 200) {
                    throw new Error('unable to open site: ' + stylistCredentials.site_url);
                }
                console.log('opened site after stylist single signon: ' + stylistCredentials.site_url)
                done();
            }).catch(function (err) {
                done(err);
            })
        });

    });
});