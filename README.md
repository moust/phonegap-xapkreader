phonegap-xapkreader
===================

Cordova plugin to access files in APK Expansion Files for Cordova/Phonegap Android application.

The plugin is an implementation of the process described here : [APK Expansion Files](http://developer.android.com/google/play/expansion-files.html)

# Installation

This plugin use the Cordova CLI's plugin command. To install it to your application, simply execute the following (and replace variables).

```
cordova plugin add org.apache.cordova.xapkreader --variable MAIN_VERSION=1 --variable PATCH_VERSION=1 --variable FILE_SIZE=1095520
```

- `MAIN_VERSION` :  Can be `1` or `0`. Define if your APK expansion file is the main file or a patch.
- `PATCH_VERSION` :  The version of your expansion file.
- `FILE_SIZE` : The byte size of your expansion file. This is used to verify the intégrity of the file after downloading.

After installation you need to edit `src/org/apache/cordova/xapkreader/XAPKDownloaderService.java` to put your own application public key.

Further, you need to add the your application the __Downloader Library__ and __APK Expansion Zip Library__ from the `Android Extras` section using the Android SDK manager (run `android`).

This libraries are respectively located in :
- APK Expansion Zip Library : `<sdk>/extras/google/google_market_apk_expansion/zip_file/`
- Downloader Library : `<sdk>/extras/google/market_apk_expansion/downloader_library/`

Note that the Downloader Library require the __Google Play Licensing Library__ located in `<sdk>/extras/google/market_licensing/`.

You can find an explanation on how to do this in the following page : [Preparing to use the Downloader Library](http://developer.android.com/google/play/expansion-files.html#Preparing)

# Using

The file is returned to a success callback as URL object that you can use like in the example below or with the File API.

```
XAPKReader.get(filename, successCallback, [errorCallback], [fileType]);
```

## Parameters

- **filename** : The name of the file to load form the expansion file
- **successCallback** : The callback that executes when the file is loaded.
- **errorCallback** (Optional) : The callback that executes if an error occurs.
- **fileType** (Optional) : The file type.

## Example

```javascript
XAPKReader.get(
    'image.jpg',
    function (url) {
        var img = new Image();
        img.src = url;
        document.body.appendChild(img);
    },
    function (error) {
        console.error(error);
    },
    'image/jpeg'
);
```

# Licence MIT

Copyright 2013 Quentin Aupetit

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.