package org.apache.cordova.xapkreader;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.Helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class XAPKReader extends CordovaPlugin {

    private static final String LOG_TAG = "XAPKReader";

    private int mainVersion = 1;

    private int patchVersion = 1;

    private long fileSize = 0L;

    @Override
    public void initialize(final CordovaInterface cordova, CordovaWebView webView) {

        int mainVersionId = cordova.getActivity().getResources().getIdentifier("main_version", "integer", cordova.getActivity().getPackageName());
        mainVersion = cordova.getActivity().getResources().getInteger(mainVersionId);

        int patchVersionId = cordova.getActivity().getResources().getIdentifier("patch_version", "integer", cordova.getActivity().getPackageName());
        patchVersion = cordova.getActivity().getResources().getInteger(patchVersionId);

        int fileSizeId = cordova.getActivity().getResources().getIdentifier("file_size", "integer", cordova.getActivity().getPackageName());
        fileSize = cordova.getActivity().getResources().getInteger(fileSizeId);

        final Bundle bundle = new Bundle();
        bundle.putInt("mainVersion", mainVersion);
        bundle.putInt("patchVersion", patchVersion);
        bundle.putLong("fileSize", fileSize);

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = cordova.getActivity().getApplicationContext();
                Intent intent = new Intent(context, XAPKDownloaderActivity.class);
                intent.putExtras(bundle);
                cordova.getActivity().startActivity(intent);
            }
        });

        super.initialize(cordova, webView);
    }

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
            final Context ctx = cordova.getActivity().getApplicationContext();
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        // Read file
                        PluginResult result = XAPKReader.readFile(ctx, filename, mainVersion, patchVersion, PluginResult.MESSAGE_TYPE_ARRAYBUFFER);
                        callbackContext.sendPluginResult(result);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        callbackContext.error(e.getLocalizedMessage());
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
     * @return         PluginResult
     */
    private static PluginResult readFile(Context ctx, String filename, int mainVersion, int patchVersion, final int resultType) throws IOException {
        // Get APKExpensionFile
        ZipResourceFile expansionFile = APKExpansionSupport.getAPKExpansionZipFile(ctx, mainVersion, patchVersion);

        if (null == expansionFile) {
            Log.e(LOG_TAG, "APKExpansionFile not found.");
            throw new IOException("APKExpansionFile not found.");
        }

        // Find file in ExpansionFile
        AssetFileDescriptor fileDescriptor = expansionFile.getAssetFileDescriptor(filename);

        if (null == fileDescriptor) {
            Log.e(LOG_TAG, "File not found (" + filename + ").");
            throw new IOException("File not found (" + filename + ").");
        }

        // Read file
        InputStream inputStream = fileDescriptor.createInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            os.write(buffer, 0, read);
        }
        os.flush();

        // get file content type
        String contentType = URLConnection.guessContentTypeFromStream(inputStream);

        PluginResult result;
        switch (resultType) {
            case PluginResult.MESSAGE_TYPE_STRING:
                result = new PluginResult(PluginResult.Status.OK, os.toString("UTF-8"));
                break;
            case PluginResult.MESSAGE_TYPE_ARRAYBUFFER:
                result = new PluginResult(PluginResult.Status.OK, os.toByteArray());
                break;
            case PluginResult.MESSAGE_TYPE_BINARYSTRING:
                result = new PluginResult(PluginResult.Status.OK, os.toByteArray(), true);
                break;
            default: // Base64.
                byte[] base64 = Base64.encode(os.toByteArray(), Base64.NO_WRAP);
                String s = "data:" + contentType + ";base64," + new String(base64, "US-ASCII");
                result = new PluginResult(PluginResult.Status.OK, s);
        }

        return result;
    }

}