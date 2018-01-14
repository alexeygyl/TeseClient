package com.example.smit.teseclient.Units;


import com.example.smit.teseclient.General;

public class MusicUnits {
     public int Mid;
     public String Mtitle;
     public String MAuthor;
     public String Malbum;
     public Integer MDuration;
     public String Myear;
     public String Mtime;


    public MusicUnits(int Mid,String Mtitle, String MAuthor,String Malbum,Integer MDuration,String Myear){
        this.Mid = Mid;
        this.Mtime = General.getStrTime(MDuration);
        this.MDuration = MDuration;
        this.Malbum = Malbum;
        this.MAuthor = MAuthor;
        this.Mtitle = Mtitle;
        this.Myear = Myear;
    }


}
