package com.example.pgsr_daegu_safety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static int tag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView emergency = (ImageView) findViewById(R.id.main_button1);
        ImageView fire = (ImageView) findViewById(R.id.main_button2);
        ImageView safety = (ImageView) findViewById(R.id.main_button3);
        ImageView disaster = (ImageView) findViewById(R.id.main_button4);

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag=1;
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }
        });

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag=2;
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }
        });

        safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag=3;
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);

            }
        });

        disaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag=4;
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);

            }
        });
    }
}