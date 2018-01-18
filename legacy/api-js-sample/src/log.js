const bunyan = require('bunyan');
var log = bunyan.createLogger({ name: 'api-js-sample' });

module.exports = log;