package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import application.AnnaMain;
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
import model.MedModel;

public class EditMedController {
		
	@FXML
    private AnchorPane content_view;

    @FXML
    private TextField medName;

    @FXML
    private TextField medDosage;

    @FXML
    private TextField prescribDoc;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button cancelBTN;

    @FXML
    private TextArea medDescript;

    @FXML
    private DatePicker DOPPicker;

    @FXML
    private TextField purpOfPrescript;
    
    private URL toPane;
	private AnchorPane temp;
	
	private MedModel editMed;

   
	public void initialize(){
		System.out.println("*******EDIT MED*******");
		setMed();
	}
	
	public void setMed() {
		CurrentMedsController test = new CurrentMedsController();
		editMed = test.getEdit();
		System.out.println("MED TO EDIT... " + editMed);
		
		//prepopulate textfields
		medName.setText(editMed.getMedName().get());
		medDosage.setText(editMed.getMedDosage().get());
    	medDescript.setText(editMed.getDetails().get());
    	prescribDoc.setText(editMed.getDoc().get());
    	purpOfPrescript.setText(editMed.getPurpose().get());
    	
    	//setting the datepicker
    	String prescribDateString = editMed.getDate().get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	LocalDate prescribDate = LocalDate.parse(prescribDateString, formatter);
\    	DOPPicker.setValue(prescribDate);
    	DOPPicker.setDisable(true);
    	
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
    	String dateUpdated = java.time.LocalDate.now().toString();
    	
    	String query = "UPDATE currentMeds SET medName = ?, medDosage = ?, medDescript = ?, "
    			+ "prescribDoc = ?, purpPresrcipt = ?, dateUpdated = ? "
    			+ "WHERE patientCode = ? AND medID = ?";
    	
    	
    	Connection connection = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	
    	try {
			connection = DataSource.getInstance().getConnection();    		
    		
    		ps = connection.prepareStatement(query);
        	ps.setString(1, mName);
        	ps.setString(2, mDosage);
        	ps.setString(3, mDescript);
        	ps.setString(4, pDoc);
        	ps.setString(5, pPurpose);
        	ps.setString(6, dateUpdated);
        	
        	//parameters from edit med to update by
        	ps.setString(7, editMed.getPatientCode().get());
        	ps.setString(8, editMed.getMedID().get());
        	
        	ps.execute();
        	
        	System.out.println("MEDICATION UPDATED");
        	
        	
        	//go back to current meds
        	try {
        		
    			//Replace content_view's current display with the view for this controller
    			toPane = getClass().getResource("/view/CurrentMedsView.fxml");
    			temp = FXMLLoader.load(toPane);
    			content_view.getChildren().setAll(temp);
        		
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        	
        	
        
    	} catch (SQLException e) {
    		
    		DBConfig.displayException(e);
    		
    		System.out.println("FAILED UPDATE");
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
    }//end submit
    
    
    
    
}
