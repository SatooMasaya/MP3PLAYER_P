package sample;

import java.io.*;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static sample.Main.SongEngine;
import static sample.Main.media;
import static sample.Main.mplayer;

/**
 * ボタンを押したときのアクションの仕様
 * @author Sato Masaya
 */


public class ButtonController {
    /**
     * 下に表示するテキスト。曲順を現在の曲から５曲までを表示。
     */
    public TextField SongOrderTextField;

    /**
     * 上に表示するテキスト。現在再生される曲を表示。
     */
    public TextField NowPlayingTextField;

    /**
     * ロック。再生中に、一時停止する時に使用する。falseのとき一時停止が可能。
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
        AutoPlay();

    }

    /**
     * 再生ボタンアクションメソッド
     * @param actionEvent
     */
    public void onPlayButtonClicked(javafx.event.ActionEvent actionEvent) {

        //一回押すと再生し、もう一度押すと一時停止する
        if (LOCK) {//再生ボタンをおした場合
            // 曲が流れる。流れる曲はNowListの再生番号NowPlayingNumberである。
            AutoPlay();
        }else{//再生中におした場合
            //曲が止まる。
            mplayer.pause();
            LOCK = true;
        }

    }

    /**
     * 次へ再生ボタンアクションメソッド
     * @param actionEvent
     */
    public void onNextPlayButtonClicked(javafx.event.ActionEvent actionEvent) {

        mplayer.stop();//前にかかっている曲と次に再生する曲が同時にかかるのを防ぐため、
                //前の再生されている曲を止める。

        // 次に再生する曲を手に入れ、その曲を再生する

        //再生が終了して、もう一度再生ボタンを押した場合、
        media = new Media(new File(SongEngine.getNextSong().name + ".mp3").toURI().toString());
        mplayer = new MediaPlayer(media);
        AutoPlay();

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
        }else{
            SongEngine.Shuffle();
        }

        media = new Media(new File(SongEngine.NowList.get(0).name + ".mp3").toURI().toString());
        mplayer = new MediaPlayer(media);
        AutoPlay();
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

    /**
     * オートプレイメソッド
     * 自動再生（今の曲をきいたら、次の曲が自動でかかる）を可能にするメソッド
     */
    public void AutoPlay(){
        Runnable []repeatFunc = new Runnable[SongEngine.TOTAL - SongEngine.NowPlayingNumber +1];//NowListの中でまだ聴いていない分の曲数を配列数として配列を用意する

        for (int i = 0; i < SongEngine.TOTAL - SongEngine.NowPlayingNumber; i++) {
            int cnt = i;//カウント変数cntはiと同じ値をとる。iだと思ってよい。

            //Runnable関数repeatFunc[cnt + 1]を定義します。
            repeatFunc[cnt + 1] = () ->
            {
                media = new Media(new File(SongEngine.getNextSong().name + ".mp3").toURI().toString());// 次の曲をmediaに代入します。
                mplayer = new MediaPlayer(media);//mplayerにmediaの情報をいれる。
                mplayer.setOnEndOfMedia(repeatFunc[cnt]);//mplayerの再生終了時の設定を決定する。設定内容はRunnable関数repeatFunc[cnt]である。
                mplayer.play();//再生
                LOCK = false;//一時停止可能状態にする。

                //上のテキストフィールドに次の再生中の曲名を表示する
                NowPlayingTextField.setText("Now playing  " + SongEngine.NowList.get(SongEngine.NowPlayingNumber).name);

                //下のテキストフィールドにPEEKMAX分の曲順を表示する。
                SongOrderTextField.setText(SongEngine.getSongOrderText());
                SongEngine.SongOrderText = "";
            };
        }

        //Runnable関数repeatFunc[0]を定義します。
        repeatFunc[0] = () ->
        {
            media = new Media(new File(SongEngine.getNextSong().name + ".mp3").toURI().toString());// 次の曲をmediaに代入します。
            mplayer = new MediaPlayer(media);//mplayerにmediaの情報をいれる。
            mplayer.setOnEndOfMedia(repeatFunc[1]);//mplayerの再生終了時の設定を決定する。設定内容はRunnable関数repeatFunc[1]である。
            mplayer.play();//再生
            LOCK = false;//一時停止可能状態にする

            //上のテキストフィールドに次の再生中の曲名を表示する
            NowPlayingTextField.setText("Now playing  " + SongEngine.NowList.get(SongEngine.NowPlayingNumber).name);

            //下のテキストフィールドにPEEKMAX分の曲順を表示する。
            SongOrderTextField.setText(SongEngine.getSongOrderText());
            SongEngine.SongOrderText = "";
        };

        mplayer.setOnEndOfMedia(repeatFunc[0]);//mplayerの再生終了時の設定を決定する。設定内容はRunnable関数repeatFunc[0]である。
        mplayer.play();//再生
        LOCK = false;//一時停止可能状態にする

        //上のテキストフィールドに次の再生中の曲名を表示する
        NowPlayingTextField.setText("Now playing  " + SongEngine.NowList.get(SongEngine.NowPlayingNumber).name);

        //下のテキストフィールドにPEEKMAX分の曲順を表示する。
        SongOrderTextField.setText(SongEngine.getSongOrderText());
        SongEngine.SongOrderText = "";

    }
}
