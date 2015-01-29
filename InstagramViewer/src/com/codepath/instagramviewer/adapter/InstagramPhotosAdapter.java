package com.codepath.instagramviewer.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.instagramviewer.R;
import com.codepath.instagramviewer.model.InstagramPhoto;
import com.codepath.instagramviewer.model.InstagramPhotoComment;
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
        final InstagramPhoto currentPhoto = getItem(position);
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
        ImageView imgBubble1 = (ImageView) convertView.findViewById(R.id.ivCommentBubble1);
        imgBubble1.setVisibility(View.GONE);
        TextView firstComment = (TextView) convertView.findViewById(R.id.tvComment1);
        firstComment.setVisibility(View.GONE);
        ImageView imgBubble2 = (ImageView) convertView.findViewById(R.id.ivCommentBubble2);
        imgBubble2.setVisibility(View.GONE);
        TextView secondComment = (TextView) convertView.findViewById(R.id.tvComment2);
        secondComment.setVisibility(View.GONE);
        TextView allComments = (TextView) convertView.findViewById(R.id.tvCommentMore);
        allComments.setVisibility(View.GONE);

        //Populate the subviews with the correct data
        tvUsername.setText(currentPhoto.getUsername());
        tvLikesCount.setText(""+currentPhoto.getLikesCount() +" likes");
        imgLikes.setImageResource(R.drawable.ic_likes);

        //populate the first and second comment with the first and second element of the comments arrayList
        List<InstagramPhotoComment> comments = currentPhoto.getComments();

        Log.d("martas", "comments size!!!: " + comments.size());


        if (currentPhoto.getCommentsCount() > 0) {
            firstComment.setText(Html.fromHtml("<font color=\"#206199\"><b>" +comments.get(0).getUserName()
                    + "  " + "</b></font>" + "<font color=\"#000000\">" + comments.get(0).getCommentText()+ "</font>"));
            firstComment.setVisibility(View.VISIBLE);
            imgBubble1.setVisibility(View.VISIBLE);
        }

        if (comments.size() > 1) {
            secondComment.setText(Html.fromHtml("<font color=\"#206199\"><b>" + comments.get(1).getUserName()
                    + "  " + "</b></font>" + "<font color=\"#000000\">" + comments.get(1).getCommentText()+ "</font>"));
            secondComment.setVisibility(View.VISIBLE);
            imgBubble2.setVisibility(View.VISIBLE);
        }

        if (comments.size() > 2) {
            allComments.setText(Html.fromHtml("<font color=\"#f27a3d\"><b>View all " + currentPhoto.getCommentsCount() + " comments</b></font>"));
            allComments.setVisibility(View.VISIBLE);

            allComments.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "photo id: " + currentPhoto.getPhotoId(), Toast.LENGTH_SHORT).show();
                }

            });
        }

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
