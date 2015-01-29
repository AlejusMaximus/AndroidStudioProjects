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

/*
This code has been designed to work with:

1) JY-MCU Bluetooth to UART Wireless Serial Port -> TTL Converter needed (two resistors)
    Baudrate = 38400 bps ; key = 1234;

2) Modem Bluetooth BlueSMIRF Gold RN-41

http://tienda.bricogeek.com/modulos-radiofrecuencia/242-modem-bluetooth-bluesmirf-gold.html

General features for both:  Bluetooth V2.0+EDR (Enhanced Data Rate) 3Mbps
                            Bluetooth SPP (Serial Port Protocol)

 */

package alejusmaximus.apps101.bluev2edr;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends ActionBarActivity {

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mArrayAdapter;
    /**
     * Newly discovered devices
     */
    //private ArrayAdapter<String> mNewDevicesArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        STEP 1: GET BLUETOOTH ADAPTER
        The BluetoothAdapter is required for any and all Bluetooth activity. To get the BluetoothAdapter,
        call the static getDefaultAdapter() method. This returns a BluetoothAdapter that represents the device's
        own Bluetooth adapter (the Bluetooth radio). There's one Bluetooth adapter for the entire system, and your
        application can interact with it using this object. If getDefaultAdapter() returns null, then the device
        does not support Bluetooth and your story ends here. For example:
        */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.d("onCreate", "Device does not support Bluetooth :-(");
        }else{
            Log.d("onCreate", "Device support Bluetooth :-D");
        }
        /*
        STEP 2: Enable Bluetooth
        Next, you need to ensure that Bluetooth is enabled. Call isEnabled() to check whether Bluetooth is currently enable.
        If this method returns false, then Bluetooth is disabled. To request that Bluetooth be enabled, call
        startActivityForResult() with the ACTION_REQUEST_ENABLE action Intent. This will issue a request to enable Bluetooth
        through the system settings (without stopping your application). For example:
        */
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        /*
        STEP 3: Querying paired devices
        Before performing device discovery, its worth querying the set of paired devices to see if the desired device is already known.
        To do so, call getBondedDevices(). This will return a Set of BluetoothDevices representing paired devices.
        For example, you can query all paired devices and then show the name of each device to the user, using an ArrayAdapter:
         */
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<String>(this, R.layout.device_name);

        //mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);

        pairedListView.setAdapter(pairedDevicesArrayAdapter);

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        // DeviceListActivity.java from Android Developers!
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }

    }
    /*
    BluetoothChatFragment.java from Android Developers!

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }
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
}
