package controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import application.DBConfig;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PreexistingArchiveController {

	Stage stage;
	Parent root;
	Scene scene;
	
	
    @FXML
    private AnchorPane content_view;

    @FXML
    private TextField medName;

    @FXML
    private TextField medDosage;

    @FXML
    private TextArea medDescript;

    @FXML
    private TextField prescribDoc;

    @FXML
    private TextField purpOfPrescript;

    @FXML
    private DatePicker DOPPicker;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button cancelBTN;

    @FXML
    private ComboBox<String> doseType;

    @FXML
    private Label nameLBL;

    @FXML
    private Label doctorLBL;

    @FXML
    private Label doseLBL;

    @FXML
    private Label purposeLBL;

    @FXML
    private Label datePrescribedLBL;
    
    @FXML
    private Label lblDescript;
    
	
	public void initialize() {

		System.out.println("*******PREEXISTING ARCHIVE MED*******");
		
		doseType.getItems().addAll("mg", "g", "kg", "oz", "tab", "tsp", "tbsp");
	}

    
    @FXML
    void returnMain(ActionEvent event) {
    	
    	try {
			stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
			scene = new Scene(root, 1200, 670);
			stage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

    @FXML
	void submit(ActionEvent event) {

		boolean validName, validDose, validDoseType, validDoctor, validPurpose, validDate, validDescript = true;

		validName = validateName();
		validDose = validateDose();
		validDoseType = validateDoseType();
		validDoctor = validateDoctor();
		validPurpose = validatePurpose();
		validDate = validateDate();
		validDescript = validateMedDescription();

		if (validName && validDose && validDoseType && validDoctor && validPurpose && validDate && validDescript) {

			String mName = medName.getText();
			String mDosage = medDosage.getText();
			String mDoseType = doseType.getValue().toString();
			String mDescript = medDescript.getText();
			String pDoc = prescribDoc.getText();
			String pPurpose = purpOfPrescript.getText();
			String pDate = DOPPicker.getValue().toString();
			String dateArchived = java.time.LocalDate.now().toString();

			Connection connection = null;
			PreparedStatement ps = null;

			String query = "INSERT INTO archivedMeds (patientCode, medName, medDosage, doseType, medDescript, prescribDoc, purpPresrcipt, prescribDate, dateArchived)"
					+ "VALUES (?,?,?,?,?,?,?,?,?)";

			try {
				connection = DataSource.getInstance().getConnection();

				ps = connection.prepareStatement(query);
				ps.setString(1, LoginController.currentPatientID);
				ps.setString(2, mName);
				ps.setString(3, mDosage);
				ps.setString(4, mDoseType);
				ps.setString(5, mDescript);
				ps.setString(6, pDoc);
				ps.setString(7, pPurpose);
				ps.setString(8, pDate);
				ps.setString(9, dateArchived);

				ps.execute();

				System.out.println("Successful insertion of preexisting archive medication");

			} catch (SQLException e) {

				DBConfig.displayException(e);

				System.out.println("Failed insertion of preexisting archive information.");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} // end finally

			// Clears fields after medication insertion
			medName.setText("");
			medDosage.setText("");
			doseType.setValue(null);
			medDescript.setText("");
			prescribDoc.setText("");
			purpOfPrescript.setText("");
			DOPPicker.setValue(null);
			btnSubmit.setText("Add Another!");
			btnSubmit.setPrefWidth(120);
			
			//TODO fix insets
			//cancelBTN.setStyle("-fx-background-insets: 0 130px 0 0;");
			cancelBTN.setLayoutX(130);

		} // end if

	}// end submit

	private boolean validateName() {
		nameLBL.setText(null);
		System.out.println("VALIDATING MED NAME");
		boolean nameFlag = true;

		String name = medName.getText();
		name = name.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z]+\\s?[a-zA-Z]*$");// . represents single character
		Matcher m = p.matcher(name);
		boolean b = m.matches();

		if (name == null || name.equals("")) {
			nameLBL.setText("Name cannot be empty");
			nameFlag = false;
			System.out.println("MED NAME IS EMPTY...");
		} else if (!b) {
			nameLBL.setText("No numbers, special characters, or extra white spaces");
			System.out.println("MED NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			nameFlag = false;
		}
		return nameFlag;
	}

	private boolean validateDose() {
		System.out.println("VALIDATING MED DOSE");
		doseLBL.setText(null);
		boolean doseFlag = true;

		String dose = medDosage.getText();
		dose = dose.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[0-9]+(.[0-9]+)?$");// . represents single character
		Matcher m = p.matcher(dose);
		boolean b = m.matches();

		if (dose == null || dose.equals("")) {
			doseLBL.setText("Dose cannot be empty");
			doseFlag = false;
			System.out.println("DOSE IS EMPTY...");
		} else if (!b) {
			doseLBL.setText("Dose must only contain numbers and decimals");
			System.out.println("DOSE HAD LETTERS OR TOO MANY DECIMALS");
			doseFlag = false;
		}
		return doseFlag;
	}
	
	private boolean validateDoseType() {
		System.out.println("VALIDATING DOSAGE TYPE");
		boolean dtFlag = true;
		String dose = medDosage.getText();
		
		if(doseType.getValue() == null && dose.equals("")) {
			dtFlag = false;
			doseLBL.setText("Please fill in dosage amount and select a dosage type.");
		} else if(doseType.getValue() ==  null) {
			dtFlag = false;
			doseLBL.setText("Please select a dosage type.");
		}
		
		return dtFlag;
	}

	private boolean validateDoctor() {
		doctorLBL.setText(null);
		System.out.println("VALIDATING DOCTOR");
		boolean doctorFlag = true;

		String doctor = prescribDoc.getText();
		doctor = doctor.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z\\.\\-\\']+(\\s?[a-zA-Z\\.\\-\\'])*$");// . represents single character
		Matcher m = p.matcher(doctor);
		boolean b = m.matches();

		if (doctor == null || doctor.equals("")) {
			doctorLBL.setText("Doctor name cannot be empty");
			doctorFlag = false;
			System.out.println("DOCTOR NAME IS EMPTY...");
		} else if (!b) {
			doctorLBL.setText("No numbers, special characters, or extra white spaces");
			System.out.println("DOCTOR NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			doctorFlag = false;
		}
		return doctorFlag;
	}

	private boolean validatePurpose() {
		purposeLBL.setText(null);
		System.out.println("VALIDATING PURPOSE");
		boolean purposeFlag = true;

		String purpose = purpOfPrescript.getText();
		purpose = purpose.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z\\.\\-\\']+(\\s?[a-zA-Z\\.\\-\\'])*$");// . represents single character
		Matcher m = p.matcher(purpose);
		boolean b = m.matches();

		if (purpose == null || purpose.equals("")) {
			purposeLBL.setText("Purpose cannot be empty");
			purposeFlag = false;
			System.out.println("PURPOSE IS EMPTY...");
		} else if (!b) {
			purposeLBL.setText("No numbers, special characters, or extra white spaces");
			System.out.println("PURPOSE CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			purposeFlag = false;
		}
		return purposeFlag;
	}

	private boolean validateDate() {
		
		System.out.println("VALIDATING DATE...");

		datePrescribedLBL.setText(null);

		String dopString = DOPPicker.getEditor().getText();

		// rough check of dob format
		Pattern p = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$");
		Matcher m = p.matcher(dopString);
		boolean b = m.matches();

		System.out.println("Date " + dopString);

		// date is empty
		if (dopString.equals(null) || dopString.equals("")) {
			datePrescribedLBL.setText("Please enter a date");
			System.out.println("DOB EMTPY");
			return false;
		}
		// date format is wrong
		else if (!b) {
			System.out.println("INVALID DATE FORMAT");
			datePrescribedLBL.setText("Invalid date format. MM/DD/YYYY");
			return false;
		} else {

			try {
				// parse the date to see if it is a real date
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				df.setLenient(false);
				df.parse(dopString);

				// get current date and convert date into date object
				Date curDate = new Date();
				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dopString);

				// date is after current date
				if (date.after(curDate)) {
					System.out.println("DATE CANNOT BE AFTER TODAY'S DATE");
					datePrescribedLBL.setText("Date cannot be after today's date");
					return false;
				}
				return true;
			} catch (ParseException e) {
				// not an actual date
				System.out.println("INVALID DATE");
				datePrescribedLBL.setText("Incorrect date.");
				return false;
			}
		}
	}
	
	private boolean validateMedDescription() {
		System.out.println("VALIDATING MED DESCRIPTION...");
		
		lblDescript.setText(null);
		String details = medDescript.getText();
		
		if((!details.equals(null) || !details.equals("")) && details.length() >= 500)
		{
			System.out.println("DESCRIPTION TOO LONG");
			lblDescript.setText("Description is too long.");
			return false;
		}
		return true;
	}

}
