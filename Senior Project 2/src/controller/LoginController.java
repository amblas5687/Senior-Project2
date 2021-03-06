package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.jfoenix.controls.JFXButton;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginController {
	
	 Parent root;
	 Scene scene;
	 Stage stage;
	 
	 @FXML
	 private AnchorPane ap;
	 
	 @FXML
	 private BorderPane bp;
	 
	 @FXML
	 private AnchorPane ap2;
	 
	 @FXML
	 private AnchorPane ap3;
	 
	 @FXML
	 private GridPane grid;
	 
	 @FXML
	 private GridPane grid2;
	 
	 @FXML
	 private GridPane grid3;
	 
	 @FXML
	 private GridPane grid4;
	 
	 @FXML
	 private TextField emailTF;

	 @FXML
	 private PasswordField passwordTF;

	 @FXML
	 private Button btnLogin;
	 
	 @FXML
	 private JFXButton btnHelp;

	 @FXML
     private JFXButton btnUser;

	 @FXML
	 private JFXButton btnPatient;
	 
	 @FXML 
	 private Label lblLogin;
	 
	 @FXML
	 private ImageView logo;
	 
	 
	//global variables for login
	 public static String currentUserID = null;
	 public static String currentPatientID = null;
	 public static String currentUserName = null;
		 
	 public void initialize(){
			
			System.out.println("*******LOGIN*******");
			logo.setImage(new Image("/application/logo.png"));
			btnHelp.setPadding(Insets.EMPTY);
			btnHelp.setPadding(new Insets(8, 8, 8, 8));
			
			//binds borderpane to parent anchorpane
			//resizes borderpane based on anchorpane size
			bp.prefWidthProperty().bind(ap.widthProperty());
			bp.prefHeightProperty().bind(ap.heightProperty());
			bp.setMinSize(600, 400);
			bp.setMaxSize(1200, 678);

	 }
	 
	 @FXML
	 void loadHelp(ActionEvent event) {
		 
		 try {
			stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/view/LoginHelpDocs.fxml"));
			scene = new Scene(root);
			stage.setScene(scene);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 
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
		 
		 String email = emailTF.getText();
		 String password = passwordTF.getText();
		 
		 lblLogin.setText(null);
		 
		 if(email.equals("") || password.equals("")) {
			 lblLogin.setText("Please fill in both fields");
		 } else {
			 Connection connection = null;
		     PreparedStatement ps = null;
		     ResultSet rs = null;
			 
			 try {
				 
				 connection = DataSource.getInstance().getConnection();
				 
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
							String first = rs.getString("fname");
							String last = rs.getString("lname");
							currentUserName = first + " " + last;
							currentPatientID = rs.getString("patientCode");
							
							//System.out.println("CURRENT USER AND PATIENT... " + currentUserID + "__" + currentPatientID);
							 
						 } else {
							 System.out.println("FAILED LOGIN");
						 }
				 }
				 
				 lblLogin.setText("Incorrect email address or password");
				 
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
		  }//end else
	 }//end login
	 
	 
}
