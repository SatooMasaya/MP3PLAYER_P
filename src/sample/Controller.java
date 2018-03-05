package sample;

import javafx.fxml.FXML;

import java.awt.event.ActionEvent;

import java.io.*;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Controller {

    public Engine SongEngine = new Engine();

    Media media = new Media(new File("おやすみ.mp3").toURI().toString());
    MediaPlayer m = new MediaPlayer(media);

    boolean stoplay = true;
    boolean first = true;


    @FXML
    /**
     * 前へ再生ボタンアクションメソッド
     * @param actionEvent
     */
    public void onBackPlayButtonClicked(javafx.event.ActionEvent actionEvent) {
        media = new Media(new File(SongEngine.getBackSong().name).toURI().toString());
        m = new MediaPlayer(media);
        m.play();
    }

    /**
     * 再生ボタンアクションメソッド
     * @param actionEvent
     */
    public void onPlayButtonClicked(javafx.event.ActionEvent actionEvent) {

        if (first) {
            try {
                SongEngine.songDataInSet();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }first =false;


        //一回押すと再生し、もう一度押すと止まる

        if (stoplay) {//再生ボタンをおした場合
            // 曲が流れる。流れる曲はNowListの再生番号NowPlayingNumberである。
            m.play();

            stoplay = false;
        }else{//再生中におした場合
            //曲が止まる。
            m.pause();

            stoplay = true;
        }
    }

    /**
     * 次へ再生ボタンアクションメソッド
     * @param actionEvent
     */
    public void onNextPlayButtonClicked(javafx.event.ActionEvent actionEvent) {
        media = new Media(new File(SongEngine.getNextSong().name).toURI().toString());
        m = new MediaPlayer(media);
        m.play();
    }

    /**
     * シャッフルボタンアクションメソッド
     * @param actionEvent
     */
    public void onShufflePlayButtonClicked(javafx.event.ActionEvent actionEvent) {
        SongEngine.Shuffle();
        media = new Media(new File(SongEngine.NowList.get(0).name).toURI().toString());
        m = new MediaPlayer(media);
        m.play();
    }
}
