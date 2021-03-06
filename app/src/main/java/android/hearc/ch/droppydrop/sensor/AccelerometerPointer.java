package android.hearc.ch.droppydrop.sensor;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hearc.ch.droppydrop.R;
import android.preference.PreferenceManager;


/**
 * Class AccelerometerPointer
 * Reperesent a pointer in a given zone that moves depending
 * the accelerometer values
 */
public class AccelerometerPointer implements SensorEventListener {

    private static final String TAG = AccelerometerPointer.class.getSimpleName();

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private Point origin;
    private Point pointer;

    private SharedPreferences sharedPreferences;

    private int sensibility;

    /**
     * Constructor of AccelerometerPointer
     *
     * @param context parent context
     * @param height  height of the view used for origin point
     * @param width   width of the screen used for origin point
     */
    public AccelerometerPointer(Context context, int height, int width) {
        super();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        sensibility = sharedPreferences.getInt(context.getString(R.string.sensibility), 0) / 6 + 1;

        origin = new Point();
        pointer = new Point();

        resetPointer(height, width);
    }

    /**
     * When value of sensor changed, a the pointer is displaced
     * depending on the accelerometer values
     *
     * @param sensorEvent sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            //float z = sensorEvent.values[2];
            synchronized (pointer) {
                pointer.x -= x * sensibility;
                pointer.y += y * sensibility;
            }

        }
    }

    /**
     * When the accuracy changed, noting is done
     *
     * @param sensor   sensor
     * @param accuracy accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do
    }

    /**
     * Reset the pointer to the center of the screen
     * by computing a new origin point
     *
     * @param height height of the new zone
     * @param width  width of the new zone
     */
    public void resetPointer(int height, int width) {
        origin.x = width / 2;
        origin.y = height / 2;

        pointer.x = width / 2;
        pointer.y = height / 2;
    }

    /**
     * Return a new point at the position of the pointer
     *
     * @return Point
     */
    public Point getPointer() {

        return new Point(pointer);
    }

    /**
     * Stop listening to the accelerometer sensor
     */
    public void stopAccelerometerSensor() {
        senSensorManager.unregisterListener(this);
    }

    /**
     * Resume listening to the accelerometer sensor
     */
    public void resumeAccelerometerSensor() {
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Displace the actual x value of the pointer
     *
     * @param x new x position
     */
    public void setPointerX(int x) {
        synchronized (pointer) {
            pointer.x = x;
        }
    }

    /**
     * Displace the actual y value of the pointer
     *
     * @param y new y position
     */
    public void setPointerY(int y) {
        synchronized (pointer) {
            pointer.y = y;
        }
    }
}
