package com.example.dinasaad.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DinaSaad on 14/08/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NumberViewHolder> {

    private static final String TAG = RecyclerAdapter.class.getSimpleName();

    // COMPLETED (3) Create a final private ListItemClickListener called mOnClickListener
    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;

    private static String viewHolderCount;
    int layoutIdForListItem;
    private ArrayList<String> mNumberItems;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecyclerAdapter(ArrayList<String> numberOfItems, ListItemClickListener listener,int xmltype) {
        mNumberItems = numberOfItems;
        mOnClickListener = listener;
        if(xmltype == 0)
            layoutIdForListItem = R.layout.list_item;
        else if(xmltype==1)
            layoutIdForListItem = R.layout.list_item_detail;
       // viewHolderCount = 0;
    }


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        viewHolderCount=mNumberItems.get(position);
        holder.viewHolderIndex.setText( viewHolderCount);
        Log.d(TAG, "#" + viewHolderCount);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return mNumberItems.size();
    }
    class NumberViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        // Will display which ViewHolder is displaying this data
        TextView viewHolderIndex;
        public NumberViewHolder(View itemView) {
            super(itemView);
            viewHolderIndex = (TextView) itemView.findViewById(R.id.Recipe_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}



