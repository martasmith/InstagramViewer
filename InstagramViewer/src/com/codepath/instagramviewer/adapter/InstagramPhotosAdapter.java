
package com.codepath.instagramviewer.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.instagramviewer.R;
import com.codepath.instagramviewer.fragment.CommentsFragment;
import com.codepath.instagramviewer.model.InstagramPhoto;
import com.codepath.instagramviewer.model.InstagramPhotoComment;
import com.codepath.instagramviewer.util.CircleTransform;
import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, android.R.layout.simple_list_item_1, photos);
    }

    public static class ViewHolder {
        TextView tvUserName, tvLikesCount, tvCaption, tvCreateDate, firstComment, secondComment,
        allComments;
        ImageView imgPhoto, imgProfilePic, imgLikes, imgBubble1, imgBubble2;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Take datasource at position
        // Get the data item
        final InstagramPhoto currentPhoto = getItem(position);

        ViewHolder viewHolder;

        // Check if we are using s recycled view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent,
                    false);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.tvCreateDate = (TextView) convertView.findViewById(R.id.tv_createDate);
            viewHolder.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
            viewHolder.imgProfilePic = (ImageView) convertView.findViewById(R.id.imgProfilePic);
            viewHolder.imgLikes = (ImageView) convertView.findViewById(R.id.imgLikes);
            viewHolder.firstComment = (TextView) convertView.findViewById(R.id.tvComment1);
            viewHolder.imgBubble1 = (ImageView) convertView.findViewById(R.id.ivCommentBubble1);
            viewHolder.imgBubble2 = (ImageView) convertView.findViewById(R.id.ivCommentBubble2);
            viewHolder.secondComment = (TextView) convertView.findViewById(R.id.tvComment2);
            viewHolder.allComments = (TextView) convertView.findViewById(R.id.tvCommentMore);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Exclude all conditional images and text from the view for now
        viewHolder.imgBubble1.setVisibility(View.GONE);
        viewHolder.firstComment.setVisibility(View.GONE);
        viewHolder.imgBubble2.setVisibility(View.GONE);
        viewHolder.secondComment.setVisibility(View.GONE);
        viewHolder.allComments.setVisibility(View.GONE);

        // Populate the subviews with the correct data
        viewHolder.tvUserName.setText(currentPhoto.getUsername());
        viewHolder.tvLikesCount.setText("" + currentPhoto.getLikesCount() + " likes");
        viewHolder.imgLikes.setImageResource(R.drawable.ic_likes);

        // populate the first and second comment with the first and second element of the comments
        // arrayList
        List<InstagramPhotoComment> comments = currentPhoto.getComments();

        if (currentPhoto.getCommentsCount() > 0) {
            viewHolder.firstComment.setText(Html.fromHtml("<font color=\"#206199\"><b>"
                    + comments.get(0).getUserName()
                    + "  " + "</b></font>" + "<font color=\"#000000\">"
                    + comments.get(0).getCommentText() + "</font>"));
            viewHolder.firstComment.setVisibility(View.VISIBLE);
            viewHolder.imgBubble1.setVisibility(View.VISIBLE);
        }

        if (comments.size() > 1) {
            viewHolder.secondComment.setText(Html.fromHtml("<font color=\"#206199\"><b>"
                    + comments.get(1).getUserName()
                    + "  " + "</b></font>" + "<font color=\"#000000\">"
                    + comments.get(1).getCommentText() + "</font>"));
            viewHolder.secondComment.setVisibility(View.VISIBLE);
            viewHolder.imgBubble2.setVisibility(View.VISIBLE);
        }

        if (comments.size() > 2) {
            viewHolder.allComments.setText(Html.fromHtml("<font color=\"#f27a3d\"><b>View all "
                    + currentPhoto.getCommentsCount() + " comments</b></font>"));
            viewHolder.allComments.setVisibility(View.VISIBLE);

            viewHolder.allComments.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentActivity activity = (FragmentActivity) (getContext());
                    FragmentManager fm = activity.getSupportFragmentManager();
                    CommentsFragment commentsFragment = CommentsFragment.newInstance(
                            currentPhoto.getPhotoId(), currentPhoto.getUsername(),
                            currentPhoto.getCaption());
                    commentsFragment.show(fm, "fragment_comments");
                }

            });
        }

        // Format timestamp into elapsed time
        String timePassed = DateUtils.getRelativeTimeSpanString(
                currentPhoto.getCreatedTime() * 1000, System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS)
                + "";
        // print only digits and 1st char of time unit
        String[] timeParts = timePassed.split(" ");
        // Apparently, we have some malformed timestrings returned once in a while
        if (timeParts.length >= 2) {
            if (timeParts[0].equals("in")) {
                // in 12 hours
                viewHolder.tvCreateDate.setText(timeParts[1] + timeParts[2].substring(0, 1));
            } else {
                // 12 hours ago
                viewHolder.tvCreateDate.setText(timeParts[0] + timeParts[1].substring(0, 1));
            }
        }

        viewHolder.tvCaption.setText(Html.fromHtml("<font color=\"#206199\"><b>"
                + currentPhoto.getUsername()
                + "  " + "</b></font>" + "<font color=\"#000000\">" + currentPhoto.getCaption()
                + "</font>"));

        // Reset the image from the recycled view
        viewHolder.imgPhoto.setImageResource(0);
        viewHolder.imgProfilePic.setImageResource(0);
        // Ask for the photo to be added to the imageview based on the photo url
        // In the background, send a network request to the url, download the image bytes,
        // convert into bitmap, resize images, insert bitmap into imageview
        // main image
        Picasso.with(getContext()).load(currentPhoto.getImageUrl())
        .placeholder(R.drawable.ic_placeholder).into(viewHolder.imgPhoto);
        // profile photo
        Picasso.with(getContext()).load(currentPhoto.getProfilePicUrl())
        .transform(new CircleTransform()).into(viewHolder.imgProfilePic);
        // Return view for that data item
        return convertView;

    }
}
