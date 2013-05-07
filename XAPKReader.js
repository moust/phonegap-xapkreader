cordova.define("cordova/plugins/xapkreader",
function(require, exports, module) {
	var exec = require("cordova/exec");
	var XAPKReader = function() {};

	XAPKReader.prototype.get = function(filename, success, fail) {
		if(!filename) { return false; }

		if (!fail) { fail = function(){}; }

		if (typeof fail != "function")  {
			console.log("XAPKReader.get failure: failure parameter not a function");
			return;
		}

		if (typeof success != "function") {
			fail("success callback parameter must be a function");
			return;
		}

		return cordova.exec(success, fail, "XAPKReader", "get", [filename]);
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