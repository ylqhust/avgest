package com.ylq.avgest;


import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ylq.FileManager;
import com.ylq.singable.Song;
import com.ylq.task.DownloadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by apple on 16/3/19.
 */
public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Song> mSongs;
    private Context mContext;
    public SongAdapter(List<Song> songs,Context context){
        this.mSongs = songs;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_song,null)
                ;
        return new SongItem(view)
                ;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SongItem)holder)
                .bindData(mContext,mSongs.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }


    private static class SongItem extends RecyclerView.ViewHolder {
        SimpleDraweeView mmImageView;
        TextView mmSinger;
        TextView mmName;
        TextView mmPath;
        TextView mmUse;
        public SongItem(View itemView) {
            super(itemView);
            mmImageView = (SimpleDraweeView) itemView.findViewById(R.id.item_song_xml_png);
            mmSinger = (TextView) itemView.findViewById(R.id.item_song_xml_tv_songSinger);
            mmPath = (TextView) itemView.findViewById(R.id.item_song_xml_tv_songPath);
            mmUse = (TextView) itemView.findViewById(R.id.item_song_xml_tv_use);
            mmName = (TextView) itemView.findViewById(R.id.item_song_xml_tv_songName);
        }

        public void bindData(Context context,Song song){
            mmImageView.setImageURI(Uri.parse(TABLE.getTitleIpad(song.path)));
            mmSinger.setText(song.singer);
            mmName.setText(song.name);
            mmPath.setText(song.path);
            mmUse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String songPath = song.getSongPath();
                    File file = new File(songPath);
                    if (!file.isDirectory() && !file.exists())
                        file.mkdirs();

                    Map<String, String> readyDownloadFilesUrl = song.checkFile(file);

                    List<DownloadTask.DownloadItem> itemList = new ArrayList<DownloadTask.DownloadItem>();
                    Observable.from(readyDownloadFilesUrl.keySet())
                            .map(fileName->new DownloadTask.DownloadItem(fileName,readyDownloadFilesUrl.get(fileName)))
                            .subscribe(item->itemList.add(item));
                    DownloadTask.DownloadItem[] items = new DownloadTask.DownloadItem[itemList.size()];
                    for(int i=0;i<items.length;i++)
                        items[i] = itemList.get(i);

                    View view = LayoutInflater.from(context)
                            .inflate(R.layout.number_progressbar,null);
                    NumberProgressBar numberProgressBar = (NumberProgressBar) view.findViewById(R.id.numberbar1);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("正在获取文件信息")
                            .setCancelable(false)
                            .setView(view)
                            .create();
                    new DownloadTask(dialog,
                            (TextView)view.findViewById(R.id.wenjiandaxiao),
                            (TextView)view.findViewById(R.id.wenjianming),
                            numberProgressBar,file, new Dealable() {
                        @Override
                        public void onSuccess(Object o) {
                            if (dialog.isShowing())
                                dialog.dismiss();
                            List<String> imds = FileManager.MoveFile(file);
                            new AlertDialog.Builder(context)
                                    .setTitle(imds.size()==0?"友情提示":"使用成功")
                                    .setMessage(imds.size()==0?"这首歌曲没有可以使用的imd文件":getMessage(imds)+"进入蓝猫3选择玩名侦探柯南即可")
                                    .create()
                                    .show();
                        }

                        @Override
                        public void onFailed(Object o) {
                            if (dialog.isShowing())
                                dialog.dismiss();
                            Toast.makeText(context,(String)o,Toast.LENGTH_LONG).show();
                        }
                    }).execute(items);
                }

                private String getMessage(List<String> imds) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("可使用imd列表\n");
                    for(int i=0;i<imds.size();i++)
                        sb.append(imds.get(i)+"\n");
                    return sb.toString();
                }
            });
        }


    }


}
