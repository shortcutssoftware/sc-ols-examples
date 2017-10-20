const log = require('../src/log.js');

before(function() {
    log.level('error');
})