
package com.codepath.instagramviewer.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class JSONProcessor extends Activity {

    public static ArrayList<InstagramPhoto> fetchJSONResponse(ArrayList<InstagramPhoto> photoList, JSONObject response) throws JSONException {

        JSONArray photosJSON = response.getJSONArray("data");

        // Loop through each element in the data array
        for (int i = 0; i < photosJSON.length(); i++) {

            // get the JSONObject for each data element
            JSONObject photoJSON = photosJSON.getJSONObject(i);

            // fill photo object with processed JSON dT
            InstagramPhoto photo = new InstagramPhoto();

            if (photoJSON.optJSONObject("user") != null) {
                photo.setUsername(photoJSON.getJSONObject("user").getString("username"));
                photo.setProfilePicUrl(photoJSON.getJSONObject("user").getString("profile_picture"));
            }

            if (photoJSON.optJSONObject("images").optJSONObject("standard_resolution") != null) {
                photo.setImageUrl(photoJSON.getJSONObject("images")
                        .getJSONObject("standard_resolution").getString("url"));
                photo.setImageHeight(photoJSON.getJSONObject("images")
                        .getJSONObject("standard_resolution").getInt("height"));
            }

            if (photoJSON.optJSONObject("caption") != null
                    && photoJSON.getJSONObject("caption").has("text")) {
                photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
            }

            if (photoJSON.optJSONObject("likes") != null) {
                photo.setLikesCount(photoJSON.getJSONObject("likes").getInt("count"));
            }

            if (photoJSON.optJSONObject("comments") != null) {
                List<InstagramPhotoComment> comments = new ArrayList<InstagramPhotoComment>();
                JSONArray commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
                for (int j = 0; j < commentsJSON.length(); j++) {

                    JSONObject commentJSON = commentsJSON.getJSONObject(j);

                    // the comment should only be added to the comments if it has a userName and commentText
                    if (commentJSON.has("text") && commentJSON.optJSONObject("from").has("username")) {
                        String commentText = commentJSON.getString("text");
                        String userName = commentJSON.getJSONObject("from").getString("username");
                        comments.add(new InstagramPhotoComment(userName, commentText));
                    }
                }

                // add list of comments to the photo
                photo.setComments(comments);
                photo.setCommentsCount(photoJSON.getJSONObject("comments").getInt("count"));
            }

            photo.setCreatedTime(photoJSON.optLong("created_time"));
            photo.setPhotoId(photoJSON.optString("id"));

            // add photo to the list of photos
            photoList.add(photo);

        }

        return photoList;
    }

}
