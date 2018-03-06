package controller;

import java.net.URL;
import java.sql.Connection;
import application.AnnaMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class EditMedController {
	
	Connection conn = AnnaMain.con;
	
	@FXML
    private AnchorPane content_view;

    @FXML
    private TextField medName;

    @FXML
    private TextField medDosage;

    @FXML
    private TextField prescribDoc;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button cancelBTN;

    @FXML
    private TextArea medDescript;

    @FXML
    private DatePicker DOPPicker;

    @FXML
    private TextField purpOfPrescript;
    
    private URL toPane;
	private AnchorPane temp;

    @FXML
    void returnMain(ActionEvent event) {

    	try {
    		
			//Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/ViewMedInfo.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }

    @FXML
    void submit(ActionEvent event) {

    }
}
