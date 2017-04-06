package com.example.hpkorisnik.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText editTextNickname;
    private TextView textViewScores;
    private final ArrayList<Player> scores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNickname = (EditText)findViewById(R.id.editTextNickname);
        textViewScores = (TextView)findViewById(R.id.textViewScores);
        imageView = (ImageView)findViewById(R.id.imageView);

        Picasso.with(getApplicationContext()).load(R.drawable.image3).into(imageView);

        getLeaderboard();

    }

    public void onClick(View view) {
        if (!editTextNickname.getText().toString().equals("")) {
            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
            intent.putExtra(getString(R.string.nickname),editTextNickname.getText().toString());
            startActivity(intent);
            finish();
        }
        else {
            editTextNickname.setError("Enter your nickname");
            editTextNickname.requestFocus();
        }
    }

    public void getLeaderboard() {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(getString(R.string.api_endpoint_leaderboard), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                        String name = response.getJSONArray("data").getJSONObject(i).getString("name");
                        String score = response.getJSONArray("data").getJSONObject(i).getString("score");
                        scores.add(new Player(name,Integer.valueOf(score)));
                    }
                    if (scores != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < scores.size(); i++) {
                            stringBuilder.append(scores.get(i)).append("\n");
                        }
                        textViewScores.setText(stringBuilder.toString());
                    }
                    else {
                        textViewScores.setText("No one played yet :(");
                    }
                }
                catch (Exception e) {
                    System.out.print(e);
                }
            }
        });
    }

}
