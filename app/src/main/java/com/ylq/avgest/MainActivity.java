package com.ylq.avgest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EditText mEtSearch;
    private Button mBtSearch;
    private List<Song> allSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        initView();
        init();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_xml_recyclerview);
        mEtSearch = (EditText) findViewById(R.id.activity_main_xml_et_search);
        mBtSearch = (Button) findViewById(R.id.activity_main_xml_button_search);
    }

    private void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        allSongs = new ArrayList<Song>();
        Observable.just(getResources().openRawResource(R.raw.all))
                .map(is -> {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String len;
                    try {
                        while((len = br.readLine())!=null)
                            sb.append(len);
                        br.close();
                        return new JSONArray(sb.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(jsonarray->{
                    JSONObject[] jsonObjects = new JSONObject[jsonarray.length()];
                    for(int i=0;i<jsonarray.length();i++)
                        try {
                            jsonObjects[i] = jsonarray.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }
                    return jsonObjects;
                })
                .flatMap(jsonObjects1 -> Observable.from(jsonObjects1))
                .subscribe(jo -> {
                    try {
                        allSongs.add(new Song(
                                jo.getString("name"),
                                jo.getString("path"),
                                jo.getString("singer")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

        mRecyclerView.setAdapter(new SongAdapter(allSongs,MainActivity.this));
        mBtSearch.setOnClickListener(v->{
            mRecyclerView.setAdapter(new SongAdapter(filter(
                    mEtSearch.getText().toString()),
                    MainActivity.this
            ));
        });
    }


    public List<Song> filter(String regx) {
        List<Song> fter = new ArrayList<>();
        for(Song song:allSongs)
            if (song.contain(regx))
                fter.add(song);
        return fter;
    }
}
