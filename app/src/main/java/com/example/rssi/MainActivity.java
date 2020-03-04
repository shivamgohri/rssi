package com.example.rssi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    Button press;
    TextView rssi, distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        press = findViewById(R.id.press);
        rssi = findViewById(R.id.rssi);
        distance = findViewById(R.id.distance);

        press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWifi();
            }
        });

    }

    public void checkWifi(){

        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if(wifiMgr.isWifiEnabled()){

            WifiInfo Info = wifiMgr.getConnectionInfo();
            if( Info.getNetworkId() == -1 || Info==null){
                Toast.makeText(this,"Not Connected!",Toast.LENGTH_LONG).show();
            }
            else{

                WifiManager wifiCont = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                int rssiVal = wifiCont.getConnectionInfo().getRssi();
                rssi.setText(String.valueOf(rssiVal));

                int distanceVal= (int) GetDistanceFromRssiAndTxPowerOn1m(rssiVal,-45);
                distance.setText(String.valueOf(distanceVal));

            }

        }
        else{
            Toast.makeText(this,"Turn On Your WiFi!",Toast.LENGTH_LONG).show();
        }

    }


    public double GetDistanceFromRssiAndTxPowerOn1m(double rssi, int txPower)
    {
        /*
         * RSSI = TxPower - 10 * n * lg(d)
         * n = 2 (in free space)
         * d = 10 ^ ((TxPower - RSSI) / (10 * n))
         */
        return Math.pow(10, ((double)txPower - rssi) / (10 * 2));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
