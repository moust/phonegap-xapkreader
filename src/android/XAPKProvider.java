package org.apache.cordova.xapkreader;

import com.android.vending.expansion.zipfile.APEZProvider;

public class XAPKProvider extends APEZProvider {
    @Override
    public String getAuthority() {
        return "org.apache.cordova.xapkreader";
    }
}