const url = require('url');
const _ = require('underscore');
const api = require('./api');
const config = require('./config.js');
const site = require('./site.js');
const log = require('./log.js');

var search = (function () {
    var siteHref, defaultFromDate, defaultTodate, defaultDateTimeFilter;

    siteHref = url.resolve(config.apiUri, 'site/' + config.siteId);
    defaultFromDate = new Date(new Date().getTime() + 3 * 24 * 60 * 60 * 1000);
    defaultTodate = new Date(new Date().getTime() + 14 * 24 * 60 * 60 * 1000);

    defaultDateTimeFilter = {
        from_date: defaultFromDate.getFullYear().toString() + '-' + (defaultFromDate.getMonth() + 1) + '-' + defaultFromDate.getDate(),
        to_date: defaultTodate.getFullYear().toString() + '-' + (defaultTodate.getMonth() + 1) + '-' + defaultTodate.getDate(),
        start_time: '00:00:00',
        finish_time: '23:59:59',
        days_of_week: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
    };

    function byServiceCategoryName(jwtToken, serviceCategoryName, done) {
        site.retrieveServicesByServiceCategoryName(
            jwtToken,
            serviceCategoryName,
            function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                var availabilitySearches = [];
                for (var i = 0; i < result.content.services.length; i++) {
                    var serviceHref = result.content.services[i].href;

                    availabilitySearches.push(new Promise(function (resolve, reject) {
                        api.post(
                            url.resolve(siteHref + '/', 'calculate_available_appointments'),
                            {
                                requested_services: [{
                                    gender_code: 'unknown',
                                    links: [
                                        {rel: 'site/service', href: serviceHref}
                                    ]
                                }],
                                date_time_filter: [defaultDateTimeFilter]
                            },
                            function (err, result) {
                                if (err) reject(err); else resolve(result);
                            });

                    }));
                }

                log.info('Searching for %s services', availabilitySearches.length);

                Promise.all(availabilitySearches).then(
                    function (results) {
                        var result = {status: 200, content: {available_appointments: []}};
                        log.info('Processing results: %s', JSON.stringify(results));
                        for (var i = 0; i < results.length; i++) {
                            result.content.available_appointments = _.union(result.available_appointments, results[i].content.available_appointments);
                        }
                        done(null, result);
                    },
                    function (err) {
                        done(err);
                    }
                );
            })
    }

    function byServiceCategoryAndDateTimeFilter(jwtToken, serviceCategoryName, dateTimeFilter, done) {
        site.retrieveServicesByServiceCategoryName(
            jwtToken,
            serviceCategoryName,
            function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                var availabilitySearches = [];
                for (var i = 0; i < result.content.services.length; i++) {
                    var serviceHref = result.content.services[i].href;

                    availabilitySearches.push(new Promise(function (resolve, reject) {
                        api.post(
                            url.resolve(siteHref + '/', 'calculate_available_appointments'),
                            { Authorization: 'JWT ' + jwtToken },
                            {
                                requested_services: [{
                                    gender_code: 'unknown',
                                    links: [
                                        {rel: 'site/service', href: serviceHref}
                                    ]
                                }],
                                date_time_filter: [dateTimeFilter]
                            },
                            function (err, result) {
                                if (err) reject(err); else resolve(result);
                            });

                    }));
                }

                log.info('Searching for %s services', availabilitySearches.length);

                Promise.all(availabilitySearches).then(
                    function (results) {
                        var result = {status: 200, content: {available_appointments: []}};
                        log.info('Processing results: %s', JSON.stringify(results));
                        for (var i = 0; i < results.length; i++) {
                            result.content.available_appointments = _.union(result.available_appointments, results[i].content.available_appointments);
                        }
                        done(null, result);
                    },
                    function (err) {
                        done(err);
                    }
                );
            })
    }

    function byServiceName(jwtToken, serviceName, done) {
        site.retrieveServicesByServiceName(
            jwtToken,
            serviceName,
            function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                var serviceHref = result.content.services[0].href;
                api.post(
                    url.resolve(siteHref + '/', 'calculate_available_appointments'),
                    { Authorization: 'JWT ' + jwtToken },
                    {
                        requested_services: [{
                            gender_code: 'unknown',
                            links: [
                                {rel: 'site/service', href: serviceHref}
                            ]
                        }],
                        date_time_filter: [defaultDateTimeFilter]
                    },
                    done);
            });
    }

    function byServiceNameAndDateTimeFilter(jwtToken, serviceName, dateTimeFilter, done) {
        site.retrieveServicesByServiceName(
            jwtToken,
            serviceName,
            function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                var serviceHref = result.content.services[0].href;
                api.post(
                    url.resolve(siteHref + '/', 'calculate_available_appointments'),
                    { Authorization: 'JWT ' + jwtToken },
                    {
                        requested_services: [{
                            gender_code: 'unknown',
                            links: [
                                {rel: 'site/service', href: serviceHref}
                            ]
                        }],
                        date_time_filter: [dateTimeFilter]
                    },
                    done);
            });
    }

    function byServiceNameAndDateTimeFilterAndPriceBand(jwtToken, serviceName, dateTimeFilter, priceBand, done) {
        byServiceNameAndDateTimeFilter(
            jwtToken,
            serviceName,
            dateTimeFilter,
            function (err, result) {
                if (err) {
                    done(err)
                }

                // filter available appointments by price band
                var filtered = [];
                for (var i = 0; i < result.content.available_appointments.length; i++) {
                    var available_appointment = result.content.available_appointments[i];
                    if (!priceBand) {
                        filtered.push(available_appointment);
                    } else {
                        if ((priceBand.lower <= available_appointment.sell_price)
                            && (priceBand.upper >= available_appointment.sell_price)) {
                            filtered.push(available_appointment);
                        }
                    }
                }

                result.content.available_appointments = filtered;
                done(null, result);
            });
    }

    function byServiceAndEmployeeName(jwtToken, serviceName, employeeAlias, done) {

        Promise.all([
            new Promise(function (resolve, reject) {
                site.retrieveServicesByServiceName(jwtToken, serviceName, function (err, result) {
                    if (err) reject(err); else resolve(result);
                })
            }),
            new Promise(function (resolve, reject) {
                site.retrieveEmployeeByEmployeeAlias(jwtToken, employeeAlias, function (err, result) {
                    if (err) reject(err); else resolve(result);
                })
            })
        ]).then(
            function (results) {
                var serviceHref = results[0].content.services[0].href;
                var employeeHref = results[1].content.employees[0].href;

                api.post(
                    url.resolve(siteHref + '/', 'calculate_available_appointments'),
                    { Authorization: 'JWT ' + jwtToken },
                    {
                        requested_services: [{
                            gender_code: 'unknown',
                            links: [
                                {rel: 'site/service', href: serviceHref},
                                {rel: 'site/employee', href: employeeHref}
                            ]
                        }],
                        date_time_filter: [defaultDateTimeFilter]
                    },
                    done);

            },
            function (err) {
                done(err);
            }
        );
    }

    function byServiceAndEmployeeNameAndDateTimeFilter(jwtToken, serviceName, employeeAlias, dateTimeFilter, done) {

        Promise.all([
            new Promise(function (resolve, reject) {
                site.retrieveServicesByServiceName(jwtToken, serviceName, function (err, result) {
                    if (err) reject(err); else resolve(result);
                })
            }),
            new Promise(function (resolve, reject) {
                site.retrieveEmployeeByEmployeeAlias(jwtToken, employeeAlias, function (err, result) {
                    if (err) reject(err); else resolve(result);
                })
            })
        ]).then(
            function (results) {
                var serviceHref = results[0].content.services[0].href;
                var employeeHref = results[1].content.employees[0].href;

                api.post(
                    url.resolve(siteHref + '/', 'calculate_available_appointments'),
                    { Authorization: 'JWT ' + jwtToken },
                    {
                        requested_services: [{
                            gender_code: 'unknown',
                            links: [
                                {rel: 'site/service', href: serviceHref},
                                {rel: 'site/employee', href: employeeHref}
                            ]
                        }],
                        date_time_filter: [dateTimeFilter]
                    },
                    done);

            },
            function (err) {
                done(err);
            }
        );
    }

    return {
        byServiceCategoryName: byServiceCategoryName,
        byServiceName: byServiceName,
        byServiceAndEmployeeName: byServiceAndEmployeeName,
        byServiceNameAndDateTimeFilter: byServiceNameAndDateTimeFilter,
        byServiceNameAndDateTimeFilterAndPriceBand: byServiceNameAndDateTimeFilterAndPriceBand,
        byServiceCategoryAndDateTimeFilter: byServiceCategoryAndDateTimeFilter,
        byServiceAndEmployeeNameAndDateTimeFilter: byServiceAndEmployeeNameAndDateTimeFilter
    }
})();

module.exports = search;