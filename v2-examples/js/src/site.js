const url = require('url');
const _ = require('underscore');
const api = require('./api');
const config = require('./config.js');

var site = (function () {
    var siteHref = url.resolve(config.apiUri, 'site/' + config.siteId)

    // Retrieving the site details
    function retrieveSiteDetails(jwtToken, done) {
        api.get(
            siteHref,
            {Authorization: 'JWT ' + jwtToken},
            done);
    }

    // Retrieving the service categories available at the site - ensuring we only list those with customer bookable services currently active in the site catalog.
    // Only interested in the names and resource URIs.
    function retrieveServiceCategories(jwtToken, done) {
        api.get(
            url.resolve(siteHref + '/', 'service_categories?is_active=true&is_bookable=true&is_customer_bookable=true&fields=display,href'),
            {Authorization: 'JWT ' + jwtToken},
            done);
    }


    // Retrieving services within the service category with name {serviceCategoryName}
    function retrieveServicesByServiceCategoryName(jwtToken, serviceCategoryName, done) {
        retrieveServiceCategories(
            jwtToken,
            function (err, result) {
                var serviceCategoryHref;
                if (err) {
                    done(err);
                    return;
                }

                serviceCategoryHref = _.findWhere(result.content.service_categories, {'display_name': serviceCategoryName}).href;

                api.get(
                    url.resolve(serviceCategoryHref + '/', 'services?is_active=true&is_bookable=true&is_customer_bookable=true&fields=display,href'),
                    { Authorization: 'JWT ' + jwtToken },
                    done);
            });
    }

    // Retrieving services with name {serviceName}
    function retrieveServicesByServiceName(jwtToken, serviceName, done) {
        api.get(
            url.resolve(siteHref + '/', 'services?search=' + serviceName + '&is_active=true&is_bookable=true&is_customer_bookable=true&fields=display,href'),
            {Authorization: 'JWT ' + jwtToken},
            done);
    }

    // Retrieving employee by alias/name {alias}
    function retrieveEmployeeByEmployeeAlias(jwtToken, employeeAlias, done) {
        api.get(
            url.resolve(siteHref + '/', 'employees?search=' + employeeAlias + '&is_active=true&is_bookable=true&is_customer_bookable=true&fields=display,href'),
            {Authorization: 'JWT ' + jwtToken},
            done);
    }

    function retrieveServiceEmployeePricing(jwtToken, serviceName, done) {
        retrieveServicesByServiceName(
            jwtToken,
            serviceName,
            function (err, result) {
                var serviceHref;
                if (err) {
                    done(err);
                    return;
                }
                serviceHref = result.content.services[0].href;
                api.get(
                    url.resolve(serviceHref + '/', 'employee_pricing?is_active=true&is_bookable=true&is_customer_bookable=true'),
                    {Authorization: 'JWT ' + jwtToken},
                    done);
            });
    }

    function retrieveEmployeeServicePricing(jwtToken, employeeAlias, done) {
        retrieveEmployeeByEmployeeAlias(
            jwtToken,
            employeeAlias,
            function (err, result) {
                var employeeHref;
                if (err) {
                    done(err);
                    return;
                }
                employeeHref = result.content.employees[0].href;
                api.get(
                    url.resolve(employeeHref + '/', 'service_pricing?is_active=true&is_bookable=true&is_customer_bookable=true'),
                    {Authorization: 'JWT ' + jwtToken},
                    done);
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