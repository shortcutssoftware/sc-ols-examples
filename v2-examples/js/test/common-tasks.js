const url = require('url');
const expect = require('expect.js');
const config = require('../src/config.js');
const search = require('../src/search.js');
const authenticate = require('../src/authenticate.js');
const _ = require('underscore');

describe('Common Tasks', function () {
    this.timeout(10000);

    describe('Common Appointment Searches', function () {

        var sharedState = {
            access_token: undefined
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

        var fromDate = new Date(new Date().getTime() + 3 * 24 * 60 * 60 * 1000);
        var toDate = new Date(new Date().getTime() + 14 * 24 * 60 * 60 * 1000);
        var dateTimeFilter = {
            from_date: fromDate.getFullYear().toString() + '-' + (fromDate.getMonth() + 1) + '-' + fromDate.getDate(),
            to_date: toDate.getFullYear().toString() + '-' + (toDate.getMonth() + 1) + '-' + toDate.getDate(),
            start_time: '00:00:00',
            finish_time: '23:59:59',
            days_of_week: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
        };


        it('search for appointments by service and date/time window', function (done) {

            search.byServiceNameAndDateTimeFilter(
                sharedState.access_token,
                'Blowdry',
                dateTimeFilter,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.available_appointments).to.not.be.empty();

                    console.log(JSON.stringify(result.content.available_appointments));

                    done();
                });
        });

        it('search for appointments by service category and date/time window', function (done) {

            search.byServiceCategoryAndDateTimeFilter(
                sharedState.access_token,
                'Hair Styling',
                dateTimeFilter,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.available_appointments).to.not.be.empty();

                    console.log(JSON.stringify(result.content.available_appointments));

                    done();
                });
        });

        it('search for appointments by service and employee and date/time window', function (done) {

            search.byServiceAndEmployeeNameAndDateTimeFilter(
                sharedState.access_token,
                'Blowdry',
                'Katie',
                dateTimeFilter, function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.available_appointments).to.not.be.empty();

                    console.log(JSON.stringify(result.content.available_appointments));

                    done();
                });
        });

        it('search for appointments by service and date/time window and price band - inclusive', function (done) {

            var priceBand = {
                upper: 100,
                lower: 10
            };
            search.byServiceNameAndDateTimeFilterAndPriceBand(
                sharedState.access_token,
                'Blowdry',
                dateTimeFilter,
                priceBand,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.available_appointments).to.not.be.empty();

                    console.log(JSON.stringify(result.content.available_appointments));

                    for (var i = 0; i < result.content.available_appointments.length; i++) {
                        var appointment = result.content.available_appointments[i];
                        expect(appointment.sell_price).to.be.above(priceBand.lower);
                        expect(appointment.sell_price).to.be.below(priceBand.upper);
                    }

                    done();
                });

        });

        it('search for appointments by service and date/time window and price band - exclusive', function (done) {

            var priceBand = {
                upper: 10,
                lower: 1
            };
            search.byServiceNameAndDateTimeFilterAndPriceBand(
                sharedState.access_token,
                'Blowdry',
                dateTimeFilter,
                priceBand,
                function (err, result) {
                    if (err) {
                        done(err);
                        return;
                    }

                    expect(result.status).to.eql(200);
                    expect(result.content.available_appointments).to.be.empty();

                    console.log(JSON.stringify(result.content.available_appointments));

                    done();
                });
        });

    });

});