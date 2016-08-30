package lei.k.application.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class VideoPlayer {
	
	private String mediaURL;
	
	private Media media;
	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	
	public VideoPlayer(String URL){
		this.mediaURL = URL;
		media = new Media(mediaURL);
		mediaPlayer = new MediaPlayer(media);
		mediaView = new MediaView(mediaPlayer);
	}
	
	public void setURL(String URL){
		mediaURL = URL;
		media = new Media(mediaURL);
	}
	
	public void play(){
		mediaPlayer.play();
	}
	
	public void pause(){
		mediaPlayer.pause();
	}
	
	public void seek(Duration seekTime){
		mediaPlayer.seek(seekTime);
	}
	
	public Status getStatus(){
		return mediaPlayer.getStatus();
	}
	
	public MediaPlayer getPlayer(){
		return mediaPlayer;
	}
	
	public MediaView getView(){
		return mediaView;
	}
}
