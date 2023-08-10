package com.example.pair_click.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pair_click.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ImageButton btnguide=findViewById(R.id.guidetomain);
        btnguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}