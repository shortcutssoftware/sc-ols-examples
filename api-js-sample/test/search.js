const assert = require('assert');
const search = require('../src/search.js');

describe('Search', function () {
    this.timeout(5000);

    describe('byServiceName()', function () {
        it('should be able to search for availabilities of the wax leg service', function (done) {
            search.byServiceName('Wax Leg', function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                if (result.status !== 200) {
                    assert.fail('Status Code:' + result.status);
                }
                assert.ok(result.content.available_appointments);
                done();
            })
        })
    })
});