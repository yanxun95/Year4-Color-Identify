package com.example.coloridentifierapplication.ColorIdentity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coloridentifierapplication.Camera.CameraActivity;
import com.example.coloridentifierapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class ColorIdentity extends AppCompatActivity {

    public static final int IMAGE_PICK_CODE = 101;
    public static final int STORAGE_PERMISSION_CODE = 102;

    DatabaseHelper databaseHelper;
    ImageView images, colorPointer;
    TextView colorHexDisplay, colorRgbDisplay, colorNameDisplay;
    View showColor;
    BottomNavigationView bottomNavigationView;
    Bitmap bitmap;

    int r, g, b;
    String hex, cName;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_identity);
        images = findViewById(R.id.colorPicker);
        colorPointer = findViewById(R.id.colorPointer);
        colorHexDisplay = findViewById(R.id.displayHexValues);
        colorRgbDisplay = findViewById(R.id.displayRgbValues);
        colorNameDisplay = findViewById(R.id.displayColorNameValues);
        showColor = findViewById(R.id.displayColor);
        bottomNavigationView = findViewById(R.id.bottomColorMenu);
        databaseHelper = new DatabaseHelper(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.loadPhoto:
                        askStoragePermission();
                        return true;

                    case R.id.listOfSaveColor:
                        Intent intent = new Intent(ColorIdentity.this, ListSaveColor.class);
                        startActivity(intent);
                        return true;

                    case R.id.saveColor:
                        addColor();
                        return true;

                    default:
                        return false;
                }
            }
        });

        images.setDrawingCacheEnabled(true);
        images.buildDrawingCache(true);
        images.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    bitmap = images.getDrawingCache();
                    colorPointer.setVisibility(View.VISIBLE);
                    CheckColorName colorName = new CheckColorName();
                    int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());

                    r = Color.red(pixel);
                    g = Color.green(pixel);
                    b = Color.blue(pixel);
                    hex = String.format("#%02X%02X%02X", r, g, b);
                    showColor.setBackgroundColor(Color.rgb(r,g,b));
                    colorHexDisplay.setText(hex);
                    cName = colorName.getColorNameFromRgb(r,g,b);
                    colorRgbDisplay.setText(r+ ", " +g+ ", " +b);
                    colorNameDisplay.setText(cName);
                    colorPointer.setX(x-60);
                    colorPointer.setY(y+100);
                }
                return true;
            }
        });
    }

    private void pickImagesFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void askStoragePermission(){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //permission is not granted, request it
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, STORAGE_PERMISSION_CODE);
        }else{
            //permission already granted
            pickImagesFromGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickImagesFromGallery();
            }else {
                Toast.makeText(this, "Permission denied!.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //clear the images and drawableCache in ImageView
            images.setImageBitmap(null);
            images.destroyDrawingCache();
            //set image to image view
            images.setImageURI(data.getData());
            colorHexDisplay.setText(null);
            colorRgbDisplay.setText(null);
            colorNameDisplay.setText(null);
            showColor.setBackgroundColor(Color.rgb(255,255,255));
            colorPointer.setVisibility(View.INVISIBLE);
        }
    }

    private void addColor(){
        if(colorHexDisplay.getText().toString().trim().length()!= 0 ||
                colorRgbDisplay.getText().toString().trim().length()!= 0 ||
                colorNameDisplay.getText().toString().trim().length()!= 0 ){
            Boolean x = databaseHelper.findColor(colorHexDisplay.getText().toString().trim());
            if(!x){
                Toast.makeText(this,"Color already exist.", Toast.LENGTH_SHORT).show();
            }else{
                databaseHelper.addColor(new com.example.coloridentifierapplication.ColorIdentity.Color(colorNameDisplay.getText().toString(),
                        colorRgbDisplay.getText().toString(),
                        colorHexDisplay.getText().toString()));
                Toast.makeText(this,"Color save success.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"No color is select.", Toast.LENGTH_SHORT).show();
        }
    }
}