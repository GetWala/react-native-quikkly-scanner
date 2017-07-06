package com.getwala.quikkly.reactnative;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import net.quikkly.android.Quikkly;
import net.quikkly.android.ui.RenderTagView;
import net.quikkly.core.Skin;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import static com.getwala.quikkly.reactnative.Constants.BACKGROUND_COLOR;
import static com.getwala.quikkly.reactnative.Constants.BORDER_COLOR;
import static com.getwala.quikkly.reactnative.Constants.DATA_COLOR;
import static com.getwala.quikkly.reactnative.Constants.IMAGE_PATH;
import static com.getwala.quikkly.reactnative.Constants.MASK_COLOR;
import static com.getwala.quikkly.reactnative.Constants.OVERLAY_COLOR;
import static com.getwala.quikkly.reactnative.Constants.QUIKKLY_DATA_CODE;
import static com.getwala.quikkly.reactnative.Constants.TEMPLATE_NAME;
import static com.getwala.quikkly.reactnative.R.id.render_tag;

/**
 * Created by jmaleonard on 6/29/17.
 */

public class GenerateQuikklyCodeActivity extends AppCompatActivity {

    private String backgroundColor;
    private String borderColor;
    private String dataColor;
    private String maskColor;
    private String overlayColor;
    private String quikklyCode;
    private String imagePath;
    private String templateName;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.render_activity);
        Quikkly.getDefaultInstance();
        validateParams();
        renderCode();
    }

    private void validateParams() {
        backgroundColor = getIntent().getStringExtra(BACKGROUND_COLOR);
        if (backgroundColor == null || backgroundColor.isEmpty()) {
            backgroundColor = "#5cb7a6";
        }
        borderColor = getIntent().getStringExtra(BORDER_COLOR);
        if (borderColor == null || borderColor.isEmpty()) {
            borderColor = "#ffffff";
        }
        dataColor = getIntent().getStringExtra(DATA_COLOR);
        if (dataColor == null || dataColor.isEmpty()) {
            borderColor = "#000000";
        }
        maskColor = getIntent().getStringExtra(MASK_COLOR);
        if (maskColor == null || maskColor.isEmpty()) {
            maskColor = "#5cb7a6";
        }
        overlayColor = getIntent().getStringExtra(OVERLAY_COLOR);
        if (overlayColor == null || overlayColor.isEmpty()) {
            overlayColor = "#ffffff";
        }
        quikklyCode = getIntent().getStringExtra(QUIKKLY_DATA_CODE);
        if (quikklyCode == null || quikklyCode.isEmpty()) {
            quikklyCode = "0";
        }
        imagePath = getIntent().getStringExtra(IMAGE_PATH);
        if (imagePath == null || imagePath.isEmpty()) {
            //defaults to quikkly squiddy
            imagePath = "https://s3-eu-west-1.amazonaws.com/qkly-service-albums/temp_icons/squiddy.png";
        }
        templateName = getIntent().getStringExtra(TEMPLATE_NAME);
        if (templateName == null || templateName.isEmpty()) {
            templateName = "template0002style5";
        }
    }

    RenderTagView renderView;

    private void renderCode() {
        Intent resultIntent = new Intent();
        renderView = (RenderTagView) findViewById(render_tag);
        Skin skin = new Skin();

        skin.backgroundColor = backgroundColor;
        skin.borderColor = borderColor;
        skin.dataColor = dataColor;
        skin.maskColor = maskColor;
        skin.overlayColor = overlayColor;

        skin.imageFit = Skin.IMAGE_FIT_MEET;
        skin.logoFit = Skin.IMAGE_FIT_DEFAULT;
        BigInteger data = new BigInteger(quikklyCode);
        try {
            skin.imageUrl = readAndBase64EncodeFromAssets(this, imagePath);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            setResult(Activity.RESULT_OK, resultIntent);
        }

        renderView.setAll(templateName, data, skin);

    }

    private String readAndBase64EncodeFromAssets(Context context, String pathToFile) throws IOException {
        String mime;
        if (pathToFile.toLowerCase().endsWith(".png"))
            mime = "image/png";
        else if (pathToFile.toLowerCase().endsWith(".jpg") || pathToFile.toLowerCase().endsWith(".jpeg"))
            mime = "image/jpeg";
        else
            throw new IllegalArgumentException("Unknown image file extension, cannot determine mime type: " + pathToFile);
        Uri imagePath = Uri.fromFile(new File(pathToFile));
        InputStream stream = getContentResolver().openInputStream(imagePath);
        try {
            byte[] bytes = IOUtils.toByteArray(stream);
            String dataUri = "data:" + mime + ";base64," + Base64.encodeToString(bytes, Base64.DEFAULT);
            return dataUri;
        } finally {
            stream.close();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect renderViewSize = new Rect();
        renderView.getDrawingRect(renderViewSize);

        if(!renderViewSize.contains((int)event.getX() , (int)event.getY())){
            finish();
        }
        return true;
    }
}


