package mybaidu.admin.example.com.mybaidumapdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AddressView avInfo;
    private MapView mvBaidu;
    private BaiduMap baiduMap;
    private static final int ITEM_ID_NORMAL_MAP = 101;
    private static final int ITEM_ID_SAFELLITE_MAP = 102;
    private static final int ITEM_LOCATION = 103;
    private static final int ITEM_SHOW_HOTEL = 104;
    private static final String TAG = "RealgodJJ";
    public static final String ADDRESS_INFO = "addressInfo";
    private LocationInstance locationInstance;
    private BDLocation lastBdLocation;
    private SensorInstance sensorInstance;
    private boolean isFirstLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mvBaidu = findViewById(R.id.mv_baidu);
        avInfo = findViewById(R.id.av_info);
        baiduMap = mvBaidu.getMap();
        //直接缩放至缩放级别16
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16f));
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        init();
    }

    private void initEvent() {
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                avInfo.setVisibility(View.GONE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle extraInfo = marker.getExtraInfo();
                AddressInfo addressInfo = (AddressInfo) extraInfo.getSerializable(ADDRESS_INFO);

                //展示图文信息
                avInfo.setAddressInfo(addressInfo);
                return false;
            }
        });
    }

    private void init() {
        initLocationDetect();
        initSensorDetect();
        initEvent();
    }

    private void initSensorDetect() {
        sensorInstance = new SensorInstance(getApplicationContext());
        sensorInstance.setOnOrientationChangedListener(new SensorInstance.OnOrientationChangedListener() {
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
        menu.add(Menu.NONE, ITEM_SHOW_HOTEL, 0, "定位附近宾馆");
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

            case ITEM_SHOW_HOTEL:
                avInfo.setVisibility(View.GONE);
                //定义Maker坐标点
                List<AddressInfo> addressInfos = AddressInfoLab.generateDatas();
                for (AddressInfo addressInfo : addressInfos) {
                    LatLng hotel = new LatLng(addressInfo.getLatitude(), addressInfo.getLongitude());
                    //构建Marker图标
                    BitmapDescriptor hotelBitmap = BitmapDescriptorFactory.fromResource(R.drawable.hotel);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ADDRESS_INFO, addressInfo);
                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions hotelOption = new MarkerOptions().position(hotel)
                            .extraInfo(bundle).icon(hotelBitmap);
                    //在地图上添加Marker，并显示
                    baiduMap.addOverlay(hotelOption);
                }
                //自动移动到第一个宾馆的位置
                if (addressInfos.isEmpty()) {
                    Toast.makeText(this, "附近未找到宾馆！", Toast.LENGTH_SHORT).show();
                }
                LatLng hotel = new LatLng(addressInfos.get(0).getLatitude(), addressInfos.get(0).getLongitude());
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(hotel));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
