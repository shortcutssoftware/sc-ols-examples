const oauth = require('../src/oauth.js');

describe('Oauth', function () {

    describe('sign()', function () {
        it('should create a valid signature', function () {
            let oAuthData = {
                OAuthAccessKey: 'G4atM6ij1EKiet8O',
                OAuthAccessSecret: 'GyMd0BtOv6fw6bH4',
                OAuthExpirationDate: '1524028500',
                OAuthConsumerKey: 'MQTBolOfuj13PrR3a4LR',
                OAuthConsumerSecret: 'LWn1GDSpzIhmY149uB9V'
            };
            // we hardcode nonce and timestamp so that the
            // assertions below continue to work in future
            let options = {
                generateNonce: function () {
                    return 'aBcDeFgH';
                },
                generateTimestamp: function () {
                    return '1523942655';
                }
            };
            var authorization = oauth.sign(
                'GET',
                'https://pos.shortcutssoftware.com/webapi/site/12345?fields=all',
                options,
                oAuthData);
            expect(authorization).toEqual('OAuth realm="https://pos.shortcutssoftware.com/webapi/site/12345", oauth_consumer_key="MQTBolOfuj13PrR3a4LR", oauth_token="G4atM6ij1EKiet8O", oauth_nonce="aBcDeFgH", oauth_timestamp="1523942655", oauth_signature_method="HMAC-SHA1", oauth_version="1.0", oauth_signature="zGld%2FthAmcfVPwMv7H7VQpeSHR8%3D"');
        })
    })
});