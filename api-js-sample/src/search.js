const url = require('url');
const _ = require('underscore');
const api = require('./api');
const config = require('./config.js');
const site = require('./site.js');

var search = (function () {
    var siteHref = url.resolve(config.apiUri, 'site/' + config.siteId)

    // Retrieving the site details
    function byServiceName(serviceName, done) {
        site.retrieveServicesByServiceName(serviceName, function (err, result) {
            var fromDate, toDate;
            fromDate = new Date(new Date().getTime() + 1 * 24 * 60 * 60 * 1000);
            toDate = new Date(new Date().getTime() + 14 * 24 * 60 * 60 * 1000);

            if (err) {
                done(err);
                return;
            }

            var serviceHref = _.first(result.content.services).href;

            api.post(
                url.resolve(siteHref + '/', 'calculate_available_appointments'),
                {
                    requested_services: [{
                        gender_code: 'unknown',
                        links: [{
                            rel: 'site/service',
                            href: serviceHref
                        }]
                    }],
                    date_time_filter: [{
                        from_date: fromDate.getFullYear().toString() + '-' + (fromDate.getMonth() + 1) + '-' + fromDate.getDate(),
                        to_date: toDate.getFullYear().toString() + '-' + (toDate.getMonth() + 1) + '-' + toDate.getDate(),
                        start_time: '00:00:00',
                        finish_time: '23:59:59',
                        days_of_week: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
                    }]
                },
                done);
        });
    }

    return {
        byServiceName: byServiceName
    }
})();

module.exports = search;