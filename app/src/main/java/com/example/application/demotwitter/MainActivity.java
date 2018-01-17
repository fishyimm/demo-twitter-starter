package com.example.application.demotwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    public void redirectUser() {
        if(ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(this, UserListActivity.class);
            startActivity(intent);
        }
    }

    public void signUpLogin(View view) {
        final EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);

        final EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);

        ParseUser.logInInBackground(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null) {
                    Log.i("info", "login");
                    redirectUser();
                } else {
                    ParseUser parseUser = new ParseUser();

                    parseUser.setUsername(usernameEditText.getText().toString().trim());
                    parseUser.setPassword(passwordEditText.getText().toString().trim());

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                Log.i("info", "signup");
                                redirectUser();
                            } else {
                                Toast.makeText(MainActivity.this, e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Twitter: Login");
        redirectUser();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

}
