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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.UserModel;

public class NewUserController {

	Parent root;
	Scene scene;
	Stage stage;
	
    @FXML
    private TextField fnameTF;
    @FXML
    private TextField lnameTF;
    @FXML
    private DatePicker DOBPicker;
    @FXML
    private TextField emailTF;
    @FXML
    private PasswordField password1TF;
    @FXML
    private PasswordField password2TF;
    @FXML
    private Button nextBTN;
    @FXML
    private Button cancelBTN;
    @FXML
    private ComboBox<String> relationBox;
    
	Connection con = AnnaMain.con;
    
    

    public void initialize(){
    	
    	//add combo box selections
    	relationBox.getItems().addAll("Son/Daughter", "Spouse", "Grandchild", "Friend", "Medical Professional");
    }
    
    //user selects next button and enters code
    @FXML
    void patientCodeEnter(ActionEvent event) {
    	TextInputDialog dialog = new TextInputDialog("");
    	dialog.getDialogPane().getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
    	dialog.setTitle("Patient Code");
    	dialog.setHeaderText("Enter the patient code that was \ngenerated when you created your patient");
    	dialog.setContentText("Please enter the code:");
    	
    	//grab the fields for the user
	    UserModel subUser = grabFields();
	    
	    //generate userCode
	    String userCode = genCode();
	    subUser.setUserID(userCode);
    	
    	//get response
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){
    		
    		//TODO need to check patient table to verify the patient
    	    System.out.println("Code: " + result.get());
    	    
    	    subUser.setPatientCode(result.get());
    	    
    	    //prepare the query
    	    String userQ = "INSERT into user (fname, lname, DOB, pRelation, email, password, patientCode, userID) "
        			+ "VALUES (?,?,?,?,?,?,?,?)";
        	try {
    			PreparedStatement user = con.prepareStatement(userQ);
    			user.setString(1, subUser.getFname());
    			user.setString(2, subUser.getLname());
    			user.setString(3, subUser.getDOB());
    			user.setString(4, subUser.getRelation());
    			user.setString(5, subUser.getEmail());
    			user.setString(6, subUser.getPassword());
    			user.setString(7, subUser.getPatientCode());
    			user.setString(8, subUser.getUserID());

    			
    			user.execute();
    			System.out.println(user);
    			
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			DBConfig.displayException(e);
    		}
    	    
    	    
    	}

    

    }
    
    //user cancels submission
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
    
    
    //helper methods
    
    UserModel grabFields()
    {
    	String fname = fnameTF.getText();
    	String lname = lnameTF.getText();
    	
    	String DOB = DOBPicker.getValue().toString();
    	
    	String pRelation = relationBox.getValue();
    	String email = emailTF.getText();
    	String password = password1TF.getText();
    	
    	UserModel tempUser = new UserModel(fname, lname, DOB, email, password, pRelation);
    	System.out.println(tempUser);
		return tempUser;
    	
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("User code: " + sb.toString());
				return sb.toString();
    	
    		}

}
