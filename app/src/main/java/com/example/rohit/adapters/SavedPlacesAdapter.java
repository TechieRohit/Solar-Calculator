package com.example.rohit.adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rohit.modals.Places;
import com.example.rohit.views.customview.CustomDialogs;
import com.rohit.solarcalulator.R;


import java.util.List;

public class SavedPlacesAdapter extends RecyclerView.Adapter<SavedPlacesAdapter.ViewHolder> {

    List<Places> placesList;
    Context context;

    public SavedPlacesAdapter(Context context,List<Places> places) {
        this.context = context;
        placesList = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_saved_places,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewTitle.setText(placesList.get(position).getPlaceName() + " (" + placesList.get(position).getDate() + ")");
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.place_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    CustomDialogs.showSavedLocationDialogs(context,placesList.get(getAdapterPosition()).getSunrise(),
                            placesList.get(pos).getSunset(),placesList.get(pos).getPlaceName(),placesList.get(pos).getDate());
                }
            });
        }

        @Override
        public void onClick(View v) {
        }
    }

    public void setNotes(List<Places> places) {
        this.placesList = places;
        notifyDataSetChanged();
    }
}
