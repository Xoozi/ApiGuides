package com.xoozi.apiguides.sensor;

import java.util.List;

import com.xoozi.apiguides.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class ActivitySensorList extends ListActivity
    implements OnItemClickListener{


    private SensorAdapter   _sensorAdapter;
    private LayoutInflater  _inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _inflater = LayoutInflater.from(this);

        SensorManager sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor>    sensorList = sm.getSensorList(Sensor.TYPE_ALL);

        _sensorAdapter = new SensorAdapter(sensorList);
        setListAdapter(_sensorAdapter);
        getListView().setTextFilterEnabled(true);
        getListView().setOnItemClickListener(this);
    }



    @Override
    protected Dialog onCreateDialog(final int id) {
        Sensor sensor =  _sensorAdapter.getItem(id);
        StringBuilder sb = new StringBuilder();
        sb.append("Name:"+sensor.getName()+"\n");
        sb.append("Vendor:"+sensor.getVendor()+"\n");
        sb.append("Version:"+sensor.getVersion()+"\n");
        sb.append("Type:"+sensor.getType()+"\n");
        sb.append("Resolution:"+sensor.getResolution()+"\n");
        sb.append("Powr:"+sensor.getPower()+"\n");
        sb.append("MaximumRange:"+sensor.getMaximumRange()+"\n");
        sb.append("MinDelay:"+sensor.getMinDelay()+"\n");
        return new AlertDialog.Builder(this)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("Sensor Detail")
            .setMessage(sb.toString())
            .setPositiveButton("Connect",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0,
                            int arg1) {
                                            
                        Sensor sensor =  _sensorAdapter.getItem(id);
                        Intent  intent   = new Intent(ActivitySensorList.this, ActivitySensorDetail.class);
                        intent.putExtra(ActivitySensorDetail.KEY_SENSOR_NAME, sensor.getName());
                        startActivity(intent);
                    }
            })
            .create();
    }

    private class SensorAdapter extends BaseAdapter {

        private List<Sensor>    _sensorList;

        SensorAdapter(List<Sensor> sensorList){
            _sensorList = sensorList;
        }

        @Override
        public int getCount() {
            return _sensorList.size();
        }

        @Override
        public Sensor getItem(int pos) {
            return _sensorList.get(pos);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup arg2) {
            if(null == convertView){
                convertView = _inflater.inflate(R.layout.list_item_sensor, null);
            }
            TextView lableName = (TextView)convertView.findViewById(R.id.lable_name);
            lableName.setText(_sensorList.get(pos).getName());
            return convertView;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
        showDialog(pos);
    }
}
