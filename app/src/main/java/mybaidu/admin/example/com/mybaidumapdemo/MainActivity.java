package mybaidu.admin.example.com.mybaidumapdemo;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {
    private static final int ITEM_ID_NORMAL_MAP = 101;
    private static final int ITEM_ID_SAFELLITE_MAP = 102;
    private static final int ITEM_LOCATION = 103;
    private static final String TAG = "RealgodJJ";
    private MapView mvBaidu;
    private BaiduMap baiduMap;
    private LocationInstance locationInstance;
    private BDLocation lastBdLocation;
    private SensorInstance sensorInstance;
    private boolean isFirstLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mvBaidu = findViewById(R.id.mv_baidu);
        baiduMap = mvBaidu.getMap();
        //直接缩放至缩放级别16
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16f));
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        init();
    }

    private void init() {
        initLocationDetect();
        initSensorDetect();
    }

    private void initSensorDetect() {
        sensorInstance = new SensorInstance(getApplicationContext());
        sensorInstance.setOnOrientationChangedListener(new SensorInstance.onOrientationChangedListener() {
            @Override
            public void onOrientation(float x) {
                //设置定位图标
                if (lastBdLocation == null) {
                    return;
                }

                //构造定位数据
                MyLocationData locationData = new MyLocationData.Builder()
                        .accuracy(lastBdLocation.getRadius()).direction(x)
                        .latitude(lastBdLocation.getLatitude())
                        .longitude(lastBdLocation.getLongitude()).build();
                //设置定位数据
                baiduMap.setMyLocationData(locationData);
                //设置定位图层的配置（定位模式，是否允许方向信息，用户自定义图标）
                BitmapDescriptor currentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark);
                MyLocationConfiguration configuration = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL, true, null);
                baiduMap.setMyLocationConfiguration(configuration);

                if (isFirstLocation) {
                    isFirstLocation = false;
                    LatLng point = new LatLng(lastBdLocation.getLatitude(), lastBdLocation.getLongitude());
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(point));
                }

                //当不需要定位图层时关闭定位图层
//                baiduMap.setMyLocationEnabled(false);
            }
        });
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
        sensorInstance.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationInstance.stop();
        sensorInstance.stop();
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
        menu.add(Menu.NONE, ITEM_LOCATION, 0, "定位当前位置");
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

            case ITEM_LOCATION:
                baiduMap.clear();
                //定义Maker坐标点
                LatLng point = new LatLng(lastBdLocation.getLatitude(), lastBdLocation.getLongitude());
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
                //在地图上添加Marker，并显示
//                baiduMap.addOverlay(option);
                //创建一个新坐标点
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(point));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
