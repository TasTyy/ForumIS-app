package com.example.forumis_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView posts;
    private String url = "https://forumis.azurewebsites.net/api/v1/PostsApi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        posts = (TextView) findViewById(R.id.posts);
    }

    public void showPosts(View view) {
        if (view != null) {
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    String title = object.getString("title");
                    String content = object.getString("content");
                    String created = object.getString("created");
                    String lastEdit = object.getString("lastEdit");
                    String category = object.getString("categoryId");

                    data.add(title + "\n" + content);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }

            for (String row: data) {
                String currentText = posts.getText().toString();
                posts.setText((currentText+ "\n\n" + row));
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };
}