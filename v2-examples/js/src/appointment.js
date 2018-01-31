const url = require('url');
const _ = require('underscore');
const api = require('./api');
const config = require('./config.js');

var appointment = (function () {
    var companyHref = url.resolve(config.apiUri, 'company/' + config.companyId);
    var siteHref = url.resolve(config.apiUri, 'site/' + config.siteId)

    function authenticateCustomer(jwtToken, username, password, done) {
        api.post(
            url.resolve(companyHref + '/', 'authenticate_customer'),
            {Authorization: 'JWT ' + jwtToken},
            {
                credential_type_code: 'password',
                username: username,
                password: password
            },
            done);
    }

    function bookAppointment(jwtToken, customerSessionHref, appointmentDetails, done) {
        api.post(
            url.resolve(customerSessionHref + '/', 'customer/appointments'),
            {Authorization: 'JWT ' + jwtToken},
            _.extend(appointmentDetails, {
                links: [{rel: 'resource/site', href: siteHref}]
            })
            , done);
    }

    function retrieveAppointments(jwtToken, customerSessionHref, done) {
        api.get(
            url.resolve(customerSessionHref + '/', 'customer/appointments'),
            {Authorization: 'JWT ' + jwtToken},
            done);
    }

    function cancelAppointment(jwtToken, customerSessionHref, appointmentHref, done) {
        var siteCustomerSessionHref = customerSessionHref.replace(companyHref, siteHref);
        var siteCustomerSessionAppointmentHref = appointmentHref.replace(siteHref, siteCustomerSessionHref + '/customer');
        api.del(
            siteCustomerSessionAppointmentHref,
            {Authorization: 'JWT ' + jwtToken},
            done);
    }

    return {
        authenticate: authenticateCustomer,
        bookAppointment: bookAppointment,
        retrieveAppointments: retrieveAppointments,
        cancelAppointment: cancelAppointment
    }
})();

module.exports = appointment;
