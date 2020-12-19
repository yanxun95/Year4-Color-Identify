package com.example.coloridentifierapplication.ColorIdentity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coloridentifierapplication.Camera.CheckColorName;
import com.example.coloridentifierapplication.R;

import org.w3c.dom.Text;

public class ColorIdentity extends AppCompatActivity {

    public static final int IMAGE_PICK_CODE = 101;
    public static final int STORAGE_PERMISSION_CODE = 102;

    ImageView images;
    TextView colorValuesDisplay, colorNameDisplay;
    View showColor;
    Button loadImages;
    Bitmap bitmap;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_identity);
        images = findViewById(R.id.colorPicker);
        colorValuesDisplay = findViewById(R.id.displayColorValues);
        colorNameDisplay = findViewById(R.id.displayColorName);
        showColor = findViewById(R.id.displayColor);
        loadImages = findViewById(R.id.btn_loadImages);
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
                    CheckColorName colorName = new CheckColorName();
                    int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);
                    String hex = "#"+Integer.toHexString(pixel);
                    showColor.setBackgroundColor(Color.rgb(r,g,b));
                    colorValuesDisplay.setText("RGB: " +r+ ", " +g+ ", " +b+ " \nHEX: " + hex);
                    String cName = colorName.getColorNameFromRgb(r,g,b);
                    colorNameDisplay.setText(cName);

                }
                return true;
            }
        });
    }

    public void btn_loadImages(View view) {
        askStoragePermission();
    }

    private void pickImagesFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    public void askStoragePermission(){
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
                //Toast.makeText(this, "try to run camera.", Toast.LENGTH_SHORT).show();
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
            //set image to image view
            images.setImageURI(data.getData());
        }
    }
}