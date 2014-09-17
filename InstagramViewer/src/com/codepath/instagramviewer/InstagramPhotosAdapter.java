package com.codepath.instagramviewer;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
		tvUsername.setText(currentPhoto.username);
		tvLikesCount.setText(""+currentPhoto.likesCount+" likes");
		imgLikes.setImageResource(R.drawable.ic_likes);
		
		// Format timestamp into elapsed time
		CharSequence timePassed = DateUtils.getRelativeTimeSpanString(currentPhoto.createdTime*1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		tvCreateDate.setText(timePassed);
		
		tvCaption.setText(Html.fromHtml("<font color=\"#206199\"><b>" + currentPhoto.username
                + "  " + "</b></font>" + "<font color=\"#000000\">" + currentPhoto.caption + "</font>"));
		
		//Reset the image from the recycled view
		imgPhoto.setImageResource(0);
		imgProfilePic.setImageResource(0);
		//Ask for the photo to be added to the imageview based on the photo url
		// In the background, send a network request to the url, download the image bytes, 
		// convert into bitmap, resize images, insert bitmap into imageview
		//main image
		Picasso.with(getContext()).load(currentPhoto.imageUrl).into(imgPhoto);
		//profile photo
		Picasso.with(getContext()).load(currentPhoto.profilePicUrl).into(imgProfilePic);
		// Return view for that data item
		return convertView;
	}

}
