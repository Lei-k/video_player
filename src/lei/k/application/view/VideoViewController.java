package lei.k.application.view;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import lei.k.application.Main;

public class VideoViewController {
	
	@FXML private MediaView mediaView;
	@FXML private HBox mediaBar;
	@FXML private Button rewindButton;
	@FXML private Button playButton;
	@FXML private Button forwardButton;
	@FXML private Slider sliderDuration;
	@FXML private Slider sliderVolume;
	@FXML private Slider sliderBalance;
	@FXML private Label lblDuration;
	
	private Main main;
	private MediaPlayer mediaPlayer;
	
	private Image imgPlay;
	private Image imgPause;
	private Image imgForward;
	private Image imgRewind;
	
	@SuppressWarnings("static-access")
	public void setMain(Main main){
		this.main = main;
		mediaPlayer = new MediaPlayer(main.getMedia());
		mediaView.setMediaPlayer(mediaPlayer);
		
		mediaBar.setHgrow(sliderDuration,  Priority.ALWAYS);
		
		initMediaView();
		
		initMediaPlayer();
		
		initSliders();
		
		initLblDuration();
	}
	
	private void initMediaView(){
		main.getVideoViewPane().heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
		      mediaView.setFitHeight((Double)newValue - 40.0);
		});
		
		main.getVideoViewPane().widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
		      mediaView.setFitWidth((Double)newValue);
		});
	}
	
	private void initMediaPlayer(){
		mediaPlayer.statusProperty().addListener((Observable observable) -> {
			Platform.runLater(() -> {
				MediaPlayer.Status status = mediaPlayer.getStatus();
				
				if(status == MediaPlayer.Status.UNKNOWN || status == null)
					sliderDuration.setDisable(true);
				else{
					sliderDuration.setDisable(false);
					
					if(status == MediaPlayer.Status.PLAYING)
						playButton.setGraphic(new ImageView(imgPause));
					else
						playButton.setGraphic(new ImageView(imgPlay));
				}
			});
		});
		
		mediaPlayer.currentTimeProperty().addListener((Observable observable) -> {
			Platform.runLater(() -> {
				final Duration totalDuration = mediaPlayer.getTotalDuration();
				final Duration currentDuration = mediaPlayer.getCurrentTime();
				
				double totalMillis = totalDuration.toMillis();
				int totalSeconds = (int)(totalMillis / 1000) % 60;
				int totalMinutes = (int)(totalMillis / (1000 * 60));
		        
		        double currentMillis = currentDuration.toMillis();
		        int currentSeconds = (int) (currentMillis / 1000) % 60;
		        int currentMinutes = (int) (currentMillis / (1000 * 60));
		        
		        lblDuration.setText(String.format("%02d:%02d", currentMinutes, currentSeconds) 
		        		+ "/" + String.format("%02d:%02d", totalMinutes, totalSeconds));
		        
		        if (sliderDuration.isValueChanging())
		            return;
		        
		        if (totalDuration == null || currentDuration == null)
		            sliderDuration.setValue(0);
		        else if (currentDuration.toMillis() >= totalDuration.toMillis()) {
		          lblDuration.setText(String.format("%02d:%02d", totalMinutes, totalSeconds) 
			          + "/" + String.format("%02d:%02d", totalMinutes, totalSeconds));
		          sliderDuration.setValue(totalDuration.toMillis() / totalDuration.toMillis());
		        }
		        else {
		          sliderDuration.setValue(currentDuration.toMillis() / totalDuration.toMillis());
		        }
			});
		});
	}
	
	private void initSliders(){
		sliderDuration.valueChangingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (oldValue && !newValue) {
				double pos = sliderDuration.getValue();
		        final Duration seekTo = mediaPlayer.getTotalDuration().multiply(pos);
		        
		        if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
		          mediaPlayer.pause();
		        }
		        
		        mediaPlayer.seek(seekTo);
		        
		        if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
		            if (sliderDuration.isValueChanging())
		              return;
		          
		            final Duration total = mediaPlayer.getTotalDuration();
		          
		            if (total == null || seekTo == null) {
		                sliderDuration.setValue(0);
		            }
		            else {
		                sliderDuration.setValue(seekTo.toMillis() / total.toMillis());
		            }
		        }
			}
		});
		
		sliderVolume.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());
		
		sliderBalance.valueProperty().addListener((Observable observable) -> {
		      if (sliderBalance.isValueChanging()) {
		         mediaPlayer.setBalance(sliderBalance.getValue());
		      }
		});
	}
	
	private void initLblDuration(){
		lblDuration.setText("00:00/00:00");
	}
	
	@FXML
	private void initialize(){
		
		imgPlay = new Image(getClass().getResourceAsStream("images/play.gif"));
		imgPause = new Image(getClass().getResourceAsStream("images/pause.gif"));
		imgForward = new Image(getClass().getResourceAsStream("images/forward.gif"));
		imgRewind = new Image(getClass().getResourceAsStream("images/rewind.gif"));
		
		rewindButton.setGraphic(new ImageView(imgRewind));
		playButton.setGraphic(new ImageView(imgPlay));
		forwardButton.setGraphic(new ImageView(imgForward));
	}
	
	@FXML 
	private void rewindButtonClicked(){
		final Duration currentduration = Duration.ZERO;
		
		if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED)
	        mediaPlayer.pause();
	      
	    mediaPlayer.seek(currentduration);
	      
	    if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
	        if (sliderDuration.isValueChanging())
	            return;
	        
	        final Duration total = mediaPlayer.getTotalDuration();
	        
	        if (total == null)
	            sliderDuration.setValue(0);
	        else 
	            sliderDuration.setValue(currentduration.toMillis() / total.toMillis());
	    }
	}
	
	@FXML
	private void playButtonClicked(){
		if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
			mediaPlayer.pause();
		else
			mediaPlayer.play();
	}
	
	@FXML
	private void forwardButtonClicked(){
		final Duration totalDuration = mediaPlayer.getTotalDuration();
		
		if(mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED)
			mediaPlayer.pause();
		
		mediaPlayer.seek(totalDuration);
		
		if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
	        if (sliderDuration.isValueChanging())
	          return;
	        
	        final Duration total = mediaPlayer.getTotalDuration();
	        
	        if (total == null || totalDuration == null)
	          sliderDuration.setValue(0);
	        else
	          sliderDuration.setValue(totalDuration.toMillis() / total.toMillis());
	    }
	}
	
	public void reloadMedia(Media media){
		mediaPlayer.dispose();
		mediaPlayer = new MediaPlayer(media);
		mediaView.setMediaPlayer(mediaPlayer);
		initMediaPlayer();
		initLblDuration();
		sliderVolume.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());
	}
}
