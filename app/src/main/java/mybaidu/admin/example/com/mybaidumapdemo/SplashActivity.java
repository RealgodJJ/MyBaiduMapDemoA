package mybaidu.admin.example.com.mybaidumapdemo;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    public static List<String> needRequestPermissions = new ArrayList<>();
    private PermissionUtils permissionUtils;

    static {
        needRequestPermissions.add(Manifest.permission.READ_PHONE_STATE);
        needRequestPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        needRequestPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        needRequestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        needRequestPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        permissionUtils = new PermissionUtils(this);
        //TODO:用户设置禁止不再提示时，应提醒用户手动设置
        permissionUtils.request(needRequestPermissions, 100, new PermissionUtils.CallBack() {
            @Override
            public void grandAll() {
                toMainActivity();
                finish();
            }

            @Override
            public void denied() {
                finish();
            }
        });
    }

    private void toMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
