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
    
    //Dummy user for testing
    String tempCode = "1";
    
	Connection con = AnnaMain.con;

   
    
    public void initialize(){
    	
    	//add combo box selections
    	stageBox.getItems().addAll("Stage 1", "Stage 2", "Stage 3", "Stage 4", "Stage 5", "Stage 6", "Stage 7");    	
    	
    	//prepopulate, uses dummy user for now
    	
    	String patientQ = "SELECT * FROM patient WHERE patientCode = ?";
    	
		PatientModel curPatient = new PatientModel();
    	try {
			PreparedStatement patientInfo = con.prepareStatement(patientQ);
			patientInfo.setString(1, tempCode);
			ResultSet rs = patientInfo.executeQuery();
						
			
			while (rs.next()) {
				
				curPatient.setFname(rs.getString("firstName"));
				curPatient.setLname(rs.getString("lastName"));
				curPatient.setDOB(rs.getString("dob"));
				curPatient.setStage(rs.getString("currStage"));
				curPatient.setDiagnosesDate(rs.getString("diagnoseDate"));
				curPatient.setDoctor(rs.getString("primaryDoc"));
				curPatient.setCargiver(rs.getString("caregiver"));
				curPatient.setPatientCode(tempCode);
				
				System.out.println(curPatient);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			DBConfig.displayException(e);
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
    }

    @FXML
    void returnMain(ActionEvent event) {

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
    	updatePatient.setPatientCode(tempCode);
    	
    	String patientQ = "UPDATE patient SET firstName= ?, lastName= ?, dob= ?, currStage= ?, diagnoseDate= ?, primaryDoc= ?, "
    			+ "caregiver= ? WHERE patientCode = ?";
    	try {
    		PreparedStatement ps = con.prepareStatement(patientQ);
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
        	
        	System.out.println(updatePatient);
    	} catch (SQLException e) {
    		DBConfig.displayException(e);
    	}
    	
    	

    }

}
