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
    private Label lblDescription;
    
    @FXML
    private Label lblDisclaimer;
    
    @FXML
    private Button btnMeds;

    @FXML
    private Button btnUser;

    @FXML
    private Button btnPatient;
    
    @FXML
    private JFXSlider timeSlider;

    boolean end = false;
    
    //URL mediaUrl = getClass().getResource("/application/jellyfish-25-mbps-hd-hevc.mp4");
    URL mediaUrl = getClass().getResource("/application/moon_jellies.mp4");
    String mediaStringUrl = mediaUrl.toExternalForm();
    Media media = new Media(mediaStringUrl);
    final MediaPlayer player = new MediaPlayer(media);
    Duration time, curTime;
    
    public void initialize() {
    	
    	media_view.setMediaPlayer(player);
    	timeSlider.setValue(0);
    	lblStatus.setStyle("-fx-background-image: url('/application/play.png');"
				 + "-fx-background-repeat: no-repeat;");
    	
    	lblDescription.setText("Forget Me Not is designed to help nonprofessional inhome cargivers keep track of Alzheimer's patient information.\n\n"
    			+ "Forget Me Not seeks to cut down on communication issues between multiple caregivers for one patient.");
    	
    	lblDisclaimer.setText("This system was built as a senior project. Therefore, errors and bugs exist in this system. Also, this system is not HIPPA"
    			+ " compliant. If you find an issue, please feel free to report it to us.");
    	
    	player.setOnEndOfMedia(new Runnable() {
		    @Override
		    public void run() {
		        player.pause();
	    		lblStatus.setStyle("-fx-background-image: url('/application/replay.png');"
	    				 + "-fx-background-position: center;"
						 + "-fx-background-repeat: no-repeat;");
	    		
	    		end = true;
		        System.out.println("Status: " + player.getStatus());
		    }
    	});
    }
    
    @FXML
    void clickVideo(MouseEvent event) {
    	
    	if(end) {
    		player.stop();
    		player.play();
    		lblStatus.setStyle("-fx-background-image: url('/application/pause.png');"
					 + "-fx-background-repeat: no-repeat;");
    		
    		end = false;
    	} else if(player.getStatus() == Status.PLAYING) {
    		player.pause();
    		lblStatus.setStyle("-fx-background-image: url('/application/play.png');"
					 + "-fx-background-repeat: no-repeat;");
    	} else {
    		player.play();
    		lblStatus.setStyle("-fx-background-image: url('/application/pause.png');"
					 + "-fx-background-repeat: no-repeat;");
    	}
    	
    	System.out.println("Total time: " + player.getTotalDuration().toMinutes());
    	player.currentTimeProperty().addListener(new InvalidationListener() {
    		
    		public void invalidated(Observable ov) {
    			
    			if (!timeSlider.isValueChanging()) {
    				System.out.println("HIT");
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
    	        ae -> lblStatus.setStyle("-fx-background-image: url('/application/play.png');"
						 			   + "-fx-background-repeat: no-repeat;")));
    	tline.play();
  
    }

}
