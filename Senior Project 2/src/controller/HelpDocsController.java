package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class HelpDocsController {
	
	@FXML
    private AnchorPane content_view;

    @FXML
    private WebView web_view = new WebView();
    
    private Stage stage;
    
    public void initialize() {
    	
    	    web_view.getEngine().load(
    	      "https://www.youtube.com/embed/Sk-U8ruIQyA"
    	    );
    	    
    }
    
    @FXML
    void clickVideo(MouseEvent event) {
    	System.out.println("HIT");
    	stage = (Stage) ((WebView) event.getSource()).getScene().getWindow();
    	
    	System.out.println(content_view);//Scene@6eee3cfa
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
    		
            @Override
            public void handle(WindowEvent event) {
            	web_view.getEngine().load(null);
            	
            	System.out.println("HIT2");
            }
        });
    	
    }
}
