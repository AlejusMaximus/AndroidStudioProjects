package alejus.upv.sensores.sensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends ActionBarActivity implements SensorEventListener{

    private TextView output;
    private float[] accelerometerValues;
    private float[] magneticFieldValues;
    private Sensor mMagneticField;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Link with our layout*/
        output = (TextView) findViewById(R.id.output);
        /*The sensor manager is used to manage the sensor hardware available on Android devices
        * Use <getSystemService> to return a reference to the Sensor Manager Service:*/
        String service_name = Context.SENSOR_SERVICE;
        mSensorManager = (SensorManager) getSystemService(service_name);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
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

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null,
                accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);

        // Convert from radians to degrees if preferred.
        values[0] = (float) Math.toDegrees(values[0]); // Azimuth
        values[1] = (float) Math.toDegrees(values[1]); // Pitch
        values[2] = (float) Math.toDegrees(values[2]); // Roll
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
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Which sensor change ?
        //float mMagnetConsum = mMagneticField.getPower();
        synchronized (this){
            switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    accelerometerValues = event.values;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magneticFieldValues = event.values;
                    break;
                default:
                    //TODO
            }
            calculateOrientation();
        }
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
