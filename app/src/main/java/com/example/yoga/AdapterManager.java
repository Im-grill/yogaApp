package com.example.yoga;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterManager extends RecyclerView.Adapter<AdapterManager.ManagerViewHolder> {

    private Context context;
    private ArrayList<ModelManager> listManager;
    private DatabaseHelper dbHelper;

    public AdapterManager(Context context, ArrayList<ModelManager> managerList) {
        this.context = context;
        this.listManager = managerList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_manager_item, parent, false);
        ManagerViewHolder vh = new ManagerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerViewHolder holder, int position) {
        ModelManager modelManager = listManager.get(position);

        //get data
        String managerId = modelManager.getId();
        String managerName = modelManager.getName();
        String managerYogaId = modelManager.getYogaId();
        String managerComment = modelManager.getComment();
        String managerImage = modelManager.getImage();
        String managerAddedTime = modelManager.getAddedTime();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        calendar.setTimeInMillis(Long.parseLong(managerAddedTime));
        String timeAdd = "" + DateFormat.format("dd/MM/yy hh:mm:aa", calendar);

        //set data in view
        holder.managerName.setText(managerName);
        holder.managerComment.setText(managerComment);
        holder.managerDate.setText(timeAdd);
        if (managerImage.equals("")) {
            holder.managerImage.setImageResource(R.drawable.camera);
        } else {
            Glide.with(context).load(Uri.parse(managerImage)).into(holder.managerImage);
        }

        // handle delete click
        holder.managerDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbHelper.deleteManager(managerId);
                    Log.d("Delete Manager", "Manager deleted with ID: " + managerId);

                    ((MainActivityManager) context).onResume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.managerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAndEditManager.class);

                intent.putExtra("ID", managerId);
                intent.putExtra("NAME", managerName);
                intent.putExtra("MANAGER_ID", managerYogaId);
                intent.putExtra("COMMENT", managerComment);
                intent.putExtra("IMAGE", managerImage);
                intent.putExtra("ADD", managerAddedTime);
                intent.putExtra("isEditMode", true);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listManager.size();
    }

    class ManagerViewHolder extends RecyclerView.ViewHolder {

        ImageView managerImage, managerEdit, managerDelete;
        TextView managerName, managerComment, managerDate;

        public ManagerViewHolder(@NonNull View itemView) {
            super(itemView);

            //init view
            managerImage = itemView.findViewById(R.id.managerImage);
            managerName = itemView.findViewById(R.id.managerName);
            managerComment = itemView.findViewById(R.id.managerComment);
            managerDate = itemView.findViewById(R.id.managerDate);
            managerEdit = itemView.findViewById(R.id.managerEdit);
            managerDelete = itemView.findViewById(R.id.managerDelete);
        }
    }
}
