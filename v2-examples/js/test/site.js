const expect = require('expect.js');
const config = require('../src/config.js');
const site = require('../src/site.js');
const url = require('url');
const authenticate = require('../src/authenticate.js');

describe('Site', function () {
    this.timeout(5000);

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

    describe('retrieveEmployeeByEmployeeAlias', function () {
        it('should be able to retrieve Wendy', function (done) {
            site.retrieveEmployeeByEmployeeAlias(
                sharedState.access_token,
                'Wendy',
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.employees).to.not.be(undefined);
                    expect(result.content.employees).to.be.an('array');
                    expect(result.content.employees).to.not.be.empty();

                    done();
                })
        })
    });

    describe('retrieveSiteDetails()', function () {
        it('should be able to retrieve site details', function (done) {
            site.retrieveSiteDetails(
                sharedState.access_token,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content).to.not.be(undefined);
                    expect(result.content).to.be.an('object');
                    expect(result.content.href).to.not.be(undefined);
                    expect(result.content.href).to.be.an('string');
                    expect(result.content.href).to.eql(url.resolve(config.apiUri, 'site/' + config.siteId))

                    done();
                })
        })
    });

    describe('retrieveServiceCategories()', function () {
        it('should be able to retrieve service categories', function (done) {
            site.retrieveServiceCategories(
                sharedState.access_token,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }


                    expect(result.status).to.eql(200);
                    expect(result.content.service_categories).to.not.be(undefined);
                    expect(result.content.service_categories).to.be.an('array');
                    expect(result.content.service_categories).to.not.be.empty();

                    done();
                })
        })
    });

    describe('retrieveServicesByServiceCategoryName()', function () {
        it('should be able to retrieve beauty services', function (done) {
            site.retrieveServicesByServiceCategoryName(
                sharedState.access_token,
                'Beauty',
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.services).to.not.be(undefined);
                    expect(result.content.services).to.be.an('array');
                    expect(result.content.services).to.not.be.empty();

                    done();
                })
        })
    });

    describe('retrieveServicesByServiceName()', function () {
        it('should be able to retrieve wax leg service', function (done) {
            site.retrieveServicesByServiceName(
                sharedState.access_token,
                'Wax Leg',
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.services).to.not.be(undefined);
                    expect(result.content.services).to.be.an('array');
                    expect(result.content.services).to.not.be.empty();

                    done();
                })
        })
    });


    describe('retrieveServiceEmployeePricing()', function () {
        it('should be able to retrieve employee pricing for the Wax Leg service.', function (done) {
            site.retrieveServiceEmployeePricing(
                sharedState.access_token,
                'Wax Leg',
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.employees).to.not.be(undefined);
                    expect(result.content.employees).to.be.an('array');
                    expect(result.content.employees).to.not.be.empty();

                    done();
                })
        })
    });

    describe('retrieveEmployeeServicePricing', function () {
        it('should be able to retrieve service pricing for employee with alias of Wendy', function (done) {
            site.retrieveEmployeeServicePricing(
                sharedState.access_token,
                'Wendy',
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.services).to.not.be(undefined);
                    expect(result.content.services).to.be.an('array');
                    expect(result.content.services).to.not.be.empty();

                    done();
                })
        })
    });
});