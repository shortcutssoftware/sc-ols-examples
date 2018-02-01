const expect = require('expect.js');
const search = require('../src/search.js');

describe('Search', function () {
    this.timeout(10000);

    describe('byServiceCategoryName()', function () {
        it('should be able to search for availabilities by service category', function (done) {
            search.byServiceCategoryName('Hair Styling', function (err, result) {
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
        it('should be able to search for availabilities by service', function (done) {
            search.byServiceName('Blowdry', function (err, result) {
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
        it('should be able to search for availabilities by service and employee', function (done) {
            search.byServiceAndEmployeeName('Blowdry', 'Katie', function (err, result) {
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