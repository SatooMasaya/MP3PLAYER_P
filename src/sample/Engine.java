package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.io.*;


/**
 * Engineクラスには大きな役割が４つある。
 * SongEngineはSongData.txtに記載されている曲を読み取り、nowListにいれる。
 * nowListに入っている曲順をランダムに入れ替える。
 * nowListのどの要素（番号）の曲を流すかの情報NowPlayingNumberをもつ。
 * これから流れる曲を先読みしてUIに表示できるよう準備する。
 * @author Sato Masaya
 */
public class Engine implements ShuffleEngine{

    /**SongDataにある曲の総数*/
    int total = 0;

    /**
     * UIの下部に表示するテキスト。
     * PEEK_MAX分の曲名を保存するテキスト。このテキストは、テキストフィールドSongOrderTextFieldに表示される。
     */
    String songOrderText = "";

    /**
     * 再生番号。
     * 再生ボタンを押した時、再生番号nowPlayingNumberに対応するリストの曲を再生する。
     */
    int nowPlayingNumber = 0;

    /**
     * 今のリスト。
     * Song型の変数を要素とするリスト。再生ボタンを押すとこのリストのnowPlayingNumber番目の曲が流れる。
     */
    List<Song> nowList = new ArrayList<Song>();


    /**先読みする曲の上限数*/
    public final Integer PEEK_MAX = 5;

    /**
     * 優先度の上限。
     * ラブシャッフルするとき、ソートするときの優先度の上限数
     */
    public final Integer LOVE_VALUE_MAX = 5;


    /**
     * セットソングスメソッド。
     * 今のリストnowListにすべての曲を保存するメソッド。
     * メソッドsetSongData()の中で使われる。
     * @param songs SongData.txtで得られるすべての曲は配列として保存される。その配列が引数songsに入れられる。
     */
    public void setSongs(Song[] songs){

        for (int i = 0; i < total; i++) {//今のリストnowListにすべての曲（total分の曲）を保存
            nowList.add(songs[i]);
        }

    }

    /**
     *
     * 全曲のデータ(SongData.txt)を読み込み、NowListに保存するメソッド。
     * このメソッドはMainクラスの中で使われる。
     * @throws IOException 入出力エラーが発生した場合
     */
    public void setSongData() throws IOException{

        String songname = "";
        int lovevalue = 0;
        int line = 0;//曲数のカウント
        Song[] songs;//代入用。すべての曲がnowListに保存される。


        //データにいくつ曲が入っているかの確認をする。
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("SongData.txt"), "SJIS"));
        while(br.readLine() != null) {
            line++;
        }
        total = line;//総曲数を記録する。


        //配列songsにすべての曲を代入する。
        br = new BufferedReader(new InputStreamReader(new FileInputStream("SongData.txt"), "SJIS"));
        songs = new Song[total];
        String entry;//一行の文を一時記録する変数
        line = 0;

        while ((entry = br.readLine()) != null) {//ファイルから一行読む
            Scanner sc = new Scanner(entry);
            if (sc.hasNext()) songname = sc.next();//曲名取得
            if (sc.hasNextInt()) lovevalue = sc.nextInt();//曲の優先度取得
            System.out.println(line + songname + lovevalue);
            songs[line] = new Song(songname, lovevalue);//Songに曲データをいれる
            line++;
        }
        br.close();

        setSongs(songs);//nowListに保存する
    }

    /**
     * 次に再生する曲を手に入れるメソッド。
     * 再生番号を+１して、次に再生する曲を返す。
     * このメソッドは、onNextPlayButtonClickedメソッドの中で使われる。
     * @return nextSong この返り値は、onBackPlayButtonClickedメソッドの中で、次の曲の名前を取得するときに使う。
     */
    public Song getNextSong(){

        //再生番号を+１
        nowPlayingNumber++;


        if (nowPlayingNumber == total) {//もしすべての曲が流れたら、最初の曲に戻る
            nowPlayingNumber = 0;
        }

        //Song型で宣言
        Song nextSong = nowList.get(nowPlayingNumber);

        return nextSong;

    }

    /**
     * 前の曲を手に入れるメソッド。
     * 再生番号を-1して、前に再生した曲を返す。
     * このメソッドは、onBackPlayButtonClickedメソッドの中で使われる。
     * @return backSong この返り値は、onBackPlayButtonClickedメソッドの中で、前の曲の名前を取得するときに使う。
     */
    public Song getBackSong() {

        //再生番号を-１してオーディオにセット
        nowPlayingNumber--;


        if (nowPlayingNumber == -1) {
            /*
             * もし最初の曲が聴ける状態(nowPlayingNumberに0が代入されている状態)で
             * 前へ再生ボタンをおしたら(このメソッドが呼ばれたら)
             */
            nowPlayingNumber = 0;
        }

        //Song型で宣言
        Song backSong = nowList.get(nowPlayingNumber);

        return backSong;
    }

    /**
     *  シャッフルメソッド
     *  1巡分の曲順を決めるとき、前の一巡分の曲順とかぶらないように決めている。
     *  このメソッドは、loveShuflle、onShufflePlayButtonClickedメソッドの中で使用される。
     *  @param list onShufflePlayButtonClickedメソッドの中では、引数listに代入される変数はnowListである。
     *              loveshuffleメソッドの中では、引数listにloveList[i]が代入される。
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
                nowPlayingNumber = 0;

                //この改行は、プロンプト上の出力結果に表示されるnowListの全要素（一巡の曲たち）が見やすくなる。
                //消しても動作に影響はない。
                System.out.println("");
            }
        }

    }


    /**
     * ラブシャッフルメソッド
     * nowListにある曲Songの値loveValueを基準に、その値が高い曲からランダムにソートされ、nowListには、高い優先度の曲から入る。
     * このメソッドは、onShufflePlayButtonClickedメソッドの中で使用される。
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
     * 次に再生する予定の曲をPEEK_MAX分先読みして配列として返します。
     * このメソッドは、getSongOrderTextメソッドの中で使われます。
     * @return songs 返り値は、次に再生する予定の曲で、PEEK_MAX個の配列数をもつ配列とする。
     */

    public Song[] peekQueue(){

        //PEEKMAXの数を上限とする配列を宣言
        Song[] songs = new Song[PEEK_MAX];

            /*
            配列に今のリストNowListの曲を入れる。
            今の曲番号（nowPlayingNumber）から数えてPEEK_MAX分の曲を入れる。
            順に入れていって最後の曲を入れても、未代入のsongs[i]があるとき
            最初の曲を順に入れる。
            */
        for (int i = 0; i < PEEK_MAX; i++) {
            songs[i] = nowList.get((nowPlayingNumber + i) % total);
        }

        return songs;
    }

    /**
     * 曲順を手に入れるメソッド
     * このメソッドは、autoPlayメソッドの中で使われる。
     * @return songOrderText この返り値が、UIの下部に表示される。
     */
    public String getSongOrderText(){
        //SongOrderTextにPEEK_MAX分の曲名を曲順に並べて保存している。
        for (int i = 0; i < PEEK_MAX; i++) {
            songOrderText += "#" + (i + 1) + "," + peekQueue()[i].name + "     ->     ";
        }

        return songOrderText;

    }
}