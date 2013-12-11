phonegap-xapkreader
===================

Cordova plugin to access files in APK Expansion Files for Cordova/Phonegap Android application.

# Requirements

This suppose you have already implemented the expansion files donwloader service in your application.
If not, please look at there : http://developer.android.com/google/play/expansion-files.html

# Adding the Plugin to your project

## Manual Android Installation

1. Copy the contens of `src/android/` to yoyr project's `src/` folder.

2. Modify your `AndroidManifest.xml` and add the following lines to your manifest tag:

```xml
<!-- Required to read and write the expansion files on shared storage -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

3) Modify your `res/xml/config.xml` to include the following line as a child to the `widget` tag:

```xml
<feature name="XAPKReader">
	<param name="android-package" value="com.phonegap.plugins.xapkreader.XAPKReader" />
</feature>
```

4) Add the `xapkreader.js` script to your `assets/www` folder (or javascripts folder, wherever you want really) and reference it in your main `index.html` file. This file's usage is described in the Plugin API section below.

## Automatic Installation

This plugin is based on plugman. To install it to your app, simply execute plugman as follows:

```
plugman install --platform android --project [TARGET-PATH] --plugin [PLUGIN-PATH]

where
    [TARGET-PATH] = path to folder containing your phonegap project
    [PLUGIN-PATH] = path to folder containing this plugin
```

# Using the plugin

The plugin creates the object `window.plugins.XAPKReader` with the method `get(filename, successCallback, errorCallback)`.

The plugin returns the file encoded in Base64.

A full example:

```javascript
window.plugins.XAPKReader.get(
	"filename.png",
	function (result) {
		var img = new Image();
		img.src = "data:image/png;base64," + data;
		document.body.appendChild(img);
	},
	function (error) {
		alert("An error occurred: " + error);
	}
);
```

# Licence MIT

Copyright 2013 Quentin Aupetit

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.