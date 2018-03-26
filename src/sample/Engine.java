package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.io.*;


/**
 * next「次へ再生」、shuffle「シャッフル再生」の仕様
 * @author Sato Masaya
 */
public class Engine implements ShuffleEngine{

    /**先読みする曲の上限数*/
    public final Integer PEEK_MAX = 5;

    /**ラブシャッフルするとき、ソートするときの優先度数*/
    public final Integer LOVE_VALUE_MAX = 5;

    /**再生番号*/
    int NowPlayingNumber = 0;
    //再生ボタンを押した時、再生番号NowPlayingNumberに対応するリストの曲を再生

    /**SongDataにある曲の総数*/
    int total = 0;

    /**PEEKMAX分の曲名を保存するテキスト*/
    String SongOrderText = "";
    //このテキストは、テキストフィールドSongOrderTextFieldに表示される

    /**今のリスト*/
    List<Song> nowList = new ArrayList<Song>();


    /**
     * 今のリストNowListにすべての曲を保存するメソッド
     * @param songs 曲の配列
     */
    public void setSongs(Song[] songs){

        //今のリストNowListにすべての曲（TOTAL分の曲）を保存
        for (int i = 0; i < total; i++) {
            nowList.add(songs[i]);
        }

    }

    /**
     * 全曲の名前データ(SongData)を読み込みNowListに保存するメソッド
     * @throws IOException
     */
    public void setSongData() throws IOException{

        String songname = "";
        int lovevalue = 0;
        int line = 0;//曲数のカウント
        Song[] ss;//代入用


        //データにいくつ曲が入っているかの確認をする。
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("SongData.txt"), "SJIS"));
        while(br.readLine() != null) {
            line++;
        }
        total = line;//総曲数を記録する。


        //配列ssにすべての曲を代入する。
        br = new BufferedReader(new InputStreamReader(new FileInputStream("SongData.txt"), "SJIS"));
        ss = new Song[total];
        String entry;//一行の文を一時記録する変数
        line = 0;

        while ((entry = br.readLine()) != null) {//ファイルから一行読む
            Scanner sc = new Scanner(entry);
            if (sc.hasNext()) songname = sc.next();//曲名取得
            if (sc.hasNextInt()) lovevalue = sc.nextInt();//曲の優先度取得
            System.out.println(line + songname + lovevalue);
            ss[line] = new Song(songname, lovevalue);//Songに曲データをいれる
            line++;
        }
        br.close();

        //nowListに保存する
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
        if (NowPlayingNumber == total) {
            NowPlayingNumber = 0;
        }

        //Song型で宣言
        Song nextSong = nowList.get(NowPlayingNumber);

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
        Song backSong = nowList.get(NowPlayingNumber);

        return backSong;
    }

    /**
     *  シャッフルメソッド
     *  @param list
     *　最初に1巡分の曲順を決定しておくロジックである。
     *  1巡分の曲順を決めるとき、前の一巡分の曲順とかぶらないように決めている。
     */
    public void shuffle(List<Song> list){

        List<Song> oldList = new ArrayList<Song>();//過去のリスト（シャッフル前のリストを保存するために用意したリスト）

        //今のリストのすべての曲を過去のリストへコピー
        for (int i = 0; i < list.size(); i++) {
            oldList.add(i, list.get(i));
        }

        //今のリストの曲の順番をシャッフル
        Collections.shuffle(list);

        /*
        ここでは、過去のリスト（シャッフル前の曲の順番）と
        今のリスト（シャッフル後の曲の順番）を比較している。
        */
        for (int i = 0; i < list.size(); i++) {

            //２つのリストが違うなら、ループから出る（条件を満たすシャッフルが成功）
            if(oldList.get(i)!= list.get(i)){
                break;
            }

            //２つのリストが同じなら、今のリストをシャッフルし、ループをもう一度やり直す。(シャッフル失敗）
            else if(i==(list.size() -1)){
                Collections.shuffle(list);
                i=0;
            }
        }


        //プロンプト上に曲順と曲名、優先度を表示
        //消しても動作に影響はでない。
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i).name + " " + list.get(i).loveValue + "  ->  ");

            //再生番号を１番（リストの要素は０に対応）にする
            if (i == total-1) {
                NowPlayingNumber = 0;

                //この改行は、プロンプト上の出力結果に表示されるnowListの全要素（一巡の曲たち）が見やすくなる。
                //消しても動作に影響はない。
                System.out.println("");
            }
        }

    }


    /**
     * ラブシャッフルメソッド
     * nowListにある曲Songの値LoveValueを基準に、その値が高い曲からランダムにソートされ、
     * nowListには、高い優先度の曲から入る。
     */
    public void loveShuffle() {

        List<Song>[] loveList = new ArrayList[LOVE_VALUE_MAX + 1];//Love_Value_Maxの数だけ用意する。

        for (int i = 0; i < LOVE_VALUE_MAX + 1; i++) {
            loveList[i] = new ArrayList();
        }

        //loveValueの値より振り分ける。
        for (int i = 0; i < total; i++) {
            int value = nowList.get(i).loveValue;
            switch (value){
                case 1:
                    loveList[1].add(nowList.get(i));
                    break;
                case 2:
                    loveList[2].add(nowList.get(i));
                    break;
                case 3:
                    loveList[3].add(nowList.get(i));
                    break;
                case 4:
                    loveList[4].add(nowList.get(i));
                    break;
                case 5:
                    loveList[5].add(nowList.get(i));
                    break;
            }
        }

        nowList = new ArrayList<Song>();//nowListの中身を一旦、空にします。

        //それぞれのリストをそのリストの中でシャッフルして、NowListに結合する。
        for (int i = LOVE_VALUE_MAX; i >= 1; i--) {
            if(loveList[i].size() != 0){
                shuffle(loveList[i]);//シャッフル
                nowList.addAll(loveList[i]);//結合
            }

            //この下のコードは、プロンプト上の出力結果に表示されるnowListの全要素（一巡の曲たち）が見やすくなる。
            //消しても動作に影響はでない。
            if (i == 1){
                System.out.println("");
            }
        }
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
        Song[] Songs = new Song[PEEK_MAX];

            /*
            配列に今のリストNowListの曲を入れる。
            今の曲番号（NowPlayingNumber）から数えてPEEKMAX分の曲を入れる。
            順に入れていって最後の曲を入れても、未代入のSongs[i]があるとき
            最初の曲を順に入れる。
            */
        for (int i = 0; i < PEEK_MAX; i++) {
            Songs[i] = nowList.get((NowPlayingNumber + i) % total);
        }

        return Songs;
    }

    /**
     * 曲順を手に入れるメソッド
     * @return SongOrderText
     */
    public String getSongOrderText(){
        //SongOrderTextにPEEKMAX分の曲名を曲順に並べて保存している。
        for (int i = 0; i < PEEK_MAX; i++) {
            SongOrderText += "#" + (i + 1) + "," + peekQueue()[i].name + "     ->     ";
        }

        return SongOrderText;

    }
}