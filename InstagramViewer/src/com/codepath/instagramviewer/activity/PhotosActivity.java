package com.codepath.instagramviewer.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;

import com.codepath.instagramviewer.R;
import com.codepath.instagramviewer.adapter.InstagramPhotosAdapter;
import com.codepath.instagramviewer.model.InstagramPhoto;
import com.codepath.instagramviewer.model.JSONProcessor;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class PhotosActivity extends FragmentActivity {

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
                aPhotos.clear();

                try {
                    photos = JSONProcessor.fetchPhotosJSONResponse(photos, response);
                    aPhotos.notifyDataSetChanged();
                } catch(JSONException e) {
                    //if parsing fails,print to stack trace
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
