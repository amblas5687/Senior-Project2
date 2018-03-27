package controller;

import java.net.URL;

import com.jfoenix.controls.JFXSlider;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


public class HelpDocsController {
	
	@FXML
    private AnchorPane content_view;

    @FXML
    private MediaView media_view;
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private Button btnMeds;

    @FXML
    private Button btnUser;

    @FXML
    private Button btnPatient;
    
    @FXML
    private JFXSlider timeSlider;

    
    //URL mediaUrl = getClass().getResource("/application/jellyfish-25-mbps-hd-hevc.mp4");
    URL mediaUrl = getClass().getResource("/application/moon_jellies_hd_stock_video (1).mp4");
    String mediaStringUrl = mediaUrl.toExternalForm();
    Media media = new Media(mediaStringUrl);
    final MediaPlayer player = new MediaPlayer(media);
    Duration time, curTime;
    
    public void initialize() {
    	
    	media_view.setMediaPlayer(player);
    	timeSlider.setValue(0);
    }
    
    @FXML
    void clickPlay(MouseEvent event) {
    	
    	if(player.getStatus() == Status.PLAYING) {
    		player.pause();
    		lblStatus.setStyle("-fx-font: 24 arial;");
    		lblStatus.setText(">");
    	} else {
    		player.play();
    		lblStatus.setStyle("-fx-font: 16 arial;"
    						 + "-fx-font-weight: bold;");
    		lblStatus.setText("||");
    	}
    	
    	player.setOnEndOfMedia(new Runnable() {
    	    @Override
    	    public void run() {
    	        player.stop();
    	    }
    	});
    	
    	System.out.println("Total time: " + player.getTotalDuration().toMinutes());
    	player.currentTimeProperty().addListener(new InvalidationListener() {
    		
    		public void invalidated(Observable ov) {
    			
    			if (!timeSlider.isValueChanging()) {
    				System.out.println("HIT");
    				System.out.println(player.currentTimeProperty().get());
    				System.out.println(player.currentTimeProperty().get().toMinutes());
    				timeSlider.setValue(player.currentTimeProperty().get().toMinutes());
    				
    			}
            }
        });
    }
    
    @FXML
    void dragSlider(MouseEvent event) {
    	
    	timeSlider.valueProperty().addListener(new InvalidationListener() {
    	    public void invalidated(Observable ov) {
    	       if (timeSlider.isValueChanging()) {
    	    	   System.out.println("Value: " + timeSlider.getValue());
    	       
    	          player.seek(Duration.minutes(timeSlider.getValue()));
    	       }
    	    }
    	});
    }
    
    @FXML
    void clickVideo(MouseEvent event) {
    	
    	if(player.getStatus() == Status.PLAYING) {
    		player.pause();
    		lblStatus.setStyle("-fx-font: 24 arial;");
    		lblStatus.setText(">");
    	} else {
    		player.play();
    		lblStatus.setStyle("-fx-font: 16 arial;"
    						 + "-fx-font-weight: bold;");
    		lblStatus.setText("||");
    	}
    	
    	player.currentTimeProperty().addListener(new InvalidationListener() {
    		
    		public void invalidated(Observable ov) {
    			if (!timeSlider.isValueChanging()) {
    				System.out.println("HIT2");
    				System.out.println(player.currentTimeProperty().get());
    				System.out.println(player.currentTimeProperty().get().toMinutes());
    				timeSlider.setValue(player.currentTimeProperty().get().toMinutes());
    				
    			}
            }
        });	
    }
    
    @FXML
    void medsVideo(ActionEvent event) {

    }

    @FXML
    void patientVideo(ActionEvent event) {

    }
    

    @FXML
    void userVideo(ActionEvent event) {

    }
    
    @FXML
    void exitVideo(MouseEvent event) {
    	
    	//pauses video
    	Timeline timeline = new Timeline(new KeyFrame(
    	        Duration.seconds(1),
    	        ae -> player.pause()));
    	timeline.play();
    	
    	//sets font size on video pause
    	Timeline tline = new Timeline(new KeyFrame(
    	        Duration.seconds(1),
    	        ae -> lblStatus.setStyle("-fx-font: 24 arial;")));
    	tline.play();
    	
    	//changes label on video pause
    	Timeline tlin = new Timeline(new KeyFrame(
    	        Duration.seconds(1),
    	        ae -> lblStatus.setText(">")));
    	tlin.play();
  
    }

}
