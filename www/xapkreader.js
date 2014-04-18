var exec = require("cordova/exec");

/**
 * Constructor
 **/
var XAPKReader = function() {
    this.mainVersion = 1;
    this.patchVersion = 1;
};

/**
 * Initialize XAPKReader with the expansion file configuration
 *
 * @param mainVersion        The main version of the expansion file
 * @param patchVersion       The patch version of the expansion file
 */
XAPKReader.prototype.initialize = function(mainVersion, patchVersion) {
    this.mainVersion = mainVersion;
    this.patchVersion = patchVersion;
};

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
    if (null === filename) {
        console.error("XAPKReader.get failure: filename parameter needed");
        return;
    }
    cordova.exec(successCallback, errorCallback, "XAPKReader", "get", [filename, this.mainVersion, this.patchVersion]);
};

module.exports = new XAPKReader();