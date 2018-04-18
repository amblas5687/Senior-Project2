package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
	
	@FXML
	private ComboBox<String> doseType;

	@FXML
	private Label nameLBL;

	@FXML
	private Label doseLBL;

	@FXML
	private Label doctorLBL;

	@FXML
	private Label purposeLBL;
	
	@FXML
	private Label lblDescript;

	
	private URL toPane;
	private AnchorPane temp;

	private MedModel editMed;

	public void initialize() {
		System.out.println("*******EDIT MED*******");
		setMed();
		
		doseType.getItems().addAll("mg", "g", "kg", "oz", "tab", "tsp", "tbsp");
	}

	
	public void setMed() {
		CurrentMedsController test = new CurrentMedsController();
		editMed = test.getEdit();
		System.out.println("MED TO EDIT... " + editMed);

		// prepopulate textfields
		medName.setText(editMed.getMedName().get());
		medDosage.setText(editMed.getMedDosage().get());
		doseType.setValue(editMed.getDoseType().get());
		medDescript.setText(editMed.getDetails().get());
		prescribDoc.setText(editMed.getDoc().get());
		purpOfPrescript.setText(editMed.getPurpose().get());

		// setting the datepicker
		String prescribDateString = editMed.getDate().get();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate prescribDate = LocalDate.parse(prescribDateString, formatter);
		DOPPicker.setValue(prescribDate);
		DOPPicker.setDisable(true);

	}

	@FXML
	void returnMain(ActionEvent event) {

		try {

			// Replace content_view's current display with the view for this controller
			toPane = getClass().getResource("/view/CurrentMedsView.fxml");
			temp = FXMLLoader.load(toPane);
			content_view.getChildren().setAll(temp);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	void submit(ActionEvent event) {

		boolean validName, validDose, validDoseType, validDoctor, validPurpose, validDescript = true;

		validName = validateName();
		validDose = validateDose();
		validDoseType = validateDoseType();
		validDoctor = validateDoctor();
		validPurpose = validatePurpose();
		validDescript = validateMedDescription();

		if (validName && validDose && validDoseType && validDoctor && validPurpose && validDescript) {
			String mName = medName.getText();
			String mDosage = medDosage.getText();
			String mDoseType = doseType.getValue();
			String mDescript = medDescript.getText();
			String pDoc = prescribDoc.getText();
			String pPurpose = purpOfPrescript.getText();
			String dateUpdated = java.time.LocalDate.now().toString();
			String updatedBy = LoginController.currentUserName;

			String query = "UPDATE currentMeds SET medName = ?, medDosage = ?, doseType = ?, medDescript = ?, "
					+ "prescribDoc = ?, purpPresrcipt = ?, dateUpdated = ?, updatedBy = ?" + "WHERE patientCode = ? AND medID = ?";

			Connection connection = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				connection = DataSource.getInstance().getConnection();

				ps = connection.prepareStatement(query);
				ps.setString(1, mName);
				ps.setString(2, mDosage);
				ps.setString(3, mDoseType);
				ps.setString(4, mDescript);
				ps.setString(5, pDoc);
				ps.setString(6, pPurpose);
				ps.setString(7, dateUpdated);
				ps.setString(8, updatedBy);

				// parameters from edit med to update by
				ps.setString(9, editMed.getPatientCode().get());
				ps.setString(10, editMed.getMedID().get());

				ps.execute();

				System.out.println("MEDICATION UPDATED");

				// go back to current meds
				try {

					// Replace content_view's current display with the view for this controller
					toPane = getClass().getResource("/view/CurrentMedsView.fxml");
					temp = FXMLLoader.load(toPane);
					content_view.getChildren().setAll(temp);

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (SQLException e) {

				DBConfig.displayException(e);

				System.out.println("FAILED UPDATE");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// close connections
			finally {
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

				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} // end finally
		}
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

		if (name == null || name.equals(null) || name.equals("")) {
			nameLBL.setText("Enter medication name");
			nameFlag = false;
			System.out.println("MED NAME IS EMPTY...");
		} else if (!b) {
			nameLBL.setText("Remove numbers and special characters");
			System.out.println("MED NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			nameFlag = false;
		}
		else if(name.length()>=60)
		{
			nameLBL.setText("Medication name length greater than 60 characters");
			nameFlag = false;
			System.out.println("NAME TOO LONG");
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

		if (dose == null || dose.equals(null) || dose.equals("")) {
			doseLBL.setText("Enter dosage amount");
			doseFlag = false;
			System.out.println("DOSE IS EMPTY...");
		} else if (!b) {
			doseLBL.setText("Dose must be a whole number or decimal number");
			System.out.println("DOSE HAD LETTERS OR TOO MANY DECIMALS");
			doseFlag = false;
		}
		else if(dose.length()>=25)
		{
			doseLBL.setText("Dose length greater than 25 characters");
			doseFlag = false;
			System.out.println("DOSE TOO LONG");
		}
		return doseFlag;
	}
	
	private boolean validateDoseType() {
		System.out.println("VALIDATING DOSAGE TYPE");
		boolean dtFlag = true;
		String dose = medDosage.getText();
		
		if(doseType.getValue() == null && (dose == null || dose.equals(null) || dose.equals(""))) {
			dtFlag = false;
			doseLBL.setText("Please enter dosage amount and select a dosage type");
		} else if(doseType.getValue() ==  null) {
			dtFlag = false;
			doseLBL.setText("Please select a dosage type");
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

		if (doctor == null || doctor.equals(null) || doctor.equals("")) {
			doctorLBL.setText("Enter doctor name");
			doctorFlag = false;
			System.out.println("DOCTOR NAME IS EMPTY...");
		} else if (!b) {
			doctorLBL.setText("Remove numbers and special characters");
			System.out.println("DOCTOR NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			doctorFlag = false;
		}
		else if(doctor.length()>=60)
		{
			doctorLBL.setText("Doctor name length greater than 60 characters");
			doctorFlag = false;
			System.out.println("NAME TOO LONG");
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

		if (purpose == null || purpose.equals(null) || purpose.equals("")) {
			purposeLBL.setText("Enter purpose of prescription");
			purposeFlag = false;
			System.out.println("PURPOSE IS EMPTY...");
		} else if (!b) {
			purposeLBL.setText("Remove numbers and special characters");
			System.out.println("PURPOSE CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			purposeFlag = false;
		}
		else if(purpose.length()>=60)
		{
			purposeLBL.setText("Purpose of prescription length greater than 60 characters");
			purposeFlag = false;
			System.out.println("PURPOSE TOO LONG");
		}
		return purposeFlag;
	}
	
	
	private boolean validateMedDescription() {
		System.out.println("VALIDATING MED DESCRIPTION...");
		
		lblDescript.setText(null);
		String details = medDescript.getText();
		
		if((details != null || !details.equals(null) || !details.equals("")) && details.length() >= 500)
		{
			System.out.println("DESCRIPTION TOO LONG");
			lblDescript.setText("Description exceeds maximum possible length");
			return false;
		}
		return true;
	}

}
