package io.androntainer.application.fixedplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.blankj.utilcode.util.AppUtils;

public class FixedPlay extends AppCompatActivity {

    PackageManager packageManager;
    final String THIS_PACKAGE = AppUtils.getAppPackageName();
    String mode = "r2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        packageManager = getPackageManager();
        go();
        finish();
    }

    @SuppressLint("WrongConstant")
    public void go() {
        SharedPreferences read = PreferenceManager.getDefaultSharedPreferences(FixedPlay.this);
        String app = read.getString("app", "");
        String claseName = read.getString("class", "");
        mode = read.getString("mode", "r2");
        if (app.length() > 0 && !app.equals(THIS_PACKAGE)) {
            switch (mode) {
                case "r2": {
                    Log.w("MainActivity mode2", mode);
                    Intent intent = packageManager.getLaunchIntentForPackage(app);
                    if (intent != null) startActivity(intent);
                    break;
                }
                case "r1":
                    if (claseName.length() > 5) {
                        Intent intent = new Intent();
                        intent.setClassName(app, claseName);
                        startActivity(intent);
                    } else {
                        Intent intent;
                        intent = packageManager.getLaunchIntentForPackage(app);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        this.startActivity(intent);
                    }
                    break;
            }
        } else {
            Intent intent = new Intent(FixedPlay.this, Settings.class);
            startActivity(intent);
        }
    }
}