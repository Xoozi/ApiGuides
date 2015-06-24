package com.xoozi.apiguides.ble;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ActivityBle extends Activity 
    implements LeScanCallback{

    private BluetoothAdapter _adapter;
    private TextView         _display;
    private GaussFilter      _filter;
    private BLEReceiver      _bleReceiver;
    private IntentFilter     _intentFilterBle;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _adapter = BluetoothAdapter.getDefaultAdapter();

        _display = new TextView(this);

        _filter = new GaussFilter();

        setContentView(_display);

        _bleReceiver = new BLEReceiver();

        _intentFilterBle = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    }

    @Override
    protected void onResume() {
        super.onResume();

        _filter.reset();
        //_adapter.startLeScan(this);
        

        _adapter.startDiscovery();
        registerReceiver(_bleReceiver, _intentFilterBle);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(_bleReceiver);
        //_adapter.stopLeScan(this);
        _adapter.cancelDiscovery();
    }

    private String _logScanRecord(byte[] sr){
        StringBuilder sb = new StringBuilder();
        for(byte b:sr){
            int g = b;
            g &= 0xFF;
            sb.append(Long.toHexString(g));
            sb.append("-");
        }

        return sb.toString();
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] sr){
        
        Log.e("ggg", "onLeScan:"+device.getName()+", rssi:"+rssi+"sr:"+_logScanRecord(sr));
        if("JC_BLE4.2".equals(device.getName())){

            double s = _filter.addSample(rssi);
            String out;
            if(Double.NEGATIVE_INFINITY == s){
                out = String.format("N/A");
            }else{
                out = String.format("%f<%d>", s, _filter.getCount());
            }
            _display.setText(out);
        }
    }

    public class BLEReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {

            //String action = intent.getAction();
            // When discovery finds a device
             //if (BluetoothDevice.ACTION_FOUND.equals(action)) 
             {
                 // Get the BluetoothDevice object from the Intent
                 BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                 short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short)0);
                 // Add the name and address to an array adapter to show in a ListView
                 Log.w("gg", "name:"+device.getName() + 
                                "\naddr:" + device.getAddress() +
                                "\nrssi:" + rssi);
             }
        }
    }
}
