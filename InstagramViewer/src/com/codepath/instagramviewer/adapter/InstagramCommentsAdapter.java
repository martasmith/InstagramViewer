
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
import com.codepath.instagramviewer.model.InstagramPhotoComment;
import com.codepath.instagramviewer.util.CircleTransform;
import com.squareup.picasso.Picasso;

public class InstagramCommentsAdapter extends ArrayAdapter<InstagramPhotoComment> {

    public InstagramCommentsAdapter(Context context, List<InstagramPhotoComment> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    public static class ViewHolder {
        TextView tvUserComment, tvCreatedDateComment;
        ImageView ivProfilePic;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InstagramPhotoComment currentComment = getItem(position);

        ViewHolder viewHolder;

        // Check if we are using s recycled view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent,
                    false);
            viewHolder.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivBubbleComments);
            viewHolder.tvUserComment = (TextView) convertView.findViewById(R.id.tvUserComment);
            viewHolder.tvCreatedDateComment = (TextView) convertView
                    .findViewById(R.id.tvCreatedDateComment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Reset the image from the recycled view
        viewHolder.ivProfilePic.setImageResource(0);
        // profile photo
        Picasso.with(getContext()).load(currentComment.getProfilePicUrl())
        .placeholder(R.drawable.ic_profile_placeholder).transform(new CircleTransform())
        .into(viewHolder.ivProfilePic);

        // username and comment
        viewHolder.tvUserComment.setText(Html.fromHtml("<font color=\"#206199\"><b>"
                + currentComment.getUserName()
                + "  " + "</b></font>" + "<font color=\"#000000\">"
                + currentComment.getCommentText() + "</font>"));

        // Format timestamp into elapsed time
        CharSequence timePassed = DateUtils.getRelativeTimeSpanString(
                currentComment.getCreatedTime() * 1000, System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS);
        viewHolder.tvCreatedDateComment.setText(timePassed);

        return convertView;
    }

}
