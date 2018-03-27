package sample;

/**
 * SongData.txtに書かれた１曲が、これに代入される。
 * @author Sato Masaya
 */
public class Song {
    /**
     * 曲名
     */
    String name;

    /**
     * 優先度
     */
    int loveValue;

    //コンストラクト
    public Song(String n, int l){
        this.name = n;
        this.loveValue = l;
    }

    public Song(String n){
        this.name = n;
    }
}

//aaa