const bunyan = require('bunyan');
var log = bunyan.createLogger({ name: 'js-example' });

module.exports = log;