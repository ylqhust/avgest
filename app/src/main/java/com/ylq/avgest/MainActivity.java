package com.ylq.avgest;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.ylq.singable.CustomSong;
import com.ylq.singable.Song;
import com.ylq.singable.TencentSong;
import com.ylq.task.DownloadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SocketHandler;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EditText mEtSearch;
    private Button mBtSearch;
    private List<Song> allTencentSongs;
    private List<Song> allCustomSongs;
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
        allTencentSongs = new ArrayList<Song>();
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
                        allTencentSongs.add(new TencentSong(
                                jo.getString("name"),
                                jo.getString("path"),
                                jo.getString("singer"),
                                TencentSong.getReadyFilter(jo.getString("path"))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

        mRecyclerView.setAdapter(new SongAdapter(allTencentSongs,MainActivity.this));
        mBtSearch.setOnClickListener(v->{
            if (mEtSearch.getText().toString().matches(TABLE.SPEC_CODE_REGX)){
                if (allCustomSongs != null && allCustomSongs.size() != 0){
                    mRecyclerView.setAdapter(new SongAdapter(allCustomSongs,MainActivity.this));
                    return;
                }
                try {
                    EnterCustomWorld();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this,"IOException",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return;
            }
            mRecyclerView.setAdapter(new SongAdapter(filter(
                    mEtSearch.getText().toString()),
                    MainActivity.this
            ));
        });
    }

    //进入自定义歌曲
    private void EnterCustomWorld() throws IOException {
        //首先要下载歌曲列表文件
        View view = LayoutInflater.from(this)
                .inflate(R.layout.number_progressbar,null);
        NumberProgressBar numberProgressBar = (NumberProgressBar) view.findViewById(R.id.numberbar1);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("正在下载自定义歌曲列表")
                .setCancelable(false)
                .setView(view)
                .create();
        File dis = new File(Environment.getExternalStorageDirectory().toString()
        +File.separator+"BlueCat3");
        if (!dis.isDirectory() || !dis.exists())
            dis.mkdirs();
        new DownloadTask(dialog, numberProgressBar,dis, new Dealable() {
            @Override
            public void onSuccess(Object o) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this,"加载完成", Toast.LENGTH_SHORT).show();
               allCustomSongs = readAllCustomSong(dis.toString()
               +File.separator+"customSpectrum.json");
                if (allCustomSongs != null && allCustomSongs.size()!=0)
               mRecyclerView.setAdapter(new SongAdapter(allCustomSongs,MainActivity.this));
            }

            @Override
            public void onFailed(Object o) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this,(String)o,Toast.LENGTH_LONG).show();
            }
        }).execute(new DownloadTask.DownloadItem("customSpectrum.json",TABLE.CUSTOM_SPECTRUM_URL));
    }

    private List<Song> readAllCustomSong(String s) {
        File file = new File(s);
        if (!file.exists()){
            Toast.makeText(this,"Download csjson Failed",Toast.LENGTH_LONG).show();
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String len;
            while((len = br.readLine()) != null)
                sb.append(len);
            br.close();
            allCustomSongs = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(sb.toString());
            for(int i=0;i<jsonArray.length();i++)
                allCustomSongs.add(CustomSong.parser(jsonArray.getJSONObject(i)));
            return allCustomSongs;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Song> filter(String regx) {
        List<Song> fter = new ArrayList<>();
        for(Song song: allTencentSongs)
            if (song.contain(regx))
                fter.add(song);
        return fter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        final int id = item.getItemId();
        switch (id){
            case R.id.about:
                new AlertDialog.Builder(this).setTitle("关于开发者")
                        .setMessage("如有问题或建议请发送邮件至\n351068784@qq.com")
                        .create().show();
                break;
            case R.id.shengming:
                new AlertDialog.Builder(this)
                        .setTitle("声明")
                        .setMessage("此软件无任何商业用途,仅用于学习交流")
                        .create().show();
                break;
            case R.id.jianjie:
                new AlertDialog.Builder(this)
                        .setTitle("使用说明")
                        .setMessage("首先你必须下载蓝猫3并安装，然后选择你喜欢的一首歌曲，点击使用，软件会从优选择本地已经存在的文件，如果本地不存在，将会下载文件" +
                                "。软件会自动替换文件名，你只需要进入蓝猫3选择名侦探柯南这首曲子玩即可").create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
