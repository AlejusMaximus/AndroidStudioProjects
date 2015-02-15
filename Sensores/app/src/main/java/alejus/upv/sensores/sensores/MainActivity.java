/*
Copyright (c) 2015 Aleix Pascual
Dual licensed under Apache2.0 and MIT Open Source License (included below):

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package alejus.upv.sensores.sensores;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


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
    MovingAverage mMovingAverageAzimuth;
    MovingAverage mMovingAveragePitch;
    MovingAverage mMovingAverageRoll;
    /*
    private float mLastAzimuthLPF = 0.0f;
    private float mLastPitchLPF = 0.0f;
    private float mLastRollLPF = 0.0f;
    private float mPastAzimuthLPF = 0.0f;
    private float mPastPitchLPF = 0.0f;
    private float mPastRollLPF = 0.0f;
    private float mHighPassAzimuth = 0.0f;
    private float mHighPassPitch = 0.0f;
    private float mHighPassRoll = 0.0f;
    private float mLastAzimuthHPF = 0.0f;
    private float mLastPitchHPF = 0.0f;
    private float mLastRollHPF = 0.0f;
    MovingAverage mMovingAverageAzimuth;
    MovingAverage mMovingAveragePitch;
    MovingAverage mMovingAverageRoll;
    */

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
        /*
        Create a Moving Average filter:
        */
        int sizeBuffer = 20;
        mMovingAverageAzimuth = new MovingAverage(sizeBuffer);
        mMovingAveragePitch = new MovingAverage(sizeBuffer);
        mMovingAverageRoll = new MovingAverage(sizeBuffer);
        /*
        Finding the screen orientation relative to the natural orientation
         */
        String windowSrvc = Context.WINDOW_SERVICE;
        WindowManager wm = ((WindowManager) getSystemService(windowSrvc));
        Display display = wm.getDefaultDisplay();
        int rotation = display.getRotation();
        switch(rotation){
            case (Surface.ROTATION_0):
                Toast.makeText(this, "Natural", Toast.LENGTH_SHORT).show();
                break;
            case (Surface.ROTATION_90):
                Toast.makeText(this, "On its left side", Toast.LENGTH_SHORT).show();
                break;
            case (Surface.ROTATION_180):
                Toast.makeText(this, "Upside down", Toast.LENGTH_SHORT).show();
                break;
            case (Surface.ROTATION_270):
                Toast.makeText(this, "On its right side", Toast.LENGTH_SHORT).show();
                break;
            default: break;

        }
        /*
        OPTIONAL remapping the Orientation reference frame
        http://developer.android.com/reference/android/hardware/SensorManager.html#remapCoordinateSystem%28float%5B%5D,%20int,%20int,%20float%5B%5D%29
        http://stackoverflow.com/questions/18782829/android-sensormanager-strange-how-to-remapcoordinatesystem
         */
        /*
        Find device orientation
        http://developer.android.com/reference/android/content/res/Configuration.html
         */

        Configuration config = getResources().getConfiguration();

        if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
        else if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        }
        /*
        NOTE: This methods to find device orientation are not useful if in the AndroidManifest
        portrait or landscape mode is hardcoded.
         */
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

                //Using moving average filter, Push the newly collected sensor value:

                mMovingAverageAzimuth.pushValue(azimuth);
                mMovingAveragePitch.pushValue(pitch);
                mMovingAverageRoll.pushValue(roll);

                //Get the averaged value:
                float mSMAAzimuth = mMovingAverageAzimuth.getValue();
                float mSMAPitch = mMovingAveragePitch.getValue();
                float mSMARoll = mMovingAverageRoll.getValue();

                /*
                Bandpass filer = high pass filter and then low pass filter
                 */
                /*
                simple high pass filter signal processing
                private float highPass(float current, float last, float filtered)
                Note:has not a good result for orientation sensors
                 */
                /*
                mHighPassAzimuth = highPass(azimuth, mLastAzimuthHPF, mHighPassAzimuth);
                mHighPassPitch = highPass(pitch, mLastPitchHPF, mHighPassPitch);
                mHighPassRoll = highPass(roll, mLastRollHPF, mHighPassRoll);
                mLastAzimuthHPF = azimuth;
                mLastPitchHPF = pitch;
                mLastRollHPF = roll;
                */
                /*
                simple low pass filter signal processing
                private float lowPass(float current, float last)
                 */

                /*
                float mLowPassAzimuth = lowPass(azimuth,mLastAzimuthLPF);
                float mLowPassPitch = lowPass(pitch,mLastPitchLPF);
                float mLowPassRoll = lowPass(roll,mLastRollLPF);
                // x[n-1] <- x[n]
                mLastAzimuthLPF = azimuth;
                mLastPitchLPF = pitch;
                mLastRollLPF = roll;
                */

                /*
                Using Hanning filter instead of simple low pass filter
                private float hanningFilter(float current, float last, float past)
                */
                /*
                float mLowPassAzimuth = hanningFilter(azimuth,mLastAzimuthLPF,mPastAzimuthLPF);
                float mLowPassPitch = hanningFilter(pitch, mLastPitchLPF,mPastPitchLPF);
                float mLowPassRoll = hanningFilter(roll, mLastRollLPF, mPastRollLPF);
                // x[n-2] <- x[n-1]
                mPastAzimuthLPF = mLastAzimuthLPF;
                mPastPitchLPF = mLastPitchLPF;
                mPastRollLPF =  mLastRollLPF;
                // x[n-1] <- x[n]
                mLastAzimuthLPF = azimuth;
                mLastPitchLPF = pitch;
                mLastRollLPF = roll;
                */
                /*
                Azimuth.setText("Azimuth: "+String.valueOf(Math.round(mLowPassAzimuth)));//Display Azimuth
                Pitch.setText("Pitch: "+String.valueOf(Math.round(mLowPassPitch)));//Display Pitch
                Roll.setText("Roll: "+String.valueOf(Math.round(mLowPassRoll)));//Display Roll
                */
                Azimuth.setText("Azimuth: "+String.valueOf(Math.round(mSMAAzimuth)));//Display Azimuth
                Pitch.setText("Pitch: "+String.valueOf(Math.round(mSMAPitch)));//Display Pitch
                Roll.setText("Roll: "+String.valueOf(Math.round(mSMARoll)));//Display Roll

            }
            else{
                //Log.d("onSensorChanged", "success is false");
            }
        }
        else{
            //Log.d("onSensorChanged", "accelerometerValues = null && || magneticFieldValues = null");
        }
    }
    /*
    Simple low-pass filter
     */
    private float lowPass(float current, float last) {
        //Simple low-pass filter
        float a = 0.2f;//Smoothing parameter
        return last*(1.0f - a) + current*a;
    }

    /*
    One of the simplest smoothing filters is the Hanning moving average filter
    y[n] = 1/4*(x[n]+ 2*x[n-1]+ x[n-2]) [pag 114 pdf DSP book]
    */
    private float hanningFilter(float current, float last, float past){
        return 0.25f*current + 0.5f*last +0.25f*past;
    }

    /*
    Simple high-pass filter
     */
    private float highPass(float current, float last, float filtered) {
        //Simple high-pass filter
        float a = 0.9f;//Smoothing parameter
        return a*(filtered+current-last);
    }
    /*
    Bandpass filter = apply a high pass filer and then a low pass filter
     */


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
    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    */


}
