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
    URL mediaUrl = getClass().getResource("/application/dolbycanyon.mp4");
    String mediaStringUrl = mediaUrl.toExternalForm();
    Media media = new Media(mediaStringUrl);
    final MediaPlayer player = new MediaPlayer(media);

    
    public void initialize() {
    	
    	media_view.setMediaPlayer(player);
    }
    
    @FXML
    void startVideo(ActionEvent event) {
    	
    	player.play();
    	
    	
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
    	
    	player.pause();
    }

}
