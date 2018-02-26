const expect = require('expect.js');
const config = require('../src/config.js');
const search = require('../src/search.js');
const appointment = require('../src/appointment.js');
const appointmentReschedule = require('../src/appointment_reschedule.js');
const _ = require('underscore');

describe('Appointment Reschedule', function () {

    this.timeout(10000);

    describe('Appointment Create and Reschedule Operation', function () {

        var sharedState = {
            customerSessionHref: null,
            appointmentDetails: null,
            appointmentHref: null,
            appointment: null,
            next_scheduled_date: null,
            next_start_time: null
        };

        it('must be able to authenticate using user validated by ratner', function (done) {
            appointmentReschedule.authenticateCustomer(config.customerUsername, config.customerPassword, function (err, result) {
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

        it('Must be able to search for a bookable service', function (done) {
            if (!sharedState.customerSessionHref) {
                done('Authentication failed');
                return;
            }
            search.byServiceName('Shampoo and Cut', function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                expect(result.status).to.eql(200);
                expect(result.content.available_appointments).to.not.be(undefined);
                expect(result.content.available_appointments).to.be.an('array');
                expect(result.content.available_appointments).to.not.be.empty();

                sharedState.appointmentDetails = result.content.available_appointments[0];

                // remember these details for the reschedule operation later
                var nextAppointment = result.content.available_appointments[1];
                sharedState.next_scheduled_date = nextAppointment.scheduled_date;
                sharedState.next_start_time = nextAppointment.services[0].start_time;

                done();
            });
        });

        it('Should be able to book the service', function (done) {
            if (!sharedState.customerSessionHref || !sharedState.appointmentDetails) {
                done('Search for bookable appointment failed');
                return;
            }
            appointment.bookAppointment(sharedState.customerSessionHref, sharedState.appointmentDetails, function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                expect(result.status).to.eql(200);
                expect(result.content.href).to.not.be(undefined);

                sharedState.appointmentHref = result.content.appointments[0].href;
                done();
            });
        });

        it('Must be able to retrieve appointment', function (done) {
            if (!sharedState.appointmentHref) {
                done('Booking an appointment failed');
                return;
            }
            appointmentReschedule.retrieveAppointment(sharedState.appointmentHref, function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                expect(result.status).to.eql(200);
                sharedState.appointment = result.content;
                done();
            });
        });

        it('Must be able to reschedule an appointment', function (done) {
            if (!sharedState.appointment) {
                done('Retrieving a booked appointment failed');
                return;
            }

            var oldVersion = sharedState.appointment.version;
            console.log('version before reschedule:', oldVersion)

            var rescheduleAppointmentRequest = _.clone(sharedState.appointment);
            rescheduleAppointmentRequest.scheduled_date = sharedState.next_scheduled_date
            rescheduleAppointmentRequest.start_time = sharedState.next_start_time;

            appointmentReschedule.rescheduleAppointment(rescheduleAppointmentRequest, function (err, result) {
                if (err) {
                    done(err);
                    return;
                }

                expect(result.status).to.eql(200);
                expect(result.content.href).to.not.be(undefined);

                var newVersion = result.content.version;
                console.log('version after reschedule:', newVersion);
                expect(newVersion).to.be.above(oldVersion);

                done();
            });
        });
    });
});