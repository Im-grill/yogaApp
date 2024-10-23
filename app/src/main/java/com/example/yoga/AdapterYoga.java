package com.example.yoga;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterYoga extends RecyclerView.Adapter<AdapterYoga.YogaViewHolder> {

    private Context context;
    private ArrayList<ModelYoga> listYoga;
    private DatabaseHelper dbHelper;

    public AdapterYoga(Context context, ArrayList<ModelYoga> yogaList) {
        this.context = context;
        this.listYoga = yogaList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public YogaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_yoga_item, parent, false);
        YogaViewHolder vh = new YogaViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull YogaViewHolder holder, int position) {
        ModelYoga modelYoga = listYoga.get(position);

        //get data
        String yogaId = modelYoga.getId();
        String yogaEmail = modelYoga.getUserEmail();
        String yogaName = modelYoga.getName();
        String yogaDayOfTheWeek = modelYoga.getLocation();
        String yogaDate = modelYoga.getDate();
        String yogaCapacity = modelYoga.getParkingAvailable();
        String yogaPrice = modelYoga.getLength();
        String yogaTypeOfClass = modelYoga.getDifficulty();
        String yogaDescription = modelYoga.getDescription();
        String yogaImage = modelYoga.getImage();
        String yogaTeacher = modelYoga.getPartner();
        String addedTime = modelYoga.getAddedTime();
        String updateTime = modelYoga.getUpdateTime();

        //set data in view
        holder.yogaName.setText(yogaName);
        holder.yogaDayOfTheWeek.setText(yogaDayOfTheWeek);
        holder.yogaPrice.setText(yogaPrice + " USD");
        if (yogaImage.equals("")) {
            holder.yogaImage.setImageResource(R.drawable.camera);
        } else {
            holder.yogaImage.setImageURI(Uri.parse(yogaImage));
        }

        // handle delete click
        holder.yogaDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbHelper.deleteHike(yogaId);
                    Log.d("Delete Yoga", "Yoga deleted with ID: " + yogaId);

                    ((MainActivity) context).onResume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.yogaEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAndEditYoga.class);

                intent.putExtra("ID", yogaId);
                intent.putExtra("EMAIL", yogaEmail);
                intent.putExtra("NAME", yogaName);
                intent.putExtra("LOCATION", yogaDayOfTheWeek);
                intent.putExtra("DATE", yogaDate);
                intent.putExtra("PARKING", yogaCapacity);
                intent.putExtra("LENGTH", yogaPrice);
                intent.putExtra("DIFFICULTY", yogaTypeOfClass);
                intent.putExtra("DESCRIPTION", yogaDescription);
                intent.putExtra("IMAGE", yogaImage);
                intent.putExtra("PARTNER", yogaTeacher);
                intent.putExtra("ADD", addedTime);
                intent.putExtra("UPDATE", updateTime);
                intent.putExtra("isEditMode", true);

                context.startActivity(intent);
            }
        });

        holder.yogaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailYoga.class);
                intent.putExtra("yogaId", yogaId);
                context.startActivity(intent);
                Toast.makeText(context, "Detail of yoga " + yogaId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listYoga.size();
    }

    class YogaViewHolder extends RecyclerView.ViewHolder {

        //view for row_yoga_item
        ImageView yogaImage, yogaEdit, yogaDelete;
        TextView yogaName, yogaDayOfTheWeek, yogaPrice;
        RelativeLayout relativeLayout;

        public YogaViewHolder(@NonNull View itemView) {
            super(itemView);

            //init view
            yogaImage = itemView.findViewById(R.id.yogaImage);
            yogaName = itemView.findViewById(R.id.yogaName);
            yogaDayOfTheWeek = itemView.findViewById(R.id.yogaDayOfTheWeek);
            yogaPrice = itemView.findViewById(R.id.yogaPrice);
            yogaEdit = itemView.findViewById(R.id.yogaEdit);
            yogaDelete = itemView.findViewById(R.id.yogaDelete);
        }
    }
}
