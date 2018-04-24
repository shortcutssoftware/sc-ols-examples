const stylistCredentials = require('./example-stylist-credentials');
const stylistSingleSignon = require('../src/stylist-single-signon.js');
const oauth = require('../src/oauth.js');
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

    var sharedOAuthData = undefined;
    var sharedOAuthHeader = undefined;
    var sharedOAuthQueryString = undefined;

    describe('getOAuthData()', function () {

        it('must return an error when called with empty credentials', function (done) {
            stylistSingleSignon.getOAuthData(null, function (err, oAuthData) {
                expect(err).toBeDefined();
                expect(oAuthData).toBeUndefined();
                done();
            });
        });

        it('must return an error when called with invalid credentials', function (done) {
            var badStylistCredentials = JSON.parse(JSON.stringify(stylistCredentials));
            badStylistCredentials.stylist_password = 'bogus';
            stylistSingleSignon.getOAuthData(badStylistCredentials, function (err, oAuthData) {
                expect(err).toBeDefined();
                expect(oAuthData).toBeUndefined();
                done();
            });
        });

        it('must return oauth data  when called with valid credentials', function (done) {
            stylistSingleSignon.getOAuthData(stylistCredentials, function (err, oAuthData) {
                expect(err).toEqual(null);
                expect(oAuthData).toBeDefined();
                // oauth data must be present
                expect(oAuthData.OAuthAccessKey).toBeDefined();
                expect(oAuthData.OAuthAccessSecret).toBeDefined();
                expect(oAuthData.OAuthExpirationDate).toBeDefined();
                sharedOAuthData = oAuthData;
                done();
            });
        });

        it('must return a valid OAuth header for the derived oAuthData', function (done) {
            expect(sharedOAuthData).toBeDefined();
            sharedOAuthData.OAuthConsumerKey = stylistCredentials.oauth_consumer_key;
            sharedOAuthData.OAuthConsumerSecret = stylistCredentials.oauth_consumer_secret;
            console.log('shared oauth data', sharedOAuthData);
            let oAuthHeader = oauth.signedHeader(
                'GET',
                stylistCredentials.site_url,
                {},
                sharedOAuthData
            );
            expect(oAuthHeader).toBeDefined();
            expect(oAuthHeader).toMatch(/^OAuth realm="https:\/\/pos\.shortcutssoftware\.com.*/);
            sharedOAuthHeader = oAuthHeader;
            done();
        });

        it('must return a valid response for the derived oAuth signature in the header', function (done) {
            expect(sharedOAuthHeader).toBeDefined();
            let requestUri = stylistCredentials.site_url;
            request(
                {
                    method: 'GET',
                    uri: requestUri,
                    headers: {
                        'Authorization': sharedOAuthHeader
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

        it('must return a valid OAuth query string for the derived oAuthData', function (done) {
            expect(sharedOAuthData).toBeDefined();
            sharedOAuthData.OAuthConsumerKey = stylistCredentials.oauth_consumer_key;
            sharedOAuthData.OAuthConsumerSecret = stylistCredentials.oauth_consumer_secret;
            console.log('shared oauth data', sharedOAuthData);
            let oAuthQueryString = oauth.signedQueryString(
                'GET',
                stylistCredentials.site_url,
                {},
                sharedOAuthData
            );
            expect(oAuthQueryString).toBeDefined();
            expect(oAuthQueryString).toMatch(/^.*\&oauth_signature=.*/);
            sharedOAuthQueryString = oAuthQueryString;
            done();
        });

        it('must return a valid response for the derived oAuth signature in the query string', function (done) {
            expect(sharedOAuthQueryString).toBeDefined();
            let requestUri = stylistCredentials.site_url + '?' + sharedOAuthQueryString;
            request(
                {
                    method: 'GET',
                    uri: requestUri,
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