const expect = require('expect.js');
const oauth = require('../src/oauth.js');

describe('Oauth', function() {

    describe('sign()', function() {
        it('should create a valid signature', function() {
            var authorization = oauth.sign('GET', 'https://pos.shortcutssoftware.com/webapi/site/30179?fields=all', {
                generateNonce: function() { return 'awruFGf4'; },
                generateTimestamp: function() { return '1508467466'; }
            });
            expect(authorization).to.eql('OAuth realm="https://pos.shortcutssoftware.com/webapi/site/30179", oauth_consumer_key="dYQ3ZMrZOBDG9mMCpUby", oauth_token="F096VkVTh3KpLj6PXMtM", oauth_nonce="awruFGf4", oauth_timestamp="1508467466", oauth_signature_method="HMAC-SHA1", oauth_version="1.0", oauth_signature="EezRGSnVC6%2FhhBGckpc%2FJ8SHV2c%3D"');
        })
    })
});