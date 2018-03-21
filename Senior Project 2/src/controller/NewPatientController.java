package controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import application.DBConfig;
import application.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewPatientController {

	Stage stage;
	Parent root;
	Scene scene;


    @FXML
    private AnchorPane ap = new AnchorPane();
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
	private Button cancelBTN;
	@FXML
	private ComboBox<String> stageBox;
	@FXML
	private DatePicker diagnosesPicker;
	@FXML
	private TextField cargiverTF;
	@FXML
	private Label lblFname;
	@FXML
	private Label lblLname;
	@FXML
	private Label lblCaregiver;
	@FXML
	private Label lblDOB;
	@FXML
	private Label lblDiagnoses;
	@FXML
	private Label lblStage;
	@FXML
	private Label lblDoc;
	@FXML
	private Label lblAll;

	private boolean flag;
	private int count;
	private String dateOB = "";
	private String dateOD = "";

	public void initialize() {
		System.out.println("*******NEW PATIENT*******");
		stageBox.getItems().addAll("Stage 1", "Stage 2", "Stage 3", "Stage 4", "Stage 5", "Stage 6", "Stage 7");
	}

	@FXML
	void returnMain(ActionEvent event) {

		try {
			stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
			scene = new Scene(root);
			stage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void loadPreexisting() {

		try {
			//stage = AnnaMain.getStage();
			System.out.println(stage);
			root = FXMLLoader.load(getClass().getResource("/view/PreexistingArchiveView.fxml"));
			scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void submit(ActionEvent event) {
		
		lblAll.setText(null);

		count = 0;
		boolean fName = checkFirstName();
		boolean lName = checkLastName();
		boolean DOB = checkDOB();
		boolean cGiver = checkCaregiver();
		boolean cStage = checkStage();
		boolean dDate = checkDiagnoses();
		boolean Doc = checkDoc();

		if (count > 0) {
			lblAll.setText("Please fill in all fields.");
		} else if (!fName && !lName && !DOB && !cGiver && !cStage && !dDate && !Doc) {

			String firstName = fnameTF.getText();
			String lastName = lnameTF.getText();
			String dob = dateOB;
			String curStage = stageBox.getValue().toString();
			String diagDate = dateOD;
			String doc = doctorTF.getText();
			String caregiver = cargiverTF.getText();
			String patientCode = genCode();

			Alert alert = new Alert(AlertType.INFORMATION);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			dialogPane.setMaxHeight(200);
			dialogPane.getStyleClass().add("alert");
			alert.setTitle("Patient Code");
			alert.setHeaderText("Patient code uniquely identifies each patient. Please record for further use.");

			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/application/4getmenot1.jpg"));

			TextField textField = new TextField("Patient Code: " + patientCode);
			textField.setEditable(false);
			textField.setStyle("-fx-background-color: ligthgrey;");
			alert.getDialogPane().setContent(textField);

			Optional<ButtonType> result = alert.showAndWait();

			String query = "INSERT INTO patient (firstName, lastName, dob, currStage, diagnoseDate, primaryDoc, caregiver, patientCode)"
					+ "VALUES (?,?,?,?,?,?,?,?)";

			Connection connection = null;
			PreparedStatement ps = null;

			try {
				connection = DataSource.getInstance().getConnection();

				ps = connection.prepareStatement(query);
				ps.setString(1, firstName);
				ps.setString(2, lastName);
				ps.setString(3, dob);
				ps.setString(4, curStage);
				ps.setString(5, diagDate);
				ps.setString(6, doc);
				ps.setString(7, caregiver);
				ps.setString(8, patientCode);

				ps.execute();
				System.out.println("CREATED NEW PATIENT");


				// set the patientCode for the preexisting archive med controller
				LoginController.currentPatientID = patientCode;

			} catch (SQLException e) {
				DBConfig.displayException(e);
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

			} // end finally

			if (result.get() == ButtonType.OK) {
				alert.hide();

				try {
					// load the preexisting archive scene
					stage = (Stage)((Button)event.getSource()).getScene().getWindow();
					loadPreexisting();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}// end method

	boolean checkFirstName() {

		lblFname.setText(null);

		String name = fnameTF.getText();

		if (name.equals("")) {
			lblFname.setText("Enter first name.");
			count++;
			return flag = true;
		}

		name = name.trim();

		Pattern p = Pattern.compile("[^a-zA-Z]+\\s?[^a-zA-Z]*$");
		Matcher nam = p.matcher(name);
		boolean n = nam.find();

		if (n) {
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

		if (name.equals("")) {
			lblLname.setText("Enter last name.");
			count++;
			return flag = true;
		}

		name = name.trim();

		Pattern p = Pattern.compile("[^a-zA-Z]+[\\-\\.\\s]?[^a-zA-Z]*$");
		Matcher nam = p.matcher(name);
		boolean n = nam.find();

		if (n) {
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

		if (name.equals("")) {
			lblCaregiver.setText("Enter name of caregiver.");
			count++;
			return flag = true;
		}

		name = name.trim();

		Pattern p = Pattern.compile("[^a-zA-Z]+\\s?[^a-zA-Z]*[\\-\\.\\s]?[^a-zA-Z]*$");
		Matcher nam = p.matcher(name);
		boolean n = nam.find();

		if (n) {
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
				Format formatter = new SimpleDateFormat("MM/dd/yyyy");
				Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
				Date curDate = new Date();

				Pattern p = Pattern.compile("[0-9]{1,2}\\/[0-9]{1,2}\\/[0-9]{4}$");
				Matcher dat = p.matcher(dob);
				boolean d = dat.find();

				if (!d) {
					lblDOB.setText("Incorrect date format. Please use MM/DD/YYYY");
					flag = true;
				}

				if (sections[0].length() == 1) {
					sections[0] = "0" + sections[0];
				}
				if (sections[1].length() == 1) {
					sections[1] = "0" + sections[1];
				}

				dob = sections[0] + "/" + sections[1] + "/" + sections[2];

				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dob);
				String strDate = formatter.format(date);

				if (year < 1900) {
					lblDOB.setText("Invalid year.");
					flag = true;
				} else if (!dob.equals(strDate)) {
					lblDOB.setText("Incorrect date.");
					flag = true;
				} else if (date.after(curDate)) {
					lblDOB.setText("Invalid date.");
					flag = true;
				} else {
					dateOB = formatter2.format(date);;
					flag = false;
				}

			} catch (Exception e) {
				e.printStackTrace();
				lblDOB.setText("Incorrect date format. Please use MM/DD/YYYY");
				return flag = true;
			}

		}

		return flag;
	}

	boolean checkDiagnoses() {

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
				Format formatter = new SimpleDateFormat("MM/dd/yyyy");
				Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
				Date curDate = new Date();

				Pattern p = Pattern.compile("[0-9]{1,2}\\/[0-9]{1,2}\\/[0-9]{4}$");
				Matcher dat = p.matcher(diag);
				boolean d = dat.find();

				if (!d) {
					lblDiagnoses.setText("Incorrect date format. Please use MM/DD/YYYY");
					flag = true;
				}

				if (sections[0].length() == 1) {
					sections[0] = "0" + sections[0];
				}
				if (sections[1].length() == 1) {
					sections[1] = "0" + sections[1];
				}

				if (year < 1900) {
					lblDiagnoses.setText("Invalid year.");
					flag = true;
				}

				diag = sections[0] + "/" + sections[1] + "/" + sections[2];

				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(diag);
				String strDate = formatter.format(date);

				if (year < 1900) {
					lblDiagnoses.setText("Invalid year.");
					flag = true;
				} else if (!diag.equals(strDate)) {
					lblDiagnoses.setText("Incorrect date.");
					flag = true;
				} else if (date.after(curDate)) {
					lblDiagnoses.setText("Invalid date.");
					flag = true;
				} else {
					dateOD = formatter2.format(date);
					flag = false;
				}

			} catch (Exception e) {
				// e.printStackTrace();
				lblDiagnoses.setText("Incorrect date format. Please use MM/DD/YYYY");
				return flag = true;
			}

		}

		return flag;
	}

	boolean checkStage() {

		lblStage.setText(null);

		if (stageBox.getValue() == null) {
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

		if (doc.equals("")) {
			lblDoc.setText("Enter doctor name.");
			count++;
			return flag = true;
		}

		doc = doc.trim();

		Pattern p = Pattern.compile("[^a-zA-Z]+\\.?\\s?[^a-zA-Z]*[\\-\\.\\s]?[^a-zA-Z]*$");
		Matcher nam = p.matcher(doc);
		boolean n = nam.find();

		if (n) {
			lblDoc.setText("No numbers, special characters, or extra spaces.");
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	String genCode() {

		StringBuilder sb = new StringBuilder();

		// Create a secure random number generator using the SHA1PRNG algorithm
		try {
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");

			// Get 16 random bytes
			byte[] randBytes = new byte[16];
			rand.nextBytes(randBytes);

			for (int i = 0; i < randBytes.length; i++) {
				sb.append(String.format("%02X", randBytes[i]));
			}

			System.out.println("\nBUILT CODE " + sb);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		System.out.println("PATIENT CODE... " + sb.toString());
		return sb.toString();

	}

}
