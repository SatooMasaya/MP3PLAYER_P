package sample;

import java.io.*;
import java.util.*;

import static sample.Main.SongEngine;

public class TEST {

    public final Integer LoveValueMax = 5;

    //List<Song> LoveList[] = new ArrayList[LoveValueMax];

    public void  LoveDataSort() throws IOException {

        List<Song> LoveList[] = new ArrayList[LoveValueMax];
        String songname = "";
        int lovevalue = 0;
        int number = 0;
        Song[] ss= new Song[SongEngine.TOTAL];

        //今のリストのすべての曲を過去のリストへコピー
        for (int i = 0; i < SongEngine.TOTAL; i++) {
        //    OldList.add(i, NowList.get(i));
        }

        for (int i = 1; i <= LoveValueMax; i++) {
            LoveList[i] = new ArrayList<Song>();
            LoveList[i].add(ss[1]);
        }

        for (int i = 1; i <= SongEngine.TOTAL; i++) {
            switch (ss[i].LoveValue){
                case 1:
                    LoveList[1].add(ss[i]);
                    break;
                case 2:
                    LoveList[2].add(ss[i]);
                    break;
                case 3:
                    LoveList[3].add(ss[i]);
                    break;
                case 4:
                    LoveList[4].add(ss[i]);
                    break;
                case 5:
                    LoveList[5].add(ss[i]);
                    break;
            }
        }

        for (int i = 1; i <= LoveValueMax; i++) {
            Collections.shuffle(LoveList[i]);
        }






    }

    public static void main(String[] args) {
        TEST tt = new TEST();
        try {
            tt.LoveDataSort();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
