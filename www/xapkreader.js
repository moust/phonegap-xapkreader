var exec = require("cordova/exec");

/**
 * Constructor
 **/
var XAPKReader = function() {};

/**
 * Get a file in expansion file and return it as data base64 encoded
 *
 * @param filename              The file name
 * @param fileType              The file type (eg: "image/jpeg")
 * @param successCallback       The callback to be called when the file is found.
 *                                  successCallback()
 * @param errorCallback         The callback to be called if there is an error.
 *                                  errorCallback(int errorCode) - OPTIONAL
 **/
XAPKReader.prototype.get = function(filename, fileType, successCallback, errorCallback) {
    // only for android
    if (!navigator.userAgent.match(/Android/i)) {
        return successCallback(filename);
    }

    if (null === filename) {
        console.error("XAPKReader.get failure: filename parameter needed");
        return;
    }

    var context = this;

    cordova.exec(
        function (result) {
            context.arrayBufferToURL(result, fileType, successCallback, errorCallback);
        },
        errorCallback, "XAPKReader", "get", [filename]
    );
};

XAPKReader.prototype.arrayBufferToURL = function (arrayBuffer, fileType, successCallback, errorCallback) {
    try {
        var properties = {};
        if (fileType) {
            properties.type = fileType;
        }
        var blob = new Blob([arrayBuffer], properties);
        window.URL = window.URL || window.webkitURL;
        var url = window.URL.createObjectURL(blob);
        successCallback.call(this, url);
    }
    catch (error) {
        errorCallback.call(this, error);
    }
};

module.exports = new XAPKReader();