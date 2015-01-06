package alejus.upv.sensores.sensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Link with our layout*/
        output = (TextView) findViewById(R.id.output);
        /*The sensor manager is used to manage the sensor hardware available on Android devices
        * Use <getSystemService> to return a reference to the Sensor Manager Service:*/
        String service_name = Context.SENSOR_SERVICE;
        SensorManager sensorManager = (SensorManager) getSystemService(service_name);
        /*Rather than interacting with the sensor hardware directly, they are represented by
        * sensor objects that describe the properties of the hardware sensor that represents.
        * Now its time to find the sensors available in our device:*/
        List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        /*All sensors available are saved in allSensor list*/
        for (Sensor sensor: allSensors){
            showSensor(sensor.getName());
        }
        output.setMovementMethod(new ScrollingMovementMethod());

    }

    private void showSensor(String string) {
        output.append(string + "\n");
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
