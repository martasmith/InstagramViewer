package com.codepath.instagramviewer.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;

import com.codepath.instagramviewer.R;
import com.codepath.instagramviewer.adapter.InstagramPhotosAdapter;
import com.codepath.instagramviewer.model.InstagramPhoto;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class PhotosActivity extends Activity {

    public static final String CLIENT_ID = "71b36642630d4b73962a51613fb7c992";
    public static final String API_URL = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                // Refresh the photo feed here
                fetchPopularPhotos();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        createPhotosList();
        fetchPopularPhotos();

    }

    private void createPhotosList() {
        //create datasource
        photos = new ArrayList<InstagramPhoto>();
        //create adapter and bind it to the data in arraylist
        aPhotos = new InstagramPhotosAdapter(this, photos);
        //populate the data into the listView
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // set adapter to the listview, which triggers the population of items
        lvPhotos.setAdapter(aPhotos);
    }

    private void fetchPopularPhotos() {

        // create the network request
        AsyncHttpClient client = new AsyncHttpClient();
        // handle the successful response JSON
        client.get(API_URL, new JsonHttpResponseHandler(){
            //define success or failure callbacks
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.i("martas", response.toString());

                swipeContainer.setRefreshing(false);
                JSONArray photosJSON = null;
                aPhotos.clear();


                try {
                    //photos.clear();
                    photosJSON = response.getJSONArray("data");
                    //iterate through each element in the array and extract needed data
                    for(int i=0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();

                        if (photoJSON.optJSONObject("user") != null)
                        {
                            photo.username = photoJSON.getJSONObject("user").getString("username");
                            photo.profilePicUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        }
                        if (photoJSON.optJSONObject("images").optJSONObject("standard_resolution") != null)
                        {
                            photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                            photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        }
                        if (photoJSON.optJSONObject("caption") != null && photoJSON.getJSONObject("caption").has("text"))
                        {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        if (photoJSON.optJSONObject("likes") != null)
                        {
                            photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        }

                        photo.createdTime = photoJSON.optLong("created_time");

                        photos.add(photo);
                    }
                    // Notify adapter to populate new changes to the listView
                    aPhotos.notifyDataSetChanged();
                } catch(JSONException e) {
                    //if parsing fals,print to stacktrace
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });
    }

}
