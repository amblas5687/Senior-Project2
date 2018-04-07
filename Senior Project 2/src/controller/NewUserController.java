package controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.UserModel;

public class NewUserController {

	Parent root;
	Scene scene;
	Stage stage;

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

	// error checking labels
	@FXML
	private Label lblFname;

	@FXML
	private Label lblLname;

	@FXML
	private Label lblDOB;

	@FXML
	private Label lblRelation;

	@FXML
	private Label lblEmail;

	@FXML
	private Label lblPassword1;

	@FXML
	private Label lblPassword2;

	@FXML
	private Label lblAll;
	
	private int count;

	public void initialize() {

		System.out.println("*******NEW PATIENT*******");

		// add combo box selections
		relationBox.getItems().addAll("Son/Daughter", "Spouse", "Grandchild", "Friend", "Medical Professional");
	}

	// user selects next button and enters code
	@FXML
	void patientCodeEnter(ActionEvent event) throws ParseException {
		
		count = 0;
		lblAll.setText(null);
		
		// validate fields
		boolean validDOB, validFname, validLname, validRelation, validEmail, validPassword = false;
		validDOB = validateDOB();
		validFname = validateFName();
		validLname = validateLName();
		validRelation = validateRelation();
		validEmail = validateEmail();
		validPassword = validatePassword();
		
		if(count > 0) {
			lblAll.setText("Please fill in all fields");
		}else if (validDOB && validFname && validLname && validRelation && validEmail && validPassword) {

			// grab the fields for the user
			UserModel subUser = grabFields();

			// generate userCode
			String userCode = genCode();
			subUser.setUserID(userCode);

			// create the dialog box
			Alert dialog = new Alert(AlertType.CONFIRMATION);
			dialog.getDialogPane().getStylesheets()
					.add(getClass().getResource("/application/application.css").toExternalForm());
			dialog.setTitle("Patient Code");
			dialog.setHeaderText("Enter the patient code generated when your \npatient account was created");

			stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/application/logo_wbg.png"));

			GridPane grid = new GridPane();
			Label label = new Label("Enter your patient code:  ");
			TextField textField = new TextField();

			grid.add(label, 0, 0);
			grid.add(textField, 1, 0);
			dialog.getDialogPane().setContent(grid);

			ButtonType noCode = new ButtonType("No patient code");
			dialog.getDialogPane().getButtonTypes().setAll(noCode, ButtonType.OK, ButtonType.CANCEL);

			// get response
			Optional<ButtonType> result = dialog.showAndWait();

			// get textfield value
			String code = textField.getText();

			// if they don't have a code, send them to new patient
			if (result.get() == noCode) {

				try {
					stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
					root = FXMLLoader.load(getClass().getResource("/view/NewPatientView.fxml"));
					scene = new Scene(root);
					stage.setScene(scene);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// if they hit ok for a code
			} else if (code != null && result.get() == ButtonType.OK) {

				// TODO need to check patient table to verify the patient
				System.out.println("PATIENT CODE... " + textField.getText());

				Connection connection = null;
				PreparedStatement user = null;
				PreparedStatement patient = null;
				ResultSet rs = null;

				// check if patient code in database
				try {
					String patientQ = "SELECT patientCode FROM patient WHERE patientCode = ?";

					connection = DataSource.getInstance().getConnection();
					patient = connection.prepareStatement(patientQ);
					patient.setString(1, textField.getText());
					rs = patient.executeQuery();

					if (!rs.isBeforeFirst()) {
						System.out.println("PATIENT DOES NOT EXIST");
						Alert failure = new Alert(AlertType.ERROR);
						// safely catches error by pop-up alert.
						failure.setTitle("Error");
						failure.setHeaderText("Error!");
						failure.setContentText("Patient code not found. Please enter a different patient code or create a patient account");
						failure.getDialogPane().getStylesheets()
								.add(getClass().getResource("/application/application.css").toExternalForm());
						
						stage = (Stage) failure.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("/application/logo_wbg.png"));
						
						Optional<ButtonType> error = failure.showAndWait();

					} else {

						subUser.setPatientCode(textField.getText());

						// prepare the query
						String userQ = "INSERT into user (fname, lname, DOB, pRelation, email, password, patientCode, userID) "
								+ "VALUES (?,?,?,?,?,?,?,?)";

						user = connection.prepareStatement(userQ);
						user.setString(1, subUser.getFname().trim());
						user.setString(2, subUser.getLname().trim());
						user.setString(3, subUser.getDOB());
						user.setString(4, subUser.getRelation());
						user.setString(5, subUser.getEmail());
						user.setString(6, subUser.getPassword());
						user.setString(7, subUser.getPatientCode());
						user.setString(8, subUser.getUserID());

						user.execute();
						System.out.println("USER ENTERED..." + subUser);

						// go back to main
						try {
							stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
							root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
							scene = new Scene(root);
							stage.setScene(scene);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
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

					if (user != null) {
						try {
							user.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (patient != null) {
						try {
							patient.close();
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
				} // finally
			} // else if
		} // end if
		else {
			System.out.println("FAILED TO VALIDATE");
		}

	}// end method

	// user cancels submission
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

	// helper methods

	UserModel grabFields() throws ParseException {

		String fname = fnameTF.getText();
		String lname = lnameTF.getText();

		// grab date
		String DOB = DOBPicker.getEditor().getText();

		// format date for database
		Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		Date DOBDate = new SimpleDateFormat("MM/dd/yyyy").parse(DOB);
		String formatDate = formatter2.format(DOBDate);
		System.out.println("Before format " + DOB + " After format " + formatDate);

		String pRelation = relationBox.getValue();
		String email = emailTF.getText();
		String password = password1TF.getText();

		UserModel tempUser = new UserModel(fname, lname, formatDate, email, password, pRelation);
		System.out.println("GRABBED FIELDS FOR USER " + tempUser);
		return tempUser;
	}

	private String genCode() {

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

			System.out.println("\nBuilt string " + sb);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("User code: " + sb.toString());
		return sb.toString();

	}

	private boolean validateDOB() {

		lblDOB.setText(null);

		String dobString = DOBPicker.getEditor().getText();

		// rough check of dob format
		Pattern p = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$");
		Matcher m = p.matcher(dobString);
		boolean b = m.matches();

		System.out.println("Date " + dobString);

		// date is empty
		if (dobString == null || dobString.equals(null) || dobString.equals("")) {
			lblDOB.setText("Please select or enter your date of birth");
			count++;
			System.out.println("DOB EMTPY");
			return false;
		}
		// date format is wrong
		else if (!b) {
			System.out.println("INVALID DATE FORMAT");
			lblDOB.setText("Invalid date format. Please use MM/DD/YYYY");
			return false;
		} else {

			try {
				// parse the date to see if it is a real date
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				df.setLenient(false);
				df.parse(dobString);

				// get current date and convert date into date object
				Date curDate = new Date();
				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dobString);

				// date is after current date
				if (date.after(curDate)) {
					System.out.println("DATE CANNOT BE AFTER TODAY'S DATE");
					lblDOB.setText("Date of birth cannot be after today's date");
					return false;
				}
				return true;
			} catch (ParseException e) {
				// not an actual date
				System.out.println("INVALID DATE");
				lblDOB.setText("Incorrect date. Please enter a valid date");
				return false;
			}
		}

	}// end method

	private boolean validateFName() {

		lblFname.setText(null);
		System.out.println("VALIDATING FIRST NAME");

		String fname = fnameTF.getText();
		fname = fname.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z]+\\s?[a-zA-Z]*$");// . represents single character
		Matcher m = p.matcher(fname);
		boolean b = m.matches();

		if (fname == null || fname.equals(null) || fname.equals("")) {
			lblFname.setText("Enter your first name");
			count++;
			System.out.println("FIRST NAME IS EMPTY...");
			return false;
		} else if (!b) {
			lblFname.setText("Remove numbers and special characters");
			System.out.println("FIRST NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			return false;
		}

		return true;

	}// end method

	private boolean validateLName() {

		lblLname.setText(null);
		System.out.println("VALIDATING LAST NAME");

		String lname = lnameTF.getText();
		lname = lname.trim();

		// regex pattern only allow letters, single space, then letters
		Pattern p = Pattern.compile("^[a-zA-Z]+((\\.\\s)|[\\.\\s\\-])?[a-zA-Z]*$");// . represents single character
		Matcher m = p.matcher(lname);
		boolean b = m.matches();

		if (lname == null || lname.equals(null) || lname.equals("")) {
			lblLname.setText("Enter your last name");
			count++;
			System.out.println("LAST NAME IS EMPTY...");
			return false;
		} else if (!b) {
			lblLname.setText("Remove numbers and special characters");
			System.out.println("LAST NAME CONTAINED EITHER NUMBER, SPECIAL CHARCTERS, OR EXTRA SPACES");
			return false;
		}

		return true;
	}// end method

	private boolean validateRelation() {

		lblRelation.setText(null);
		System.out.println("VALIDATING RELATION");

		if (relationBox.getValue() == null) {
			lblRelation.setText("Please select your relation to patient");
			count++;
			return false;
		}

		return true;
	}

	private boolean validateEmail() {

		System.out.println("VALIDATING EMAIL");
		lblEmail.setText(null);
		boolean takenEmail = true;

		String email = emailTF.getText();
		// ^([a-zA-Z0-9~!$%^&*_=+}{\'?]+(\.[a-zA-Z0-9~!$%^&*_=+}{\'?]+)*@[a-zA-Z0-9_-]+(\.com|\.net|\.edu|\.gov|\.mil))$
		Pattern p = Pattern.compile("^([a-zA-Z0-9~!$%^&*_=+}{\\'?]+(\\.[a-zA-Z0-9~!$%^&*_=+}"
				+ "{\\'?]+)*@[a-zA-Z0-9_-]+(\\.com|\\.net|\\.edu|\\.gov|\\.mil))$");// . represents single character
		Matcher m = p.matcher(email);
		boolean b = m.matches();

		if (email == null || email.equals(null) || email.equals("")) {
			lblEmail.setText("Enter email address");
			count++;
			System.out.println("EMAIL IS EMPTY...");
			return false;
		} else if (!b) {
			lblEmail.setText("Invalid email format for email address");
			System.out.println("EMAIL WAS NOT IN PROPER FORMAT");
			return false;
		}
		
		//see if email taken
		takenEmail = checkUserExist();
		if(takenEmail)
		{
			return true;
		}
		else {
			return false;
		}
	}

	private boolean validatePassword() {

		String p1 = password1TF.getText();
		String p2 = password2TF.getText();

		lblPassword1.setText(null);
		lblPassword2.setText(null);

		boolean passwordFlag = true;

		/*
		 * Pattern p = Pattern.compile("^$");// . represents single character Matcher m
		 * = p.matcher(email); boolean b = m.matches();
		 */

		if ((p1 == null || p1.equals(null) || p1.equals("")) && (p2 == null || p2.equals(null) || p2.equals(""))) {
			lblPassword1.setText("Enter your password");
			lblPassword2.setText("Enter password verification");
			count++;
			System.out.println("BOTH PASSWORDS EMPTY...");
			passwordFlag = false;
		} else if (p1 == null || p1.equals(null) || p1.equals("")) {
			lblPassword1.setText("Enter your password");
			count++;
			System.out.println("PASSWORD EMPTY...");
			passwordFlag = false;
		} else if (p1.length() < 4) {
			lblPassword1.setText("Your password must be at least 4 digits long");
			System.out.println("PASSWORD < 4");
			passwordFlag = false;
		} else if (p2 == null || p2.equals(null) || p2.equals("")) {
			lblPassword2.setText("Enter password verification");
			count++;
			System.out.println("PASSWORD EMPTY...");
			passwordFlag = false;
		} else if (!p1.equals(p2)) {
			//lblPassword1.setText("Password does not match");
			lblPassword2.setText("Mismatched passwords");
			System.out.println("PASSWORDS DON'T MATCH...");
			passwordFlag = false;
		}

		return passwordFlag;
	}

	private boolean checkUserExist() {

		System.out.println("CHECKING IF EMAIL IS TAKEN");
		lblEmail.setText(null);

		String email = emailTF.getText();

		Connection connection = null;
		PreparedStatement userEmail = null;
		ResultSet rs = null;
		boolean emailFlag = true;

		// check if patient code in database
		try {
			String emailQ = "SELECT email FROM user WHERE email = ?";

			connection = DataSource.getInstance().getConnection();
			userEmail = connection.prepareStatement(emailQ);
			userEmail.setString(1, email);
			rs = userEmail.executeQuery();
			

			if (rs.isBeforeFirst()) {
				System.out.println("USER EMAIL TAKEN");
				lblEmail.setText("Email is already in use. Please enter a different email address");
				emailFlag = false;
				
			}
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

			if (userEmail != null) {
				try {
					userEmail.close();
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
		} // finally
		
		return emailFlag;
	}

}
