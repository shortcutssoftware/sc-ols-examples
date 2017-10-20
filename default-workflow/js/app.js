define(['js/config'], function (config) {

    function initializeWidgets() {
        return window.shortcuts.widgets.init({
            version: config.apiVersion,
            apiUrl: config.apiUri,
            widgetUrl: config.widgetBaseUri + '/' + config.culture,
            consumerKey: config.consumerKey,
            consumerSecret: config.consumerSecret,
            enableRegistrationCode: true,
            protocol: 'https',
            widgetCredential: {
                '*': {
                    accessToken: config.accessTokenKey,
                    accessTokenSecret: config.accessTokenSecret
                }
            }
        }).then(function () {
            shortcuts.widgets.handlers.showAlert = function (message) { console.log(message); };
        });
    }

    function renderServiceSelectionWidget(target, siteId) {
        return target.shortcutsWidget('booking/service-selection-list', { siteId: siteId });        
    }

    function initializeApplication(target, siteId) {
        return initializeWidgets().then(
            function() {
                renderServiceSelectionWidget(target, siteId);
            });
    }

    return {
        initializeApplication: initializeApplication
    }
});