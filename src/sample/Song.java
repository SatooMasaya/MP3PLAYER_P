package sample;

/**
 *
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
    int LoveValue;

    //コンストラクト
    public Song(String n, int l){
        this.name = n;
        this.LoveValue = l;
    }

    public Song(String n){
        this.name = n;
    }
}

//aaa