package com.example.yoga;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView yogaRv;
    private DatabaseHelper dbHelper;
    private AdapterYoga adapterYoga;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome");
        setSupportActionBar(toolbar);
        //init db
        dbHelper = new DatabaseHelper(this);

        //initialization
        fab = findViewById(R.id.add_yoga);
        yogaRv = findViewById(R.id.yogaRv);

        yogaRv.setHasFixedSize(true); //Sure that size of items unchanged

        // add listener
        fab.setOnClickListener(v -> {
            Intent intent = getIntent();
            String userEmail = intent.getStringExtra("user_email");
            Intent intentAddOrEdit = new Intent(getApplicationContext(), AddAndEditYoga.class);
            intentAddOrEdit.putExtra("user_email", userEmail);
            intentAddOrEdit.putExtra("isEditMode", false);
            startActivity(intentAddOrEdit);
        });
        loadData();
    }

    private void loadData() {
        adapterYoga = new AdapterYoga(this, dbHelper.getAllYogas(LoginActivity.email));
        yogaRv.setAdapter(adapterYoga);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // refresh data
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_top_menu, menu);

        //get search item from menu
        MenuItem item = menu.findItem(R.id.searchYoga);
        //search area
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchYoga(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchYoga(newText);
                return true;
            }
        });

        return true;
    }

    private void searchYoga(String query) {
        if (!query.isEmpty()) {
            adapterYoga = new AdapterYoga(this, dbHelper.getSearchYoga(query));
            yogaRv.setAdapter(adapterYoga);
        } else {
            loadData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.deleteAllYoga) {
            dbHelper.deleteAllYoga();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}