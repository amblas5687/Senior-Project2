package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.DBConfig;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
    private Label lblFname;
    @FXML
    private Label lblLname;
    @FXML
    private Label lblDOB;
    @FXML
    private Label lblCaregiver;
    @FXML
    private Label lblStage;
    @FXML
    private Label lblDiagnoses;
    @FXML
    private Label lblDoc;
    @FXML
    private Label lblAll;
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
   	
   	private boolean flag;
   	private int count;
   
    
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
    void submit(ActionEvent event) throws ParseException{
    	
    	lblAll.setText(null);
	   	
	   	count = 0;
	   	boolean fName = checkFirstName();
	   	boolean lName = checkLastName();
	   	boolean DOB = checkDOB();
	   	boolean cGiver = checkCaregiver();
	   	boolean cStage = checkStage();
	   	boolean dDate = checkDiagnoses();
	   	boolean Doc = checkDoc();
	   	
    	if(count > 0) {
	   		lblAll.setText("Please fill in all fields.");
	   	} else if(!fName && !lName && !DOB && !cGiver && !cStage && !dDate && !Doc) {
	   		
	    	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dobDate = new SimpleDateFormat("MM/dd/yyyy").parse(DOBPicker.getEditor().getText());
			Date diagnosesDate = new SimpleDateFormat("MM/dd/yyyy").parse(diagnosesPicker.getEditor().getText());
	   		
			PatientModel updatePatient = new PatientModel();
	
	    	
	    	updatePatient.setFname(fnameTF.getText());
	    	updatePatient.setLname(lnameTF.getText());
	    	updatePatient.setDOB(formatter.format(dobDate));
	    	updatePatient.setDoctor(doctorTF.getText());
	    	updatePatient.setStage(stageBox.getValue().toString());
	    	updatePatient.setDiagnosesDate(formatter.format(diagnosesDate));
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
	   	}//end if
    }//end method
    
    boolean checkFirstName() {
	   	
    	lblFname.setText(null);
    	
    	String name = fnameTF.getText();
    	
	   	if(name.equals("")) {
	   		lblFname.setText("Enter first name.");
	   		count++;
	   		return flag = true;
	   	} 
	   	
	   	name = name.trim();
		
		Pattern p = Pattern.compile("[^a-zA-Z]+\\s?[^a-zA-Z]*$");
    	Matcher nam = p.matcher(name);
    	boolean n = nam.find();
		
    	
	   	if(n) {
	   		lblFname.setText("No numbers, special characters, or extra spaces.");
	   		flag = true;
	   	} else {
	   		flag = false;
	   	}
	   	
	   	return flag;
    }
    
    boolean checkLastName() {
    	
    	lblLname.setText(null);
    	
    	String name = lnameTF.getText();
	   	
	   	if(name.equals("")) {
	   		lblLname.setText("Enter last name.");
	   		count++;
	   		return flag = true;
	   	} 
	   	
	   	name = name.trim();
		
		Pattern p = Pattern.compile("[^a-zA-Z]+[\\-\\.\\s]?[^a-zA-Z]*$");
    	Matcher nam = p.matcher(name);
    	boolean n = nam.find();
		
    	
	   	if(n) {
	   		lblLname.setText("No numbers, special characters, or extra spaces.");
	   		flag = true;
	   	} else {
	   		flag = false;
	   	}
	   	
	   	return flag;
    }
    
    boolean checkCaregiver() {
    	
    	lblCaregiver.setText(null);
    	
    	String name = cargiverTF.getText();
	   	
	   	if(name.equals("")) {
	   		lblCaregiver.setText("Enter name of caregiver.");
	   		count++;
	   		return flag = true;
	   	} 
	   	
	   	name = name.trim();
		
		Pattern p = Pattern.compile("[^a-zA-Z]+\\s?[^a-zA-Z]*[\\-\\.\\s]?[^a-zA-Z]*$");
    	Matcher nam = p.matcher(name);
    	boolean n = nam.find();
		
    	
	   	if(n) {
	   		lblCaregiver.setText("No numbers, special characters, or extra spaces.");
	   		flag = true;
	   	} else {
	   		flag = false;
	   	}
	   	
	   	return flag;
    }
    
    boolean checkDOB() {
    	
    	lblDOB.setText(null);

		String dob = DOBPicker.getEditor().getText();

		if (dob.equals("")) {

			lblDOB.setText("Please select or enter a date.");
			count++;
			flag = true;

		} else {

			try {

				String[] sections = dob.split("/");
				int year = Integer.parseInt(sections[2]);
				DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				formatter.setLenient(false);
				Date date = formatter.parse(dob);
				Date curDate = new Date();

				Pattern p = Pattern.compile("[0-9]{1,2}\\/[0-9]{1,2}\\/[0-9]{4}$");
				Matcher dat = p.matcher(dob);
				boolean d = dat.find();

				if (!d) {
					lblDOB.setText("Incorrect date format. Please use MM/DD/YYYY");
					return flag = true;
				}

				if (year < 1900) {
					lblDOB.setText("Invalid year.");
					flag = true;
				} else if (date.after(curDate)) {
					lblDOB.setText("Invalid date. Date cannot be after today's date.");
					flag = true;
				} else {
					flag = false;
				}

			} catch (ParseException e) {
				
				lblDOB.setText("Incorrect date.");
				return flag = true;
			}

		}

		return flag;
    }
    
    boolean checkDiagnoses(){
    	
    	lblDiagnoses.setText(null);

		String diag = diagnosesPicker.getEditor().getText();

		if (diag.equals("")) {
			lblDiagnoses.setText("Please select or enter a date.");
			count++;
			flag = true;
		} else {

			try {

				String[] sections = diag.split("/");
				int year = Integer.parseInt(sections[2]);
				DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date dob = formatter.parse(DOBPicker.getEditor().getText());
				formatter.setLenient(false);
				Date date = formatter.parse(diag);
				Date curDate = new Date();

				Pattern p = Pattern.compile("[0-9]{1,2}\\/[0-9]{1,2}\\/[0-9]{4}$");
				Matcher dat = p.matcher(diag);
				boolean d = dat.find();

				if (!d) {
					lblDiagnoses.setText("Incorrect date format. Please use MM/DD/YYYY");
					return flag = true;
				}

				if (year < 1900) {
					lblDiagnoses.setText("Invalid year.");
					flag = true;
				} else if (date.after(curDate)) {
					lblDiagnoses.setText("Invalid date. Date cannot be after today's date.");
					flag = true;
				} else if(date.before(dob) || date.equals(dob)) {
					lblDiagnoses.setText("Invalid date. Date cannot be before or on the day of patient's birthday.");
					flag = true;
				} else {
					flag = false;
				}

			} catch (ParseException e) {
				
				lblDiagnoses.setText("Incorrect date.");
				return flag = true;
			}

		}

		return flag;
    }
    
    boolean checkStage(){
    	
    	lblStage.setText(null);
    	
    	if(stageBox.getValue() == null) {
			lblStage.setText("Please select a stage.");
	   		count++;
			flag = true;
		} else {
			flag = false;
		}
		
		return flag;
    }
    
    boolean checkDoc() {
    	
    	lblDoc.setText(null);
    	
    	String doc = doctorTF.getText();
    
    	if(doc.equals("")) {
	   		lblDoc.setText("Enter doctor name.");
	   		count++;
	   		return flag = true;
	   	} 
	   	
	   	doc = doc.trim();
		
		Pattern p = Pattern.compile("[^a-zA-Z]+\\.?\\s?[^a-zA-Z]*[\\-\\.\\s]?[^a-zA-Z]*$");
    	Matcher nam = p.matcher(doc);
    	boolean n = nam.find();
		
    	
	   	if(n) {
	   		lblDoc.setText("No numbers, special characters, or extra spaces.");
	   		flag = true;
	   	} else {
	   		flag = false;
	   	}
	   	
	   	return flag;
    }

}
