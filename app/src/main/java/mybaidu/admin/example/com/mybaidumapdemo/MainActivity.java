package mybaidu.admin.example.com.mybaidumapdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

public class MainActivity extends AppCompatActivity {
    private static final int ITEM_ID_NORMAL_MAP = 101;
    private static final int ITEM_ID_SAFELLITE_MAP = 102;
    private static final String TAG = "RealgodJJ";
    private MapView mvBaidu;
    private BaiduMap baiduMap;
    private LocationInstance locationInstance;
    private BDLocation lastBdLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mvBaidu = findViewById(R.id.mv_baidu);
        baiduMap = mvBaidu.getMap();

        init();
    }

    private void init() {
        initLocationDetect();
    }

    private void initLocationDetect() {
        locationInstance = new LocationInstance(this, new LocationInstance.MyLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                super.onReceiveLocation(location);
                lastBdLocation = location;
                Log.d(TAG, "onReceiveLocation: " + location.getLatitude() + ", " +
                        location.getLongitude() + ", " + location.getAddrStr());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationInstance.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationInstance.stop();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, ITEM_ID_NORMAL_MAP, 0, "切换为普通地图");
        menu.add(Menu.NONE, ITEM_ID_SAFELLITE_MAP, 0, "切换为卫星地图");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //切换地图的类型
            case ITEM_ID_NORMAL_MAP:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;

            case ITEM_ID_SAFELLITE_MAP:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
