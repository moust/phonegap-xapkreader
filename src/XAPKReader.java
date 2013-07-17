/**
 * cordova is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Quentin Aupetit 2013
 */

package com.phonegap.plugins.xapkreader;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Base64;
import android.util.Log;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.Helpers;

public class XAPKReader extends CordovaPlugin
{
	final static int mainVersion = 1;
	final static int patchVersion = 1;
	
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		if (action.equals("get")) {
			final String filename = args.getString(0);
			try {
        		Context ctx = cordova.getActivity().getApplicationContext();
        		// Read file as array buffer
        		byte[] data = XAPKReader.readFile(ctx, filename);
	        	if (null != data) {
	        		// Encode to Base64 string
					String encoded = Base64.encodeToString(data, Base64.DEFAULT);
					// Return file data as base64 string
					callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, encoded));
	        	}
	        	else {
	        		callbackContext.error("File not found.");
	        	}
        	}
    		catch(Exception e) {
    			e.printStackTrace();
    			callbackContext.error(e.getMessage());
    		}
			return true;
		}
		return false;
	}
	
	private static byte[] readFile(Context ctx, String filename) throws IOException {
		// Get APKExpensionFile
		ZipResourceFile expansionFile = APKExpansionSupport.getAPKExpansionZipFile(ctx, XAPKReader.mainVersion, XAPKReader.patchVersion);
		
		if (null == expansionFile) {
			Log.e("XAPKReader", "APKExpansionFile not found.");
			return null;
    	}
		
		// Find file in ExpansionFile
		String fileName = Helpers.getExpansionAPKFileName(ctx, true, XAPKReader.patchVersion);
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
	    AssetFileDescriptor file = expansionFile.getAssetFileDescriptor(fileName + "/" + filename);
		
		if (null == file) {
			Log.e("XAPKReader", "File not found (" + filename + ").");
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