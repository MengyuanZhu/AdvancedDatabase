package com.example.mzhu7.myapplication;

import android.content.Context;
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class UserInformation extends AppCompatActivity {
    public String username="";

    Socket clientSocket;
    public class startConnection implements Runnable {
        @Override
        public void run() {
            try {
                final InetAddress hostAddr = InetAddress.getByName("target.gsu.edu");//This is the local IP of my computer where the serversocket is listening
                clientSocket = new Socket();
                clientSocket.bind(null);
                clientSocket.connect(new InetSocketAddress(hostAddr, 6970),10000);

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
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(str);

                BufferedReader input =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String answer = input.readLine();
                if (state.equals("0")) {
                    Gson gson = new Gson();
                    Pokemon[] pokemonData = gson.fromJson(answer, Pokemon[].class);
                    System.out.println(pokemonData[0].pokemon);
              

                    TextView layout = (TextView) findViewById(R.id.textView1);
                    layout.setText(pokemonData[0].pokemon);
                }
                else{
                    System.out.println(answer);
                }


            } catch (Exception e) {
                e.printStackTrace();
            } // end TryCatch block
        }
    }

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


       // TextView textView = new TextView(this);
       // textView.setTextSize(40);
       // textView.setText(str);


        Thread fst = new Thread(new startConnection());
        fst.start();
       // ViewGroup layout = (ViewGroup) findViewById(R.id.activity_user_information);
      //  layout.addView(textView);
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
    };
}
