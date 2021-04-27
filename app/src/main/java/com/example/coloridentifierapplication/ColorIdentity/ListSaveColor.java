package com.example.coloridentifierapplication.ColorIdentity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.coloridentifierapplication.Color.Color;
import com.example.coloridentifierapplication.R;

import java.util.ArrayList;
import java.util.List;


public class ListSaveColor extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    ColorSaveRecyclerAdapter colorSaveRecyclerAdapter;
    List<String> id, name, rgb, hex;
    ArrayList<Color> colorArrayList;
    List<Color> colorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_save_color);
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.colorListView);
        displayAllSavedColor();

        colorSaveRecyclerAdapter = new ColorSaveRecyclerAdapter(this, id, name, rgb, hex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(colorSaveRecyclerAdapter);
    }

    private void displayAllSavedColor(){
        colorList = databaseHelper.getAllData();
        colorArrayList = databaseHelper.findAllColors();
        if(colorArrayList.isEmpty()) {
            Toast.makeText(this,"No saved color", Toast.LENGTH_SHORT).show();
        }

        name = new ArrayList<>();
        rgb = new ArrayList<>();
        hex = new ArrayList<>();
        id = new ArrayList<>();

        for (Color color : colorList) {
            id.add(color.getId());
            name.add(color.getName());
            rgb.add(color.getRgb());
            hex.add(color.getHex());
        }
    }
}