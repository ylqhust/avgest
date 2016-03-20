package com.ylq.singable;

import android.os.Environment;

import com.ylq.avgest.TABLE;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 16/3/20.
 */
public class TencentSong extends Song {

    public TencentSong(String name, String path, String singer,Map<String,String> readyFilter) {
        super(name, path, singer,readyFilter);
    }


    @Override
    public String getSongPath() {
        String tencentPath = Environment.getExternalStorageDirectory().toString()+ File.separator+"RM"+File.separator
                +"res"
                +File.separator+"song";
        return tencentPath+File.separator+path;
    }

    public static Map<String,String> getReadyFilter(String path){
        Map<String,String> all = new HashMap<>();
        all.put(TABLE.getFileName(TABLE.get_4K_ez(path)),
                TABLE.get_4K_ez(path));
        all.put(TABLE.getFileName(TABLE.get_4K_nm(path)),
                TABLE.get_4K_nm(path));
        all.put(TABLE.getFileName(TABLE.get_4K_hd(path)),
                TABLE.get_4K_hd(path));

        all.put(TABLE.getFileName(TABLE.get_5K_ez(path)),
                TABLE.get_5K_ez(path));
        all.put(TABLE.getFileName(TABLE.get_5K_nm(path)),
                TABLE.get_5K_nm(path));
        all.put(TABLE.getFileName(TABLE.get_5K_hd(path)),
                TABLE.get_5K_hd(path));

        all.put(TABLE.getFileName(TABLE.get_6K_ez(path)),
                TABLE.get_6K_ez(path));
        all.put(TABLE.getFileName(TABLE.get_6K_nm(path)),
                TABLE.get_6K_nm(path));
        all.put(TABLE.getFileName(TABLE.get_6K_hd(path)),
                TABLE.get_6K_hd(path));

        all.put(TABLE.getFileName(TABLE.getJpg(path)),
                TABLE.getJpg(path));
        all.put(TABLE.getFileName(TABLE.getMp3(path)),
                TABLE.getMp3(path));
        all.put(TABLE.getFileName(TABLE.getTitleIpad(path)),
                TABLE.getTitleIpad(path));
        all.put(TABLE.getFileName(TABLE.getIpad(path)),
                TABLE.getIpad(path));

        return all;
    }
}
