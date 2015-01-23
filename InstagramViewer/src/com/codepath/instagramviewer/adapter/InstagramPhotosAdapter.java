package com.codepath.instagramviewer.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.instagramviewer.R;
import com.codepath.instagramviewer.model.InstagramPhoto;
import com.codepath.instagramviewer.util.CircleTransform;
import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, android.R.layout.simple_list_item_1, photos);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Take datasource  at position
        // Get the data item
        InstagramPhoto currentPhoto = getItem(position);
        //Check if we are using s recycled view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        //Lookup the subview within the template
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvCreateDate = (TextView) convertView.findViewById(R.id.tv_createDate);
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
        ImageView imgProfilePic = (ImageView) convertView.findViewById(R.id.imgProfilePic);
        ImageView imgLikes = (ImageView) convertView.findViewById(R.id.imgLikes);

        //Populate the subviews with the correct data
        tvUsername.setText(currentPhoto.getUsername());
        tvLikesCount.setText(""+currentPhoto.getLikesCount() +" likes");
        imgLikes.setImageResource(R.drawable.ic_likes);

        // Format timestamp into elapsed time
        String timePassed = DateUtils.getRelativeTimeSpanString(currentPhoto.getCreatedTime()*1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)+"";
        // print only digits and 1st char of time unit
        String[] timeParts = timePassed.split(" ");
        if (timeParts[0].equals("in")) {
            //in 12 hours
            tvCreateDate.setText(timeParts[1] + timeParts[2].substring(0,1));
        } else {
            //12 hour ago
            tvCreateDate.setText(timeParts[0] + timeParts[1].substring(0,1));
        }

        tvCaption.setText(Html.fromHtml("<font color=\"#206199\"><b>" + currentPhoto.getUsername()
                + "  " + "</b></font>" + "<font color=\"#000000\">" + currentPhoto.getCaption() + "</font>"));

        //Reset the image from the recycled view
        imgPhoto.setImageResource(0);
        imgProfilePic.setImageResource(0);
        //Ask for the photo to be added to the imageview based on the photo url
        // In the background, send a network request to the url, download the image bytes,
        // convert into bitmap, resize images, insert bitmap into imageview
        //main image
        Picasso.with(getContext()).load(currentPhoto.getImageUrl()).placeholder(R.drawable.ic_placeholder).into(imgPhoto);
        //profile photo
        Picasso.with(getContext()).load(currentPhoto.getProfilePicUrl()).transform(new CircleTransform()).into(imgProfilePic);
        // Return view for that data item
        return convertView;

    }
}
