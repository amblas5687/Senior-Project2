package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.jfoenix.controls.JFXButton;
import application.AnnaMain;
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
	
	 Connection conn = AnnaMain.con;

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
		 
		 try {
			 String email = emailTF.getText();
			 String password = passwordTF.getText();
			 String query = "SELECT * FROM user WHERE email = ? AND password = ?";
			 
			 PreparedStatement ps = conn.prepareStatement(query);
			 ps.setString(1, email);
			 ps.setString(2, password);
			 ResultSet rs = ps.executeQuery();
			 
			 while(rs.next()){

					String mail = rs.getString("email");
					String pass = rs.getString("password");
			 
					 if(mail.equals(email) && pass.equals(password)){
						stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
						root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
						scene = new Scene(root);
						stage.setScene(scene);
					 } else {
						 System.out.println("Unsuccessful login");
					 }
			 }
			 
			 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
}
