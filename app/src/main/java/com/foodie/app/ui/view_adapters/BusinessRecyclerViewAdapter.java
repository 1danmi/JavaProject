package com.foodie.app.ui.view_adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private boolean lock;
    private ProgressBar loadingImage;
    private TextView noBusinessesText;
    private Activity activity;

    public BusinessRecyclerViewAdapter(List<Business> businessesList, Context mContext, ProgressBar loading, TextView noBusiness, Activity a) {
        this.activity = a;
        this.businessesList = businessesList;
        this.noBusinessesText = noBusiness;
        loadingImage = loading;
        this.mContext = mContext;
        lock = false;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((businessesList != null) && (businessesList.size() != 0) ? businessesList.size() : 0);

    }

    @Override
    public void onBindViewHolder(final BusinessImageViewHolder holder, int position) {
        // Called by the layout manager when it wants new data in an existing row
        //while(lock){}
        final Business businessItem = businessesList.get(position);
        Log.d(TAG, "onBindViewHolder: " + businessItem.getBusinessName() + " --> " + position);

        //final int THUMBSIZE = 128;
        Bitmap bmp;
        if (businessItem.getBusinessLogo() != null) {
            bmp = BitmapFactory.decodeByteArray(businessItem.getBusinessLogo(), 0, businessItem.getBusinessLogo().length);

            //bmp = ThumbnailUtils.extractThumbnail(bmp, THUMBSIZE, THUMBSIZE);
            holder.logo.setImageBitmap(bmp);
            holder.progressBar.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.logo.setTransitionName(Business.getURI() + businessItem.getBusinessName());
        }
        holder.title.setText(businessItem.getBusinessName());
        holder.address.setText(businessItem.getBusinessAddress());
//        ContentResolverDatabase.getBusinessActivitiesList(mContext, businessItem.get_ID(), true);
        holder.numOfActivities.setText(businessItem.getBusinessPhoneNo());

        //TODO: Add query for number of activities of the business

    }

    public void addItem(Business business) {
        businessesList.add(business);
        notifyItemInserted(businessesList.size());
    }

    public void removeItem(int position) {
        businessesList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public BusinessImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Called by the layout manager when it needs a new view
        //while(lock){}
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_card, parent, false);
        return new BusinessImageViewHolder(view);
    }

    public void loadNewData(List<Business> newBusinesses) {
        businessesList.clear();
        notifyDataSetChanged();
        for (int i = 0; i < newBusinesses.size(); i++)
            add(newBusinesses.get(i), i);

    }

    public void remove(int position) {
        businessesList.remove(position);
        notifyItemRemoved(position);
    }

    public void add(Business business, final int position) {
        businessesList.add(business);
        notifyItemInserted(position);
    }

    public Business getBusiness(int position) {
        return ((businessesList != null) && (businessesList.size() != 0) ? businessesList.get(position) : null);
    }

    static class BusinessImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "BusinessImageViewHolder";
        ImageView logo;
        TextView title;
        TextView address;
        View mView;
        ImageButton menu;
        TextView numOfActivities;
        ProgressBar progressBar;

        public BusinessImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "BusinessImageViewHolder: starts");
            mView = itemView;
            this.logo = (ImageView) itemView.findViewById(R.id.gif_loading_business);
            this.title = (TextView) itemView.findViewById(R.id.businessTitleTextView);
            this.address = (TextView) itemView.findViewById(R.id.businessAddressTextView);
            this.menu = (ImageButton) itemView.findViewById(R.id.businessMenuButton);
            this.numOfActivities = (TextView) itemView.findViewById(R.id.numOfActivities);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.business_card_progress_bar);
        }
    }

    public List<Business> getBusinessesList() {
        return businessesList;
    }

    public void replaceAll(List<Business> models) {
        for (int i = businessesList.size() - 1; i >= 0; i--) {
            final Business model = businessesList.get(i);
            if (!models.contains(model)) {
                businessesList.remove(model);
            }
        }
        businessesList.addAll(models);
    }

    public ProgressBar getLoadingImage() {
        return loadingImage;
    }

    public TextView getNoBusinessesText() {
        return noBusinessesText;
    }
}

