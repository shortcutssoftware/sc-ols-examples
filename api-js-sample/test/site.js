const assert = require('assert');
const config = require('../src/config.js');
const site = require('../src/site.js');
const url = require('url');

describe('Site', function () {
    this.timeout(5000);

    describe('retrieveEmployeebyEmployeeAlias', function() {
        it('should be able to retrieve Wendy', function (done) {
            site.retrieveEmployeebyEmployeeAlias('Wendy', function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                if (result.status !== 200) {
                    assert.fail('Status Code:' + result.status);
                }
                assert.ok(result.content.employees);
                done();
            })
        })
    });

    describe('retrieveSiteDetails()', function () {
        it('should be able to retrieve site details', function (done) {
            site.retrieveSiteDetails(function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                if (result.status !== 200) {
                    assert.fail('Status Code:' + result.status);
                }

                assert.equal(url.resolve(config.apiUri, 'site/' + config.siteId), result.content.href);
                done();
            })
        })
    });

    describe('retrieveServiceCategories()', function () {
        it('should be able to retrieve service categories', function (done) {
            site.retrieveServiceCategories(function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                if (result.status !== 200) {
                    assert.fail('Status Code:' + result.status);
                }

                assert.equal(url.resolve(config.apiUri, 'site/' + config.siteId + '/service_categories'), result.content.href);
                done();
            })
        })
    });
    
    describe('retrieveServicesByServiceCategoryName()', function () {
        it('should be able to retrieve beauty services', function (done) {
            site.retrieveServicesByServiceCategoryName('Beauty', function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                if (result.status !== 200) {
                    assert.fail('Status Code:' + result.status);
                }
                assert.ok(result.content.services);
                done();
            })
        })
    });
    
    describe('retrieveServicesByServiceName()', function () {
        it('should be able to retrieve wax leg service', function (done) {
            site.retrieveServicesByServiceName('Wax Leg', function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                if (result.status !== 200) {
                    assert.fail('Status Code:' + result.status);
                }
                assert.ok(result.content.services);
                done();
            })
        })
    });

    
    describe('retrieveServiceEmployeePricing()', function () {
        it('should be able to retrieve wax leg service', function (done) {
            site.retrieveServiceEmployeePricing('Wax Leg', function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                if (result.status !== 200) {
                    assert.fail('Status Code:' + result.status);
                }
                assert.ok(result.content.employees);
                done();
            })
        })
    });

    describe('retrieveEmployeeServicePricing', function() {
        it('should be able to retrieve Wendy', function (done) {
            site.retrieveEmployeeServicePricing('Wendy', function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                if (result.status !== 200) {
                    assert.fail('Status Code:' + result.status);
                }
                assert.ok(result.content.services);
                done();
            })
        })
    });
});