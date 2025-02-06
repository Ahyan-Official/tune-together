package com.app.tunetogether.az;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> implements Filterable {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    List<Event> eventList;
    List<Event> eventListFull;
    Context context;


    // data is passed into the constructor
    EventsAdapter(Context context, List<Event> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        eventList = data;
        eventListFull = new ArrayList<>(data);

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_roww, parent, false);
        return new ViewHolder(view);

    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        Picasso.get().load(eventList.get(position).getImage_url()).into(holder.img);
        holder.tv_title.setText(eventList.get(position).getArtist_name());
        holder.tv_location.setText(eventList.get(position).getLocation()+" "+eventList.get(position).getCountry());

        holder.date.setText(eventList.get(position).getStarts_at());


        final int min = 1;
        final int max = 3;
        final int random = new Random().nextInt((max - min) + 1) + min;

        if(random==1){
            holder.bg.setImageResource(R.drawable.ss444);

        }else if (random==2){
            holder.bg.setImageResource(R.drawable.ss33);

        }else {
            holder.bg.setImageResource(R.drawable.ss555);

        }

        holder.tv_venue.setText(eventList.get(position).getName_venue());


        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,ViewEventActivity.class);
                intent.putExtra("image", eventList.get(position).getImage_url());
                intent.putExtra("artist_name", eventList.get(position).getArtist_name());
                intent.putExtra("documentId", eventList.get(position).getId());
                intent.putExtra("location", eventList.get(position).getLocation()+" "+eventList.get(position).getCountry());
                intent.putExtra("lat", eventList.get(position).getLatitude());
                intent.putExtra("lng", eventList.get(position).getLongitude());
                intent.putExtra("start_at", eventList.get(position).getStarts_at());
                intent.putExtra("venue_name", eventList.get(position).getName_venue());



                context.startActivity(intent);



            }
        });


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Event> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                filteredList.addAll(eventListFull);

            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Event item : eventListFull) {

                    if (item.getName_venue().toLowerCase().contains(filterPattern)) {

                        filteredList.add(item);

                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            eventList.clear();
            eventList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_title,date,tv_location,tv_venue;
        Button btn_detail;
        RoundedImageView img,bg;
        CardView layout;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);

            bg = itemView.findViewById(R.id.bg);

            tv_venue = itemView.findViewById(R.id.tv_venue);
            btn_detail = itemView.findViewById(R.id.btn_detail);

            tv_title = itemView.findViewById(R.id.tv_title);
            date = itemView.findViewById(R.id.date);
            tv_location = itemView.findViewById(R.id.tv_location);
            layout = itemView.findViewById(R.id.layout);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }




}