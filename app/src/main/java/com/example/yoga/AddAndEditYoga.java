package com.example.yoga;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddAndEditYoga extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2;

    private ImageView yogaImage;
    private TextView yogaEmail, yogaPrice;

    private Button yogaDate;
    private RadioGroup yogaRadioGroup;
    private RadioButton yogaYesRadioButton, yogaNoRadioButton;
    private SeekBar yogaSeekBar;
    private EditText yogaName, yogaDayOfTheWeek, yogaTypeOfClassy, yogaDescription;
    private MultiAutoCompleteTextView yogaTeacher;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    // String variables;
    public String userEmail;
    private String id, name, location, date;
    private String parkingValue, length, difficult;
    private String description, image, addedTime, updateTime;
    private String partners;
    private Boolean isEditMode;

    // action bar
    private Uri imageUri;

    // database helper
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_and_edit_yoga);

        toolbar = findViewById(R.id.addToolbar);
        // init view
        dbHelper = new DatabaseHelper(this);

        yogaEmail = findViewById(R.id.yogaEmail);
        yogaName = findViewById(R.id.yogaName);
        yogaDayOfTheWeek = findViewById(R.id.yogaDayOfTheWeek);
        yogaDate = findViewById(R.id.yogaDate);
        yogaRadioGroup = findViewById(R.id.yogaCapacity);
        yogaYesRadioButton = findViewById(R.id.yogaYes);
        yogaNoRadioButton = findViewById(R.id.yogaNo);
        yogaSeekBar = findViewById(R.id.yogaPriceSeekBar);
        yogaPrice = findViewById(R.id.yogaPriceValue);
        yogaTypeOfClassy = findViewById(R.id.yogaTypeOfClassy);
        yogaDescription = findViewById(R.id.yogaDescription);
        yogaImage = findViewById(R.id.yogaImage);
        yogaTeacher = findViewById(R.id.yogaTeacher);
        fab = findViewById(R.id.add_yoga);

        List<String> userEmails = dbHelper.getAllUserEmails();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, userEmails);
        yogaTeacher.setAdapter(adapter);
        yogaTeacher.setThreshold(1);
        yogaTeacher.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        Intent intent = getIntent();
        yogaEmail.setText(LoginActivity.email);
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        if (isEditMode) {
            // Set toolbar title
            toolbar.setTitle("Update Yoga");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Get the other values from the intent
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            location = intent.getStringExtra("LOCATION");
            date = intent.getStringExtra("DATE");
            parkingValue = intent.getStringExtra("PARKING");
            length = intent.getStringExtra("LENGTH");
            difficult = intent.getStringExtra("DIFFICULTY");
            description = intent.getStringExtra("DESCRIPTION");
            image = intent.getStringExtra("IMAGE");
            partners = intent.getStringExtra("PARTNER");
            addedTime = intent.getStringExtra("ADD");
            updateTime = intent.getStringExtra("UPDATE");

            // Set values in EditText fields
            yogaName.setText(name);
            yogaDayOfTheWeek.setText(location);
            yogaDate.setText(date);
            yogaPrice.setText(length);
            yogaTypeOfClassy.setText(difficult);
            yogaDescription.setText(description);
            yogaTeacher.setText(partners);

            if ("Yes".equals(parkingValue)) {
                yogaRadioGroup.check(R.id.yogaYes);
            } else if ("No".equals(parkingValue)) {
                yogaRadioGroup.check(R.id.yogaNo);
            }

            imageUri = Uri.parse(image);

            if (image.equals("")) {
                yogaImage.setImageResource(R.drawable.camera);
            } else {
                yogaImage.setImageURI(imageUri);
            }

        } else {
            toolbar.setTitle("Add Yoga");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        yogaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        yogaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.yogaYes) {
                    parkingValue = "Yes";
                } else if (checkedId == R.id.yogaNo) {
                    parkingValue = "No";
                }
            }
        });

        yogaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                yogaPrice.setText(progress + " USD");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(AddAndEditYoga.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        yogaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        yogaDate.setText(date);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showImagePickerDialog() {
        final CharSequence[] options = {"Take photo", "Open Album", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAndEditYoga.this);
        builder.setTitle("Please choose your option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take photo")) {
                    // Open camera
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_IMAGE);
                    }
                } else if (options[item].equals("Open Album")) {
                    // Open photo gallery
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, REQUEST_CODE_PICK_IMAGE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
                imageUri = data.getData();
                yogaImage.setImageURI(imageUri);
            } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                yogaImage.setImageBitmap(imageBitmap);
            }
        } else if (resultCode == RESULT_CANCELED) {
            // Handle the case where the user does not pick an image
            // Here, use the default image from the drawable folder
            imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(R.drawable.default_image)
                    + '/' + getResources().getResourceTypeName(R.drawable.default_image) + '/' +
                    getResources().getResourceEntryName(R.drawable.default_image));
            yogaImage.setImageURI(imageUri);
        }
    }

    private void saveData() {
        // Get data from the user
        userEmail = yogaEmail.getText().toString();
        name = yogaName.getText().toString();
        location = yogaDayOfTheWeek.getText().toString();
        length = yogaPrice.getText().toString();
        difficult = yogaTypeOfClassy.getText().toString();
        description = yogaDescription.getText().toString();
        partners = yogaTeacher.getText().toString();

        if (userEmail.isEmpty() || name.isEmpty() || location.isEmpty() || date.isEmpty() || parkingValue.isEmpty() || length.isEmpty() || difficult.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current time to use as the added time
        String timeStamp = "" + System.currentTimeMillis();

        // Check if the user has chosen an image
        if (imageUri == null) {
            // User did not choose an image, use the default image from the drawable folder
            imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(R.drawable.default_image)
                    + '/' + getResources().getResourceTypeName(R.drawable.default_image) + '/' +
                    getResources().getResourceEntryName(R.drawable.default_image));

            // Display the default image in the ImageView
            yogaImage.setImageURI(imageUri);
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        if (isEditMode) {
            // Update existing record
            boolean updateSuccess = dbHelper.updateYogaData(
                    id,
                    name,
                    location,
                    date,
                    parkingValue,
                    length,
                    difficult,
                    description,
                    imageUri.toString(),
                    partners,
                    addedTime,
                    timeStamp
            );

            if (updateSuccess) {
                Toast.makeText(getApplicationContext(), "Yoga Updated Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to update yoga data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Insert new record
            long yogaId = dbHelper.insertYogaData(
                    userEmail,
                    name,
                    location,
                    date,
                    parkingValue,
                    length,
                    difficult,
                    description,
                    imageUri.toString(),
                    partners,
                    timeStamp,
                    timeStamp
            );

            if (yogaId != -1) {
                Toast.makeText(getApplicationContext(), "Yoga Added Successfully.... " + yogaId, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to insert yoga data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Back button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
