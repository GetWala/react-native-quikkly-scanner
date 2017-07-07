package com.getwala.quikkly.reactnative;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import net.quikkly.android.Quikkly;
import net.quikkly.android.ui.ScanActivity;
import net.quikkly.core.ScanResult;
import net.quikkly.core.Tag;

import static com.getwala.quikkly.reactnative.Constants.QUIKKLY_SCANNED_CODE;

/**
 * Created by jmaleonard on 6/26/17.
 */

public class ScanQuikklyCodeActivity extends ScanActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.quikkly_scan_activity);
        Quikkly.getDefaultInstance();
    }

    @Override
    public void onScanResult(@Nullable ScanResult result) {
        if (result != null && result.isEmpty() == false) {
            Tag scannedCode = result.tags[0];
            Intent resultIntent = new Intent();
            resultIntent.putExtra(QUIKKLY_SCANNED_CODE, scannedCode.getData().toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
