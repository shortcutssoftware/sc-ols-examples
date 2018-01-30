
# Common API questions


### Client knows they want a specific service in a specific date/time window but doesn’t know who with.

Please refer to test of `search.byServiceNameAndDateTimeFilter` in 
[common-tasks.js](../js/test/common-tasks.js), 
and also function `byServiceNameAndDateTimeFilter()` in 
[search.js](../js/src/search.js)


### Client wants category of service (e.g. massage) in a specific date/time window but doesn’t know which kind of massage or who with.

Please refer to test of `search.byServiceCategoryAndDateTimeFilter` in 
[common-tasks.js](../js/test/common-tasks.js), 
and also function `byServiceCategoryAndDateTimeFilter()` in 
[search.js](../js/src/search.js)

### Client wants specific service with specific stylist/therapist but doesn’t know available times/dates or gives range of date/time.

Please refer to test of `search.byServiceAndEmployeeNameAndDateTimeFilter` in 
[common-tasks.js](../js/test/common-tasks.js), 
and also function `byServiceAndEmployeeNameAndDateTimeFilter()` in 
[search.js](../js/src/search.js)


### Client wants specific service within time/date window, wants to choose price band, doesn’t know who with.

Please refer to test of `search.byServiceNameAndDateTimeFilterAndPriceBand` in 
[common-tasks.js](../js/test/common-tasks.js), 
and also function `byServiceNameAndDateTimeFilterAndPriceBand()` in 
[search.js](../js/src/search.js)


### Client to be able to cancel booking before cancellation period expiration/cut off.

Please refer to `cancelAppointment()` in [appointment.js](../js/src/appointment.js)

