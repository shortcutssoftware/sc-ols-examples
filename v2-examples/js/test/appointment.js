const expect = require('expect.js');
const config = require('../src/config.js');
const search = require('../src/search.js');
const appointment = require('../src/appointment.js');
const authenticate = require('../src/authenticate.js');
const _ = require('underscore');

describe('Appointment', function () {
    this.timeout(10000);

    describe('Appointment Lifecycle', function () {
        var sharedState = {
            customerSessionHref: null,
            appointmentDetails: null,
            appointment: null,
            cancelledAppointment: null,
            access_token: null
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
        it('should be able to authenticate using configured user', function (done) {
            appointment.authenticate(
                sharedState.access_token,
                config.customerUsername,
                config.customerPassword,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.customer).to.not.be(undefined);
                    expect(result.content.customer).to.be.an('object');

                    sharedState.customerSessionHref = result.content.href;
                    done();
                })
        });
        it('should be able to search for a service', function (done) {
            search.byServiceName(
                sharedState.access_token,
                'Blowdry',
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.available_appointments).to.not.be(undefined);
                    expect(result.content.available_appointments).to.be.an('array');
                    expect(result.content.available_appointments).to.not.be.empty();

                    sharedState.appointmentDetails = result.content.available_appointments[1];
                    done();
                });
        });
        it('Should be able to book the service', function (done) {
            if (!sharedState.customerSessionHref || !sharedState.appointmentDetails) {
                done('Inconclusive - earlier tests failed');
                return;
            }
            appointment.bookAppointment(
                sharedState.access_token,
                sharedState.customerSessionHref,
                sharedState.appointmentDetails,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.href).to.not.be(undefined);

                    sharedState.appointment = result.content.appointments[0];
                    done();
                });
        });

        it('Should not be able to double-book the time-slot', function (done) {
            if (!sharedState.customerSessionHref || !sharedState.appointmentDetails || !sharedState.appointment) {
                done('Inconclusive - earlier tests failed');
                return;
            }
            appointment.bookAppointment(
                sharedState.access_token,
                sharedState.customerSessionHref,
                sharedState.appointmentDetails,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(409); // Conflict
                    expect(result.content.error_type_code).to.eql('system');
                    expect(result.content.message).to.eql('Proposed appointment is no longer available');

                    done();
                });
        });

        it('Should be able to retrieve the same appointment', function (done) {
            if (!sharedState.customerSessionHref || !sharedState.appointment) {
                done('Inconclusive - earlier tests failed');
                return;
            }
            appointment.retrieveAppointments(
                sharedState.access_token,
                sharedState.customerSessionHref,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }


                    expect(result.status).to.eql(200);
                    expect(result.content.appointments).to.not.be(undefined);
                    expect(result.content.appointments).to.be.an('array');
                    expect(result.content.appointments).to.not.be.empty();
                    var foundAppointment = _.findWhere(result.content.appointments, {href: sharedState.appointment.href});
                    expect(foundAppointment).to.not.be(undefined);

                    done();
                });
        });
        it('Should be able to cancel the same appointment', function (done) {
            if (!sharedState.customerSessionHref || !sharedState.appointment) {
                done('Inconclusive - earlier tests failed');
                return;
            }

            appointment.cancelAppointment(
                sharedState.access_token,
                sharedState.customerSessionHref,
                sharedState.appointment.href,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);

                    sharedState.cancelledAppointment = sharedState.appointment;
                    done();
                });
        });
    });
});