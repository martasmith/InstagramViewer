package com.codepath.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class PhotosActivity extends Activity {
	
	public static final String CLIENT_ID = "71b36642630d4b73962a51613fb7c992";
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        
        fetchPopularPhotos();
    }


    private void fetchPopularPhotos() {
    	//create datasource
    	photos = new ArrayList<InstagramPhoto>();
    	//create adapter and bind it to the data in arraylist
    	aPhotos = new InstagramPhotosAdapter(this, photos);
    	//populate the data into the listView
    	ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
    	// set adapter to the listview, which triggers the population of items
    	lvPhotos.setAdapter(aPhotos);
    	//setup popular url endpoint
    	String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
    	// create the network request
    	AsyncHttpClient client = new AsyncHttpClient();
    	// handle the successful response JSON
		client.get(popularUrl, new JsonHttpResponseHandler(){
			//define success or failure callbacks
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				//fired once successful response comes back
				//Log.i("martas", response.toString());
				// we want the url, height, username, and caption
				JSONArray photosJSON = null;
				
				try {
					photos.clear();
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


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
