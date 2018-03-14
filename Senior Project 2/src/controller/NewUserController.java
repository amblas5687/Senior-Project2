package controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import application.AnnaMain;
import application.DBConfig;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
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
        
    

    public void initialize(){
		
    	System.out.println("*******NEW PATIENT*******");
    	
    	//add combo box selections
    	relationBox.getItems().addAll("Son/Daughter", "Spouse", "Grandchild", "Friend", "Medical Professional");
    }
    
    //user selects next button and enters code
    @FXML
    void patientCodeEnter(ActionEvent event) {
    	Alert dialog = new Alert(AlertType.CONFIRMATION);
    	dialog.getDialogPane().getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
    	dialog.setTitle("Patient Code");
    	dialog.setHeaderText("Enter the patient code that was \ngenerated when you created your patient");
    	
    	stage = (Stage) dialog.getDialogPane().getScene().getWindow();
    	stage.getIcons().add(new Image("/application/4getmenot1.jpg"));
    	
    	GridPane grid = new GridPane();
    	Label label = new Label("Please enter your patient code:  ");
    	TextField textField = new TextField();
    	
    	grid.add(label, 0, 0);
    	grid.add(textField, 1, 0);
    	dialog.getDialogPane().setContent(grid);
    	
    	ButtonType noCode = new ButtonType("No patient code");
    	dialog.getDialogPane().getButtonTypes().setAll(noCode, ButtonType.OK, ButtonType.CANCEL);
    	
    	//grab the fields for the user
	    UserModel subUser = grabFields();
	    
	    //generate userCode
	    String userCode = genCode();
	    subUser.setUserID(userCode);
    	
    	//get response
    	Optional<ButtonType> result = dialog.showAndWait();
    	
		if(result.get() == noCode) {
			
			 try {
					stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
					root = FXMLLoader.load(getClass().getResource("/view/NewPatientView.fxml"));
					scene = new Scene(root);
					stage.setScene(scene);
			 } catch(Exception e) {
				 e.printStackTrace();
			 }
			 
		} else if (result.get() == ButtonType.OK){
    		
    		//TODO need to check patient table to verify the patient
    	    System.out.println("PATIENT CODE... " + result.get());
    	    
    	    subUser.setPatientCode(textField.getText());
    	    
    		Connection connection = null;
        	PreparedStatement user = null;
    	    
    	    //prepare the query
    	    String userQ = "INSERT into user (fname, lname, DOB, pRelation, email, password, patientCode, userID) "
        			+ "VALUES (?,?,?,?,?,?,?,?)";
        	try {
				connection = DataSource.getInstance().getConnection();
        		
    			user = connection.prepareStatement(userQ);
    			user.setString(1, subUser.getFname());
    			user.setString(2, subUser.getLname());
    			user.setString(3, subUser.getDOB());
    			user.setString(4, subUser.getRelation());
    			user.setString(5, subUser.getEmail());
    			user.setString(6, subUser.getPassword());
    			user.setString(7, subUser.getPatientCode());
    			user.setString(8, subUser.getUserID());

    			
    			user.execute();
    			System.out.println("USER ENTERED..."+subUser);
    			
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			DBConfig.displayException(e);
    		}catch (Exception e)
        	{
        		e.printStackTrace();
        	}
        	//close connections
        	finally {
    			if(connection!=null)
    			{
    				try {
    					connection.close();
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    			
    			if(user!=null)
    			{
    				try {
    					user.close();
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    		}//finally
    	}//else if
    }//end method
    
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
