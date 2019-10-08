# Shortcuts Widgets THC Sample

Sample website implementation over the Shortcuts Widgets.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Running website using Node

1. Ensure that Node.JS and NPM are installed.

2. Install node packages

```
npm install
```

3. Run server

```
npm start
```

### Running website by static content delivery

This website has been designed to be able to be run independently of a dynamic server by copying the files to an accessible web location.  It is recommended that the website is not run from the file directory as this can cause issues with AJAX calls.

1. Copy all files other than the package.json and README.md files to a static web server.

2. Browse to the index.html file on your static web server.

## Shortcuts Widget Events
The widget does not come with AJAX loading indicator. It is up to the host site to provide the loading indicator for the user.

> shortcuts.widgets.events.{Event name}

|Event name|Raw string name|Purpose|
|-----------|-------|-----|
|WIDGET_AJAX_BEGIN | shortcutsWidgetAjaxBegin|AJAX call is in progress|
|WIDGET_AJAX_END | shortcutsWidgetAjaxEnd|AJAX call has finished |
|WIDGET_LOADING| shortcutsWidgetLoading | the widget is fetching the view  | 
|WIDGET_RENDERED | shortcutsWidgetRendered|the widget has finished rendering the view |

The event listener is JQuery event listener. For example,

    $('#shortcuts-widget-anchor').on('shortcutsWidgetLoading', function() {
        // show the spinner while the widget is fetching the view
    })
    
    $('#shortcuts-widget-anchor').on(shortcuts.widgets.events.WIDGET_RENDERED, function() {
        // hide the spinner as the widget has fetched and displayed the view
    })

`#shortcuts-widget-anchor` is the element that the site host site uses for the Shortcuts Widget to anchor on.
