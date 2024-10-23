package com.example.yoga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.Locale;

public class DetailYoga extends AppCompatActivity {

    private TextView yogaNameTextView, yogaDayOfTheWeekTextView, yogaDateTextView,
            yogaCapacityTextView, yogaPriceTextView, yogaTypeOfClassyTextView,
            yogaDescriptionTextView, yogaTeacherTextView, yogaAddedTime, yogaUpdateTime;
    private ImageView yogaImageView;
    private Button manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_yoga);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.detailYogaToolbar);
        toolbar.setTitle("Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Views
        yogaNameTextView = findViewById(R.id.detailyogaName);
        yogaDayOfTheWeekTextView = findViewById(R.id.detailyogaDayOfTheWeek);
        yogaDateTextView = findViewById(R.id.detailyogaDate);
        yogaCapacityTextView = findViewById(R.id.detailyogaCapacity);
        yogaPriceTextView = findViewById(R.id.detailyogaPriceValue);
        yogaTypeOfClassyTextView = findViewById(R.id.detailyogaTypeOfClassy);
        yogaDescriptionTextView = findViewById(R.id.detailyogaDescription);
        yogaTeacherTextView = findViewById(R.id.detailyogaTeacher);
        yogaAddedTime = findViewById(R.id.detailAddedTime);
        yogaUpdateTime = findViewById(R.id.detailUpdateTime);
        yogaImageView = findViewById(R.id.detailyogaImage);
        manager = findViewById(R.id.btnManager);


        Intent intent = getIntent();
        String yogaId = intent.getStringExtra("yogaId");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ModelYoga yoga = databaseHelper.getYogaById(yogaId);
        // Store yogaId
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("yogaId", yogaId);
        editor.apply();

        if (yoga != null) {
            String addTime = yoga.getAddedTime();
            String updateTime = yoga.getUpdateTime();
            //convert time to dd/mm/yy hh:mm:aa format
            Calendar calendar = Calendar.getInstance(Locale.getDefault());

            calendar.setTimeInMillis(Long.parseLong(addTime));
            String timeAdd = "" + DateFormat.format("dd/MM/yy hh:mm:aa", calendar);

            calendar.setTimeInMillis(Long.parseLong(updateTime));
            String timeUpdate = "" + DateFormat.format("dd/MM/yy hh:mm:aa", calendar);
            // Set yoga details to views
            yogaNameTextView.setText(yoga.getName());
            yogaDayOfTheWeekTextView.setText(yoga.getLocation());
            yogaDateTextView.setText(yoga.getDate());
            yogaCapacityTextView.setText(yoga.getParkingAvailable());
            yogaPriceTextView.setText(yoga.getLength());
            yogaTypeOfClassyTextView.setText(yoga.getDifficulty());
            yogaDescriptionTextView.setText(yoga.getDescription());
            yogaTeacherTextView.setText(yoga.getPartner());
            yogaAddedTime.setText(timeAdd);
            yogaUpdateTime.setText(timeUpdate);
            if (yoga.getImage().equals("null")) {
                yogaImageView.setImageResource(R.drawable.ic_baseline_person_24);
            } else {
                yogaImageView.setImageURI(Uri.parse(yoga.getImage()));
            }

        }
        manager.setOnClickListener(view -> {
            Intent intentManager = new Intent(getApplicationContext(), MainActivityManager.class);
            startActivity(intentManager);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

