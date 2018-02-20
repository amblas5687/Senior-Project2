package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.AnnaMain;
import application.DBConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewMedController {
	Connection conn = AnnaMain.con;
	
	Stage stage;
	Parent root;
	Scene scene;
	
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
    void returnMain(ActionEvent event) {
    	try {
    		stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    		root = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
    		scene = new Scene(root);
    		stage.setScene(scene);
    		
    		
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
