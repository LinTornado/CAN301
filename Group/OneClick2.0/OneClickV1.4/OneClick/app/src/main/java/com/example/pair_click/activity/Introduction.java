package com.example.pair_click.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.pair_click.R;

public class Introduction extends AppCompatActivity {
    private int[] picarray = new int[]{R.drawable.introduction};
    int trynow = R.drawable.tryrevrev;
    private ImageSwitcher imageSwitcher;
    private int index;
    private float touchDownX;
    private float touchUpX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        imageSwitcher=findViewById(R.id.switchback);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView=new ImageView(Introduction.this);
                imageView.setImageResource(picarray[index]);
                return imageView;
            }
        });
    }
}