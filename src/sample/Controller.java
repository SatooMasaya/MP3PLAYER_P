package sample;

import java.io.*;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static sample.Main.SongEngine;
import static sample.Main.media;
import static sample.Main.mplayer;

public class Controller {
    /**
     * 下に表示するテキスト。曲順を現在の曲から５曲までを表示。
     */
    public TextField SongOrderTextField;

    /**
     * 上に表示するテキスト。現在再生される曲を表示。
     */
    public TextField NowPlayingTextField;

    /**
     * ロック。再生中に、一時停止する時に使用する。
     */
    boolean LOCK = true;

    /**
     * シャッフルモード。trueなら、なら、ラブシャッフル。falseなら、通常シャッフル。
     */
    boolean LoveShaffleMode = false;

    /**
     *  LoveButton（ラジオボタン）に確認マークがついているかの確認。
     */
    boolean checkLoveButton = true;




    /**
     * 前へ再生ボタンアクションメソッド
     * @param actionEvent
     */
    public void onBackPlayButtonClicked(javafx.event.ActionEvent actionEvent) {

        mplayer.stop();//前にかかっている曲と次に再生する曲が同時にかかるのを防ぐため、
                //前の再生されている曲を止める。

        // 前に再生した曲を手に入れ、その曲を再生する
        media = new Media(new File(SongEngine.getBackSong().name + ".mp3").toURI().toString());
        mplayer = new MediaPlayer(media);
        mplayer.play();
        LOCK = false;

        //上のテキストフィールドに次の再生中の曲名を表示する
        NowPlayingTextField.setText("Now playing  " + SongEngine.NowList.get(SongEngine.NowPlayingNumber).name);

        //下のテキストフィールドにPEEKMAX分の曲順を表示する。
        SongOrderTextField.setText(SongEngine.getSongOrderText());
        SongEngine.SongOrderText = "";

    }

    /**
     * 再生ボタンアクションメソッド
     * @param actionEvent
     */
    public void onPlayButtonClicked(javafx.event.ActionEvent actionEvent) {



        //一回押すと再生し、もう一度押すと一時停止する
        if (LOCK) {//再生ボタンをおした場合
            // 曲が流れる。流れる曲はNowListの再生番号NowPlayingNumberである。
            mplayer.play();//仕様上、再生が終了すると、再び再生することができないことに注意

            LOCK = false;
        }else{//再生中におした場合
            //曲が止まる。
            mplayer.pause();
            LOCK = true;
        }

        //再生が終了したとき、再び同じ曲を再生できるコード
        if (mplayer.getStopTime().toSeconds() == mplayer.getCurrentTime().toSeconds()){//再生が終了して、もう一度再生ボタンを押した場合、
            mplayer.stop();//このメソッドが使われるときには既に再生は終了しているが、
                     //このメソッドを使用すれば、再び再生が可能になる。
            mplayer.play();
            LOCK = false;
        }

    }

    /**
     * 次へ再生ボタンアクションメソッド
     * @param actionEvent
     */
    public void onNextPlayButtonClicked(javafx.event.ActionEvent actionEvent) {

        mplayer.stop();//前にかかっている曲と次に再生する曲が同時にかかるのを防ぐため、
                //前の再生されている曲を止める。

        //Runnable     repeatFunc      = () ->
        //{
            // 連続再生ボタンの状態を取得し
            //if( true )
            //{
                //media = new Media(new File(SongEngine.getNextSong().name + ".mp3").toURI().toString());
                //mplayer = new MediaPlayer(media);
                // 頭だしして再生
            //    mplayer.seek( mplayer.getStartTime() );
            //    mplayer.play();
            //}else{
                // 頭だしして停止
            //    mplayer.seek( mplayer.getStartTime() );
            //    mplayer.stop();
            //};
        //};

        // 次に再生する曲を手に入れ、その曲を再生する
        media = new Media(new File(SongEngine.getNextSong().name + ".mp3").toURI().toString());
        mplayer = new MediaPlayer(media);

        //mplayer.setOnEndOfMedia(repeatFunc);
        mplayer.play();
        LOCK = false;


        //上のテキストフィールドに次の再生中の曲名を表示する
        NowPlayingTextField.setText("Now playing  " + SongEngine.NowList.get(SongEngine.NowPlayingNumber).name);

        //下のテキストフィールドにPEEKMAX分の曲順を表示する。
        SongOrderTextField.setText(SongEngine.getSongOrderText());
        SongEngine.SongOrderText = "";

    }

    /**
     * シャッフルボタンアクションメソッド
     * @param actionEvent
     */
    public void onShufflePlayButtonClicked(javafx.event.ActionEvent actionEvent) {

        mplayer.stop();//前にかかっている曲と次に再生する曲が同時にかかるのを防ぐため、
        //前の再生されている曲を止める。

        //シャッフルモードの確認
        if(LoveShaffleMode){
            SongEngine.LoveShuffle();
            System.out.println("ppap");
        }else{
            SongEngine.Shuffle();
        }

        media = new Media(new File(SongEngine.NowList.get(0).name + ".mp3").toURI().toString());
        mplayer = new MediaPlayer(media);
        mplayer.play();
        LOCK = false;


        //上のテキストフィールドに次の再生中の曲名を表示する
        NowPlayingTextField.setText("Now playing  " + SongEngine.NowList.get(SongEngine.NowPlayingNumber).name);

        //下のテキストフィールドにPEEKMAX分の曲順を表示する。
        SongOrderTextField.setText(SongEngine.getSongOrderText());
        SongEngine.SongOrderText = "";
    }

    public void onLoveButtonClicked(ActionEvent actionEvent) {

        if (checkLoveButton) {//LoveButton（ラジオボタン）の確認マークをつけたら
            LoveShaffleMode = true;//LoveShaffleModeを適用
            checkLoveButton = false;//次に押すとマークが消えるので、まえもってfalseに変更
        }else{//LoveButton（ラジオボタン）の確認マークをけしたら
            LoveShaffleMode = false;//LoveShaffleModeを解除
            checkLoveButton = true;//次に押すとマークがtつくのでまえもってtrueに変更
        }

    }
}
