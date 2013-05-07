phonegap-xapkreader
===================

Cordova plugin to access files in APK Expansion Files

# Adding the Plugin to your project

1. In your project properties, add the *APK Expansion Zip Library* and *Downloader Library* to your project (You can find them in `<sdk>/extras/google/google_market_apk_expansion/` folder).
2. Move `xapkreader.js` to your peoject's `www` folder and include a reference to it in yout html files.
3. Creat a folder called `com.phonegap.plugins.xapkreader` within your project's `src` folder and copy `XAPKReader.java` into it.
4. In the `XAPKReader.java` file, change the `mainVersion` and `patchVersion` variables to match with your APK Expansion File's version.
5. In your `res/wml/plugins.xml` file add the following line:

```xml
<plugin name="XAPKReader" value="com.phonegap.plugins.xapkreader.XAPKReader"/>
```

6. Make sure you have the following permissions in your `AndroidManifest.xml` file to be abble to read the expansion files on shared storage:

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

# Using the plugin

The plugin creates the object `window.plugins.barcodeScanner` with the method `get(filename, success, fail)`.

A full example:

```javascript
window.plugins.xapkreader.get("monimage.png", function(data){
	var img = new Image();
	img.src = "data:image/png;base64," + data;
	document.body.appendChild(img);
}, function(error){
	console.log("An error occurred: " + error);
});
```

# Licence

Copyright 2013 Quentin Aupetit

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.