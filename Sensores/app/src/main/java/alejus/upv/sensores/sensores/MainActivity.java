package alejus.upv.sensores.sensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends ActionBarActivity implements SensorEventListener{

    private TextView Azimuth;
    private TextView Pitch;
    private TextView Roll;
    private float[] accelerometerValues;
    private float[] magneticFieldValues;
    private float[] rotationMatrix = new float[9];
    private float[] I = new float[9];
    private float[] orientationVals = new float[3];
    private Sensor mMagneticField;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private float mLowPassAzimuth = 0.0f;
    private float mLowPassPitch = 0.0f;
    private float mLowPassRoll = 0.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Link with our layout*/
        Azimuth = (TextView) findViewById(R.id.azimuth);
        Pitch = (TextView) findViewById(R.id.pitch);
        Roll = (TextView) findViewById(R.id.roll);
        /*The sensor manager is used to manage the sensor hardware available on Android devices
        * Use <getSystemService> to return a reference to the Sensor Manager Service:*/
        String service_name = Context.SENSOR_SERVICE;
        mSensorManager = (SensorManager) getSystemService(service_name);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Log.d("onCreate","Done!");
        /*Rather than interacting with the sensor hardware directly, they are represented by
        * sensor objects that describe the properties of the hardware sensor that represents.
        * Now its time to find the sensors available in our device:*/
        /*
        List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
         */
        /*All sensors available are saved in allSensor list*/
        /*for (Sensor sensor: allSensors){
            showSensor(sensor.getName());
        }
        output.setMovementMethod(new ScrollingMovementMethod());*/
    }

    /*private void showSensor(String string) {
        output.append(string + "\n");
    }*/

    /*It's also important to note that this example uses the onResume() and onPause() callback methods
    to register and unregister the sensor event listener. As a best practice you should always disable
    sensors you don't need, especially when your activity is paused. Failing to do so can drain the battery
    in just a few hours because some sensors have substantial power requirements and can use up battery power quickly.
    The system will not disable sensors automatically when the screen turns off.*/
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_UI);
        Log.d("onResume","mMagneticField registerListener");
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        Log.d("onResume","mAccelerometer registerListener");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        Log.d("onPause","unregisterListener(this)");
    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
       // Log.d("onSensorChanged","Starting onSensorChanged");
    // Which sensor change ?
    //float mMagnetConsum = mMagneticField.getPower();
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:{
                accelerometerValues = event.values.clone();
                break;}
            case Sensor.TYPE_MAGNETIC_FIELD:{
                magneticFieldValues = event.values.clone();
                break;}
            default:
                break;
        }
        if (accelerometerValues != null && magneticFieldValues != null) {
            //Log.d("onSensorChanged", "Trying to obtain rotationMatrix");
            boolean success = SensorManager.getRotationMatrix(rotationMatrix, I,
                    accelerometerValues,
                    magneticFieldValues);
            //Log.d("onSensorChanged", "Success is " + success);
            if (success) {
                SensorManager.getOrientation(rotationMatrix, orientationVals);

                float azimuth = (float) Math.toDegrees(orientationVals[0]); // Azimuth
                float pitch = (float) Math.toDegrees(orientationVals[1]); // Pitch
                float roll = (float) Math.toDegrees(orientationVals[2]); // Roll
                //simple low pass filter signal processing
                mLowPassAzimuth = lowPass(azimuth,mLowPassAzimuth);
                mLowPassPitch = lowPass(pitch,mLowPassPitch);
                mLowPassRoll = lowPass(roll,mLowPassRoll);

                Azimuth.setText("Azimuth: "+String.valueOf(Math.round(mLowPassAzimuth)));//Display Azimuth
                Pitch.setText("Pitch: "+String.valueOf(Math.round(mLowPassPitch)));//Display Pitch
                Roll.setText("Roll: "+String.valueOf(Math.round(mLowPassRoll)));//Display Roll
            }
            else{
                //Log.d("onSensorChanged", "success is false");
            }
        }
        else{
            //Log.d("onSensorChanged", "accelerometerValues = null && || magneticFieldValues = null");
        }
    }
    //Simple low-pass filter
    private float lowPass(float current, float last) {
        float a = 0.1f;
        return last*(1.0f - a) + current*a;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
