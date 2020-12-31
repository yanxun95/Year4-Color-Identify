package com.example.coloridentifierapplication.ColorIdentity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;


import com.example.coloridentifierapplication.R;

import java.util.List;

public class ColorSaveRecyclerAdapter extends RecyclerView.Adapter<ColorSaveRecyclerAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<String> id, name, rgb, hex;
    DatabaseHelper databaseHelper;
    ColorSaveRecyclerAdapter colorSaveRecyclerAdapter = ColorSaveRecyclerAdapter.this;
    Context context;

    public ColorSaveRecyclerAdapter(Context context, List<String> id,List<String> name, List<String> rgb, List<String> hex) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.id = id;
        this.name = name;
        this.rgb = rgb;
        this.hex = hex;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView name, rgb, hex;
        View displayColor;
        ImageView btnOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerViewColorName);
            rgb = itemView.findViewById(R.id.recyclerViewRgb);
            hex = itemView.findViewById(R.id.recyclerViewHex);
            displayColor = itemView.findViewById(R.id.recyclerViewDisplayColor);
            btnOption = itemView.findViewById(R.id.recyclerViewOption);
            btnOption.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            popupMenu(view);
        }

        public void popupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.popup_save_list_menu);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.copyListName:
                    copyName(getAdapterPosition());
                    return true;

                case R.id.copyListRgb:
                    copyRgb(getAdapterPosition());
                    return true;

                case R.id.copyListHex:
                    copyHex(getAdapterPosition());
                    return true;

                case R.id.deleteList:
                    deleteColor(getAdapterPosition());
                    return true;

                default:
                    return false;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_recyclerview_save_color,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String displayColor = rgb.get(position);
        String[] arrDisplayColor = displayColor.split(", ");
        holder.displayColor.setBackgroundColor(android.graphics.Color.rgb(Integer.parseInt(arrDisplayColor[0]),Integer.parseInt(arrDisplayColor[1]),Integer.parseInt(arrDisplayColor[2])));
        holder.name.setText("Color name: " + name.get(position));
        holder.rgb.setText("RGB: " + rgb.get(position));
        holder.hex.setText("Hex: " + hex.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public void deleteColor(int position){
        databaseHelper = new DatabaseHelper(inflater.getContext());
        databaseHelper.deleteColor(new Color(id.get(position).trim()));
        id.remove(position);
        name.remove(position);
        rgb.remove(position);
        hex.remove(position);
        colorSaveRecyclerAdapter.notifyDataSetChanged();
    }

    public void copyName(int position){
        String x = name.get(position);
        ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("color", x);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, x + " has copy to the clipboard", Toast.LENGTH_SHORT).show();
    }

    public void copyRgb(int position){
        String y = rgb.get(position);
        ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("color", y);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, y + " has copy to the clipboard", Toast.LENGTH_SHORT).show();
    }

    public void copyHex(int position){
        String z = hex.get(position);
        ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("color", z);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, z + " has copy to the clipboard", Toast.LENGTH_SHORT).show();
    }
}
