package com.ylq.avgest;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/3/19.
 */
public class Song {
    public String path;
    public String name;
    public String singer;

    public Song(String name, String path, String singer) {
        this.name = name;
        this.path = path;
        this.singer = singer;
    }

    public boolean contain(String o){
        if (path.contains(o) || name.contains(o)|| singer.contains(o))
            return true;
        return false;
    }



}
