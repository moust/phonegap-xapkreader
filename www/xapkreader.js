cordova.define("cordova/plugins/xapkreader", function(require, exports, module) {

	var exec = require("cordova/exec");

	var XAPKReader = function() {};

	XAPKReader.prototype.get = function(filename, successCallback, errorCallback) {
		if (null === filename) {
			console.log("XAPKReader.get failure: filename parameter needed");
			return;
		}

		if (null === errorCallback) {
			errorCallback = function () {};
		}

		if (typeof errorCallback != "function") {
			console.log("XAPKReader.get failure: error callback parameter must be a function");
			return;
		}

		if (typeof successCallback != "function") {
			console.log("XAPKReader.get failure: success callback parameter must be a function");
			return;
		}

		return cordova.exec(successCallback, errorCallback, "XAPKReader", "get", [filename]);
	};

	module.exports = new XAPKReader();

});

//-------------------------------------------------------------------
if(!window.plugins) {
	window.plugins = {};
}
if (!window.plugins.xapkreader) {
	window.plugins.xapkreader = cordova.require("cordova/plugins/xapkreader");
}