package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    /**
     * エンジンを宣言する。
     */
    public static Engine SongEngine = new Engine();

    @Override
    public void start(Stage primaryStage) throws Exception{

        //背景設定
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Music PLAYER");
        primaryStage.setScene(new Scene(root, 550, 125));
        primaryStage.show();

        //SongData.txtの全曲を読み込み、NowListにいれる。
        try {
            SongEngine.songDataInSet();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);

    }
}
