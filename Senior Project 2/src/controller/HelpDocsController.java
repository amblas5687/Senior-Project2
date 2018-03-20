package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

public class HelpDocsController {
	
	@FXML
    private AnchorPane content_view;

    @FXML
    private WebView web_view;
    
    public void initialize() {
    	
    	    web_view.getEngine().load(
    	      "https://www.youtube.com/embed/Sk-U8ruIQyA"
    	    );
    	    
    }
}
