package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import application.DBConfig;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class NewMedController {
		
    @FXML
    private TextField medName;

    @FXML
    private TextField medDosage;

    @FXML
    private TextField prescribDoc;

    @FXML
    private TextField purpOfPrescript;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button cancelBTN;

    @FXML
    private TextArea medDescript;

    @FXML
    private DatePicker DOPPicker;

    @FXML
	private AnchorPane content_view;
    
    private URL toPane;
	private AnchorPane temp;
	 
	
	public void initialize(){
		
		System.out.println("*******NEW MED*******");
	}
	
	
    @FXML
    void returnMain(ActionEvent event) {
    	
    	try {
    		
			//Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/CurrentMedsView.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }

    @FXML
    void submit(ActionEvent event) {
    	
    	String mName = medName.getText();
    	String mDosage = medDosage.getText();
    	String mDescript = medDescript.getText();
    	String pDoc = prescribDoc.getText();
    	String pPurpose = purpOfPrescript.getText();
    	String pDate = DOPPicker.getValue().toString();
    	String dateAdded = java.time.LocalDate.now().toString();  

    	Connection connection = null;
    	PreparedStatement ps = null;
    	
    	String query = "INSERT INTO currentMeds (patientCode, medName, medDosage, medDescript, prescribDoc, purpPresrcipt, prescribDate, dateAdded)"
    			+ "VALUES (?,?,?,?,?,?,?,?)";
    	
    	try {
				connection = DataSource.getInstance().getConnection();
			
	    		ps = connection.prepareStatement(query);
	    		ps.setString(1, LoginController.currentPatientID);
	        	ps.setString(2, mName);
	        	ps.setString(3, mDosage);
	        	ps.setString(4, mDescript);
	        	ps.setString(5, pDoc);
	        	ps.setString(6, pPurpose);
	        	ps.setString(7, pDate);
	        	ps.setString(8, dateAdded);
	        	
	        	ps.execute();
	        	
	        	System.out.println("Successful insertion of medication information.");
	        
	    	} catch (SQLException e) {
	    		
	    		DBConfig.displayException(e);
	    		
	    		System.out.println("Failed insertion of medication information.");
	    	}catch (Exception e)
	    	{
	    		e.printStackTrace();
	    	}
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
    		}
	    	
	    	//Clears fields after medication insertion
	    	medName.setText("");
	    	medDosage.setText("");
	    	medDescript.setText("");
	    	prescribDoc.setText("");
	    	purpOfPrescript.setText("");
	    	DOPPicker.setValue(null);
    }
    
}
