package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import application.AnnaMain;
import application.DBConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class NewMedController {
	Connection conn = AnnaMain.con;
	
    @FXML
    private TextField medName;

    @FXML
    private TextField medDosage;

    @FXML
    private TextField prescribDoc;

    @FXML
    private TextField purpOfPrescript;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button cancelBTN;

    @FXML
    private TextArea medDescript;

    @FXML
    private DatePicker DOPPicker;

    @FXML
	private AnchorPane content_view;
    
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
    	
    	String mName = medName.getText();
    	String mDosage = medDosage.getText();
    	String mDescript = medDescript.getText();
    	String pDoc = prescribDoc.getText();
    	String pPurpose = purpOfPrescript.getText();
    	String pDate = DOPPicker.getValue().toString();
    	
    	String query = "INSERT INTO currentMeds (medName, medDosage, medDescript, prescribDoc, purpPresrcipt, prescribDate)"
    			+ "VALUES (?,?,?,?,?,?)";
    	
    	try {
    		PreparedStatement ps = conn.prepareStatement(query);
        	ps.setString(1, mName);
        	ps.setString(2, mDosage);
        	ps.setString(3, mDescript);
        	ps.setString(4, pDoc);
        	ps.setString(5, pPurpose);
        	ps.setString(6, pDate);
        	
        	ps.execute();
        
    	} catch (SQLException e) {
    		DBConfig.displayException(e);
    	}
    	
    	System.out.println("Successful insertion of medication information.");
    	
    	//Clear form for next addition
    	medName.setText("");
    	medDosage.setText("");
    	medDescript.setText("");
    	prescribDoc.setText("");
    	purpOfPrescript.setText("");
    	DOPPicker.setValue(null);
    }
}
