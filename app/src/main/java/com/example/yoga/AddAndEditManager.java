package com.example.yoga;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddAndEditManager extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2;

    private EditText managerName, managerComment;
    private ImageView managerImage;
    private FloatingActionButton addManagerButton;
    private TextView yogaName;
    private Toolbar toolbar;
    private DatabaseHelper dbHelper;

    // String variables;
    private String name, comment, idYoga;
    private Uri imageUri;
    private boolean isEditMode;
    private String managerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_and_edit_manager);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.addAndEditToolbar);
        managerName = findViewById(R.id.managerName);
        managerComment = findViewById(R.id.managerComment);
        managerImage = findViewById(R.id.managerImage);
        addManagerButton = findViewById(R.id.addManager);
        yogaName = findViewById(R.id.yogaManagerName);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        idYoga = sharedPreferences.getString("yogaId", null);
        ModelYoga modelYoga = dbHelper.getYogaById(idYoga);
        yogaName.setText(modelYoga.getName());

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);

        if (isEditMode) {
            toolbar.setTitle("Edit manager");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            managerId = intent.getStringExtra("ID");

            ModelManager manager = dbHelper.getManagerById(managerId);
            managerName.setText(manager.getName());
            managerComment.setText(manager.getComment());
            imageUri = Uri.parse(manager.getImage());
            managerImage.setImageURI(imageUri);
        } else {
            toolbar.setTitle("Add Manager");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        managerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        addManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void showImagePickerDialog() {
        final CharSequence[] options = {"Take photo", "Open Album", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAndEditManager.this);
        builder.setTitle("Please choose your option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_IMAGE);
                    }
                } else if (options[item].equals("Open Album")) {
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
                managerImage.setImageURI(imageUri);
            } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                managerImage.setImageBitmap(imageBitmap);
            }
        } else if (resultCode == RESULT_CANCELED) {
            imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(R.drawable.default_image)
                    + '/' + getResources().getResourceTypeName(R.drawable.default_image) + '/' +
                    getResources().getResourceEntryName(R.drawable.default_image));
            managerImage.setImageURI(imageUri);
        }
    }

    private void saveData() {
        name = managerName.getText().toString();
        comment = managerComment.getText().toString();

        if (name.isEmpty() || comment.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String timeStamp = "" + System.currentTimeMillis();
        if (imageUri == null) {
            imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(R.drawable.default_image)
                    + '/' + getResources().getResourceTypeName(R.drawable.default_image) + '/' +
                    getResources().getResourceEntryName(R.drawable.default_image));
            managerImage.setImageURI(imageUri);
        }

        if (isEditMode) {
            // Update existing manager
            boolean updateSuccess = dbHelper.updateManagerData(
                    managerId,
                    name,
                    comment,
                    imageUri.toString(),
                    timeStamp
            );

            if (updateSuccess) {
                Toast.makeText(getApplicationContext(), "Manager Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to update manager data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Insert new manager
            long managerId = dbHelper.insertManagerData(
                    idYoga,
                    name,
                    comment,
                    imageUri.toString(),
                    timeStamp
            );

            if (managerId != -1) {
                Toast.makeText(getApplicationContext(), "Manager Added Successfully.... " + managerId, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to insert manager data.", Toast.LENGTH_SHORT).show();
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
