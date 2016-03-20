package com.ylq.avgest;

import java.net.URL;

/**
 * Created by apple on 16/3/19.
 */
public class TABLE {

    public static final String SPEC_CODE_REGX = "[aA][vV][gG][eE][sS][tT]";

    private static final String PREFIX = "http://game.ds.qq.com/Com_SongRes/song/";
    private static final String EASY = "ez";
    private static final String NORMAL = "nm";
    private static final String HARD = "hd";
    private static final String _4k_ = "_4k_";
    private static final String _5k_ = "_5k_";
    private static final String _6k_ = "_6k_";
    private static final String NULL_INFIX = "";
    private static final String JPG_ORDERFIX = ".jpg";
    private static final String MP3_ORDERFIX = ".mp3";
    private static final String IMD_ORDERFIX = ".imd";
    private static final String MDE_ORDERFIX = ".mde";
    private static final String _IPAD_INFIX = "_ipad";
    private static final String _TITLE_IPAD_INFIX = "_title_ipad";
    private static final String _PE_INFIX = "_Papa_Easy";
    private static final String _PN_INFIX = "_Papa_Normal";
    private static final String _PH_INFIX = "_Papa_Hard";

    public static final String CUSTOM_SPECTRUM_URL = "https://raw.githubusercontent.com/ylqhust/customSpectrum/master/customSpectrum.json";



    public static String get_4K_ez(String path){
        return getImd(path,_4k_,EASY);
    }

    public static String get_4K_nm(String path){
        return getImd(path,_4k_,NORMAL);
    }

    public static String get_4K_hd(String path){
        return getImd(path,_4k_,HARD);
    }

    public static String get_5K_ez(String path){
        return getImd(path,_5k_,EASY);
    }

    public static String get_5K_nm(String path){
        return getImd(path,_5k_,NORMAL);
    }

    public static String get_5K_hd(String path){
        return getImd(path,_5k_,HARD);
    }

    public static String get_6K_ez(String path){
        return getImd(path,_6k_,EASY);
    }

    public static String get_6K_nm(String path){
        return getImd(path,_6k_,NORMAL);
    }

    public static String get_6K_hd(String path){
        return getImd(path,_6k_,HARD);
    }

    public static String getJpg(String path){
        return getBase(path,NULL_INFIX,JPG_ORDERFIX);
    }

    public static String getMp3(String path){
        return getBase(path,NULL_INFIX,MP3_ORDERFIX);
    }

    public static String getIpad(String path){
        return getBase(path,_IPAD_INFIX,JPG_ORDERFIX);
    }

    public static String getTitleIpad(String path){
        return getBase(path,_TITLE_IPAD_INFIX,JPG_ORDERFIX);
    }

    public static String getPE(String path){
        return getBase(path,_PE_INFIX,MDE_ORDERFIX);
    }

    public static String getPN(String path){
        return getBase(path,_PN_INFIX,MDE_ORDERFIX);
    }

    public static String getPH(String path){
        return getBase(path,_PH_INFIX,MDE_ORDERFIX);
    }

    private static String getImd(String path,String keyCount,String rank){
        return getBase(path,keyCount+rank,IMD_ORDERFIX);
    }

    private static String getBase(String path,String INFIX,String ORDERFIX){
        return PREFIX+path+"/"+path+INFIX+ORDERFIX;
    }

    public static String getFileName(String str){
        return str.substring(str.lastIndexOf('/')+1);
    }


}
