package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import application.AnnaMain;
import application.DBConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ViewUserController {
	Connection conn = AnnaMain.con;

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
    private Button btnEdit;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button cancelBTN;
    @FXML
    private ComboBox<String> relationBox;
    
    public void initialize(){
    	String dob = "", relation = "";
    	String query = "SELECT * FROM user WHERE ID = 3";
    	
    	try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				fnameTF.setText(rs.getString("fname"));
				lnameTF.setText(rs.getString("lname"));
				dob = rs.getString("DOB");
				emailTF.setText(rs.getString("email"));
				relation = rs.getString("pRelation");
			}
			
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(dob, df);
			DOBPicker.setValue(date);
			
			relationBox.setValue(relation);
		} catch (SQLException e) {
			DBConfig.displayException(e);
		}
    	
    	System.out.println("Successful pull of user information.");
    }
 
    @FXML
    void edit(ActionEvent event) {
    	btnEdit.setVisible(false);
    	btnSubmit.setVisible(true);
    	fnameTF.setDisable(false);
    	lnameTF.setDisable(false);
    	DOBPicker.setDisable(false);
    	relationBox.setDisable(false);
    	relationBox.getItems().addAll("Son/Daughter", "Spouse", "Grandchild", "Friend", "Medical Professional");
    	emailTF.setDisable(false);	
    }
    
    @FXML
    void submit(ActionEvent event) {
    	String firstName = fnameTF.getText();
    	String lastName = lnameTF.getText();
    	String dob = DOBPicker.getValue().toString();
    	String relation = relationBox.getValue();
    	String email = emailTF.getText();
    	
    	String query = "UPDATE user SET fname = ?, lname = ?, DOB = ?, pRelation = ?, "
    			+ "email = ? WHERE ID = 3";
    			    			
    	try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, dob);
			ps.setString(4, relation);
			ps.setString(5, email);
			
			ps.execute();
			
		} catch (SQLException e) {
			DBConfig.displayException(e);
		}
    	System.out.println("Successful change of user information.");
    	btnSubmit.setVisible(false);
    	fnameTF.setDisable(true);
    	lnameTF.setDisable(true);
    	DOBPicker.setDisable(true);
    	relationBox.setDisable(true);
    	emailTF.setDisable(true);
    	btnEdit.setVisible(true);
    }

    //user cancels submission
    @FXML
    void returnMain(ActionEvent event) {

    }

}


