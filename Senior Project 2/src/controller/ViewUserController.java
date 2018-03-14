package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import application.DBConfig;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ViewUserController {

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
    @FXML
	private AnchorPane content_view;
    
    private URL toPane;
	private AnchorPane temp;
    
    public void initialize(){
		
    	System.out.println("*******VIEW USER INFO*******");

    	Connection connection = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	
    	
    	String dob = "", relation = "";
    	String query = "SELECT * FROM user WHERE userID = ?";
    	
    	try {
			connection = DataSource.getInstance().getConnection();
    		
			ps = connection.prepareStatement(query);
			ps.setString(1, LoginController.currentUserID);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				fnameTF.setText(rs.getString("fname"));
				lnameTF.setText(rs.getString("lname"));
				dob = rs.getString("DOB");
				emailTF.setText(rs.getString("email"));
				relation = rs.getString("pRelation");
				password1TF.setText(rs.getString("password"));
				password2TF.setText(rs.getString("password"));

			}
			
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(dob, df);
			DOBPicker.setValue(date);
			relationBox.setValue(relation);
			
	    	System.out.println("PULLED USER INFO");
			
		} catch (SQLException e) {
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
		}//END FINALLY
    	
    }//END METHOD
 
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
    	cancelBTN.setVisible(true);
    }
    
    @FXML
    void submit(ActionEvent event) {
    	String firstName = fnameTF.getText();
    	String lastName = lnameTF.getText();
    	String dob = DOBPicker.getValue().toString();
    	String relation = relationBox.getValue();
    	String email = emailTF.getText();
    	
    	String query = "UPDATE user SET fname = ?, lname = ?, DOB = ?, pRelation = ?, "
    			+ "email = ? WHERE userID = ?";
    	
    	Connection connection = null;
    	PreparedStatement ps = null;    	
    	
    	try {
			connection = DataSource.getInstance().getConnection();
    		
			ps = connection.prepareStatement(query);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, dob);
			ps.setString(4, relation);
			ps.setString(5, email);
			ps.setString(6, LoginController.currentUserID);
			
			ps.execute();
			
	    	System.out.println("CHANGED USER INFO");
			
		} catch (SQLException e) {
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
			
			if(ps!=null)
			{
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
					
		}//end finally
    	
    	btnSubmit.setVisible(false);
    	fnameTF.setDisable(true);
    	lnameTF.setDisable(true);
    	DOBPicker.setDisable(true);
    	relationBox.setDisable(true);
    	emailTF.setDisable(true);
    	btnEdit.setVisible(true);
    	cancelBTN.setVisible(false);
    }

    //user cancels submission
    @FXML
    void returnMain(ActionEvent event) {

    	try {
    		
			//Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/ViewUserInfo.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }

}


