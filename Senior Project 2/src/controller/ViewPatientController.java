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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.PatientModel;

public class ViewPatientController {

    @FXML
    private TextField fnameTF;
    @FXML
    private TextField lnameTF;
    @FXML
    private DatePicker DOBPicker;
    @FXML
    private TextField doctorTF;
    @FXML
    private Button submitBTN;
    @FXML
    private Button editBTN;
    @FXML
    private Button cancelBTN;
    @FXML
    private ComboBox<String> stageBox;
    @FXML
    private DatePicker diagnosesPicker;
    @FXML
    private TextField cargiverTF;
    @FXML
   	private AnchorPane content_view;
       
    private URL toPane;
   	private AnchorPane temp;
   
    
    public void initialize(){
    	
		System.out.println("*******VIEW PATIENT INFORMATION*******");

    	
    	//add combo box selections
    	stageBox.getItems().addAll("Stage 1", "Stage 2", "Stage 3", "Stage 4", "Stage 5", "Stage 6", "Stage 7");    	
    	
    	//prepopulate, uses dummy user for now
    	
    	String patientQ = "SELECT * FROM patient WHERE patientCode = ?";
    	
    	Connection connection = null;
    	PreparedStatement patientInfo = null;
    	ResultSet rs = null;
    	
    	
		PatientModel curPatient = new PatientModel();
    	try {
			connection = DataSource.getInstance().getConnection();
    		
			patientInfo = connection.prepareStatement(patientQ);
			patientInfo.setString(1, LoginController.currentPatientID);
			rs = patientInfo.executeQuery();
						
			
			while (rs.next()) {
				
				curPatient.setFname(rs.getString("firstName"));
				curPatient.setLname(rs.getString("lastName"));
				curPatient.setDOB(rs.getString("dob"));
				curPatient.setStage(rs.getString("currStage"));
				curPatient.setDiagnosesDate(rs.getString("diagnoseDate"));
				curPatient.setDoctor(rs.getString("primaryDoc"));
				curPatient.setCargiver(rs.getString("caregiver"));
				curPatient.setPatientCode(LoginController.currentPatientID);
				
				System.out.println("VIEWING PATIENT... " + curPatient);
			}
			
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
			
			if(patientInfo!=null)
			{
				try {
					patientInfo.close();
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
		}
    	
    	//*********************************************************************
    	
    	
    	//set fields with prepoulate text
    	fnameTF.setText(curPatient.getFname());
    	lnameTF.setText(curPatient.getLname());
    	
    	//format dates
    	String tempDOB = curPatient.getDOB();
    	String tempDiagnoseDate = curPatient.getDiagnosesDate();
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    	//convert DOB to LocalDate
    	LocalDate localDOB = LocalDate.parse(tempDOB, formatter);
    	//convert DiagnoseDate to LocalDate
    	LocalDate localDDate = LocalDate.parse(tempDiagnoseDate, formatter);
    	
    	
    	DOBPicker.setValue(localDOB);
    	
    	doctorTF.setText(curPatient.getDoctor());
    	
    	stageBox.setValue(curPatient.getStage());
    	
    	diagnosesPicker.setValue(localDDate);
    	
    	cargiverTF.setText(curPatient.getCargiver());

    	
    }
    
    
    @FXML
    void edit(ActionEvent event) {
    	fnameTF.setDisable(false);
    	lnameTF.setDisable(false);
    	DOBPicker.setDisable(false);
    	doctorTF.setDisable(false);
    	stageBox.setDisable(false);
    	diagnosesPicker.setDisable(false);
    	cargiverTF.setDisable(false);
    	
    	cancelBTN.setVisible(true);
    	editBTN.setVisible(false);
    	submitBTN.setVisible(true);
    }
    
    private void disableEdit()
    {
    	fnameTF.setDisable(true);
    	lnameTF.setDisable(true);
    	DOBPicker.setDisable(true);
    	doctorTF.setDisable(true);
    	stageBox.setDisable(true);
    	diagnosesPicker.setDisable(true);
    	cargiverTF.setDisable(true);
    	
    	cancelBTN.setVisible(false);
    	submitBTN.setVisible(false);
    	editBTN.setVisible(true);
    }

    @FXML
    void returnMain(ActionEvent event) {
    	
    	try {
    		
			//Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/ViewPatientInfo.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }

    @FXML
    void submit(ActionEvent event) {
    	
		PatientModel updatePatient = new PatientModel();

    	
    	updatePatient.setFname(fnameTF.getText());
    	updatePatient.setLname(lnameTF.getText());
    	updatePatient.setDOB(DOBPicker.getValue().toString());
    	updatePatient.setDoctor(doctorTF.getText());
    	updatePatient.setStage(stageBox.getValue().toString());
    	updatePatient.setDiagnosesDate(diagnosesPicker.getValue().toString());
    	updatePatient.setCargiver(cargiverTF.getText());
    	updatePatient.setPatientCode(LoginController.currentPatientID);
    	
    	
    	Connection connection = null;
    	PreparedStatement ps = null;
    	
    	String patientQ = "UPDATE patient SET firstName= ?, lastName= ?, dob= ?, currStage= ?, diagnoseDate= ?, primaryDoc= ?, "
    			+ "caregiver= ? WHERE patientCode = ?";
    	try {
			connection = DataSource.getInstance().getConnection();
    		
    		ps = connection.prepareStatement(patientQ);
        	ps.setString(1, updatePatient.getFname());
        	ps.setString(2, updatePatient.getLname());
        	ps.setString(3, updatePatient.getDOB());
        	ps.setString(4, updatePatient.getStage());
        	ps.setString(5, updatePatient.getDiagnosesDate());
        	ps.setString(6, updatePatient.getDoctor());
        	ps.setString(7, updatePatient.getCargiver());
        	ps.setString(8, updatePatient.getPatientCode());
        	ps.execute();
        	
        	//disable editability
        	disableEdit();
        	
        	System.out.println("PATIENT UPDATED... " + updatePatient);
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
    }//end method
    
    
    

}
