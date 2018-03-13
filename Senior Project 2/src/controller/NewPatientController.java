package controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import application.AnnaMain;
import application.DBConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewPatientController {
	Connection conn = AnnaMain.con;
	
	Stage stage;
	Parent root;
	Scene scene;
	
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
    	stageBox.getItems().addAll("Stage 1", "Stage 2", "Stage 3", "Stage 4", "Stage 5", "Stage 6", "Stage 7");
    }
    
    @FXML
    void returnMain(ActionEvent event) {

		 try {
				stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
				root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
				scene = new Scene(root);
				stage.setScene(scene);
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
    	
    }

    @FXML
    void submit(ActionEvent event) {
    	
    	String firstName = fnameTF.getText();
    	String lastName = lnameTF.getText();
    	String dob = DOBPicker.getValue().toString();
    	String doc = doctorTF.getText();
    	String curStage = stageBox.getValue().toString();
    	String diagDate = diagnosesPicker.getValue().toString();
    	String caregiver = cargiverTF.getText();
    	String patientCode = genCode();
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
    	DialogPane dialogPane = alert.getDialogPane();
    	dialogPane.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
    	dialogPane.setMaxHeight(200);
    	dialogPane.getStyleClass().add("alert");
    	alert.setTitle("Patient Code");
    	alert.setHeaderText("Patient code uniquely identifies each patient. Please record for further use."); 

    	TextField textField = new TextField("Patient Code: " + patientCode);
    	textField.setEditable(false);
    	textField.setStyle("-fx-background-color: ligthgrey;");
    	alert.getDialogPane().setContent(textField);
    	
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	String query = "INSERT INTO patient (firstName, lastName, dob, currStage, diagnoseDate, primaryDoc, caregiver, patientCode)"
    			+ "VALUES (?,?,?,?,?,?,?,?)";
    	try {
    		PreparedStatement ps = conn.prepareStatement(query);
        	ps.setString(1, firstName);
        	ps.setString(2, lastName);
        	ps.setString(3, dob);
        	ps.setString(4, curStage);
        	ps.setString(5, diagDate);
        	ps.setString(6, doc);
        	ps.setString(7, caregiver);
        	ps.setString(8, patientCode);
        	
        	ps.execute();
        	
    	} catch (SQLException e) {
    		DBConfig.displayException(e);
    	}
    	
    	System.out.println("Successful insertion of patient information.");
    	
    	if(result.get() == ButtonType.OK)
    	{
    		alert.hide();
    		
    		//Takes user to login page
    		try {
        		stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        		root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
        		scene = new Scene(root);
        		stage.setScene(scene);
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    }

    String genCode() {
		
		StringBuilder sb = new StringBuilder();

		// Create a secure random number generator using the SHA1PRNG algorithm
		try {
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
				
			// Get 16 random bytes
	    	byte[] randBytes = new byte[16];
	    	rand.nextBytes(randBytes);

	    	for(int i = 0; i < randBytes.length; i++)
	    	{
	    		sb.append(String.format("%02X", randBytes[i]));
	    	}
    			
    		System.out.println("\nBuilt string " + sb);
				
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
				
		System.out.println("Patient code: " + sb.toString());
		return sb.toString();
    	
    }

}
