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

    function renderCustomerWidget(target, siteId){
        // target.on(shortcuts.widget.events.WIDGET_DONE, function(){
        //     return target.shortcutsWidget('customer/edit', {siteId: siteId});
        // });
        return target.shortcutsWidget('customer/edit', {siteId: siteId});
    }

    function initializeApplication(target, siteId) {
        return initializeWidgets().then(
            function () {
                renderCustomerWidget(target, siteId);
            });
    }

    return {
        initializeApplication: initializeApplication
    }
});