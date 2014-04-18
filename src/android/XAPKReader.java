package org.apache.cordova.xapkreader;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Base64;
import android.util.Log;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.Helpers;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
// import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class XAPKReader extends CordovaPlugin {

    private static final String LOG_TAG = "XAPKReader";

    /**
     * Executes the request.
     *
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     *     cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     *     cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return                Whether the action was valid.
     * @throws JSONException
     *
     * @sa https://github.com/apache/cordova-android/blob/master/framework/src/org/apache/cordova/CordovaPlugin.java
     */
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("get")) {
            final String filename = args.getString(0);
            final int mainVersion = args.getInt(1);
            final int patchVersion = args.getInt(2);
            final Context ctx = cordova.getActivity().getApplicationContext();
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        // Read file as array buffer
                        byte[] data = XAPKReader.readFile(ctx, filename, mainVersion, patchVersion);
                        if (null != data) {
                            // Encode to Base64 string
                            String encoded = Base64.encodeToString(data, Base64.DEFAULT);
                            // Return file data as base64 string
                            callbackContext.success(encoded);
                        }
                        else {
                            callbackContext.error("File not found.");
                        }
                    }
                    catch(Exception e) {
                        // e.printStackTrace();
                        callbackContext.error(e.getMessage());
                    }
                }
            });
            return true;
        }
        return false;
    }

    /**
     * Read file in APK Expansion file.
     *
     * @param ctx      The context of the main Activity.
     * @param filename The filename to read
     * @return         Byte array of data
     */
    private static byte[] readFile(Context ctx, String filename, int mainVersion, int patchVersion) throws IOException {
        // Get APKExpensionFile
        ZipResourceFile expansionFile = APKExpansionSupport.getAPKExpansionZipFile(ctx, mainVersion, patchVersion);

        if (null == expansionFile) {
            Log.e(LOG_TAG, "APKExpansionFile not found.");
            return null;
        }

        // Find file in ExpansionFile
        String fileName = Helpers.getExpansionAPKFileName(ctx, true, patchVersion);
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        AssetFileDescriptor file = expansionFile.getAssetFileDescriptor(fileName + "/" + filename);

        if (null == file) {
            Log.e(LOG_TAG, "File not found (" + filename + ").");
            return null;
        }

        // Read file
        int size = (int) file.getLength();
        byte[] data = new byte[size];
        BufferedInputStream buf = new BufferedInputStream(file.createInputStream());
        buf.read(data, 0, data.length);
        buf.close();

        return data;
    }

}