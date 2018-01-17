package com.example.application.demotwitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setTitle("Your Feed");
        final ListView feedListView = (ListView)findViewById(R.id.feedListView);
        final List<Map<String, String>> tweetData = new ArrayList<Map<String, String>>();

//        for(int i=1; i <=5; i++) {
//            Map<String, String> tweetInfo = new HashMap<String, String>();
//            tweetInfo.put("content", "Tweet content : " + Integer.toString(i));
//            tweetInfo.put("username", "User : " + Integer.toString(i));
//
//            tweetData.add(tweetInfo);
//        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");
        query.setLimit(20);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {
                    if(objects.size() > 0 ) {
                        for(ParseObject tweet : objects) {
                            Map<String, String> tweetInfo = new HashMap<String, String>();
                            tweetInfo.put("content", tweet.getString("tweet"));
                            tweetInfo.put("username", tweet.getString("username"));

                            tweetData.add(tweetInfo);
                        }

                        SimpleAdapter simpleAdapter = new SimpleAdapter(FeedActivity.this, tweetData, android.R.layout.simple_list_item_2, new String[] {"content", "username"}, new int[] {android.R.id.text1, android.R.id.text2});
                        feedListView.setAdapter(simpleAdapter);

                    } else {
                        Toast.makeText(FeedActivity.this, "Your are not following anyone yet, explore more and get one", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}
