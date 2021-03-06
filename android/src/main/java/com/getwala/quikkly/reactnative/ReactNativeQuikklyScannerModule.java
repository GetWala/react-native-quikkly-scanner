
package com.getwala.quikkly.reactnative;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.facebook.react.bridge.ReadableMap;
import com.onehilltech.metadata.ManifestMetadata;

import static com.getwala.quikkly.reactnative.Constants.*;

public class ReactNativeQuikklyScannerModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private Promise quikklyPromise;

    private final ReactApplicationContext reactContext;

    private String QUIKKLY_API_KEY;

    public static final String EXTRA_SHOW_OVERLAY = "show_overlay";

    public ReactNativeQuikklyScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(this);
        try {
            ManifestMetadata metadata = ManifestMetadata.get(reactContext);
            QUIKKLY_API_KEY = metadata.getValue("QUIKKLY_API_KEY");
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
            quikklyPromise.reject(E_FAILED_TO_START_ACTIVITY, e.getMessage());
            quikklyPromise = null;
        }
    }

    @ReactMethod
    public void generateCode(ReadableMap quikklyData, final Promise promise){
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, E_ACTIVITY_DOES_NOT_EXIST);
            return;
        }
        quikklyPromise = promise;
        try{
            final Intent intent = new Intent(currentActivity, GenerateQuikklyCodeActivity.class);
            intent.putExtra(TEMPLATE_NAME, quikklyData.getString(TEMPLATE_NAME));
            intent.putExtra(QUIKKLY_DATA_CODE, quikklyData.getString(QUIKKLY_DATA_CODE));
            intent.putExtra(BACKGROUND_COLOR, quikklyData.getString(BACKGROUND_COLOR));
            intent.putExtra(BORDER_COLOR, quikklyData.getString(BORDER_COLOR));
            intent.putExtra(DATA_COLOR, quikklyData.getString(DATA_COLOR));
            intent.putExtra(MASK_COLOR, quikklyData.getString(MASK_COLOR));
            intent.putExtra(OVERLAY_COLOR, quikklyData.getString(OVERLAY_COLOR));
            intent.putExtra(IMAGE_PATH, quikklyData.getString(IMAGE_PATH));
            currentActivity.startActivityForResult(intent, GENERATE_QUIKKLY_SCANNABLE);
        }catch (Exception e){
            quikklyPromise.reject(E_FAILED_TO_START_ACTIVITY, e.getMessage());
            quikklyPromise = null;
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
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
        } else if (requestCode == GENERATE_QUIKKLY_SCANNABLE){
            if(resultCode == Activity.RESULT_CANCELED || resultCode == Activity.RESULT_OK){
                quikklyPromise.resolve(null);
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}