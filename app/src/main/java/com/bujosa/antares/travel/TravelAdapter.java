package com.bujosa.antares.travel;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bujosa.antares.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.ViewHolder>{
    private final List<Travel> travelList;
    private final Context context;
    private final Boolean global;

    public TravelAdapter(List<Travel> travelList, Context context, Boolean global){
        this.travelList = travelList;
        this.context = context;
        this.global = global;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View travelView = layoutInflater.inflate(R.layout.list_element, parent, false);
        return new ViewHolder(travelView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Travel travel = travelList.get(position);
        holder.textView.setText(travel.getTitle());
        if(travel.getFavorite()) {
            holder.button.setBackgroundResource(R.drawable.ic_favorite_red_24);
        } else {
            holder.button.setBackgroundResource(R.drawable.ic_favorite_shadow_24);
        }
        Picasso.get()
                .load(travel.getImage())
                .placeholder(android.R.drawable.ic_dialog_map)
                .error(android.R.drawable.ic_dialog_alert)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return travelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ImageView imageView;
        public Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            button=itemView.findViewById(R.id.itemFavoriteButton);
            textView=itemView.findViewById(R.id.itemTextView);
            imageView=itemView.findViewById(R.id.itemImageView);
            imageView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                Travel travel = travelList.get(position);
                moveToDetail(travel);
            });
            button.setOnClickListener(view -> {
                int position = getAdapterPosition();
                Travel travel = travelList.get(position);

                likeClick(travel, button, position);
            });
        }

    }

    private void likeClick(Travel travel, Button button, int position){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("MyPreferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("mytravels", null);
        Type type = new TypeToken<ArrayList<Travel>>() {}.getType();
        List<Travel> travels = gson.fromJson(json, type);
        if (travels == null){
            travels = new ArrayList<>();
        }

        if(travel.getFavorite()){
            button.setBackgroundResource(R.drawable.ic_favorite_shadow_24);
            travel.setFavorite(false);
            for (int i = 0; i < travels.size(); i++) {
                Travel currentTravel = travels.get(i);
                if(currentTravel.getKey().equals(travel.getKey())){
                    travels.remove(i);
                    if(!global){
                        travelList.remove(i);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,travelList.size());
                    }
                    break;
                }
            }
        }else{
            button.setBackgroundResource(R.drawable.ic_favorite_red_24);
            travel.setFavorite(true);
            travels.add(travel);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson favoriteList = new Gson();
        String result = favoriteList.toJson(travels);
        editor.putString("mytravels",result);

        if(global){
            Gson currentList = new Gson();
            String currentResult = currentList.toJson(travelList);
            editor.putString("travels", currentResult);
        }else{
            String globalJson = sharedPreferences.getString("travels", null);
            Type globalType = new TypeToken<ArrayList<Travel>>() {}.getType();
            List<Travel> globalTravels = gson.fromJson(globalJson, globalType);

            for(int i = 0; i < globalTravels.size(); i++){
                Travel currentTravel = globalTravels.get(i);
                if(currentTravel.getKey().equals(travel.getKey())){
                    currentTravel.setFavorite(travel.getFavorite());
                    break;
                }
            }
            Gson globalList = new Gson();
            String globalResult = globalList.toJson(globalTravels);
            editor.putString("travels", globalResult);
        }
        editor.apply();
    }

    private void moveToDetail(Travel travel){
        Intent intent = new Intent(context, TravelDetailActivity.class);
        intent.putExtra("Travel", travel);
        context.startActivity(intent);
    }
}
