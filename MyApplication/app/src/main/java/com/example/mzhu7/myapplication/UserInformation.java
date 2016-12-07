package com.example.mzhu7.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class UserInformation extends AppCompatActivity {
    public String username;
    public MyHandler myHandler;
    Socket clientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE);
        username=message;

        ListView listView = (ListView)findViewById(R.id.listView);

        UserAccount tom = new UserAccount("Pikachu","300");
        UserAccount jerry = new UserAccount("Victreebel","1000");
        UserAccount donald = new UserAccount("Ponyta","600", false);

        UserAccount[] users = new UserAccount[]{tom,jerry, donald};
        ArrayAdapter<UserAccount> arrayAdapter
        = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 , users);
        listView.setAdapter(arrayAdapter);

        myHandler= new MyHandler();
        MyThread m = new MyThread();
        new Thread(m).start();
    }

    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();

            UserAccount[] users = new UserAccount[b.size()];

            int i=0;
            for (String key : b.keySet()) {
                users[i]= new UserAccount(key, b.get(key).toString());
                i++;
                //Toast.makeText(UserInformation.this, b.get("Pikachu").toString(), Toast.LENGTH_LONG).show();
            }
            ListView listView = (ListView)findViewById(R.id.listView);
            ArrayAdapter<UserAccount> arrayAdapter = new ArrayAdapter<>(UserInformation.this, android.R.layout.simple_list_item_1 , users);
            listView.setAdapter(arrayAdapter);
        }
    }

    class MyThread implements Runnable {
        public void run() {
            Message msg = new Message();
            Bundle b = new Bundle();
            try {
                String str = "0,"+username; //if state is 0

                Socket s = new Socket("target.gsu.edu", 6970);
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                out.println(str);

                BufferedReader input =  new BufferedReader(new InputStreamReader(s.getInputStream()));
                String answer = input.readLine();

                Gson gson = new Gson();
                Pokemon[] pokemonData = gson.fromJson(answer, Pokemon[].class);
                int i=0;
                while (i<pokemonData.length){
                    b.putString(pokemonData[i].pokemon,pokemonData[i].hp);
                    i++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            msg.setData(b);

            UserInformation.this.myHandler.sendMessage(msg);

        }
    }

    class Pokemon{
        public String pokemon;
        public String hp;
        public String weight;
        public String type;
        public String grass;
        public String height;
        public String attack;
        public String defense;
    }
}
