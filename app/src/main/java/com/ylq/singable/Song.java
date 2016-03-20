package com.ylq.singable;


import java.io.File;
import java.util.Map;

import rx.Observable;

/**
 * Created by apple on 16/3/19.
 */
public abstract class Song {
    public String path;
    public String name;
    public String singer;
    public Map<String,String> readyFilter;

    public Song(String name, String path, String singer,Map<String,String> readyFilter) {
        this.name = name;
        this.path = path;
        this.singer = singer;
        this.readyFilter = readyFilter;
    }

    public boolean contain(String o){
        if (path.contains(o) || name.contains(o)|| singer.contains(o))
            return true;
        return false;
    }

    //获取这首歌的默认存储地址
    public abstract String getSongPath();

    //检查有哪些文件还没有被下载
    public Map<String,String> checkFile(File file){
        File[] files = file.listFiles();
        if (files == null)
            return null;
        Observable.from(files)
                .map(f0->f0.getName())
                .subscribe(name->{
                    if (readyFilter.containsKey(name))
                        readyFilter.remove(name);
                });
        return readyFilter;
    }

}
