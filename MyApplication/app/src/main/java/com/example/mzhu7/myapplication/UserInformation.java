package com.example.mzhu7.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

class Pokemon{
    public String pokemon;
    public String hp;
    public String weight;
    public String type;
    public String grass;
    public String height;
    public String attack;
    public String defense;
};



public class UserInformation extends AppCompatActivity {
    public String username="";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        username=message;

        String state=String.valueOf(0);
        String username="mzhu7";
        String password="123";
        String pokemonName = "zmy";
        String hp = String.valueOf(500);
        String weight = String.valueOf(10);
        String type = "grass";
        String height = String.valueOf(1);
        String attack = String.valueOf(100);
        String defense = String.valueOf(200);

        OutputStreamWriter osw;
        String str = state+","+username+","+password; //if state is 5
        str = state+","+username; //if state is 0


        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(str);

        Socket socket;
        try {// 创建一个Socket对象，并指定服务端的IP及端口号
            socket = new Socket("target.gsu.edu", 6970);
            // 创建一个InputStream用户读取要发送的文件。

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_user_information);
        layout.addView(textView);
    }
}
