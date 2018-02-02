package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.AnnaMain;
import application.DBConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.UserModel;

public class NewUserController {

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
    void codeEnter(ActionEvent event) {
    	
    	//temporary database submission for testing
    	//*********************************************************************
    	String fname = fnameTF.getText();
    	String lname = lnameTF.getText();
    	
    	String DOB = DOBPicker.getValue().toString();
    	
    	String pRelation = relationBox.getValue();
    	String email = emailTF.getText();
    	String password = password1TF.getText();
    	
    	UserModel tempUser = new UserModel(fname, lname, DOB, email, password, pRelation);
    	System.out.println(tempUser);
    	
    	String userQ = "INSERT into user (fname, lname, DOB, pRelation, email, password) "
    			+ "VALUES (?,?,?,?,?,?)";
    	try {
			PreparedStatement user = con.prepareStatement(userQ);
			user.setString(1, tempUser.getFname());
			user.setString(2, tempUser.getLname());
			user.setString(3, tempUser.getDOB());
			user.setString(4, tempUser.getRelation());
			user.setString(5, tempUser.getEmail());
			user.setString(6, tempUser.getPassword());
			
			user.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			DBConfig.displayException(e);
		}
    	//*********************************************************************

    }

    //user cancels submission
    @FXML
    void returnMain(ActionEvent event) {

    }

}
