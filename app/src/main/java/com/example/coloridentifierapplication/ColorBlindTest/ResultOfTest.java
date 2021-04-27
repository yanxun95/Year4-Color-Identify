package com.example.coloridentifierapplication.ColorBlindTest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coloridentifierapplication.Menu.MainActivity;
import com.example.coloridentifierapplication.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ResultOfTest extends AppCompatActivity {

    TextView score;
    EditText inputEmail;
    String textSendToPdf, textSendToEmail;
    Boolean pdfExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_of_test);
        score = findViewById(R.id.tvScore);
        inputEmail = findViewById(R.id.inputEmail);

        int correctAnswer = getIntent().getIntExtra("correctAnswer", 0);
        float percent = ((float)correctAnswer/12) * 100;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        String result = (df.format(percent) + "% (" + correctAnswer + "/12)");
        score.setText(result);
        textSendToPdf = "Ishihara color blind test\n\nYour result is " + result + ".\n\nPlease note that this is just a simple test, it is " +
                "\nhighly recommended to go to a doctor for a proper \ntest." ;
        textSendToEmail = "Hi,\n\nYour result is " + result + ".Please note that this is just a simple test, it is " +
                "highly recommended to go to a doctor for a proper test.\n\nThank You" ;
    }

    //override the back button to the main activity to prevent crash
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnBackToMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnSendEmail(View view){ sendEmail(); }

    public void btnDownloadPDF(View view){
        createPDF();
    }

    private void sendEmail() {
        String mEmail = inputEmail.getText().toString().trim();
        if(mEmail.isEmpty()){
            Toast.makeText(this, "Please enter the email address.", Toast.LENGTH_SHORT).show();
        }else if(isValid(mEmail)){
            String mSubject = "Result of ishihara color blind test";
            String mMessage = textSendToEmail;
            if(pdfExist){
                JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);
                javaMailAPI.execute();
            }else{
                createPDF();
                sendEmail();
            }
        }else{
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
        }
    }

    private void createPDF(){
        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        int x = 10, y=25;

        for (String line:textSendToPdf.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }
        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/result.pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
            Toast.makeText(this, "Save to " + myFilePath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        myPdfDocument.close();
        pdfExist = true;
    }

    public boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}