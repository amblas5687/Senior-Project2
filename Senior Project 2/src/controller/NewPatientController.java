package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import application.AnnaMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class NewPatientController {
	Connection conn = AnnaMain.con;
	
    @FXML
    private TextField fnameTF;
    @FXML
    private TextField lnameTF;
    @FXML
    private DatePicker DOBPicker;
    @FXML
    private TextField doctorTF;
    @FXML
    private Button submitBTN;
    @FXML
    private Button cancelBTN;
    @FXML
    private ComboBox<String> stageBox;
    @FXML
    private DatePicker diagnosesPicker;
    @FXML
    private TextField cargiverTF;
    
    
    public void initialize(){
    	
    	//add combo box selections
    	stageBox.getItems().addAll("Stage 1", "Stage 2", "Stage 3", "Stage 4", "Stage 5", "Stage 6", "Stage 7");
    }
    
    @FXML
    void returnMain(ActionEvent event) {

    }

    @FXML
    void submit(ActionEvent event) {
    	String firstName = fnameTF.getText();
    	String lastName = lnameTF.getText();
    	String dob = DOBPicker.getValue().toString();
    	String doc = doctorTF.getText();
    	String stage = stageBox.getValue().toString();
    	String diagDate = diagnosesPicker.getValue().toString();
    	String caregiver = cargiverTF.getText();
    	
    	String query = "INSERT INTO patient (firstName, lastName, dob, currStage, diagnoseDate, primaryDoc, caregiver)"
    			+ "VALUES (?,?,?,?,?,?,?)";
    	try {
    		PreparedStatement ps = conn.prepareStatement(query);
        	ps.setString(1, firstName);
        	ps.setString(2, lastName);
        	ps.setString(3, dob);
        	ps.setString(4, stage);
        	ps.setString(5, diagDate);
        	ps.setString(6, doc);
        	ps.setString(7, caregiver);
        	
        	ps.execute();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }

}
