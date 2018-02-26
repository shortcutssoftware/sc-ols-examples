const url = require('url');
const _ = require('underscore');
const api = require('./api');
const config = require('./config.js');

var appointmentReschedule = (function () {
    var companyHref = url.resolve(config.apiUri, 'company/' + config.companyId);
    var siteHref = url.resolve(config.apiUri, 'site/' + config.siteId)

    function authenticateCustomer(username, password, done) {
        api.get(url.resolve(config.tomaxUri, 'authenticate/' + username + '/' + password), function (err, result) {
            if (err) {
                done(err);
            }
            if (result.status != 200) {
                done(new Error('3rd party authentication failed'));
            }
            var bearerToken = result.content;
            var authenticateRequest = {
                credential_type_code: 'access_token',
                token_type: 'thc',
                customer_id: config.customerId,
                access_token: bearerToken
            };
            api.post(url.resolve(companyHref + '/', 'authenticate_customer'), authenticateRequest, function (err, result) {
                if (err) {
                    done(err);
                }
                if (result.status != 200) {
                    done(new Error('Shortcuts single sign-on validation failed'));
                }
                done(null, result);
            })
        });
    }

    function retrieveAppointment(href, done) {
        api.get(href, done);
    }

    function rescheduleAppointment(appointmentData, done) {
        api.put(appointmentData.href, appointmentData, done);
    }

    return {
        authenticateCustomer: authenticateCustomer,
        retrieveAppointment: retrieveAppointment,
        rescheduleAppointment: rescheduleAppointment
    }
})();

module.exports = appointmentReschedule;
