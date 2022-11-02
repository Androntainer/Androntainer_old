package io.androntainer.application.fixedplay.App;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.preference.PreferenceManager;

import java.util.List;

import io.androntainer.R;
import io.androntainer.application.fixedplay.Settings;


public class ItemAdapter extends ArrayAdapter<Item> {
    private final int layoutId;
    private String mode = "r2";
    private String uri = "";

    public ItemAdapter(Context context, int layoutId, List<Item> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @NonNull
    @Override
    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Item item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        final String packageName = item.getPackageName();

        ((AppCompatImageView) view.findViewById(R.id.item_img)).setImageDrawable(item.getAppIcon());
        ((AppCompatTextView) view.findViewById(R.id.item_text)).setText(item.getName());
        ((AppCompatTextView) view.findViewById(R.id.item_packageName)).setText(packageName);
        view.setOnClickListener(v -> {
            PackageManager pm = getContext().getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            if (intent != null) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                editor.putString("app", packageName);
                editor.putString("label", item.getName());
                editor.putString("class", item.getClassName());
                editor.putString("uri", uri);
                editor.putString("mode", mode);
                editor.apply();
                intent = new Intent(getContext(), Settings.class);
                getContext().startActivity(intent);
            } else {
                Toast.makeText(getContext(), R.string.error_could_not_start, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}