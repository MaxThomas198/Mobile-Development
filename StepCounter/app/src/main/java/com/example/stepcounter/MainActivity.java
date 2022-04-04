package com.example.stepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class MainActivity extends AppCompatActivity {
    int stepCount = 0;
    long lastStepTimestamp = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatTextView plusButton = findViewById(R.id.step_counter);
        plusButton.setOnClickListener((view) -> {
            stepCount = 0;
            AppCompatTextView numberText = findViewById(R.id.step_counter);
            numberText.setText("" + stepCount);
        });

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(
                new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {
                        float x = sensorEvent.values[0];
                        float y = sensorEvent.values[1];
                        float z = sensorEvent.values[2];

                        float gx = x / SensorManager.GRAVITY_EARTH;
                        float gy = y / SensorManager.GRAVITY_EARTH;
                        float gz = z / SensorManager.GRAVITY_EARTH;

                        float normalizedGForce = (float)Math.sqrt(gx * gx + gy * gy + gz * gz);
                        if(normalizedGForce > 1.5){
                            long now = System.currentTimeMillis();
                            if(lastStepTimestamp + 500 > now){
                                return;
                            }
                            else{
                                stepCount += 1;
                                AppCompatTextView stepCounter = findViewById(R.id.step_counter);
                                stepCounter.setText("" + stepCount);
                                lastStepTimestamp = now;
                            }
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                    }
                },
                accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }
}
