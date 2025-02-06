package com.app.tunetogether.az;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class EventTopAdapter extends RecyclerView.Adapter<EventTopAdapter.ViewHolder> implements Filterable {

    private LayoutInflater mInflater;
    List<EventTop> eventList;
    List<EventTop> eventListFull;
    Context context;
    OnItemClickListener listener;

    // data is passed into the constructor
    EventTopAdapter(Context context, List<EventTop> data,OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        eventList = data;
        eventListFull = new ArrayList<>(data);
        this.listener = listener;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.events_top, parent, false);
        return new ViewHolder(view);

    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.bind(eventList.get(position), listener);
        holder.tv_title.setText(eventList.get(position).getName());


        if(eventList.get(position).getUsername().toLowerCase().startsWith("tay")){
            holder.img.setImageResource(R.drawable.taylor);

        }else  if(eventList.get(position).getUsername().toLowerCase().startsWith("zedd")){
            holder.img.setImageResource(R.drawable.zedd);

        }else  if(eventList.get(position).getUsername().toLowerCase().startsWith("samsmith")){
            holder.img.setImageResource(R.drawable.sam);

        }else  if(eventList.get(position).getUsername().toLowerCase().startsWith("edsheeran")){
            holder.img.setImageResource(R.drawable.ed);

        }else  if(eventList.get(position).getUsername().toLowerCase().startsWith("katyperry")){
            holder.img.setImageResource(R.drawable.katy);

        }else  if(eventList.get(position).getUsername().toLowerCase().startsWith("louis")){
            holder.img.setImageResource(R.drawable.louis);

        }else  if(eventList.get(position).getUsername().toLowerCase().startsWith("drake")){
            holder.img.setImageResource(R.drawable.drake);

        }else  if(eventList.get(position).getUsername().toLowerCase().startsWith("charlieputh")){
            holder.img.setImageResource(R.drawable.charlie);

        }else  if(eventList.get(position).getUsername().toLowerCase().startsWith("TheWeeknd")){
            holder.img.setImageResource(R.drawable.weekend);

        }




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
            List<EventTop> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(eventListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (EventTop item : eventListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
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

    public interface OnItemClickListener {
        void onItemClick(EventTop item);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tv_title;
        RoundedImageView img;
        RelativeLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);

            tv_title = itemView.findViewById(R.id.tv_title);

            layout = itemView.findViewById(R.id.layout);


        }

        public void bind(final EventTop item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    Toast.makeText(context, "asddsadsa", Toast.LENGTH_SHORT).show();
                    listener.onItemClick(item);
                }
            });
        }

    }

}
