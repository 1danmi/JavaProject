package com.foodie.app.ui.view_adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.entities.Business;

import java.util.List;

/**
 * Created by Daniel on 12/16/2016.
 */

public class BusinessRecyclerViewAdapter extends RecyclerView.Adapter<BusinessRecyclerViewAdapter.BusinessImageViewHolder> {
    private static final String TAG = "businessRecyclerViewAda";
    private List<Business> businessesList;
    private Context mContext;


    public BusinessRecyclerViewAdapter(List<Business> businessesList, Context mContext) {
        this.businessesList = businessesList;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((businessesList != null) && (businessesList.size() != 0) ? businessesList.size() : 0);

    }

    @Override
    public void onBindViewHolder(final BusinessImageViewHolder holder, int position) {
        // Called by the layout manager when it wants new data in an existing row

        final Business businessItem = businessesList.get(position);
        Log.d(TAG, "onBindViewHolder: " + businessItem.getBusinessName() + " --> " + position);

        //final int THUMBSIZE = 128;
        Bitmap bmp = BitmapFactory.decodeByteArray(businessItem.getBusinessLogo(), 0, businessItem.getBusinessLogo().length);

        //bmp = ThumbnailUtils.extractThumbnail(bmp, THUMBSIZE, THUMBSIZE);
        holder.logo.setImageBitmap(bmp);


        holder.title.setText(businessItem.getBusinessName());
        holder.address.setText(businessItem.getBusinessAddress());
        //TODO: Add query for number of activities of the business

    }

    public void addItem(Business business){
        businessesList.add(business);
        notifyItemInserted(businessesList.size());
    }

    public void removeItem(int position){
        businessesList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public BusinessImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_card, parent, false);
        return new BusinessImageViewHolder(view);
    }


    public void loadNewData(List<Business> newBusinesses) {
        this.businessesList = newBusinesses;
        notifyDataSetChanged();
    }


    public Business getBusiness(int position) {
        return ((businessesList != null) && (businessesList.size() != 0) ? businessesList.get(position) : null);
    }


    static class BusinessImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "BusinessImageViewHolder";
        ImageView logo;
        TextView title;
        TextView address;
        TextView activitiesCounter;
        View mView;

        public BusinessImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "BusinessImageViewHolder: starts");
            mView = itemView;
            this.logo = (ImageView) itemView.findViewById(R.id.business_image_view);
            this.title = (TextView) itemView.findViewById(R.id.businessTitleTextView);
            this.address = (TextView) itemView.findViewById(R.id.businessAddressTextView);
            this.activitiesCounter = (TextView) itemView.findViewById(R.id.numOfActivities);
        }
    }
}

