
package com.codepath.instagramviewer.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.instagramviewer.R;
import com.codepath.instagramviewer.adapter.InstagramCommentsAdapter;
import com.codepath.instagramviewer.model.InstagramPhotoComment;
import com.codepath.instagramviewer.model.JSONProcessor;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class CommentsFragment extends DialogFragment {

    public static String photoId;
    public static final String CLIENT_ID = "71b36642630d4b73962a51613fb7c992";
    public static final String API_URL_BASE = "https://api.instagram.com/v1/media/";
    public static final String API_URL_END = "/comments?client_id=" + CLIENT_ID;

    private ArrayList<InstagramPhotoComment> comments;
    private InstagramCommentsAdapter aComments;
    private ListView lvComments;
    private Button closeBtn;
    private AsyncHttpClient client;

    private String apiUrl, userId, commentText;

    public CommentsFragment() {
        // TODO Auto-generated constructor stub
    }

    public static CommentsFragment newInstance(String photoId, String userId, String commentText) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString("photoId", photoId);
        args.putString("userId", userId);
        args.putString("commentText", commentText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container);
        photoId = getArguments().getString("photoId", "0");
        // userId and commentText is pulled in for creating a custom title for the dialogFragment at
        // some later point
        userId = getArguments().getString("userId");
        commentText = getArguments().getString("commentText");
        apiUrl = API_URL_BASE + photoId + API_URL_END;

        // TODO: try to style the title to output username: commentText
        // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // getDialog().setStyle(style, theme));
        getDialog().setTitle("All comments");

        lvComments = (ListView) view.findViewById(R.id.lvComments);
        closeBtn = (Button) view.findViewById(R.id.btnBack);
        client = new AsyncHttpClient();
        createCommentsList();
        fetchPhotoComments();
        setupBtnClose();
        return view;
    }

    private void createCommentsList() {
        // create datasource
        comments = new ArrayList<InstagramPhotoComment>();
        // create adapter and bind it to the data in arraylist
        aComments = new InstagramCommentsAdapter(getActivity(), comments);
        // set adapter to the listview, which triggers the population of items
        lvComments.setAdapter(aComments);
    }

    private void fetchPhotoComments() {
        Log.d("martas", "inside fetchPhotoComments");
        Log.d("martas", "API URL: " + apiUrl);
        client.get(apiUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    comments = JSONProcessor.fetchCommentsJSONResponse(comments, response);
                    aComments.notifyDataSetChanged();
                } catch (JSONException e) {
                    // if parsing fails,print error
                    Log.e("InstagramViewer",
                            "Error occured while processing Instagram JSON response: ", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Error occured while trying to retrieve Instagam photo stream!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setupBtnClose() {
        closeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }

        });
    }

}
