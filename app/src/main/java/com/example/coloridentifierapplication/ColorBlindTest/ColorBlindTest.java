package com.example.coloridentifierapplication.ColorBlindTest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coloridentifierapplication.R;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ColorBlindTest extends AppCompatActivity {

    TextView numberOfTest, outputAnswer;
    ImageView colorBlindTestImg;
    EditText inputAnswer;
    Button btnShowAnswer, btnNext;

    List<String> images = new ArrayList<>();
    List<String> description = new ArrayList<>();
    List<String> answer = new ArrayList<>();
    Set<Integer> randomSequence = new LinkedHashSet<>();
    List<Integer> listRandomSequence;

    int currentTest = 1;
    int correctAnswer = 0;
    String userInput = "";
    Boolean goToNextPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_blind_test);
        numberOfTest = findViewById(R.id.numberOfTest);
        colorBlindTestImg = findViewById(R.id.colorBlindTestImg);
        inputAnswer = findViewById(R.id.inputAnswer);
        outputAnswer = findViewById(R.id.tvDisplayAnswer);
        btnShowAnswer = findViewById(R.id.btnShowAnswer);
        btnNext = findViewById(R.id.btnNext);
        initial();
        initialRandomSequence();

        //Set the first image
        String firstImg = "ishiharaplate" + listRandomSequence.get(0);
        int id = this.getResources().getIdentifier("drawable/"+firstImg, null, this.getPackageName());
        colorBlindTestImg.setImageResource(id);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionNext();
            }
        });

        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionShowAnswer();
            }
        });
    }

    private void initial(){
        String imgNo = "";
        for(int x = 1; x < 26; x++){
            imgNo = "R.drawable.ishiharaplate" + x;
            images.add(imgNo);
        }

        description.add("Everyone should see number 12.");
        description.add("The number in the picture is 8. \nMost color blind people see 12.");
        description.add("The number in the picture is 6. Most color blind people see 5.");
        description.add("The number in the picture is 29. Most color blind people see 70.");
        description.add("The number in the picture is 57. Most color blind people see 35.");
        description.add("The number in the picture is 5. Most color blind people see 2.");
        description.add("The number in the picture is 3. Most color blind people see 5.");
        description.add("The number in the picture is 15. Most color blind people see 17.");
        description.add("The number in the picture is 74. Most color blind people see 21.");
        description.add("The number in the picture is 2. Most color blind people don’t see anything or see something wrong.");
        description.add("The number in the picture is 6. Most color blind people don’t see anything or see something wrong.");
        description.add("The number in the picture is 97. Most color blind people don’t see anything or see something wrong.");
        description.add("The number in the picture is 45. Most color blind people don’t see anything or see something wrong.");
        description.add("The number in the picture is 5. Most color blind people don’t see anything or see something wrong.");
        description.add("The number in the picture is 7. Most color blind people don’t see anything or see something wrong.");
        description.add("The number in the picture is 16. Most color blind people don’t see anything or see something wrong.");
        description.add("The number in the picture is 73. Most color blind people don’t see anything or see something wrong.");
        description.add("There is nothing in the picture. Most color blind see 5.");
        description.add("There is nothing in the picture. Most color blind see 2.");
        description.add("There is nothing in the picture. Most color blind see 45.");
        description.add("There is nothing in the picture. Most color blind see 73.");
        description.add("The number in the picture is 26. Most color blind people see either 6 or 2.");
        description.add("The number in the picture is 42. Most color blind people see either 2 or 4.");
        description.add("The number in the picture is 35. Most color blind people see either 5 or 3.");
        description.add("The number in the picture is 96. Most color blind people see either 6 or 9.");

        answer.add("12");
        answer.add("8");
        answer.add("6");
        answer.add("29");
        answer.add("57");
        answer.add("5");
        answer.add("3");
        answer.add("15");
        answer.add("74");
        answer.add("2");
        answer.add("6");
        answer.add("97");
        answer.add("45");
        answer.add("5");
        answer.add("7");
        answer.add("16");
        answer.add("73");
        answer.add("-");
        answer.add("-");
        answer.add("-");
        answer.add("-");
        answer.add("26");
        answer.add("42");
        answer.add("35");
        answer.add("96");
    }

    private void initialRandomSequence(){
        Random randNum = new Random();
        while (randomSequence.size() < 12) {
            randomSequence.add(randNum.nextInt(25)+1);
        }
        listRandomSequence = new ArrayList<>(randomSequence);
    }

    private void functionNext() {
        userInput = inputAnswer.getText().toString();
        if(goToNextPage){
            inputAnswer.getText().clear();
            btnShowAnswer.setEnabled(true);
            outputAnswer.setText(null);
            inputAnswer.setFocusableInTouchMode(true);
            String image;
            currentTest += 1;
            String displayCurrentTest = Integer.toString(currentTest);
            if(currentTest<=12){
                numberOfTest.setText(displayCurrentTest + "/12");
                image = "ishiharaplate" + listRandomSequence.get(currentTest-1);
                int id = this.getResources().getIdentifier("drawable/"+image, null, this.getPackageName());
                colorBlindTestImg.setImageResource(id);
                goToNextPage = false;
            }else{
                System.out.println("End of the test!");
                Intent intent = new Intent(this, ResultOfTest.class);
                intent.putExtra("correctAnswer", correctAnswer);
                startActivity(intent);
            }
        }else if(userInput.isEmpty()){
            Toast.makeText(this, "Please enter the number in the picture.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please check the answer before going to next test.", Toast.LENGTH_SHORT).show();
        }
    }

    private void functionShowAnswer() {
        userInput = inputAnswer.getText().toString();
        if(userInput.isEmpty()){
            Toast.makeText(this, "Please enter the number in the picture.", Toast.LENGTH_SHORT).show();
        }else{
            checkAnswer();
        }
    }

    private void checkAnswer() {
        int x = listRandomSequence.get(currentTest-1);
        String compareAnswer = answer.get(x-1);
        if(userInput.equals(compareAnswer)){
            correctAnswer++;
            System.out.println("Mark: " + correctAnswer);
            outputAnswer.setText(R.string.correctAnswer);
        }else{
            outputAnswer.setText("Incorrect.\n\n"+description.get(x-1));
        }
        goToNextPage = true;
        btnShowAnswer.setEnabled(false);
        inputAnswer.setFocusable(false);
    }
}