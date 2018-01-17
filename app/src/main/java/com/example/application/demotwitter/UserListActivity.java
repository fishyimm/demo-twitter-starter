package com.example.application.demotwitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ParallelExecutorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.tweet_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.tweet) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Send a tweet");

            final EditText tweetContentEditText = new EditText(this);
            builder.setView(tweetContentEditText);

            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ParseObject tweet = new ParseObject("Tweet");

                    tweet.put("username", ParseUser.getCurrentUser().getUsername());
                    tweet.put("tweet", tweetContentEditText.getText().toString());

                    tweet.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                Toast.makeText(UserListActivity.this, "Tweet sent successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserListActivity.this, "Tweet sent failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Log.i("info", tweetContentEditText.getText().toString());
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else if(item.getItemId() == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if(item.getItemId() == R.id.viewFeed) {
            Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle("User List");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        users.add("adf");
//        users.add("aaaa");
        if(ParseUser.getCurrentUser().get("isFollowing") == null) {
            List<String> list = new ArrayList<>();
            ParseUser.getCurrentUser().put("isFollowing", list);
        }


        final ListView listView = (ListView)findViewById(R.id.userListView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, users);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view;

                if(checkedTextView.isChecked()) {
                    Log.i("info", "is checked" + users.get(position));
//                    https://stackoverflow.com/questions/37038386/remove-string-from-array-in-parse-user-android/37038565
                    ParseUser user = ParseUser.getCurrentUser();
                    List<String> myList = user.getList("isFollowing");
                    myList.add(users.get(position));
                    user.put("isFollowing", myList);

                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null) {
                                Log.i("info", "saveInBackground success" );
                            } else {
                                Log.i("info", "saveInBackground failed" + e.getMessage());
                            }
                        }
                    });

                } else {
                    Log.i("info", "not checked" + users.get(position));
                    Log.i("info", "ParseUser.getCurrentUser()" + ParseUser.getCurrentUser().getList("isFollowing") );
                    ParseUser user = ParseUser.getCurrentUser();
                    List<String> myList = user.getList("isFollowing");
                    myList.remove(users.get(position));
                    user.put("isFollowing", myList);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null) {
                                Log.i("info", "remove success" + ParseUser.getCurrentUser().getList("isFollowing"));
                            } else {
                                Log.i("info", "remove failed" + e.getMessage());
                            }
                        }
                    });
                }

                arrayAdapter.notifyDataSetChanged();
            }
        });

        users.clear();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null) {
                    if(objects.size() > 0 ) {
                        for(ParseUser user : objects) {
                            users.add(user.getUsername());
                        }

                        arrayAdapter.notifyDataSetChanged();

                        for(String username : users) {
                            if(ParseUser.getCurrentUser().getList("isFollowing").contains(username)) {
                                listView.setItemChecked(users.indexOf(username), true);
                            }
                        }
                    }
                }
            }
        });
    }
}
