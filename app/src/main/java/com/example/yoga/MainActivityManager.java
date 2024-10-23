package com.example.yoga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivityManager extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView obRv;
    private DatabaseHelper dbHelper;
    private AdapterManager adapterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_manager);

        Toolbar toolbar = findViewById(R.id.obToolbar);
        toolbar.setTitle("Manager");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //init db
        dbHelper = new DatabaseHelper(this);

        //initialization
        fab = findViewById(R.id.obAdd);
        obRv = findViewById(R.id.obRv);

        obRv.setHasFixedSize(true); //Sure that size of items unchanged

        // add listener
        fab.setOnClickListener(v -> {
            Intent intent = getIntent();
            String idYoga = intent.getStringExtra("idYoga");
            Intent intentAddOrEdit = new Intent(getApplicationContext(), AddAndEditManager.class);
            intentAddOrEdit.putExtra("idYoga", idYoga);
            intentAddOrEdit.putExtra("isEditMode", false);
            startActivity(intentAddOrEdit);
        });
        loadData();
    }

    private void loadData() {
        // Receive yogaId
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String idYoga = sharedPreferences.getString("yogaId", null);
        adapterManager = new AdapterManager(this, dbHelper.getAllManagers(idYoga));
        obRv.setAdapter(adapterManager);

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
                searchManager(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchManager(newText);
                return true;
            }
        });
        return true;
    }

    private void searchManager(String query) {
        if (!query.isEmpty()) {
            adapterManager = new AdapterManager(this, dbHelper.getSearchManager(query));
            obRv.setAdapter(adapterManager);
        } else {
            loadData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.deleteAllYoga) {
            dbHelper.deleteAllManager();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
