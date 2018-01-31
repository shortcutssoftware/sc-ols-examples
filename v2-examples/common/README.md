# Common API questions and example solutions

### Customer knows they want a specific service in a specific date/time window but doesn’t know who with.

Firstly, prepare a date-time filter

```js
        var fromDate = new Date(new Date().getTime() + 3 * 24 * 60 * 60 * 1000);
        var toDate = new Date(new Date().getTime() + 14 * 24 * 60 * 60 * 1000);
        var dateTimeFilter = {
            from_date: fromDate.getFullYear().toString() + '-' + (fromDate.getMonth() + 1) + '-' + fromDate.getDate(),
            to_date: toDate.getFullYear().toString() + '-' + (toDate.getMonth() + 1) + '-' + toDate.getDate(),
            start_time: '00:00:00',
            finish_time: '23:59:59',
            days_of_week: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
        };
```

Then use it when searching for available appointments. Note, in this search, the 
service is known, and is able to be supplied as a parameter.

```js
    function byServiceNameAndDateTimeFilter(serviceName, dateTimeFilter, done) {
        site.retrieveServicesByServiceName(serviceName, function (err, result) {
            if (err) {
                done(err);
                return;
            }

            var serviceHref = result.content.services[0].href;
            api.post(
                url.resolve(siteHref + '/', 'calculate_available_appointments'),
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
```

Please refer to test of `search.byServiceNameAndDateTimeFilter` in 
[common-tasks.js](../../v1-examples/js/test/common-tasks.js), 
and also function `byServiceNameAndDateTimeFilter()` in 
[search.js](../../v1-examples/js/src/search.js)


### Customer wants category of service (e.g. massage) in a specific date/time window but doesn’t know which kind of massage or who with.

Firstly, prepare a date-time filter as before.

Then use it when searching for available appointments. Note, in this search, the 
service category is known, but is not able to be supplied as a parameter. We use 
the service category to get a list of services, and then call `calculate_avilable_appointments`
for them all. The result is the union of all the individual search results.

```js
    function byServiceCategoryAndDateTimeFilter(serviceCategoryName, dateTimeFilter, done) {
        site.retrieveServicesByServiceCategoryName(serviceCategoryName, function (err, result) {
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
```

Please refer to test of `search.byServiceCategoryAndDateTimeFilter` in 
[common-tasks.js](../../v1-examples/js/test/common-tasks.js), 
and also function `byServiceCategoryAndDateTimeFilter()` in 
[search.js](../../v1-examples/js/src/search.js)

### Customer wants specific service with specific stylist/therapist but doesn’t know available times/dates or gives range of date/time.

Firstly, prepare a date-time filter as before.

Then use it when searching for available appointments. Note, in this search, the 
service definition is retrieved, as well as the employee definition. these are 
then used as parameters to call `calculate_avilable_appointments`.

```js
    function byServiceAndEmployeeNameAndDateTimeFilter(serviceName, employeeAlias, dateTimeFilter, done) {

        Promise.all([
            new Promise(function (resolve, reject) {
                site.retrieveServicesByServiceName(serviceName, function (err, result) {
                    if (err) reject(err); else resolve(result);
                })
            }),
            new Promise(function (resolve, reject) {
                site.retrieveEmployeeByEmployeeAlias(employeeAlias, function (err, result) {
                    if (err) reject(err); else resolve(result);
                })
            })
        ]).then(
            function (results) {
                var serviceHref = results[0].content.services[0].href;
                var employeeHref = results[1].content.employees[0].href;

                api.post(
                    url.resolve(siteHref + '/', 'calculate_available_appointments'),
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

```

Please refer to test of `search.byServiceAndEmployeeNameAndDateTimeFilter` in 
[common-tasks.js](../../v1-examples/js/test/common-tasks.js), 
and also function `byServiceAndEmployeeNameAndDateTimeFilter()` in 
[search.js](../../v1-examples/js/src/search.js)


### Customer wants specific service within time/date window, wants to choose price band, doesn’t know who with.

Firstly, prepare a date-time filter as before.

Then, prepare a price band definition as follows:

```js
            var priceBand = {
                upper: 100,
                lower: 10
            };
```

Then, since the service is known, we can reuse the logic that allows us to search 
by specific service and date-time range. After executing this search, we iterate 
through the results, keeping only the appointments that fall within the price band.

```js
    function byServiceNameAndDateTimeFilterAndPriceBand(serviceName, dateTimeFilter, priceBand, done) {
        byServiceNameAndDateTimeFilter(serviceName, dateTimeFilter, function (err, result) {
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
```

Please refer to test of `search.byServiceNameAndDateTimeFilterAndPriceBand` in 
[common-tasks.js](../../v1-examples/js/test/common-tasks.js), 
and also function `byServiceNameAndDateTimeFilterAndPriceBand()` in 
[search.js](../../v1-examples/js/src/search.js)


### Customer to be able to cancel booking before cancellation period expiration/cut off.

This is a single-step process. The function takes three arguments: 

1. the customer session href from the authenticateCustomer step.
1. an appointment href.
1. a callback of the form `function(err, result)`.

```js
    function cancelAppointment(customerSessionHref, appointmentHref, done) {
        var siteCustomerSessionHref = customerSessionHref.replace(companyHref, siteHref);
        var siteCustomerSessionAppointmentHref = appointmentHref.replace(siteHref, siteCustomerSessionHref + '/customer');
        api.del(siteCustomerSessionAppointmentHref, done);
    }
```

Please refer to `cancelAppointment()` in [appointment.js](../../v1-examples/js/src/appointment.js)