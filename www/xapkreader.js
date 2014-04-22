var exec = require("cordova/exec");

/**
 * Constructor
 **/
var XAPKReader = function() {};

/**
 * Get a file in expansion file and return it as data base64 encoded
 *
 * @param filename              The file name
 * @param successCallback       The callback to be called when the file is found.
 *                                  successCallback()
 * @param errorCallback         The callback to be called if there is an error.
 *                                  errorCallback(int errorCode) - OPTIONAL
 **/
XAPKReader.prototype.get = function(filename, successCallback, errorCallback) {
    // only for android
    if (!navigator.userAgent.match(/Android/i)) {
        return successCallback(filename);
    }

    if (null === filename) {
        console.error("XAPKReader.get failure: filename parameter needed");
        return;
    }

    cordova.exec(successCallback, errorCallback, "XAPKReader", "get", [filename]);
};

module.exports = new XAPKReader();