package mybaidu.admin.example.com.mybaidumapdemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorInstance implements SensorEventListener {
    private Context context;
    private SensorManager sensorManager;

    public SensorInstance(Context context) {
        this.context = context;
    }

    public void start() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            //设置传感器类型并进行创建
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            if (sensor != null) {
                //注册传感器监听
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = event.values[SensorManager.DATA_X];
            if (listener != null) {
                listener.onOrientation(x);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private OnOrientationChangedListener listener;

    void setOnOrientationChangedListener(OnOrientationChangedListener listener) {
        this.listener = listener;
    }

    public interface OnOrientationChangedListener {
        void onOrientation(float x);
    }
}
