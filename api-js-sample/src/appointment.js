const url = require('url');
const _ = require('underscore');
const api = require('./api');
const config = require('./config.js');

var appointment = (function () {
    var companyHref = url.resolve(config.apiUri, 'company/' + config.companyId);
    var siteHref = url.resolve(config.apiUri, 'site/' + config.siteId)

    function authenticate(done) {
        api.post(url.resolve(companyHref + '/', 'authenticate_customer'),
            {
                credential_type_code: 'password',
                username: config.customerUsername,
                password: config.customerPassword
            }, done);
    }

    function bookAppointment(customerSessionHref, appointmentDetails, done) {
        api.post(url.resolve(customerSessionHref + '/', 'customer/appointments'), _.extend(appointmentDetails, {
            links: [{ rel: 'resource/site', href: siteHref }]
        }), done);
    }

    function retrieveAppointments(customerSessionHref, done) {
        api.get(url.resolve(customerSessionHref + '/', 'customer/appointments'), done);
    }

    function cancelAppointment(customerSessionHref, appointmentHref, done) {
        var siteCustomerSessionHref = customerSessionHref.replace(companyHref, siteHref);
        var siteCustomerSessionAppointmentHref = appointmentHref.replace(siteHref, siteCustomerSessionHref + '/customer');
        api.del(siteCustomerSessionAppointmentHref, done);
    }

    return {
        authenticate: authenticate,
        bookAppointment: bookAppointment,
        retrieveAppointments: retrieveAppointments,
        cancelAppointment: cancelAppointment
    }
})();

module.exports = appointment;