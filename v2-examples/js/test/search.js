const expect = require('expect.js');
const search = require('../src/search.js');
const authenticate = require('../src/authenticate.js');

describe('Search', function () {
    this.timeout(10000);

    var sharedState = {
        access_token: undefined
    };

    it('must authenticate', function (done) {
        authenticate.authenticate(function (err, result) {
            if (err) {
                done(err);
                return;
            }

            expect(result.status).to.eql(200);
            expect(result.content.access_token).to.not.be(undefined);
            sharedState.access_token = result.content.access_token;
            done();
        })
    });

    describe('byServiceCategoryName()', function () {
        it('should be able to search for availabilities Beauty services', function (done) {
            search.byServiceCategoryName(
                sharedState.access_token,
                'Beauty',
                function (err, result) {
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
            search.byServiceName(
                sharedState.access_token,
                'Wax Leg',
                function (err, result) {
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
            search.byServiceAndEmployeeName(
                sharedState.access_token,
                'Wax Leg',
                'Wendy',
                function (err, result) {
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