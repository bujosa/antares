package com.bujosa.antares.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bujosa.antares.R;
import com.bujosa.antares.entity.Menu;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    List<Menu> menus;
    Context context;

    public MenuAdapter(List<Menu> menus, Context context){
        this.menus = menus;
        this.context = context;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int i) {
        return menus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return menus.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Menu menu = menus.get(i);
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.menu_item, viewGroup, false);
        }
        CardView cardView = view.findViewById(R.id.cardView);
        TextView textView = view.findViewById(R.id.textView);
        ImageView imageView = view.findViewById(R.id.imageView);

        textView.setText(menu.getDescription());
        imageView.setImageResource(menu.getResourceImageView());
        cardView.setOnClickListener(view1 -> context.startActivity(new Intent(context, menu.getClase())));

        return view;
    }
}
