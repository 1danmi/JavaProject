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
import android.widget.RatingBar;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.entities.Activity;

import java.util.List;


public class ActivityRecyclerViewAdapter extends RecyclerView.Adapter<ActivityRecyclerViewAdapter.ActivityImageViewHolder> {

    private static final String TAG = "businessRecyclerViewAda";
    private List<Activity> activitiesList;
    private Context mContext;


    public ActivityRecyclerViewAdapter(List<Activity> activitiesList, Context mContext) {
        this.activitiesList = activitiesList;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((activitiesList != null) && (activitiesList.size() != 0) ? activitiesList.size() : 0);

    }

    @Override
    public void onBindViewHolder(final ActivityRecyclerViewAdapter.ActivityImageViewHolder holder, int position) {
        // Called by the layout manager when it wants new data in an existing row

        final Activity activityItem = activitiesList.get(position);
        Log.d(TAG, "onBindViewHolder: " + activityItem.getActivityName() + " --> " + position);

        Bitmap bmp = BitmapFactory.decodeByteArray(activityItem.getActivityImage(), 0, activityItem.getActivityImage().length);
        holder.image.setImageBitmap(bmp);
        holder.activityName.setText(activityItem.getActivityName());
        holder.description.setText(activityItem.getActivityDescription());
        holder.ratingText.setText(Double.toString(activityItem.getActivityRating()));
        holder.ratingBar.setRating((float) activityItem.getActivityRating());
        holder.price.setText("$" + Double.toString(activityItem.getActivityCost()));

    }

    @Override
    public ActivityRecyclerViewAdapter.ActivityImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card, parent, false);
        return new ActivityRecyclerViewAdapter.ActivityImageViewHolder(view);
    }


    public void loadNewData(List<Activity> newActivities) {
        activitiesList.clear();
        boolean exist;
        for (Activity a : newActivities) {
            exist = false;
            if (!a.get_ID().equals("")) {
                for (Activity a2 : activitiesList) {
                    if (a.get_ID().equals(a2.get_ID())) {
                        exist = true;
                        break;
                    }
                }
                if (!exist){
                    activitiesList.add(a);
                }
            }
        }
//        this.activitiesList = newActivities;
        notifyDataSetChanged();
    }


    public Activity getActivity(int position) {
        return ((activitiesList != null) && (activitiesList.size() != 0) ? activitiesList.get(position) : null);
    }


    static class ActivityImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ActivityImageViewHolder";
        ImageView image;
        TextView activityName;
        TextView description;
        TextView ratingText;
        RatingBar ratingBar;
        TextView price;

        public ActivityImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "BusinessImageViewHolder: starts");
            this.image = (ImageView) itemView.findViewById(R.id.activity_image_view);
            this.activityName = (TextView) itemView.findViewById(R.id.activity_title_text_view);
            this.description = (TextView) itemView.findViewById(R.id.dish_description_text_view);
            this.ratingText = (TextView) itemView.findViewById(R.id.rating_text_view);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.activity_rating_bar);
            this.price = (TextView) itemView.findViewById(R.id.price_text_view);
        }
    }

    public List<Activity> getActivitiesList() {
        return activitiesList;
    }
}
