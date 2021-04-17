package com.example.spaceday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ImageView picture;
    TextView infoText;

    private final String ADDRESS = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";
    SpaceInfo spaceInfo = new SpaceInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picture     = findViewById(R.id.picture);
        infoText    =   findViewById(R.id.scienceInfo);

        SpaceTask spaceTask = new SpaceTask();
        spaceTask.execute();
    }
    class SpaceTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient spaceClient = new OkHttpClient();

            HttpUrl httpUrl = HttpUrl.parse(ADDRESS).newBuilder().build();
            Request spaceRequest = new Request.Builder().url(httpUrl).build();
            try {
                Response spaceResponse = spaceClient.newCall(spaceRequest).execute();
                Gson gson = new Gson();
                spaceInfo = gson.fromJson(spaceResponse.body().string(), SpaceInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            infoText.setText(spaceInfo.explanation);
            if(spaceInfo.media_type.equals("image")) {
                Picasso.get()
                        .load(spaceInfo.url)
                        .placeholder(R.drawable.space)
                        .into(picture);
            }else{
                picture.setImageResource(R.drawable.space);
                infoText.append("\n\n" + spaceInfo.url);
            }
        }
    }
}
