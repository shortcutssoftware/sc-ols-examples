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
        it('should be able to search for availabilities by service category', function (done) {
            search.byServiceCategoryName(
                sharedState.access_token,
                'Hair Styling',
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
        it('should be able to search for availabilities by service name', function (done) {
            search.byServiceName(
                sharedState.access_token,
                'Blowdry',
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
        it('should be able to search for availabilities by service and employee', function (done) {
            search.byServiceAndEmployeeName(
                sharedState.access_token,
                'Blowdry',
                'Katie',
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