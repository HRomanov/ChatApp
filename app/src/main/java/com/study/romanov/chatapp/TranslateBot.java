package com.study.romanov.chatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TranslateBot extends AppCompatActivity {


    private EditText msgText;

    private Button btnTranslate;
    public final String URL = "https://translate.yandex.net";
    public final String KEY = "trnsl.1.1.20161223T172242Z.4b70bdbf39cc4ef5.6e1b74b2cb1c73885f3924b9d3b7389d5adf3723";


    private Gson gson = new GsonBuilder().create();

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();

    private Link intf = retrofit.create(Link.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_bot);

        msgText = (EditText) findViewById(R.id.etMassageText);
        btnTranslate = (Button) findViewById(R.id.btnTranslate);

        ListView listView = (ListView) findViewById(R.id.translate_msg_input);
        final ArrayList<String> translate_msg = new ArrayList<String>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.label, translate_msg);
        listView.setAdapter(adapter);

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> mapJson = new HashMap<String, String>();
                mapJson.put("key", KEY);
                mapJson.put("text", msgText.getText().toString());
                mapJson.put("lang", "ru-en");

                intf.translate(mapJson).enqueue(new Callback<TranslateData>() {
                    @Override
                    public void onResponse(@NonNull Call<TranslateData> call, @NonNull Response<TranslateData> response) {
                        TranslateData data = response.body();
                        ArrayList<String> text = data != null ? data.getText() : null;
                        String translateText = text != null ? text.toString() : null;
                        if (translateText != null) {
                            translateText = translateText.substring(1, translateText.length() - 1);
                        }
                        translate_msg.add(msgText.getText().toString());
                        translate_msg.add(translateText);
                        adapter.notifyDataSetChanged();
                        msgText.setText("");

                    }
                    @Override
                    public void onFailure(@NonNull Call<TranslateData> call, Throwable t) {
                    }
                });
            }
        });

    }
}