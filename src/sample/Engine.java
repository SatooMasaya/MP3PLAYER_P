package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import java.io.*;
import java.net.MalformedURLException;

import javafx.scene.media.AudioClip;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * next「次へ再生」、shuffle「シャッフル再生」の仕様
 * @author Sato Masaya
 */
public class Engine implements ShuffleEngine{

    /**先読みする曲の上限数*/
    public final Integer PEEKMAX = 5;

    /**ラブシャッフルするとき、ソートするときの優先度*/
    public final Integer LoveValueMax = 5;

    /**再生番号*/
    int NowPlayingNumber = 0;
    //playSong()を使用した時、再生番号NowPlayingNumberに対応するリストの曲を再生

    /**曲の総数*/
    int TOTAL = 0;

    /**PEEKMAX分の曲名を保存するテキスト*/
    String SongOrderText = "";
    //このテキストは、テキストフィールドSongOrderTextFieldに表示される

    /**今のリスト*/
    List<Song> NowList = new ArrayList<Song>();

    /**過去のリスト*/
    List<Song> OldList = new ArrayList<Song>();
        /*今のリストと過去のリストはシャッフルメソッドで使う。
        シャッフル前のリストを過去のリストへ
        シャッフル後のリストを今のリストへ保存するために
        用意した。
        */



    /**
     * 今のリストNowListにすべての曲を保存するメソッド
     * @param songs 曲の配列
     */
    public void setSongs(Song[] songs){

        //今のリストNowListにすべての曲（TOTAL分の曲）を保存
        for (int i = 0; i < TOTAL; i++) {
            NowList.add(songs[i]);
        }

    }

