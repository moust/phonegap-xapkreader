var exec = require("cordova/exec");

module.exports = {

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
    get: function(filename, successCallback, errorCallback, fileType) {
        // only for android
        if (!navigator.userAgent.match(/Android/i)) {
            return successCallback(filename);
        }

        if (null === filename) {
            console.error("XAPKReader.get failure: filename parameter needed");
            return;
        }

        var context = this;

        var success = function (result) {
            try {
                var url = context.arrayBufferToURL(result, fileType);
                successCallback(url);
            }
            catch (e) {
                errorCallback(e);
            }
        };

        cordova.exec(success, errorCallback, "XAPKReader", "get", [filename]);
    },

    /**
     * Convert ArrayBuffer to URL
     *
     * @param arrayBuffer   ArrayBuffer to convert
     * @param fileType      (optional) The file type (eg: "image/jpeg")
     * @return URL          URL string
     **/
    arrayBufferToURL: function (arrayBuffer, fileType) {
        var blob = this.createBlob(arrayBuffer);
        window.URL = window.URL || window.webkitURL;
        return window.URL.createObjectURL(blob);
    },

    /**
     * Create Blob from data
     *
     * @param part          ArrayBuffer, ArrayBufferView, Blob or DOMString part
     * @param fileType      (optional) The file type (eg: "image/jpeg")
     **/
    createBlob: function (part, fileType) {
        var blob;
        try {
            var properties = {};
            if (fileType) {
                properties.type = fileType;
            }
            blob = new Blob([part], properties);
        }
        catch (e) {
            // TypeError try old constructor
            window.BlobBuilder = window.BlobBuilder ||
                window.WebKitBlobBuilder ||
                window.MozBlobBuilder ||
                window.MSBlobBuilder;

            if (e.name == 'TypeError' && !window.BlobBuilder) {
                throw new Error('This platform does not support Blob type.');
            }

            var bb = new BlobBuilder();
            bb.append(part);
            blob = bb.getBlob(fileType);
        }
        return blob;
    }

};