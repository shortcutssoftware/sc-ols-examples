const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const stylistSingleSignon = require('./stylist-single-signon.js');

app.use(bodyParser.json());

app.post('/stylist-single-signon', function (req, res) {

    // receive the stylist credentials as a JSON payload.
    // Pass this on to authenticate and return the cookie
    // data if authentication is successful.

    let stylistCredentials = req.body;

    stylistSingleSignon.getAuthCookies(stylistCredentials, function (err, authCookies) {

        if (err) {
            res.send('{ "message": "Unauthorized" }', 401);
            return;
        }

        // authentication success, send back the cookies
        res.send(JSON.stringify(authCookies), 200);
    });


});

app.listen(8080, () => console.log('Stylist single signon server listening on port 8080!'))