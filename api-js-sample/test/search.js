var expect = require('expect.js');
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

                expect(result.status).to.eql(200);
                expect(result.content.available_appointments).to.not.be(undefined);
                expect(result.content.available_appointments).to.be.an('array');
                expect(result.content.available_appointments).to.not.be.empty();

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

                expect(result.status).to.eql(200);
                expect(result.content.available_appointments).to.not.be(undefined);
                expect(result.content.available_appointments).to.be.an('array');
                expect(result.content.available_appointments).to.not.be.empty();

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

                expect(result.status).to.eql(200);
                expect(result.content.available_appointments).to.not.be(undefined);
                expect(result.content.available_appointments).to.be.an('array');
                expect(result.content.available_appointments).to.not.be.empty();

                done();
            })
        })
    });
});