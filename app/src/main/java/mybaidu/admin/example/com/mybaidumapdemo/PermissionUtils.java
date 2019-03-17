package mybaidu.admin.example.com.mybaidumapdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PermissionUtils {
    private Activity activity;
    private Map<Integer, CallBack> requestBack;
//    private int requestCode;
//    private CallBack callBack;

    public interface CallBack {
        void grandAll();

        void denied();
    }

    @SuppressLint("UseSparseArrays")
    public PermissionUtils(Activity activity) {
        this.activity = activity;
        requestBack = new HashMap<>();
    }

    public void request(List<String> needPermissions, int requestCode, CallBack callBack) {
//        if (Build.VERSION.SDK_INT  < 23) {
//            callBack.grandAll();
//            return;
//        }

        if (activity == null) {
            throw new IllegalArgumentException("Activity is null");
        }
        requestBack.put(requestCode, callBack);
//        this.requestCode = requestCode;
//        this.callBack = callBack;

        //判断是否所有权限都已授权
        List<String> requestPermissions = new ArrayList<>();
        for (String permission : needPermissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions.add(permission);
            }
        }

        if (requestPermissions.isEmpty()) {
            callBack.grandAll();
            return;
        }

        activity.requestPermissions(requestPermissions.toArray(new String[]{}), requestCode);
    }

    //申请权限的结果
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (Integer key : requestBack.keySet()) {
            if (key == requestCode) {
                boolean grandAll = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        grandAll = false;
                        Toast.makeText(activity, permissions[i] + "未授权", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (grandAll) {
                    for (Integer request : requestBack.keySet())
                        Objects.requireNonNull(requestBack.get(request)).grandAll();
                } else {
                    for (Integer request : requestBack.keySet())
                        Objects.requireNonNull(requestBack.get(request)).denied();
                }
            }
        }
    }
}
