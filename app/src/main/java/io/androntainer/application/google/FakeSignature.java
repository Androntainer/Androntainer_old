package io.androntainer.application.google;

import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class FakeSignature extends AppCompatActivity {

    protected void main(int code) {
        if (check(FakeSignature.this)){
            setResult(RESULT_OK);
        } else {
            requires(FakeSignature.this, code);
        }
        finish();
    }

    public static boolean check(AppCompatActivity activity){
        return activity.checkSelfPermission("android.permission.FAKE_PACKAGE_SIGNATURE") == PackageManager.PERMISSION_GRANTED;
    }

    public static void requires(AppCompatActivity activity, int code){
        activity.requestPermissions(new String[]{"android.permission.FAKE_PACKAGE_SIGNATURE"}, code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3 && grantResults.length == 3) {
            setResult(grantResults[0] == PackageManager.PERMISSION_GRANTED ? RESULT_OK : RESULT_CANCELED);
            finish();
        }
    }
}
