package com.example.coloridentifierapplication.Menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coloridentifierapplication.Camera.CameraActivity;
import com.example.coloridentifierapplication.ColorBlindTest.ColorBlindTest;
import com.example.coloridentifierapplication.ColorIdentity.ColorIdentity;
import com.example.coloridentifierapplication.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<String> titles;
    List<Integer> images;
    LayoutInflater inflater;

    public Adapter(Context context, List<String> titles, List<Integer> images){
        this.titles = titles;
        this.images = images;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gridview_menu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.icon.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView icon;
        Integer position;
        Intent intent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.menuName);
            icon = itemView.findViewById(R.id.menuIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    if(position.equals(0)){
                        intent = new Intent(v.getContext(), CameraActivity.class);
                    }else if(position.equals(1)){
                        intent = new Intent(v.getContext(), ColorIdentity.class);
                    }else if(position.equals(2)){
                        intent = new Intent(v.getContext(), ColorBlindTest.class);
                    }
                    v.getContext().startActivity(intent);
                    //Toast.makeText(v.getContext(), "Clicked -> " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
