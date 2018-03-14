package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import application.AnnaMain;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	 Parent root;
	 Scene scene;
	 Stage stage;
	 
	 @FXML
	 private TextField emailTF;

	 @FXML
	 private PasswordField passwordTF;

	 @FXML
	 private Button btnLogin;

	 @FXML
     private JFXButton btnUser;

	 @FXML
	 private JFXButton btnPatient;
	 
	 
	//global variables for login
	 public static String currentUserID;
	 public static String currentPatientID;
		 
	 public void initialize(){
			
			System.out.println("*******LOGIN*******");
	 }
	 
	 
	 @FXML
	 void loadNewPatient(ActionEvent event) {
		 
		 try {
			stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/view/NewPatientView.fxml"));
			scene = new Scene(root);
			stage.setScene(scene);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 
	 }

	 @FXML
	 void loadNewUser(ActionEvent event) {
		 
		 try {
			stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/view/NewUserView.fxml"));
			scene = new Scene(root);
			stage.setScene(scene);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 
	 }

	 @FXML
	 void login(ActionEvent event) {
		 
		 	Connection connection = null;
	    	PreparedStatement ps = null;
	    	ResultSet rs = null;
		 
		 try {
			 
			 connection = DataSource.getInstance().getConnection();

			 String email = emailTF.getText();
			 String password = passwordTF.getText();
			 String query = "SELECT * FROM user WHERE email = ? AND password = ?";
			 
			 ps = connection.prepareStatement(query);
			 ps.setString(1, email);
			 ps.setString(2, password);
			 rs = ps.executeQuery();
			 
			 while(rs.next()){

					String mail = rs.getString("email");
					String pass = rs.getString("password");
			 
					 if(mail.equals(email) && pass.equals(password)){
						stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
						root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
						scene = new Scene(root);
						stage.setScene(scene);
						
						currentUserID = rs.getString("userID");
						currentPatientID = rs.getString("patientCode");
						
						System.out.println("CURRENT USER AND PATIENT... " + currentUserID + "__" + currentPatientID);
						 
					 } else {
						 System.out.println("FAILED LOGIN");
					 }
			 }
			 
			 
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
				
				if(ps!=null)
				{
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(rs!=null)
				{
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}//end finally
	 }//end login
	 
	 
}
