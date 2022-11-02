package io.androntainer.application.fixedplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import io.androntainer.R;
import io.androntainer.application.fixedplay.App.SelectOne;
import io.androntainer.databinding.ActivityLauncherSettingsBinding;

public class Settings extends AppCompatActivity {

    private ActivityLauncherSettingsBinding binding;
    private PackageManager packageManager;
    private String _mode = "";
    private AppCompatTextView PackageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // packageManager
        packageManager = getPackageManager();
        // Intent
        Intent intent = getIntent();
        // Layout
        binding = ActivityLauncherSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Views
        AppCompatButton button1 = binding.button1;
        AppCompatButton button2 = binding.button2;
        PackageName = binding.textPakageName;
        Toolbar toolbar = binding.toolbar;
        // Click
        button2.setOnClickListener(view -> button2());
        //button1.setOnClickListener(v -> Core.AndroidApp(Settings.this, 8));
        toolbar.setNavigationOnClickListener(v -> finish());
        // Update Layout
        go();
        // Select App
        String setting = intent.getStringExtra("settings");
        if (setting != null) {
            if (setting.equals("select_app")) {
                button2();
            }
        }
    }

    @SuppressLint("SdCardPath")
    private void button2() {
        String btf = getString(R.string.btf);
        String act = getString(R.string.act);
        String mode = getString(R.string.mode);
        final String[] items3 = new String[]{btf, act};
        AlertDialog alertDialog3 = new AlertDialog.Builder(Settings.this)
                .setTitle(mode)
                .setIcon(R.drawable.ic_baseline_androntainer_plat_logo_24)
                .setItems(items3, (dialogInterface, i) -> {
                    _mode = "";
                    Intent select = new Intent(Settings.this, SelectOne.class);
                    switch (i) {
                        case 0:
                            _mode = "r2";
                            select.putExtra("_mode", _mode);
                            startActivity(select);
                            break;
                        case 1:
                            _mode = "r1";
                            select.putExtra("_mode", _mode);
                            startActivity(select);
                            break;
                    }
                })
                .create();
        alertDialog3.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        go();
    }

    @SuppressLint("SetTextI18n")
    public void go() {
        SharedPreferences read = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        final String app = read.getString("app", "");
        if (app.length() < 1) return;

        final String label = read.getString("label", "");
        final String uri = read.getString("uri", "");
        final String className = read.getString("class", "");
        final Drawable icon;
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(app, 0);
            icon = (appInfo.loadIcon(packageManager));
            runOnUiThread(() -> {
                PackageName.setText(label);
                (binding.applistItem.itemImg).setImageDrawable(icon);
                (binding.applistItem.itemText).setText(app);
                (binding.applistItem.itemPackageName).setText(uri + "\n" + className);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}