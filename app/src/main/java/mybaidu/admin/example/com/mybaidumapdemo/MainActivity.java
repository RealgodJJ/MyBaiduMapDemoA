package mybaidu.admin.example.com.mybaidumapdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.MapView;

public class MainActivity extends AppCompatActivity {
    private MapView mvBaidu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mvBaidu = findViewById(R.id.mv_baidu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvBaidu.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvBaidu.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvBaidu.onResume();
    }
}
