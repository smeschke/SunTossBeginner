package com.stephen.acer.suntossbeginner;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class mAdapter extends RecyclerView.Adapter<mAdapter.mAdapterViewHolder> {

    //constant ID's for the View type for level or trick (S11.02)
    private static final int VIEW_TYPE_LEVEL = 0;
    private static final int VIEW_TYPE_TRICK = 1;
    //get context
    private final Context mContext;
    //set up the click handler
    final private mAdapterOnClickHandler mClickHandler;
    //create string for output list
    public ArrayList<String> output_list;
    private boolean mUseTrickLayout;
    //create int list for the names of the levels
    int[] level_icons = new int[] {R.drawable.one,R.drawable.two,R.drawable.three, R.drawable.four,R.drawable.five};


    //get stuff on list from Main Activity
    public mAdapter(@NonNull Context context,
                    mAdapterOnClickHandler clickHandler,
                    ArrayList<String> ol) {
        mContext = context;
        mClickHandler = clickHandler;
        output_list = ol;
        // Set mUseTrickLayout to the value specified in resources
        mUseTrickLayout = true;
    }

    //when view holder is created, inflate the views
    @Override
    public mAdapter.mAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_LEVEL: {
                layoutId = R.layout.list_item_level;
                break;
            }
            case VIEW_TYPE_TRICK: {
                layoutId = R.layout.list_item;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new mAdapterViewHolder(view);
    }

    //bind data to view holder
    @Override
    public void onBindViewHolder(mAdapter.mAdapterViewHolder holder, int position) {
        //there are two types of items on the list
        //the list_item_level is in the first position
        //the tricks make of the rest of the list
        if (position == 0) {
            SharedPreferences settings = mContext.getSharedPreferences("LOG", 0);
            String name = settings.getString("NAME", "Juggler Name\n(enter name in settings)");
            //get number for total catches
            int total_catches = settings.getInt("total_catches", 0);
            holder.totalCatchesTextView.setText("Total Catches: " + total_catches);
            holder.nameTextView.setText(name);
            float overall_level = settings.getFloat("OVERALL_LEVEL", 0);
            double level = Math.round(overall_level*10)/10.0;
            holder.levelTextView.setText(Double.toString(level));
            //draw the icon
            int levelInt = Math.round(overall_level);
            holder.levelIconView.setImageResource( level_icons[levelInt-1] );
        } else {
            //get the id for the level
            String trick_data = output_list.get(position);
            //the user's skill level is the last character in the string
            String trick_name = trick_data.split(",", 0)[0];
            String num_catches = trick_data.split(",", 0)[1];
            String goal = trick_data.split(",", 0)[2];
            String level = trick_data.split(",", 0)[3];
            //bind to holder
            holder.iconView.setImageResource(level_icons[Integer.parseInt(level)-1]);
            holder.textView.setText(trick_name);
            holder.numCatchesTextView.setText(num_catches);
            holder.goalCatchesTextView.setText("/" + goal);
            holder.levelDescriptionTextView.setText("Current Level: " + level);
        }
    }

    //How many? The size of the output_list.
    @Override
    public int getItemCount() {
        return output_list.size();
    }

    //override getItemViewType (from S11.02)
    @Override
    public int getItemViewType(int position) {
        if (mUseTrickLayout && position == 0) {
            return VIEW_TYPE_LEVEL;
        } else {
            return VIEW_TYPE_TRICK;
        }
    }

    //set up clicks
    public interface mAdapterOnClickHandler {
        void onClick(int adapterPosition);
    }

    //setting up the recycler view, and clicks
    class mAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        //initialize views
        public final ImageView iconView;
        public final TextView textView;
        public final TextView levelTextView;
        public final ImageView levelIconView;
        public final TextView nameTextView;
        public final TextView goalCatchesTextView;
        public final TextView numCatchesTextView;
        public final TextView levelDescriptionTextView;
        public final TextView totalCatchesTextView;

        //super the views so that they can be bound - set click listener too
        mAdapterViewHolder(View view) {
            super(view);
            //image  and text views for tricks
            iconView = (ImageView) view.findViewById(R.id.image_view);
            textView = (TextView) view.findViewById(R.id.tv_item_number);
            //image and text views to show users level
            levelTextView = (TextView) view.findViewById(R.id.level_text_view);
            levelIconView = (ImageView) view.findViewById(R.id.level_icon);
            nameTextView = (TextView) view.findViewById(R.id.name);
            goalCatchesTextView = (TextView) view.findViewById(R.id.goal_catches_text_view);
            numCatchesTextView = (TextView) view.findViewById(R.id.num_catches_text_view);
            levelDescriptionTextView = (TextView) view.findViewById(R.id.level_description);
            totalCatchesTextView = (TextView) view.findViewById(R.id.totalCatches);
            //set on click listener
            itemView.setOnClickListener(this);
        }

        //when a user taps the list
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}
