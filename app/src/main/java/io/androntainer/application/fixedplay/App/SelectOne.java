package io.androntainer.application.fixedplay.App;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.ArrayList;
import java.util.List;

import io.androntainer.R;
import io.androntainer.databinding.ActivityApplistBinding;

public class SelectOne extends AppCompatActivity {

    private ActivityApplistBinding binding;
    private final List<Item> list = new ArrayList<>();
    private ListView listView;
    private ProgressBar progressBar;
    private String _mode;
    private String _uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressBar = binding.progressBar;
        Intent intent = getIntent();
        _mode = intent.getStringExtra("_mode");
        _uri = intent.getStringExtra("_uri");

        new Thread(() -> {
            loadAllApps(makeIntent());
            final ItemAdapter itemAdapter = new ItemAdapter(SelectOne.this, R.layout.item_applist, list);
            itemAdapter.setMode(_mode);
            itemAdapter.setUri(_uri);
            runOnUiThread(() -> {
                listView = binding.listview;
                listView.setAdapter(itemAdapter);
                progressBar.setVisibility(View.GONE);
            });
        }).start();

        Toolbar toolbar = binding.toolbar;
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    private void loadAllApps(Intent intent) {
        List<ResolveInfo> mApps;
        mApps = new ArrayList<>();
        try {
            mApps.addAll(this.getPackageManager().queryIntentActivities(intent, 0));
            PackageManager pm = getPackageManager();
            for (ResolveInfo r : mApps) {
                Item item = new Item(
                        r.loadLabel(pm).toString(),
                        r.activityInfo.packageName,
                        r.activityInfo.name,
                        r.loadIcon(pm)
                );
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant, UseSwitchCompatOrMaterialCode")
    private Intent makeIntent() {
        switch (_mode) {
            case "r2":
            case "r1":
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_HOME);
                return mainIntent;
        }
        return null;
    }
}