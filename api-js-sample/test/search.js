const assert = require('assert');
const search = require('../src/search.js');

describe('Search', function () {
    this.timeout(10000);

    describe('byServiceCategoryName()', function () {
        it('should be able to search for availabilities Beauty services', function (done) {
            search.byServiceCategoryName('Beauty', function (err, result) {
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
    });

    describe('byServiceName()', function () {
        it('should be able to search for availabilities of the Wax Leg service', function (done) {
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
    });

    
    describe('byServiceAndEmployeeName()', function () {
        it('should be able to search for availabilities of the Wax Leg service with Wendy', function (done) {
            search.byServiceAndEmployeeName('Wax Leg', 'Wendy', function (err, result) {
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
    });
});