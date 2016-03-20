package com.ylq;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static com.facebook.common.internal.ByteStreams.copy;

/**
 * Created by apple on 16/3/19.
 */
public class FileManager {
    //默认将所有文件拷贝到蓝猫的名侦探柯南文件夹下，这意味着，玩家只需玩名侦探柯南即可
    private static final String DEFAULT_DIS = "mingzhentankenan";
    public static List<String> MoveFile(File sourDir){
        List<String> imds = new ArrayList<>();
        File disDir = getDisDir();
        File bdfbd = new File(disDir.getPath()+File.separator
        +"res"+File.separator+"song"+File.separator+DEFAULT_DIS);
        if (!bdfbd.isDirectory() && !bdfbd.exists())
            bdfbd.mkdirs();
        Observable.from(bdfbd.listFiles())
                .subscribe(file -> file.delete());//删除原目录下的所有文件

        Observable.from(sourDir.listFiles())
                .map(file -> {
                    try {
                        if (file.getName().endsWith(".imd"))
                            imds.add(file.getName());
                        return new NameWithIs(new FileInputStream(file), file.getName());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(nameWithIs -> nameWithIs!=null)
                .subscribe(nameWithIs1 -> {
                    File newFile = new File(bdfbd.toString()+File.separator+nameWithIs1.name);
                    try {
                        OutputStream os = new FileOutputStream(newFile);
                        copy(nameWithIs1.is,os);
                        nameWithIs1.is.close();
                        os.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return imds;
    }

    private static File getDisDir() {
        for(int i=3;i<100;i++){
            String disStr = Environment.getExternalStorageDirectory()
                    +File.separator+
                    "BlueCat3";
            File file = new File(disStr);
            if (file.exists())
                return file;
        }
        return new File(Environment.getExternalStorageDirectory()
                +File.separator+
                "BlueCat3");
    }

    static class NameWithIs{
        public InputStream is;
        public String name;

        public NameWithIs(InputStream is, String name) {
            this.is = is;
            if (!name.contains("_"))
                this.name = name.replaceFirst("[^_.]*\\.",DEFAULT_DIS+".");
            else
                this.name = name.replaceFirst("[^_.]*_",DEFAULT_DIS+"_");
        }
    }
}
