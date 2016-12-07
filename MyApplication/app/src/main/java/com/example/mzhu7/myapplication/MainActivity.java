package com.example.mzhu7.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editTextUsername = (EditText) findViewById(R.id.username);
        String username = editTextUsername.getText().toString();
        EditText editTextPassword = (EditText) findViewById(R.id.password);
        String password = editTextPassword.getText().toString();
        Bundle extras = new Bundle();
        extras.putString("EXTRA_USERNAME",username);
        extras.putString("EXTRA_PASSWORD",password);
        intent.putExtras(extras);
        startActivity(intent);
        // Do something in response to button
    }

    public void signIn (View view) {
        EditText editTextUsername = (EditText) findViewById(R.id.username);
        String username = editTextUsername.getText().toString();
        EditText editTextPassword = (EditText) findViewById(R.id.password);
        String password = editTextPassword.getText().toString();
        if (password.equals("123") && username.equals("mzhu7")){
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_LONG).show();
        }

    }

}
