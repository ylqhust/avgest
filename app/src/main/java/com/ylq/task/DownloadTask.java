package com.ylq.task;

import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.ylq.avgest.Dealable;
import com.ylq.avgest.TABLE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by apple on 16/3/19.
 */
public class DownloadTask extends AsyncTask<DownloadTask.DownloadItem,Integer,String> {

    private File distance;//下载文件的存储地址，默认是在官方的文件夹中
    private AlertDialog dialog;
    private Dealable dealable;
    private NumberProgressBar numberProgressBar;
    private TextView wenjianming;
    private TextView wenjiandaxiao;
    private DownloadItem[] items;

    public DownloadTask(AlertDialog dialog,TextView wenjiandaxiao,TextView wenjianming, NumberProgressBar numberProgressBar, File distance, Dealable dealable){
        this.distance = distance;
        this.dialog = dialog;
        this.dealable = dealable;
        this.wenjiandaxiao = wenjiandaxiao;
        this.wenjianming = wenjianming;
        this.numberProgressBar = numberProgressBar;
    }

    @Override
    protected void onPreExecute(){
        dialog.show();
        numberProgressBar.setProgress(0);
    }
    @Override
    protected String doInBackground(DownloadItem... params) {
        items = params;
        for(int i=0;i<params.length;i++){
            publishProgress(0,i);
            try {
                if (DownloadFile(distance,params[i].fileName,params[i].fileUrl,i))
                    publishProgress(100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        dealable.onSuccess("下载完成");
    }

    @Override
    protected void onProgressUpdate(Integer... integer){
        numberProgressBar.setProgress(integer[0]);
        if (integer.length == 2){
            dialog.setTitle("正在下载:");
            wenjianming.setText("文件名:"+items[integer[1]].fileName);
            wenjiandaxiao.setVisibility(View.GONE);
        }
        if (integer.length == 3){
            dialog.setTitle("正在下载:");
            wenjianming.setText("文件名:"+items[integer[1]].fileName);
            wenjiandaxiao.setVisibility(View.VISIBLE);
            wenjiandaxiao.setText("文件大小:"+getFormatSize(integer[2]));
        }
    }

    private String getFormatSize(Integer integer) {
        if (integer < 1024)
            return integer+"byte";
        integer /= 1024;
        if (integer < 1024)
            return integer+"kB";
        integer *=10;
        integer/=1024;
        float s = (float) (integer/10.0);
        return s+"Mb";
    }


    private boolean DownloadFile(File dir,String fileName,String url,int index) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setConnectTimeout(5000);
        int code = conn.getResponseCode();
        if (code != 200){
            conn.disconnect();
            if (code == 404)
                return true;
            return false;
        }
        int fileLength = conn.getContentLength();
        publishProgress(0,0,fileLength);

        File file = new File(dir.toString()+File.separator+fileName);
        InputStream stream = conn.getInputStream();
        OutputStream os = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int len,haveDownload = 0;
        while((len = stream.read(bytes))!=-1){
            os.write(bytes,0,len);
            haveDownload+=len;
            publishProgress(100*haveDownload/fileLength);
        }
        os.flush();
        os.close();
        stream.close();
        conn.disconnect();
        return true;
    }

    public static class DownloadItem{
        public String fileName;
        public String fileUrl;

        public DownloadItem(String fileName, String fileUrl) {
            this.fileName = fileName;
            this.fileUrl = fileUrl;
        }
    }

}
