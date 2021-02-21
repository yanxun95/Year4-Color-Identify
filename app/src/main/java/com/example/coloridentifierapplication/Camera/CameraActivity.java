package com.example.coloridentifierapplication.Camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coloridentifierapplication.ColorIdentity.CheckColorName;
import com.example.coloridentifierapplication.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 101;
    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    View viewDisplayColor;
    TextView viewName, viewRgb, viewHex;
    String hex, cName;
    private static final String TAG = "CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        frameLayout = findViewById(R.id.frameLayout);
        viewDisplayColor = findViewById(R.id.cameraLiveDisplayColor);
        viewName = findViewById(R.id.cameraLiveColorName);
        viewRgb = findViewById(R.id.cameraLiveRGB);
        viewHex = findViewById(R.id.cameraLiveHex);
        askCameraPermissions();
    }

    private void askCameraPermissions(){
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            //permission is not granted, request it
            String[] permission = {Manifest.permission.CAMERA};
            requestPermissions(permission, CAMERA_REQUEST_CODE);
        }else{
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
                startCamera();
            }else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startCamera(){
        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);

        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
                int frameWidth = camera.getParameters().getPreviewSize().width;
                int frameHeight = camera.getParameters().getPreviewSize().height;

                //number of pixels//transforms NV21 pixel data into RGB pixels
                int rgb[] = new int[frameWidth * frameHeight];
                // convertion
                decodeYUV420SP(rgb, bytes, frameWidth, frameHeight);
                Bitmap bmp = Bitmap.createBitmap(rgb, frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

                int width1 = rotatedBitmap.getWidth();
                int height1 = rotatedBitmap.getHeight();
                System.out.println("This is rotatedBitmap width: "+width1);
                System.out.println("This is rotatedBitmap height "+height1);

                int x = (rotatedBitmap.getWidth()/2) - 5;
                int y = (rotatedBitmap.getHeight()/2) - 5;
                System.out.println("This is rotatedBitmap/2 x: "+x);
                System.out.println("This is rotatedBitmap/2 y "+y);

                int redColors = 0;
                int greenColors = 0;
                int blueColors = 0;
                int pixelCount = 0;

                for (int i = y; i < (y+10); i++) {
                    for (int j = x; j < (x+10); j++) {
                        int pixel = rotatedBitmap.getPixel(x,y);
                        pixelCount++;
                        redColors += Color.red(pixel);
                        greenColors += Color.green(pixel);
                        blueColors += Color.blue(pixel);
                    }
                }

                int r = (redColors/pixelCount);
                int g = (greenColors/pixelCount);
                int b = (blueColors/pixelCount);

                CheckColorName colorName =  new CheckColorName();
                hex = String.format("#%02X%02X%02X", r, g, b);
                viewDisplayColor.setBackgroundColor(Color.rgb(r,g,b));
                viewHex.setText(hex);
                cName = colorName.getColorNameFromRgb(r,g,b);
                viewRgb.setText(r+ ", " +g+ ", " +b);
                viewName.setText(cName);
            }
        });
    }


    public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;

        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }

}