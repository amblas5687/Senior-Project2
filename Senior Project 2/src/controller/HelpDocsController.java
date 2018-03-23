package controller;

import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


public class HelpDocsController {
	
	@FXML
    private AnchorPane content_view;

    @FXML
    private MediaView media_view;
    
    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;
    
    //URL mediaUrl = getClass().getResource("/application/jellyfish-25-mbps-hd-hevc.mp4");
    URL mediaUrl = getClass().getResource("/application/moon_jellies_hd_stock_video (1).mp4");
    String mediaStringUrl = mediaUrl.toExternalForm();
    Media media = new Media(mediaStringUrl);
    final MediaPlayer player = new MediaPlayer(media);
    Duration time;
    
    public void initialize() {
    	
    	media_view.setMediaPlayer(player);
    }
    
    @FXML
    void startVideo(ActionEvent event) {
    	
    	player.setOnPlaying(new Runnable() {
    	    @Override
    	    public void run() {
    	  
    	    	player.setStopTime(Duration.millis(182732.0));
    	    }
    	});
    	
    	player.play();
    	player.setStartTime(Duration.millis(0.0));
    	
    	player.setOnEndOfMedia(new Runnable() {
    	    @Override
    	    public void run() {
    	        player.stop();
    	    }
    	});
    }

    @FXML
    void stopVideo(ActionEvent event) {
    	
    	player.pause();
    }
    
    @FXML
    void exitVideo(MouseEvent event) {
    	
    	time = player.currentTimeProperty().get().add(Duration.seconds(1.5));
    	
    	player.setStopTime(time);
    	player.setOnStopped(new Runnable() {
    	    @Override
    	    public void run() {
    	  
    	    	player.setStartTime(time);
    	    }
    	});
    	
    }

}