    /**
     * 全曲の名前データ(SongData)を読み込みNowListに保存するメッソド
     * @throws IOException
     */
    public void songDataInSet() throws IOException{

        String songname = "";
        int lovevalue = 0;
        int number = 0;//配列ssの配列番号
        int cnt = 0;//曲数のカウント
        Song[] ss;//代入用


        //データにいくつ曲が入っているかの確認をする。
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("SongData.txt"), "SJIS"));
        while(br.readLine() != null) {
            cnt++;
        }
        TOTAL  = cnt;//総曲数を記録する。


        //配列ssにすべての曲を代入する。
        br = new BufferedReader(new InputStreamReader(new FileInputStream("SongData.txt"), "SJIS"));
        ss = new Song[TOTAL];
        String entry;
        while ((entry = br.readLine()) != null) {//ファイルから一行読む
            Scanner sc = new Scanner(entry);
            if (sc.hasNext()) songname = sc.next();
            if (sc.hasNextInt()) lovevalue = sc.nextInt();
            System.out.println(number + songname + lovevalue);
            ss[number] = new Song(songname, lovevalue);
            number++;
        }
        br.close();

        //NowListに保存する
        setSongs(ss);
    }

    /**
     * 次に再生する曲を手に入れるメッソド
     * @return nextSong
     */
    public Song getNextSong(){

        //再生番号を+１
        NowPlayingNumber++;

        //もしすべての曲が流れたら、最初の曲に戻る
        if (NowPlayingNumber == TOTAL) {
            NowPlayingNumber = 0;
        }

        //Song型で宣言
        Song nextSong = NowList.get(NowPlayingNumber);

        return nextSong;

    }

    /**
     * 前の曲を再生するメソッド
     * @return backSong
     */
    public Song getBackSong() {

        //再生番号を-１してオーディオにセット
        NowPlayingNumber--;

        /*
         *もし最初の曲が聴ける状態(NowPlayingNumberに0が代入されている状態)で
         * 前へ再生をおしたら(このメソッドが呼ばれたら)
         */
        if (NowPlayingNumber == -1) {
            NowPlayingNumber = 0;
        }

        //Song型で宣言
        Song backSong = NowList.get(NowPlayingNumber);

        return backSong;
    }

    /**
     *  シャッフルメソッド
     *　最初に1巡分の曲順を決定しておくロジック
     *  曲の再生が2巡目になったら,1巡目と違う順番になるよう。
     */
    public void Shuffle(){

        //今のリストのすべての曲を過去のリストへコピー
        for (int i = 0; i < TOTAL; i++) {
            OldList.add(i, NowList.get(i));
        }

        //今のリストの曲の順番をシャッフル
        Collections.shuffle(NowList);

        /*
        ここでは、過去のリスト（シャッフル前の曲の順番）と
        今のリスト（シャッフル後の曲の順番）を比較している。
        */
        for (int i = 0; i < TOTAL; i++) {

            //２つのリストが違うなら、ループから出る（条件を満たすシャッフルが成功）
            if(OldList.get(i)!=NowList.get(i)){
                break;
            }

            //２つのリストが同じなら、今のリストをシャッフルし、ループをもう一度やり直す。(シャッフル失敗）
            else if(i==(TOTAL-1)){
                Collections.shuffle(NowList);
                i=0;
            }
        }


        //プロンプト上に曲順と曲名を表示
        for (int i = 0; i < TOTAL; i++) {
            System.out.print(NowList.get(i).name+ "  ->  ");
        }
        System.out.println();


        //再生番号を１番（リストの要素は０に対応）にする
        NowPlayingNumber = 0;
    }


    /**
     * ラブシャッフルメソッド
     * Songデータを参照して、LoveValueが高い曲からランダムにソートされる
     */
    public void LoveShuffle() {

        List<Song>[] LoveList = new ArrayList[LoveValueMax + 1];//LoveValueMaxの数だけ用意する。

        for (int i = 0; i < LoveValueMax + 1; i++) {
            LoveList[i] = new ArrayList();
        }

        //LoveValueの値より振り分ける。
        for (int i = 0; i < TOTAL; i++) {
            int value = NowList.get(i).LoveValue;
            switch (value){
                case 1:
                    LoveList[1].add(NowList.get(i));
                    break;
                case 2:
                    LoveList[2].add(NowList.get(i));
                    break;
                case 3:
                    LoveList[3].add(NowList.get(i));
                    break;
                case 4:
                    LoveList[4].add(NowList.get(i));
                    break;
                case 5:
                    LoveList[5].add(NowList.get(i));
                    break;
            }
        }

        NowList = new ArrayList<Song>();

        //それぞれのリストをそのリストの中でシャッフルして、NowListに結合する。
        for (int i = LoveValueMax; i >= 1; i--) {
            if(LoveList[i].size() != 0){
                Collections.shuffle(LoveList[i]);//シャッフル
                NowList.addAll(LoveList[i]);//結合
            }
        }


        for (int i = 0; i < TOTAL; i++) {
            System.out.println(NowList.get(i).LoveValue);
        }

        //再生番号を１番（リストの要素は０に対応）にする
        NowPlayingNumber = 0;

    }

    /**
     * 今のリストから先読みするメソッド
     * @return Songs
     * PEEKMAXの数を上限として,
     * 次に再生する予定の曲を先読みして配列として返します。
     * ただし、次に返す曲の状態（再生番号）は変わりません。
     */

    public Song[] peekQueue(){

        //PEEKMAXの数を上限とする配列を宣言
        Song[] Songs = new Song[PEEKMAX];

            /*
            配列に今のリストNowListの曲を入れる。
            今の曲番号（NowPlayingNumber）から数えてPEEKMAX分の曲を入れる。
            順に入れていって最後の曲を入れても、未代入のSongs[i]があるとき
            最初の曲を順に入れる。
            */
        for (int i = 0; i < PEEKMAX; i++) {
            Songs[i] = NowList.get((NowPlayingNumber + i) % TOTAL);
        }

        return Songs;
    }

    /**
     * 曲順を手に入れるメソッド
     * @return SongOrderText
     */
    public String getSongOrderText(){
        //SongOrderTextにPEEKMAX分の曲名を曲順に並べて保存している。
        for (int i = 0; i < PEEKMAX; i++) {
            SongOrderText += "#" + (i + 1) + "," + peekQueue()[i].name + "      ";
        }

        return SongOrderText;

        //ここから下は気にしないでください。
        //SongOrderTextにPEEKMAX分の曲名を保存している
        //for (int i = 0; i < PEEKMAX; i++) {
        //    if (i == NowPlayingNumber) {
        //        SongOrderText += "♪" + peekQueue()[i].name + " ";
        //    }else{
        //        SongOrderText += "#" + (i + 1) + "," + peekQueue()[i].name + " ";
        //    }
    }
}