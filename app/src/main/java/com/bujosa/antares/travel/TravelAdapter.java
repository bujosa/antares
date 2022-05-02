package com.bujosa.antares.travel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bujosa.antares.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

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
        View travelView = layoutInflater.inflate(R.layout.card_travel, parent, false);
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

        TravelService travelService = new TravelService(FirebaseFirestore.getInstance());


        if(travel.getFavorite()){
            button.setBackgroundResource(R.drawable.ic_favorite_shadow_24);
            travel.setFavorite(false);
            travelService.updateTravel(travel);
            for (int i = 0; i < travelList.size(); i++) {
                Travel currentTravel = travelList.get(i);
                if(currentTravel.getId().equals(travel.getId())){
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
            travelService.updateTravel(travel);
        }

        if(!global){
            for(int i = 0; i < travelList.size(); i++){
                Travel currentTravel = travelList.get(i);
                if(currentTravel.getId().equals(travel.getId())){
                    currentTravel.setFavorite(travel.getFavorite());
                    break;
                }
            }
        }
    }

    private void moveToDetail(Travel travel){
        Intent intent = new Intent(context, TravelActivity.class);
        intent.putExtra("Travel", travel);
        context.startActivity(intent);
    }
}
