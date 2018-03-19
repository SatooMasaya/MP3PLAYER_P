package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    /**
     * エンジンを宣言する。
     */
    public static Engine SongEngine = new Engine();

    /**
     * メディア。何を再生するのかを決める。
     */
    public static Media media = new Media(new File("おやすみ.mp3").toURI().toString());

    /**
     * プレイヤー。再生、一時停止、停止ができる。
     */
    public static MediaPlayer mplayer = new MediaPlayer(media);

    @Override
    public void start(Stage primaryStage) throws Exception {

        //背景設定
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Cacco Music PLAYER");
        primaryStage.setScene(new Scene(root, 600, 125));
        primaryStage.getIcons();
        primaryStage.show();

        //SongData.txtの全曲を読み込み、NowListにいれる。
        try {
            SongEngine.songDataInSet();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(mplayer.isAutoPlay());
        //mplayer.setAutoPlay(true);
        //System.out.println(mplayer.isAutoPlay());
        // 連続再生設定


    }



    public static void main(String[] args) {
        launch(args);

    }
}
