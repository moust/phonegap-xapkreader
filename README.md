phonegap-xapkreader
===================

Cordova plugin to access files in APK Expansion Files for Cordova/Phonegap Android application.

The plugin is an implementation of the process described here : [APK Expansion Files](http://developer.android.com/google/play/expansion-files.html)

# Installation

This plugin use the Cordova CLI's plugin command. To install it to your application, simply execute the following (and replace variables).

```
cordova plugin add https://github.com/moust/phonegap-xapkreader.git --variable MAIN_VERSION=1 --variable PATCH_VERSION=1 --variable FILE_SIZE=1095520
```

- `MAIN_VERSION` : The main version of your expansion file.
- `PATCH_VERSION` : The patch version of your expansion file.
- `FILE_SIZE` : The byte size of your expansion file. This is used to verify the intégrity of the file after downloading.

**After installation you need to edit `src/org/apache/cordova/xapkreader/XAPKDownloaderService.java` to put your own application public key.**

Further, you need to add to your application the __Google Play Licensing Library__ and __Google Play APK Expansion Library__ from the *Android Extras* folder using the Android SDK manager (run `android sdk`).

You can find an explanation on how to do this in the following page : [Preparing to use the Downloader Library](http://developer.android.com/google/play/expansion-files.html#Preparing)

## How to referencing to the Licensing Library and APK Expansion Library

1. Start the Android SDK Manager and install **Google Play Licensing Library** and **Google Play APK Expansion Library** in the *Extra* folder if it's not already the case.

2. Create licensing project and build library
```
android update project --path [path/to/your/android/sdk]/extras/google/play_licensing/library --target 1
cd [path/to/your/android/sdk]/extras/google/play_licensing/library
ant release
```

3. Create apk expansion project and build library
```
android update project --path [path/to/your/android/sdk]/extras/google/play_apk_expansion/downloader_library --target 1
cd [path/to/your/android/sdk]/extras/google/play_apk_expansion/downloader_library
ant release
```

4. Update app project to use apk expansion library
```
android update project --path path/to/your/project \
--library [path/to/your/android/sdk]/extras/google/play_apk_expansion/downloader_library \
--library [path/to/your/android/sdk]/extras/google/play_apk_expansion/zip_file
```

5. Edit app `project.properties` and change the path in `android.library.reference.1` and `android.library.reference.2` to use relative path and not absolute as absolute paths fail to work

# Using

You may access your files by using the org.apache.cordova.xapkreader content provider.

```
<img src="content://org.apache.cordova.xapkreader/image.jpg">
```

Or use the get method to access a file that is in your expansion pack.  The file is returned to a success callback as a URL object that you can use like in the example below or with the File API.

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




Or use the export method to extract a file from the expansion pack and have it stored on your devide.  The file is copied out of the expansion pack's zip file to the specified destination.

```
XAPKReader.export(filename, destination, successCallback, [errorCallback], [fileType]);
```


## Parameters

- **filename** : The name of the file to load form the expansion file
- **destination** : The full path and file name of where the file should be copied to
- **successCallback** : The callback that executes after the file is copied.
- **errorCallback** (Optional) : The callback that executes if an error occurs.
- **fileType** (Optional) : The file type.

## Example

```javascript
var destination = '/data/data/com.example.myproject/tmp.m4a';
XAPKReader.export(
    'sound123.m4a',
    destination,
    function (url) {
         playSound( destination );
    },
    function (error) {
        console.error(error);
    },
    'audio/mp4'
);
```


# Licence MIT

Copyright 2013 Quentin Aupetit

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.