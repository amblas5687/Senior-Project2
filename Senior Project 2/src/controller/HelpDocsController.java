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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


public class HelpDocsController {
	
	@FXML
    private AnchorPane content_view;
	
	@FXML
	private BorderPane bp;
	
	@FXML
	private AnchorPane ap;
	
	@FXML
	private GridPane grid;

    @FXML
    private MediaView media_view;
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private Label lblDescription;
    
    @FXML
    private Label lblDisclaimer;
    
    @FXML
    private Label lblContact;
    
    @FXML
    private Label lblReturn;
    
    @FXML
    private Button btnStart;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnMeds;
    
    @FXML
    private JFXSlider timeSlider;

    boolean end = false;
    
    //URL mediaUrl = getClass().getResource("/application/moon_jellies.mp4");
    URL mediaUrl = getClass().getResource("/application/tutorial.MP4");
    String mediaStringUrl = mediaUrl.toExternalForm();
    Media media = new Media(mediaStringUrl);
    final MediaPlayer player = new MediaPlayer(media);
    Duration time, curTime;
    
    public void initialize() {
		
    	System.out.println("*******HELP & DOCS*******");
    	
    	media_view.setMediaPlayer(player);
    	timeSlider.setValue(0);
    	lblStatus.setStyle("-fx-background-image: url('/application/play.png');"
				 + "-fx-background-repeat: no-repeat;");
    	
    	lblReturn.setText("To return to this page at any time just click our logo!\nThis video is a tutorial to help you navigate Forget Me Not:");
    	
    	lblDescription.setText("Forget Me Not is designed to help nonprofessional in-home caregivers keep track of Alzheimer's patient information.\n\n"
    			+ "Forget Me Not seeks to cut down on communication issues between multiple caregivers for one patient.\n\n"
    			+ "Forget Me Not is not limited to tracking just Alzheimer's. This system can be "
    			+ "used to track a patient with any illness, disease, disorder, or disability.");
    	
    	lblDisclaimer.setText("This system was built as a senior project. Therefore, errors and bugs exist in this system. "
    			+ "If you find an issue, feel free to report it to us.\n                                 Please note: This system is not HIPPA compliant.");
    	
    	lblContact.setText("If you need to contact us for any reason, please email us at:\n           gmail@gmail.com");
    	
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
    void enterScreen(MouseEvent event) {
    	/*
    	content_view.prefWidthProperty().bind(content_view.getParent().sceneProperty().get().widthProperty());
		content_view.prefHeightProperty().bind(content_view.getParent().sceneProperty().get().heightProperty());
		content_view.setMinSize(600, 400);
		content_view.setMaxSize(968, 678);

		grid.prefWidthProperty().bind(content_view.widthProperty());
		grid.prefHeightProperty().bind(content_view.heightProperty());
		grid.setMinSize(600, 400);
		grid.setMaxSize(968, 678);
		System.out.println(content_view.getParent().getParent().getScene().heightProperty());
		System.out.println(content_view.getParent().getScene().heightProperty());
		System.out.println(content_view.getScene().heightProperty());	
		System.out.println(grid.getScene().heightProperty());
		*/
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
    void createVideo(ActionEvent event) {//0-1.624
    	player.seek(Duration.minutes(0));
    	player.play();
    	
    	Timeline timeline = new Timeline(new KeyFrame(
    	        Duration.minutes(1.624),
    	        ae -> player.pause()));
    	timeline.play();
    	
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
    void editVideo(ActionEvent event) {//4.650-5.163
    	player.seek(Duration.minutes(4.650));
    	player.play();
    	
    	Timeline timeline = new Timeline(new KeyFrame(
    	        Duration.minutes(.513),
    	        ae -> player.pause()));
    	timeline.play();
    	
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
    void medsVideo(ActionEvent event) {//current: 1.624-4.650, archive: 5.163-6.025
    	player.seek(Duration.minutes(1.624));
    	player.play();
    	
    	Timeline timeline = new Timeline(new KeyFrame(
    	        Duration.minutes(3.006),
    	        ae -> player.seek(Duration.minutes(5.163))));
    	timeline.play();
    	
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
