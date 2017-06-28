
package com.getwala.quikkly.reactnative;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import net.quikkly.android.QuikklyBuilder;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.onehilltech.metadata.ManifestMetadata;

import static com.getwala.quikkly.reactnative.Constants.*;

public class ReactNativeQuikklyScannerModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private Promise quikklyPromise;

    private final ReactApplicationContext reactContext;

    private String QUIKKLY_API_KEY;

    public ReactNativeQuikklyScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(this);
        try {
            ManifestMetadata metadata = ManifestMetadata.get(reactContext);
            QUIKKLY_API_KEY = metadata.getValue("QUIKKLY_API_KEY");
            Log.d("QUIKKLY_API_KEY", QUIKKLY_API_KEY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new QuikklyBuilder()
                .setApiKey(QUIKKLY_API_KEY)
                .loadDefaultBlueprintFromLibraryAssets(reactContext)
                .build()
                .setAsDefault();
    }

    @Override
    public String getName() {
        return PACKAGE_NAME;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @ReactMethod
    public void scanQuikklyCode(final Promise promise) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, E_ACTIVITY_DOES_NOT_EXIST);
            return;
        }
        quikklyPromise = promise;
        try {
            final Intent intent = new Intent(currentActivity, ScanQuikklyCodeActivity.class);
            currentActivity.startActivityForResult(intent, SCAN_QUIKKLY_CODE_REQUEST_CODE);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            quikklyPromise.reject(E_FAILED_TO_START_ACTIVITY, e.getMessage());
            quikklyPromise = null;
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.d("GOT HERE", String.valueOf(requestCode));
        if (requestCode == SCAN_QUIKKLY_CODE_REQUEST_CODE) {
            if (quikklyPromise != null) {
                if (resultCode == Activity.RESULT_CANCELED) {
                    quikklyPromise.reject(E_ACTIVITY_CANCELLED, E_ACTIVITY_CANCELLED);
                } else {
                    if (resultCode == Activity.RESULT_OK) {
                        String quikklyScannedCode = (String) data.getExtras().getString(QUIKKLY_SCANNED_CODE);
                        if (quikklyScannedCode == null) {
                            quikklyPromise.reject(NO_CODE_FOUND, NO_CODE_FOUND);
                        } else {
                            quikklyPromise.resolve(quikklyScannedCode);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}