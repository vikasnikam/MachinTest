package com.vikas.videoplayer;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vikas.videoplayer.Adapter.VideoListAdapter;
import com.vikas.videoplayer.Model.VideoData;
import com.vikas.videoplayer.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    public  static String TAG="MainActivity";
    RecyclerView videoRecyclerView;
    EditText edtSerch;
    VideoListAdapter videoListAdapter;
    ArrayList<VideoData> videoDataList =new ArrayList<VideoData>();
    public ArrayList<VideoData> tempArrayList_videoDataList = new ArrayList<VideoData>();
    VideoData videoData;
    int id,favourite,final_id;
    String name,icon_name,file_name;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getVideoList();
        swipeRefreshLayout.setOnRefreshListener(this);
        // Search Video From List
        edtSerch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
                Log.d(TAG,"beforeTextChanged");
                filterList(cs.toString());
                Log.d(TAG,"beforeTextChanged tempArrayList_videoDataList ="+tempArrayList_videoDataList.size());
               // System.out.println("TemparrayList_contactList onTextChanged size=" + TemparrayList_allcontactList.size());
                if (tempArrayList_videoDataList.size() != 0) {
                    // Set Data to Recycler view
                    videoListAdapter = new VideoListAdapter(MainActivity.this,tempArrayList_videoDataList,videoRecyclerView);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    videoRecyclerView.setLayoutManager(mLayoutManager);
                    videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    videoRecyclerView.setAdapter(videoListAdapter);
                    videoListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                filterList(cs.toString());
                Log.d(TAG,"onTextChanged");
                Log.d(TAG,"onTextChanged tempArrayList_videoDataList ="+tempArrayList_videoDataList.size());
               // System.out.println("TemparrayList_contactList onTextChanged size=" + TemparrayList_allcontactList.size());
                if (tempArrayList_videoDataList.size() != 0) {
                    // Set Data to Recycler view
                    videoListAdapter = new VideoListAdapter(MainActivity.this,tempArrayList_videoDataList,videoRecyclerView);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    videoRecyclerView.setLayoutManager(mLayoutManager);
                    videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    videoRecyclerView.setAdapter(videoListAdapter);
                    videoListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG,"afterTextChanged");

            }
        });


    }

    private void getVideoList() {
        pDialog.show();
        String URL="http://genorainnovations.com/Audio/fetch_video.php";
        Log.d(TAG,"URL ="+URL);
        JSONObject jsonObjectRequestData=new JSONObject();
        try {
            jsonObjectRequestData.put("id",0);
            Log.d(TAG,"Request JSONObject="+jsonObjectRequestData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObjectRequestData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG," Response JSONObject="+response);
                pDialog.dismiss();
                try {

                    videoDataList.clear();
                    JSONObject jsonOject=response.getJSONObject("response");
                    final_id=jsonOject.getInt("final_id");
                    Log.d(TAG," Response final_id="+final_id);
                    JSONArray jsonArray=jsonOject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject videoObject=jsonArray.getJSONObject(i);

                        id=videoObject.getInt("id");
                        favourite=videoObject.getInt("favourite");
                        name=videoObject.getString("name");
                        icon_name=videoObject.getString("icon_name");
                        file_name=videoObject.getString("file_name");

                        videoData=new VideoData();

                        videoData.setId(id);
                        videoData.setFavourite(favourite);
                        videoData.setName(name);
                        videoData.setIcon_name(icon_name);
                        videoData.setFile_name(file_name);

                        videoDataList.add(videoData);
                        Log.d(TAG," videoDataList size="+videoDataList.size());

                    }
                    // Set Data to Recycler view
                    if(videoDataList.size()>0){
                        videoListAdapter = new VideoListAdapter(MainActivity.this,videoDataList,videoRecyclerView);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        videoRecyclerView.setLayoutManager(mLayoutManager);
                        videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        videoRecyclerView.setAdapter(videoListAdapter);
                        videoListAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(MainActivity.this,"No Data Available",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(MainActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
                Log.d(TAG," ErrorResponse VolleyError="+error);
            }
        } );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(70000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    private void init() {
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.refersh_layout);
        videoRecyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        edtSerch= (EditText) findViewById(R.id.edt_search);
        // Create a progressbar
        pDialog = new ProgressDialog(MainActivity.this);
        // Set progressbar title
        pDialog.setTitle(" Video Streaming ");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
    }

    // To filter Contact list After Search
    public void filterList(String value) {
        value = value.toUpperCase(Locale.ENGLISH);

        tempArrayList_videoDataList.clear();
        for (int i = 0; i < videoDataList.size(); i++) {
            if (videoDataList.get(i).getName().toUpperCase(Locale.ENGLISH).contains(value)) {
                tempArrayList_videoDataList.add(videoDataList.get(i));
            }
        }
    }

    @Override
    public void onRefresh() {

        if(final_id>0){
            onRefershVideoList();
        }

    }

    private void onRefershVideoList() {
        String URL="http://genorainnovations.com/Audio/fetch_video.php";
        Log.d(TAG,"URL ="+URL);
        JSONObject jsonObjectRequestData=new JSONObject();
        try {
            jsonObjectRequestData.put("id",final_id);
            Log.d(TAG,"Request JSONObject="+jsonObjectRequestData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObjectRequestData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG," Response JSONObject="+response);
                swipeRefreshLayout.setRefreshing(true);
                try {
                    videoDataList.clear();
                    JSONObject jsonOject=response.getJSONObject("response");
                    JSONArray jsonArray=jsonOject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject videoObject=jsonArray.getJSONObject(i);

                        id=videoObject.getInt("id");
                        favourite=videoObject.getInt("favourite");
                        name=videoObject.getString("name");
                        icon_name=videoObject.getString("icon_name");
                        file_name=videoObject.getString("file_name");

                        videoData=new VideoData();

                        videoData.setId(id);
                        videoData.setFavourite(favourite);
                        videoData.setName(name);
                        videoData.setIcon_name(icon_name);
                        videoData.setFile_name(file_name);

                        videoDataList.add(videoData);
                        Log.d(TAG," videoDataList size="+videoDataList.size());

                    }
                    // Set Data to Recycler view
                    if(videoDataList.size()>0){
                        swipeRefreshLayout.setRefreshing(false);
                        videoListAdapter = new VideoListAdapter(MainActivity.this,videoDataList,videoRecyclerView);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        videoRecyclerView.setLayoutManager(mLayoutManager);
                        videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        videoRecyclerView.setAdapter(videoListAdapter);
                        videoListAdapter.notifyDataSetChanged();
                    }else {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this,"No Data Available",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
                Log.d(TAG," ErrorResponse VolleyError="+error);
            }
        } );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(70000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);


    }
}
