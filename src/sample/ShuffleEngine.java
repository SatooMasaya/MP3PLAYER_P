package sample;

/**
 *
 * @author Sato Masaya
 */
public interface ShuffleEngine {
    public void setSongs(Song[] songs);
    public Song getNextSong();
    public Song[] peekQueue();
}