var expect = require('expect.js');
const search = require('../src/search.js');
const appointment = require('../src/appointment.js');
const _ = require('underscore');

describe('Appointment', function () {
    this.timeout(10000);

    describe('Appointment Lifecycle', function () {
        var sharedState = {
            customerSessionHref: null,
            appointmentDetails: null,
            appointment: null,
            cancelledAppointment: null
        };
        it('should be able to authenticate using configured user', function (done) {
            appointment.authenticate(function (err, result) {
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
        it('should be able to search for a Wax Leg service', function(done) {
            search.byServiceName('Wax Leg', function(err, result) { 
                if(err) {
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
        it('Should be able to book the service', function(done){
            if(!sharedState.customerSessionHref || !sharedState.appointmentDetails) {
                done('Inconclusive - earlier tests failed');
                return;
            }
            appointment.bookAppointment(sharedState.customerSessionHref, sharedState.appointmentDetails, function(err, result) {
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
        it('Should be able to retrieve the same appointment', function(done) {
            if(!sharedState.customerSessionHref || !sharedState.appointment) {
                done('Inconclusive - earlier tests failed');
                return;
            }
            appointment.retrieveAppointments(sharedState.customerSessionHref, function(err, result) {
                if (err) {
                    done(err);
                    return;
                }


                expect(result.status).to.eql(200);
                expect(result.content.appointments).to.not.be(undefined);
                expect(result.content.appointments).to.be.an('array');
                expect(result.content.appointments).to.not.be.empty();
                var foundAppointment = _.findWhere(result.content.appointments, { href: sharedState.appointment.href });
                expect(foundAppointment).to.not.be(undefined);

                done();
            });
        });
        it('Should be able to cancel the same appointment', function(done) {
            if(!sharedState.customerSessionHref || !sharedState.appointment) {
                done('Inconclusive - earlier tests failed');
                return;
            }

            appointment.cancelAppointment(sharedState.customerSessionHref, sharedState.appointment.href, function(err, result) {
                if (err) {
                    done(err);
                    return;
                }

                expect(result.status).to.eql(200);

                sharedState.cancelledAppointment = sharedState.appointment;
                done();
            });
        });
        xit('Should not able to retrieve the cancelled appointment', function(done) {
            if(!sharedState.customerSessionHref || !sharedState.cancelledAppointment) {
                done('Inconclusive - earlier tests failed');
                return;
            }
            appointment.retrieveAppointments(sharedState.customerSessionHref, function(err, result) {
                if (err) {
                    done(err);
                    return;
                }

                expect(result.status).to.eql(200);
                expect(result.content.appointments).to.not.be(undefined);
                expect(result.content.appointments).to.be.an('array');
                var foundAppointment = _.findWhere(result.content.appointments, { href: sharedState.cancelledAppointment.href });
                expect(foundAppointment).to.not.be(undefined);
                
                done();
            });
        });
    });
});