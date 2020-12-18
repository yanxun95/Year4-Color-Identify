package com.example.coloridentifierapplication.ColorIdentity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coloridentifierapplication.R;

import org.w3c.dom.Text;

public class ColorIdentity extends AppCompatActivity {

    ImageView images;
    TextView colorValuesDisplay;
    View showColor;

    Bitmap bitmap;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_identity);
        images = findViewById(R.id.colorPicker);
        colorValuesDisplay = findViewById(R.id.displayColorValues);
        showColor = findViewById(R.id.displayColor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Color Identity");

        images.setDrawingCacheEnabled(true);
        images.buildDrawingCache(true);
        images.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    bitmap = images.getDrawingCache();
                    int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);
                    String hex = "#"+Integer.toHexString(pixel);
                    showColor.setBackgroundColor(Color.rgb(r,g,b));
                    colorValuesDisplay.setText("RGB: " +r+ ", " +g+ ", " +b+ " \nHEX: " + hex);
                }
                return true;
            }
        });
    }
}