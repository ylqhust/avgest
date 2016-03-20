package com.ylq.singable;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by apple on 16/3/20.
 */
public class CustomSong extends Song {

    public CustomSong(String name, String path, String singer,Map<String,String> readyFilter) {
        super(name, path, singer,readyFilter);
    }

    @Override
    public String getSongPath() {
        return Environment.getExternalStorageDirectory()+File.separator
                +"BlueCat3"+File.separator+"CustomSong"
                +File.separator+path;
    }

    public static Map<String,String> getReadyFilter(JSONObject fileUrl){
        Map<String,String> map = new HashMap<>();
        Iterator iterator = fileUrl.keys();
        while(iterator.hasNext()){
            String key = (String) iterator.next();
            String url = null;
            try {
                url = fileUrl.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            map.put(key,url);
        }
        return map;
    }

    public static CustomSong parser(JSONObject object) throws JSONException {
        return new CustomSong(object.getString("name"),
                object.getString("path"),
                "",
                getReadyFilter(object.getJSONObject("fileUrl")));
    }
}
