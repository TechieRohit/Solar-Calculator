package com.example.rohit.adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.modals.Places;
import com.rohit.solarcalulator.R;


import java.util.ArrayList;
import java.util.List;

public class SavedPlacesAdapter extends RecyclerView.Adapter<SavedPlacesAdapter.ViewHolder> {

    List<Places> placesList = new ArrayList<>();
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
        holder.textViewTitle.setText("" + placesList.get(position).getPlaceName());
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
                    Toast.makeText(context,"clicked",Toast.LENGTH_LONG).show();
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
