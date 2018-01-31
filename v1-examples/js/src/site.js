const url = require('url');
const _ = require('underscore');
const api = require('./api');
const config = require('./config.js');

var site = (function () {
    var siteHref = url.resolve(config.apiUri, 'site/' + config.siteId)

    // Retrieving the site details
    function retrieveSiteDetails(done) {
        api.get(siteHref, done);
    }

    // Retrieving the service categories available at the site - ensuring we only list those with customer bookable services currently active in the site catalog.
    // Only interested in the names and resource URIs.
    function retrieveServiceCategories(done) {
        api.get(url.resolve(siteHref + '/', 'service_categories?is_active=true&is_bookable=true&is_customer_bookable=true&fields=display,href'), done);
    }


    // Retrieving services within the service category with name {serviceCategoryName}
    function retrieveServicesByServiceCategoryName(serviceCategoryName, done) {
        retrieveServiceCategories(function (err, result) {
            var serviceCategoryHref;
            if (err) {
                done(err);
                return;
            }

            serviceCategoryHref = _.findWhere(result.content.service_categories, { 'display_name': serviceCategoryName }).href;

            api.get(url.resolve(serviceCategoryHref + '/', 'services?is_active=true&is_bookable=true&is_customer_bookable=true&fields=display,href'), done);
        });
    }

    // Retrieving services with name {serviceName}
    function retrieveServicesByServiceName(serviceName, done) {
        api.get(url.resolve(siteHref + '/', 'services?search=' + serviceName + '&is_active=true&is_bookable=true&is_customer_bookable=true&fields=display,href'), done);
    }

    // Retrieving employee by alias/name {alias}
    function retrieveEmployeeByEmployeeAlias(employeeAlias, done) {
        api.get(url.resolve(siteHref + '/', 'employees?search=' + employeeAlias + '&is_active=true&is_bookable=true&is_customer_bookable=true&fields=display,href'), done);
    }

    function retrieveServiceEmployeePricing(serviceName, done) {
        retrieveServicesByServiceName(serviceName, function (err, result) {
            var serviceHref;
            if (err) {
                done(err);
                return;
            }
            serviceHref = result.content.services[0].href;
            api.get(url.resolve(serviceHref + '/', 'employee_pricing?is_active=true&is_bookable=true&is_customer_bookable=true'), done);
        });
    }

    function retrieveEmployeeServicePricing(employeeAlias, done) {
        retrieveEmployeeByEmployeeAlias(employeeAlias, function (err, result) {
            var employeeHref;
            if (err) {
                done(err);
                return;
            }
            employeeHref = result.content.employees[0].href;
            api.get(url.resolve(employeeHref + '/', 'service_pricing?is_active=true&is_bookable=true&is_customer_bookable=true'), done);
        });
    }

    return {
        retrieveSiteDetails: retrieveSiteDetails,
        retrieveServiceCategories: retrieveServiceCategories,
        retrieveServicesByServiceCategoryName: retrieveServicesByServiceCategoryName,
        retrieveServicesByServiceName: retrieveServicesByServiceName,
        retrieveEmployeeByEmployeeAlias: retrieveEmployeeByEmployeeAlias,
        retrieveServiceEmployeePricing: retrieveServiceEmployeePricing,
        retrieveEmployeeServicePricing: retrieveEmployeeServicePricing
    }
})();

module.exports = site;