package com.ylq.avgest;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by apple on 16/3/19.
 */
public class Tools {
    public static String getTencentPath(String path){
        String tencentPath = Environment.getExternalStorageDirectory().toString()+ File.separator+"RM"+File.separator
                +"res"
                +File.separator+"song";
        return tencentPath+File.separator+path;
    }

    /**
     *
     * @param file
     * @return 返回待下载的文件名称和文件的url
     */
    public static Map<String, String> checkFile(File file) {
        Map<String,String> readyDownloadUrl = getAllFileType(file.getName());
        File[] files = file.listFiles();
        if (files == null)
            return null;
        Observable.from(files)
                .map(f0->f0.getName())
                .subscribe(name->{
                    if (readyDownloadUrl.containsKey(name))
                        readyDownloadUrl.remove(name);
                });
        return readyDownloadUrl;
    }

    public static Map<String,String> getAllFileType(String path){
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
